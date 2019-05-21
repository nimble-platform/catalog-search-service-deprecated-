package eu.nimble.service.catalog.search.impl.Indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.ConceptSource;
import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.factories.ValueGroupingFactory;
import eu.nimble.service.catalog.search.impl.dao.ClassType;
import eu.nimble.service.catalog.search.impl.dao.ClassTypes;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.IndexFields;
import eu.nimble.service.catalog.search.impl.dao.ItemMappingFieldInformation;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.PropertyRelevance;
import eu.nimble.service.catalog.search.impl.dao.PropertyType;
import eu.nimble.service.catalog.search.impl.dao.UBLResult;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.item.ItemType;
import eu.nimble.service.catalog.search.impl.dao.item.SOLRResult;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertiesFromConcept;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyFromConcept;

public class IndexingServiceReader extends IndexingServiceConstant {

	private String url = "";
	private String urlForClassInformation = "";
	private String urlForPropertyInformation = "";
	private String urlForPropertyInformationUBL = "";
	private String urlForItemInformation = "";
	private String urlForIndexFields = "";
	private PropertyInformationCache propertyInformationCache = new PropertyInformationCache();
	private IndexFieldCache indexFieldCache = new IndexFieldCache();

	public IndexingServiceReader(String url) {
		super();
		this.url = url;
		if (url.charAt(url.length() - 1) != '/') {
			url += "/";
		}
		urlForClassInformation = url + "class";
		urlForPropertyInformation = url + "property";
		urlForPropertyInformationUBL = url + "property";
		urlForItemInformation = url + "item";
		urlForIndexFields = url + "item/fields";

		Collection<IndexFields> allFields = requestAllIndexFields();
		indexFieldCache.insertAllIndexFields(allFields);

	}

	public String getUrlForPropertyInformationUBL() {
		return urlForPropertyInformationUBL;
	}

	public Collection<IndexFields> requestAllIndexFields() {

		String url = urlForIndexFields;
		String response = invokeHTTPMethod(url);
		Gson gson = new Gson();
		Collection<IndexFields> result = gson.fromJson(response, Collection.class);
		return result;

	}

	public void setUrlForPropertyInformationUBL(String urlForPropertyInformationUBL) {
		this.urlForPropertyInformationUBL = urlForPropertyInformationUBL + "property";
	}

	private String invokeHTTPMethod(String url) {
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		StringBuffer stringBuffer = new StringBuffer();
		try {
			HttpResponse response = client.execute(request);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();

			return stringBuffer.toString();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The peroperties have three sources ontology, ubl, unknown.
	 * 
	 * @param urlOfClass
	 * @return
	 */
	public List<String> getAllPropertiesIncludingEverything(String urlOfClass) {

		String httpGetURL = urlForClassInformation + "?uri=" + URLEncoder.encode(urlOfClass);
		String result = invokeHTTPMethod(httpGetURL);
		Logger.getAnonymousLogger().log(Level.INFO, result);
		List<String> allProperties = new ArrayList<String>();
		Gson gson = new Gson();
		ClassType r = gson.fromJson(result, ClassType.class);
		if (r.getProperties() != null) {
			r.getProperties().forEach(x -> allProperties.add(x));
		}
		// allProperties.add(result);

		List<PropertyType> propInfosUBL = requestStandardPropertiesFromUBL();
		propInfosUBL.forEach(x -> {
			String propertyURL = x.getUri();
			boolean relevant = checkWhetherPropertyIsRelevant(propertyURL, urlOfClass);
			if (x.isVisible() && relevant) {
				x.setConceptSource(ConceptSource.CUSTOM);
				allProperties.add(x.getUri());
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"UBL specifc property is marked as not visible or has no property values in the catalogie: "
								+ propertyURL);
			}
		});

		List<PropertyType> propInfosStandard = requestStandardPropertiesFromUnknownSopurce();
		propInfosStandard.forEach(x -> {
			x.setConceptSource(ConceptSource.CUSTOM);
			allProperties.add(x.getUri());
		});

		if (!propertyInformationCache.isConceptAlreadyContained(urlOfClass)) {

			List<PropertyType> propInfos = new ArrayList<PropertyType>();
			propInfos.addAll(propInfosUBL);
			propInfos.addAll(propInfosStandard);

			for (String propertyURL : allProperties) {
				boolean relevant = checkWhetherPropertyIsRelevant(propertyURL, urlOfClass);
				if (relevant) {
					PropertyType p = requestPropertyInfos(gson, propertyURL);
					if (p != null && p.isVisible()) {
						p.setConceptSource(ConceptSource.ONTOLOGICAL);
						propInfos.add(p);
					} else {
						Logger.getAnonymousLogger().log(Level.WARNING,
								"Ignore property, because it is set to be invisible: " + propertyURL);
					}
				}
				// propInfos.add(requestPropertyInfos(gson, propertyURL));
			}
			if (propInfos.size() > 0) {
				propertyInformationCache.addConcept(urlOfClass, propInfos);
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING, "Cannot request property infos of class " + urlOfClass);
			}

		}
		Iterator<String> iterator = allProperties.iterator();
		while (iterator.hasNext()) {
			String property = iterator.next();
			PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(urlOfClass, property);
			if (pType == null) {
				iterator.remove();
			}
		}
		return allProperties;
	}

	private List<PropertyType> requestStandardPropertiesFromUnknownSopurce() {

		DefaultPropertyFactory factory = new DefaultPropertyFactory();

		return factory.createProperties();
	}

	public List<PropertyType> requestStandardPropertiesFromUBL() {
		// https://nimble-platform.salzburgresearch.at/nimble/indexing-service/property/select?q=nameSpace:%22http://www.nimble-project.org/resource/ubl%23%22)
		String urlOfUBBL = HTTP_WWW_NIMBLE_PROJECT_ORG_RESOURCE_UBL;
		String httpGetURL = urlForPropertyInformationUBL + "/select?q=nameSpace:"
				+ URLEncoder.encode("\"" + urlOfUBBL + "\"");
		String result = invokeHTTPMethod(httpGetURL);
		// System.out.println(result);

		Gson gson = new Gson();
		UBLResult r = gson.fromJson(result, UBLResult.class);
		for (PropertyType pType : r.getResult()) {
			if (pType.getUri().equals("http://www.nimble-project.org/resource/ubl#certificateType")) {

				if (!pType.getItemFieldNames().contains("certificateType")) {
					pType.getItemFieldNames().add("certificateType");
				}
			}
			if (pType.getUri().equals("http://www.nimble-project.org/resource/ubl#freeOfCharge")) {

				if (!pType.getItemFieldNames().contains("freeOfCharge")) {
					pType.getItemFieldNames().add("freeOfCharge");
				}
			}
			if (pType.getUri().equals("http://www.nimble-project.org/resource/ubl#manufacturerId")) {

				if (!pType.getItemFieldNames().contains("manufacturerId")) {
					pType.getItemFieldNames().add("manufacturerId");
				}
			}
		}
		return r.getResult();
	}

	public List<Entity> detectPossibleConceptsLanguageSpecific(
			InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific) {
		List<Entity> result = new ArrayList<Entity>();
		String field = "_txt";
		Language language = inputParameterdetectMeaningLanguageSpecific.getLanguage();
		String prefixLanguage = Language.toOntologyPostfix(language).replaceAll("@", "");
		field = prefixLanguage + field;
		String keyword = inputParameterdetectMeaningLanguageSpecific.getKeyword();
		keyword = keyword.replace(" ", "*");
		String url = this.urlForClassInformation + "/select?" + "q=" + field + ":*" + keyword +"&rows=100";

		String resultString = invokeHTTPMethod(url);

		Gson gson = new Gson();
		ClassTypes r = gson.fromJson(resultString, ClassTypes.class);
		for (ClassType concept : r.getResult()) {
			
			if (checkWhetherIndividualsAreAvailable(concept)){
			
			Entity entity = new Entity();
			entity.setConceptSource(ConceptSource.ONTOLOGICAL);
			entity.setLanguage(inputParameterdetectMeaningLanguageSpecific.getLanguage());
			entity.setUrl(concept.getUri());
			entity.setTranslatedURL(concept.getLabel().get(prefixLanguage));
			entity.setHidden(false);
			result.add(entity);
		}
		}

		//

		return result;
	}

	private boolean checkWhetherIndividualsAreAvailable(ClassType concept) {
		// TODO Auto-generated method stub --
		String url = urlForItemInformation + "/select?fq=commodityClassficationUri:"
				+ URLEncoder.encode("\"" + concept.getUri() + "\"") +"&rows=1";
		String response = invokeHTTPMethod(url);
		// System.out.println(response);
		Gson gson = new Gson();
		SOLRResult result = gson.fromJson(response, SOLRResult.class);
		if (result.getResult() != null && result.getResult().size() > 0){
			return true;
		}
		return false;
	}

	/**
	 * This method creates the grapg based view for the explorative search. The
	 * depth can be defined.
	 * 
	 * @param paramterForGetLogicalView
	 * @return a logical view
	 */
	public String getLogicalView(InputParamterForGetLogicalView paramterForGetLogicalView) {

		OutputForGetLogicalView outputStructure = new OutputForGetLogicalView();
		LocalOntologyView completeStructure = new LocalOntologyView();
		eu.nimble.service.catalog.search.impl.dao.Entity concept = new eu.nimble.service.catalog.search.impl.dao.Entity();
		concept.setUrl(paramterForGetLogicalView.getConcept());

		boolean shallThePropertyCacheUpdated = !propertyInformationCache
				.isConceptAlreadyContained(paramterForGetLogicalView.getConcept());

		String url = urlForClassInformation + "?uri=" + URLEncoder.encode(paramterForGetLogicalView.getConcept());
		String resultString = invokeHTTPMethod(url);
		Gson gson = new Gson();
		ClassType r = gson.fromJson(resultString, ClassType.class);
		String prefixLanguage = Language.toOntologyPostfix(paramterForGetLogicalView.getLanguageAsLanguage())
				.replaceAll("@", "");

		concept.setUrl(paramterForGetLogicalView.getConcept());
		if (r != null) {
			concept.setTranslatedURL(r.getLabel().get(prefixLanguage));
		} else {
			concept.setTranslatedURL(paramterForGetLogicalView.getConcept());
		}
		concept.setLanguage(paramterForGetLogicalView.getLanguageAsLanguage());
		completeStructure.setConcept(concept);

		List<String> uriPath = new ArrayList<String>();
		uriPath.add(concept.getUrl());
		completeStructure.setConceptURIPath(uriPath);

		List<PropertyType> allPropertyTypes = new ArrayList<PropertyType>();

		if (shallThePropertyCacheUpdated) {

			requestAllPropertiesFromDifferentSources(paramterForGetLogicalView, completeStructure, concept, gson, r,
					prefixLanguage, allPropertyTypes);

		} else {
			Logger.getAnonymousLogger().log(Level.INFO, "Use the cached properties instead of asking them again: "
					+ paramterForGetLogicalView.getConcept());
			List<PropertyType> allProps = propertyInformationCache
					.getAllCachedPropertiesForAConcept(paramterForGetLogicalView.getConcept());
			allProps.forEach(pType -> addDetailsToProperty(completeStructure, concept, prefixLanguage, pType.getUri(),
					pType, pType.getConceptSource()));
		}

		outputStructure.setCompleteStructure(completeStructure);
		LocalOntologyView structureForView = completeStructure.getVisibleLocalOntologyViewStructure();
		outputStructure.setViewStructure(structureForView);
		outputStructure.setCurrentSelections(paramterForGetLogicalView.getCurrentSelections());

		String result = gson.toJson(outputStructure);
		return result;

	}

	private void requestAllPropertiesFromDifferentSources(InputParamterForGetLogicalView paramterForGetLogicalView,
			LocalOntologyView completeStructure, eu.nimble.service.catalog.search.impl.dao.Entity concept, Gson gson,
			ClassType r, String prefixLanguage, List<PropertyType> allPropertyTypes) {
		if (r != null && r.getProperties() != null) {

			for (String propertyURL : r.getProperties()) {

				boolean relevant = checkWhetherPropertyIsRelevant(propertyURL, paramterForGetLogicalView.getConcept());
				if (relevant) {
					eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = requestPropertyInfos(gson,
							propertyURL);
					if (propertyType.isVisible()) {
						allPropertyTypes.add(propertyType);
						propertyType.setConceptSource(ConceptSource.ONTOLOGICAL);
						addDetailsToProperty(completeStructure, concept, prefixLanguage, propertyURL, propertyType,
								ConceptSource.ONTOLOGICAL);
					}
				}
			}
		}

		List<PropertyType> propInfosUBL = requestStandardPropertiesFromUBL();
		for (PropertyType propertyType : propInfosUBL) {
			String propertyURL = propertyType.getUri();
			boolean relevant = checkWhetherPropertyIsRelevant(propertyURL, paramterForGetLogicalView.getConcept());
			if (propertyType.isVisible() && relevant) {
				allPropertyTypes.add(propertyType);
				propertyType.setConceptSource(ConceptSource.CUSTOM);
				addDetailsToProperty(completeStructure, concept, prefixLanguage, propertyType.getUri(), propertyType,
						ConceptSource.CUSTOM);
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"UBL specifc property is marked as not visible or has no property values in the catalogie: "
								+ propertyURL);
			}

		}

		List<PropertyType> propInfosStandard = requestStandardPropertiesFromUnknownSopurce();
		for (PropertyType propertyType : propInfosStandard) {
			if (propertyType.isVisible()) {
				allPropertyTypes.add(propertyType);
				propertyType.setConceptSource(ConceptSource.CUSTOM);
				addDetailsToProperty(completeStructure, concept, prefixLanguage, propertyType.getUri(), propertyType,
						ConceptSource.CUSTOM);
			}
		}

		// store The Property Types in the cache.
		if (!propertyInformationCache.isConceptAlreadyContained(paramterForGetLogicalView.getConcept())) {
			propertyInformationCache.addConcept(paramterForGetLogicalView.getConcept(), allPropertyTypes);
		}
	}

	public boolean checkWhetherPropertyIsRelevant(String propertyURL, String conceptURL) {

		if (indexFieldCache.isPropertyRelevanceInfoContained(conceptURL, propertyURL)) {
			return indexFieldCache.isPropertyRelevanceGiven(conceptURL, propertyURL);
		} else {
			String nameField = indexFieldCache.getIndexFieldForAOntologicalProperty(propertyURL);
			// Happens if no fieldname mapping is available
			if (nameField == null) {
				return false;
			}
			String url = urlForItemInformation + "/select?fq=commodityClassficationUri:"
					+ URLEncoder.encode("\"" + conceptURL + "\"") + "&fq=" + nameField + ":[*%20TO%20*]"
					+ "&facet.field==" + nameField + "&rows=1";
			String response = invokeHTTPMethod(url);
			// System.out.println(response);
			Gson gson = new Gson();
			SOLRResult result = gson.fromJson(response, SOLRResult.class);
			if (result != null && result.getResult().size() == 1) {

				PropertyRelevance propertyRelevance = new PropertyRelevance();
				propertyRelevance.setHasItBeenChecked(true);
				propertyRelevance.setItRelevant(true);
				propertyRelevance.setLastCheck(new Date());
				indexFieldCache.addProopertyRelevance(conceptURL, propertyURL, propertyRelevance);

				return true;
			} else {
				PropertyRelevance propertyRelevance = new PropertyRelevance();
				propertyRelevance.setHasItBeenChecked(true);
				propertyRelevance.setItRelevant(false);
				propertyRelevance.setLastCheck(new Date());
				indexFieldCache.addProopertyRelevance(conceptURL, propertyURL, propertyRelevance);

				return false;
			}
		}

	}

	private void addDetailsToProperty(LocalOntologyView completeStructure,
			eu.nimble.service.catalog.search.impl.dao.Entity concept, String prefixLanguage, String propertyURL,
			eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType, ConceptSource conceptSource) {
		eu.nimble.service.catalog.search.impl.dao.Entity entity = new eu.nimble.service.catalog.search.impl.dao.Entity();
		entity.setUrl(propertyURL);
		entity.setConceptSource(conceptSource);
		entity.setTranslatedURL(propertyType.getLabel().get(prefixLanguage));
		if (isItADatatypeProperty(propertyType)) {
			completeStructure.addDataproperties(entity);
		} else {
			LocalOntologyView localOntologyView2 = new LocalOntologyView();

			eu.nimble.service.catalog.search.impl.dao.Entity conceptRange = new eu.nimble.service.catalog.search.impl.dao.Entity();
			conceptRange.setUrl(propertyType.getRange());
			conceptRange.setTranslatedURL(entity.getTranslatedURL());
			// conceptRan

			localOntologyView2.setConcept(conceptRange);
			localOntologyView2.setObjectPropertySource(entity.getUrl());
			localOntologyView2.setFrozenConcept(concept.getUrl());
			localOntologyView2.setDistanceToFrozenConcept(1);
			List<String> newPaht = new ArrayList<String>(completeStructure.getConceptURIPath());
			newPaht.add(propertyType.getRange());
			localOntologyView2.setConceptURIPath(newPaht);
			completeStructure.getObjectproperties().put(propertyType.getRange(), localOntologyView2);
		}
	}

	private eu.nimble.service.catalog.search.impl.dao.PropertyType requestPropertyInfos(Gson gson, String propertyURL) {

		if (!propertyURL.contains(namespace) && (!propertyURL.contains(nameSPACEUBL))) {

			String url;
			eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = null;
			url = urlForPropertyInformation + "?uri=" + URLEncoder.encode(propertyURL);
			String propertyInfo = invokeHTTPMethod(url);
			try {
				propertyType = gson.fromJson(propertyInfo,
						eu.nimble.service.catalog.search.impl.dao.PropertyType.class);
			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"There is no propertyInfos available for: " + propertyURL + "URL: " + url);
			}
			return propertyType;
		}
		return null;
	}

	private boolean isItADatatypeProperty(eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType) {
		if (propertyType.getRange().contains(HTTP_WWW_W3_ORG_2001_XML_SCHEMA)) {
			return true;
		}
		return false;
	}

	public List<String> getAllDifferentValuesForAProperty(String conceptURL, String propertyURL) {
		Gson gson = new Gson();
		List<String> allValues = new ArrayList<String>();
		eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = requestPropertyInfosFromCache(conceptURL,
				propertyURL);

		if (propertyType != null) {
			String url = urlForItemInformation + "/select?fq=commodityClassficationUri:"
					+ URLEncoder.encode(conceptURL);

			if (indexFieldCache.isIndexFieldInfoContained(propertyURL)) {
				String fieldName = indexFieldCache.getIndexFieldForAOntologicalProperty(propertyURL);
				url += "&fq=" + fieldName + ":[*%20TO%20*]&facet.field=" + fieldName;
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"Found no index field dliverd by /index/item/fields: " + propertyURL);
			}

			String items = invokeHTTPMethod(url);
			JSONObject jsonObject = new JSONObject(items);
			JSONArray results = jsonObject.getJSONArray("result");
			if (results != null) {
				for (String fieldName : propertyType.getItemFieldNames()) {
					for (int i = 0; i < results.length(); i++) {
						JSONObject ob = (JSONObject) results.get(i);
						extractValuesOfAFieldName(allValues, fieldName, ob);
					}
				}
				if (allValues.isEmpty()) {
					gson = new Gson();
					SOLRResult result = gson.fromJson(items, SOLRResult.class);
					for (eu.nimble.service.catalog.search.impl.dao.item.ItemType itemType : result.getResult()) {

						// I have to decide which kind of proepty it is. Each
						// kind needs
						// an one extraction process
						PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(conceptURL,
								propertyURL);

						if (pType != null) {

							if (pType.getConceptSource().equals(ConceptSource.ONTOLOGICAL)) {
								boolean contained = false;
								String value = "";
								if (itemType.getBooleanValue() != null
										&& itemType.getBooleanValueDirect().containsKey(propertyURL)) {
									contained = true;
									value = String.valueOf(itemType.getBooleanValueDirect().get(propertyURL));
								}

								if (itemType.getDoubleValue() != null
										&& itemType.getDoubleValueDirect().containsKey(propertyURL)) {
									contained = true;
									value = String.valueOf(itemType.getDoubleValueDirect().get(propertyURL));
								}

								if (itemType.getStringValue() != null
										&& itemType.getStringValueDirect().containsKey(propertyURL)) {
									contained = true;
									Collection<String> valuesInDifferentLangauges = itemType.getStringValueDirect()
											.get(propertyURL);
									// TODO need to be chnaged
									Language chosenLangauge = Language.ENGLISH;
									String lPrefix = "@" + LanguageAdapter.createPrefix(chosenLangauge);
									Iterator<String> iterator = valuesInDifferentLangauges.iterator();
									boolean found = false;
									String anyValue = null;
									while (iterator.hasNext()) {
										String v = iterator.next();
										if (anyValue == null) {
											anyValue = v;
										}
										value = v.replace(lPrefix, "");
										found = true;
									}
									if (!found) {
										value = anyValue;
									}
									// value =
									// String.valueOf(itemType.getStringValueDirect().get(propertyURL));
								}

								if (contained) {
									if (!allValues.contains(value)) {
										allValues.add(value);
									}
								}

							}
						}
					}
				}
				// propertyType.getItemFieldNames()?
				return allValues;
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING, "Cannot get property values from: " + propertyURL);
				return Collections.EMPTY_LIST;
			}
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot get property type from: " + propertyURL);
			return Collections.EMPTY_LIST;

		}

	}

	private PropertyType requestPropertyInfosFromCache(String conceptURL, String propertyURL) {
		if (propertyInformationCache.isConceptAlreadyContained(conceptURL)) {
			return propertyInformationCache.getPropertyTypeForASingleProperty(conceptURL, propertyURL);
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"getDifferentValues has been executed before getLogicalView/getProperties. Is not valid");
		}
		return null;
	}

	private void extractValuesOfAFieldName(List<String> allValues, String fieldName, JSONObject ob) {
		if (ob.has(fieldName)) {
			Object targetValue = ob.get(fieldName);
			if (targetValue instanceof JSONObject) {
				JSONObject elment = (JSONObject) targetValue;
				if (elment.keySet().size() == 1) {
					elment.keys().forEachRemaining(x -> {
						String v = elment.get(x).toString();
						if (!allValues.contains(v)) {
							allValues.add(v);
						}
					});

				} else {
					Logger.getAnonymousLogger().log(Level.WARNING,
							"More than one key. what is the default key fpr the value:" + fieldName);
				}
			}
			if (targetValue instanceof String) {
				String v = String.valueOf(targetValue);
				if (!allValues.contains(v)) {
					allValues.add(v);
				}
			}

			if (targetValue instanceof Integer) {
				String v = String.valueOf(targetValue);
				if (!allValues.contains(v)) {
					allValues.add(v);
				}
			}

			if (targetValue instanceof Float) {
				String v = String.valueOf(targetValue);
				if (!allValues.contains(v)) {
					allValues.add(v);
				}
			}
			if (targetValue instanceof Double) {
				String v = String.valueOf(targetValue);
				if (!allValues.contains(v)) {
					allValues.add(v);
				}
			}
			if (targetValue instanceof Boolean) {
				String v = String.valueOf(targetValue);
				if (!allValues.contains(v)) {
					allValues.add(v);
				}
			}

		}
	}

	/**
	 * This method is used to request all property vlaues for a given product
	 * (individual/catalogue item). The name is a relict from the ontology
	 * driven solution
	 * 
	 * @param inputParamaterForExecuteOptionalSelect
	 * @return {@link OutputForExecuteSelect}
	 */
	public OutputForExecuteSelect createOPtionalSPARQLAndExecuteIT(
			InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect) {
		String uri = inputParamaterForExecuteOptionalSelect.getUuid();
		String url = urlForItemInformation + "/select?fq=uri:" + uri;
		OutputForExecuteSelect result = new OutputForExecuteSelect();
		// result.setInput(inputParamaterForExecuteOptionalSelect);
		result.getUuids().add(inputParamaterForExecuteOptionalSelect.getUuid());
		String response = invokeHTTPMethod(url);
		Gson gson = new Gson();
		SOLRResult responseObject = gson.fromJson(response, SOLRResult.class);
		JSONObject jsonObject = new JSONObject(response);
		JSONArray results = jsonObject.getJSONArray("result");
		Map<PropertyType, List<List<String>>> intermediateResult = new HashMap<PropertyType, List<List<String>>>();
		List<PropertyType> allProps = new ArrayList<PropertyType>();
		if (results != null) {

			// Have to define the header information. It is based on standrad,
			// ubl and common result
			allProps.addAll(propertyInformationCache.getAllStandardProps());
			allProps.addAll(propertyInformationCache.getAllUBLSpeciifcProperties());
			// have to collect ontological property
			ItemType itemType = responseObject.getResult().get(0);
			for (String key : itemType.getBooleanValueDirect().keySet()) {
				PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
				allProps.add(pType);
			}
			for (String key : itemType.getDoubleValueDirect().keySet()) {
				PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
				allProps.add(pType);
			}
			for (String key : itemType.getStringValueDirect().keySet()) {
				PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
				allProps.add(pType);
			}

			// Create intermediate result + columns list
			for (PropertyType property : allProps) {
				if (property == null) {
					continue;
				}
				List<List<String>> r = new ArrayList<List<String>>();
				intermediateResult.put(property, r);
				Language lang = inputParamaterForExecuteOptionalSelect.getLanguage();
				if (lang != null && !(lang.equals(Language.UNKNOWN))) {
					String currentLabel = getLanguageLabel(property,
							inputParamaterForExecuteOptionalSelect.getLanguage());
					result.getColumns().add(currentLabel);
				} else {
					String currentLabel = getLanguageLabel(property, Language.ENGLISH);
					result.getColumns().add(currentLabel);
				}
			}

			for (int i = 0; i < results.length(); i++) {
				JSONObject ob = (JSONObject) results.get(i);
				updateCacheForFieldNames(ob);

				for (String fieldName : ob.keySet()) {
					if (propertyInformationCache.isNameFieldAlreadyContained(fieldName)) {
						PropertyType propertyType = propertyInformationCache.getPropertyTypeForANameField(fieldName);

						boolean isContained = false;
						if (propertyType != null) {
							for (PropertyType p : intermediateResult.keySet()) {
								if (p.equals(propertyType)) {
									isContained = true;
								}
							}
						}

						if (isContained) {

							List<String> allValues = new ArrayList<String>();
							extractValuesOfAFieldName(allValues, fieldName, ob);
							intermediateResult.get(propertyType).add(allValues);

							// result.getRows().add((ArrayList<String>)
							// allValues);

						}

					} else {
						if (((fieldName.equals("stringValue")) || (fieldName.equals("doubleValue"))
								|| (fieldName.equals("booleanValue")))) {
							// have to extract ontological properties TODO
							// Logger.getAnonymousLogger().log(Level.INFO,
							// "Ontological propperty not supported yet...");
						}
					}
				}

				// use the Java object to get access to the ontological
				// properties. It just simplier that using JSON
				ItemType item = responseObject.getResult().get(i);
				for (String key : item.getStringValueDirect().keySet()) {
					PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
					if (pType != null) {

						Collection<String> valuesInDifferentLangauges = itemType.getStringValueDirect().get(pType.getUri());
						Language chosenLangauge = inputParamaterForExecuteOptionalSelect.getLanguage();
						String lPrefix = "@" + LanguageAdapter.createPrefix(chosenLangauge);
						Iterator<String> iterator = valuesInDifferentLangauges.iterator();
						boolean found = true;
						String anyValue = null;
						String value = null;
						while (iterator.hasNext()) {
							String v = iterator.next();
							if (anyValue == null) {
								anyValue = v.substring(0, v.indexOf("@"));
							}
							if (v.contains(lPrefix)) {
								value = v.replace(lPrefix, "");

							}
						}
						if (value == null) {
							value = anyValue;
						}

						Collection<String> values = item.getStringValueDirect().get(pType.getUri());
						List<String> list = new ArrayList();
						list.add(value);
						intermediateResult.get(pType).add(list);
					}

				}
				for (String key : item.getDoubleValueDirect().keySet()) {
					PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
					if (pType != null) {
						Collection<Double> values = item.getDoubleValueDirect().get(pType.getUri());
						List<String> list = new ArrayList(values);
						intermediateResult.get(pType).add(list);
					}

				}

				for (String key : item.getBooleanValueDirect().keySet()) {
					PropertyType pType = propertyInformationCache.getPropertyTypeForASingleProperty(key);
					if (pType != null) {
						Boolean values = item.getBooleanValueDirect().get(pType.getUri());
						List<String> list = new ArrayList();
						list.add(String.valueOf(values));
						intermediateResult.get(pType).add(list);
					}
				}

			}

		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot get property values from: " + uri);
		}

		if (intermediateResult.size() > 0) {
			PropertyType firstProperty = intermediateResult.keySet().iterator().next();
			int resultSize = 1;

			for (int i = 0; i < resultSize; i++) {
				ArrayList<String> oneRow = new ArrayList<String>();
				result.getRows().add(oneRow);
				for (PropertyType property : allProps) {
					if (property != null) {
						if (intermediateResult.get(property).size() > i) {
							oneRow.add(intermediateResult.get(property).get(i).toString());
						} else {
							oneRow.add(N_ULL);
						}
					}
					// result.getRows().add(oneRow);
				}
			}

		}
		return result;
	}

	private void updateCacheForFieldNames(JSONObject ob) {
		JSONArray conceptURIArray = ob.getJSONArray("classificationUri");
		conceptURIArray.forEach(x -> {
			String conceptURL = (String) x;
			if (!propertyInformationCache.isConceptAlreadyContained(conceptURL)) {
				getAllPropertiesIncludingEverything(conceptURL);
			}
		});
	}

	private String getLanguageLabel(PropertyType propertyType, Language language) {
		String prefixLanguage = Language.toOntologyPostfix(language).replaceAll("@", "");
		String label = propertyType.getLabel().get(prefixLanguage);
		if (label != null) {
			return label;
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"Found no language label for: " + propertyType.getUri() + " for language: " + language);
			return propertyType.getLocalName();
		}
	}

	/**
	 * This method returns all mappable fieldNames which can be used to filter a
	 * product with specific property values
	 * 
	 * @return
	 */
	public List<ItemMappingFieldInformation> getAllMappableFields() {
		List<ItemMappingFieldInformation> result = new ArrayList<ItemMappingFieldInformation>();
		String url = urlForItemInformation + "/fields";
		String respoonse = invokeHTTPMethod(url);
		Gson gson = new Gson();
		List<ItemMappingFieldInformation> r = gson.fromJson(respoonse, List.class);
		// System.out.println(r);
		if (r != null) {
			result.addAll(r);
		}
		return result;

	}

	public OutputForExecuteSelect createSPARQLAndExecuteIT(
			InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {
		// TODO Auto-generated method stub
		/**
		 * http://nimble-staging.salzburgresearch.at/index/item/select?fq=commodityClassficationUri:%22http://www.aidimme.es/FurnitureSectorOntology.owl%23Product%22&fq=localName:540*&fq=price:*
		 * e
		 */
		String url = urlForItemInformation + "/select?fq=commodityClassficationUri:"
				+ URLEncoder.encode("\"" + inputParamaterForExecuteSelect.getConcept() + "\"");

		// I have to adapt to existing propoerties
		for (String propertyURL : inputParamaterForExecuteSelect.getParametersURL()) {
			String fieldName = indexFieldCache.getIndexFieldForAOntologicalProperty(propertyURL);
			if (fieldName != null) {
				url += "&fq=" + fieldName + ":[*%20TO%20*]";
			}
		}

		String response = invokeHTTPMethod(url);
		// System.out.println(response);
		Gson gson = new Gson();
		SOLRResult result = gson.fromJson(response, SOLRResult.class);
		List<String> columns = new ArrayList<String>();
		columns.addAll(inputParamaterForExecuteSelect.getParameters());
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();

		outputForExecuteSelect.setColumns(columns);
		outputForExecuteSelect.setInput(inputParamaterForExecuteSelect);
		List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		outputForExecuteSelect.setRows(rows);
		int index = 0;
		for (eu.nimble.service.catalog.search.impl.dao.item.ItemType itemType : result.getResult()) {
			outputForExecuteSelect.getUuids().add(itemType.getUri());
			ArrayList<String> row = new ArrayList<String>();
			rows.add(row);
			for (String propertyURL : inputParamaterForExecuteSelect.getParametersURL()) {

				// I have to decide which kind of proepty it is. Each kind needs
				// an one extraction process
				PropertyType pType = propertyInformationCache
						.getPropertyTypeForASingleProperty(inputParamaterForExecuteSelect.getConcept(), propertyURL);

				if (pType != null) {

					if (pType.getConceptSource().equals(ConceptSource.ONTOLOGICAL)) {
						boolean contained = false;
						String value = "";
						if (itemType.getBooleanValue() != null
								&& itemType.getBooleanValueDirect().containsKey(propertyURL)) {
							contained = true;
							value = String.valueOf(itemType.getBooleanValueDirect().get(propertyURL));
						}

						if (itemType.getDoubleValue() != null
								&& itemType.getDoubleValueDirect().containsKey(propertyURL)) {
							contained = true;
							value = String.valueOf(itemType.getDoubleValueDirect().get(propertyURL));
						}

						if (itemType.getStringValue() != null
								&& itemType.getStringValueDirect().containsKey(propertyURL)) {
							contained = true;
							Collection<String> valuesInDifferentLangauges = itemType.getStringValueDirect()
									.get(propertyURL);
							Language chosenLangauge = inputParamaterForExecuteSelect.getLanguage();
							String lPrefix = "@" + LanguageAdapter.createPrefix(chosenLangauge);
							Iterator<String> iterator = valuesInDifferentLangauges.iterator();
							boolean found = false;
							String anyValue = null;
							while (iterator.hasNext()) {
								String v = iterator.next();
								if (anyValue == null) {
									anyValue = v.substring(0, v.indexOf("@"));
								}
								if (v.contains(lPrefix)) {
									value = v.replace(lPrefix, "");
									found = true;
								}
							}
							if (!found) {
								value = anyValue;
							}
							// value =
							// String.valueOf(itemType.getStringValueDirect().get(propertyURL));
						}

						if (itemType.getCustomProperties() != null
								&& itemType.getCustomProperties().containsKey(propertyURL)) {
							contained = true;
							value = String.valueOf(itemType.getCustomProperties().get(propertyURL));
						}

						if (contained) {
							row.add(value);
						} else {
							Logger.getAnonymousLogger().log(Level.SEVERE,
									"found no value for requested property: " + propertyURL);
							row.add(N_ULL);
						}
					} else {
						// Custom Properties using extractionMethod
						List<String> allValues = new ArrayList<String>();
						Iterator iterator = pType.getItemFieldNames().iterator();
						while (iterator.hasNext()) {
							String fieldName = (String) iterator.next();

							JSONObject jsonObject = new JSONObject(response);
							JSONArray results = jsonObject.getJSONArray("result");
							if (results != null) {

								JSONObject ob = (JSONObject) results.get(index);
								extractValuesOfAFieldName(allValues, fieldName, ob);

							}

						}
						if (allValues.size() > 0) {
							row.add(allValues.get(0));
						} else {
							Logger.getAnonymousLogger().log(Level.WARNING, "Found no value for: " + pType.getUri());
							row.add(N_ULL);
						}
					}

				}

			}
			index++;

		}
		return outputForExecuteSelect;
	}

	public OutputForPropertiesFromConcept getAllTransitiveProperties(String conceptURL, Language language) {

		OutputForPropertiesFromConcept result = new OutputForPropertiesFromConcept();
		String prefixLanguage = Language.toOntologyPostfix(language).replaceAll("@", "");

		List<String> properties = getAllPropertiesIncludingEverything(conceptURL);
		for (String urlOfProperty : properties) {
			PropertyType propertyType = propertyInformationCache.getPropertyTypeForASingleProperty(conceptURL,
					urlOfProperty);
			OutputForPropertyFromConcept outputForPropertyFromConcept = new OutputForPropertyFromConcept();
			outputForPropertyFromConcept.setPropertyURL(urlOfProperty);
			if (propertyType.getLabel() != null) {
				String label = propertyType.getLabel().get(prefixLanguage);
				if (label == null) {
					if (propertyType.getLabel().containsKey(LanguageAdapter.getEnglishPrefix())) {
						label = propertyType.getLabel().get(LanguageAdapter.getEnglishPrefix());
					}
				}
				outputForPropertyFromConcept.setTranslatedProperty(label);
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING,
						prefixLanguage + "Found no translation for: " + urlOfProperty);
				String label = urlOfProperty.substring(urlOfProperty.indexOf("#") + 1);
				outputForPropertyFromConcept.setTranslatedProperty(label);

			}
			outputForPropertyFromConcept.setPropertySource(PropertySource.DOMAIN_SPECIFIC_PROPERTY);
			if (propertyType.getRange().contains(HTTP_WWW_W3_ORG_2001_XML_SCHEMA)) {
				outputForPropertyFromConcept.setDatatypeProperty(true);
				outputForPropertyFromConcept.setObjectProperty(false);
			} else {
				outputForPropertyFromConcept.setDatatypeProperty(false);
				outputForPropertyFromConcept.setObjectProperty(true);
			}
			result.getOutputForPropertiesFromConcept().add(outputForPropertyFromConcept);
		}

		return result;

	}

	public Map<String, List<Group>> generateGroup(int amountOfGroups, String conceptURL, String propertyURL,
			Language language) {

		String prefixLanguage = Language.toOntologyPostfix(language).replaceAll("@", "");
		PropertyType propertyType = propertyInformationCache.getPropertyTypeForASingleProperty(conceptURL, propertyURL);

		String shortPropertyName = propertyType.getLabel().get(prefixLanguage);

		List<String> values = getAllDifferentValuesForAProperty(conceptURL, propertyURL);
		for (int i = 0; i < values.size(); i++) {
			String str = values.get(i);
			int index = str.lastIndexOf("^");
			if (index > -1) {
				str = str.substring(0, index - 1);
			}
			str = str.replace(",", ".");
			values.set(i, str);
		}
		if (values != null && values.size() > 0) {
			try {
				ValueGroupingFactory valueGroupingFactory = new ValueGroupingFactory();
				return valueGroupingFactory.generateGrouping(amountOfGroups, values, shortPropertyName);

			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"Cannot transform data from " + propertyURL + " into floats");
			}
		} else {
			return new HashMap<String, List<Group>>();
		}
		return new HashMap<String, List<Group>>();
	}

	public List<String> getSupportedLanguages() {
		// TODO Auto-generated method stub
		List<PropertyType> allProps = requestStandardPropertiesFromUBL();
		List<String> allSupportedLanguages = new ArrayList<>();
		for (PropertyType pType : allProps) {
			pType.getLabel().keySet().forEach(x -> {
				if (!allSupportedLanguages.contains(String.valueOf(x))) {
					allSupportedLanguages.add(x);
				}
			});
		}
		return allSupportedLanguages;
	}

}
