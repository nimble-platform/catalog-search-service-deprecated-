package eu.nimble.service.catalog.search.impl.Indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import eu.nimble.service.catalog.search.impl.dao.ClassType;
import eu.nimble.service.catalog.search.impl.dao.ClassTypes;
import eu.nimble.service.catalog.search.impl.dao.ItemMappingFieldInformation;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.PropertyType;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.item.ItemType;
import eu.nimble.service.catalog.search.impl.dao.item.SOLRResult;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForGetLogicalView;

public class IndexingServiceReader {

	private String url = "";
	private String urlForClassInformation = "";
	private String urlForPropertyInformation = "";
	private String urlForItemInformation = "";
	private PropertyInformationCache propertyInformationCache = new PropertyInformationCache();

	public IndexingServiceReader(String url) {
		super();
		this.url = url;
		if (url.charAt(url.length() - 1) != '/') {
			url += "/";
		}
		urlForClassInformation = url + "class";
		urlForPropertyInformation = url + "property";
		urlForItemInformation = url + "item";
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

	public List<String> getAllPropertiesIncludingEverything(String urlOfClass) {

		String httpGetURL = urlForClassInformation + "?uri=" + URLEncoder.encode(urlOfClass);
		String result = invokeHTTPMethod(httpGetURL);
		System.out.println(result);
		List<String> allProperties = new ArrayList<String>();
		Gson gson = new Gson();
		ClassType r = gson.fromJson(result, ClassType.class);
		r.getProperties().forEach(x -> allProperties.add(x));
		allProperties.add(result);

		if (!propertyInformationCache.isConceptAlreadyContained(urlOfClass)) {
			List<PropertyType> propInfos = new ArrayList<PropertyType>();
			for (String propertyURL : allProperties) {
				propInfos.add(requestPropertyInfos(gson, propertyURL));
			}
			if (propInfos.size() > 0) {
				propertyInformationCache.addConcept(urlOfClass, propInfos);
			} else {
				Logger.getAnonymousLogger().log(Level.WARNING, "Cannot request property infos of class " + urlOfClass);
			}

		}

		return allProperties;
	}

	public List<Entity> detectPossibleConceptsLanguageSpecific(
			InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific) {
		List<Entity> result = new ArrayList<Entity>();
		String field = "_txt";
		Language language = inputParameterdetectMeaningLanguageSpecific.getLanguage();
		String prefixLanguage = Language.toOntologyPostfix(language).replaceAll("@", "");
		field = prefixLanguage + field;

		String url = this.urlForClassInformation + "/select?" + "q=" + field + ":*"
				+ inputParameterdetectMeaningLanguageSpecific.getKeyword();

		String resultString = invokeHTTPMethod(url);

		Gson gson = new Gson();
		ClassTypes r = gson.fromJson(resultString, ClassTypes.class);
		for (ClassType concept : r.getResult()) {
			Entity entity = new Entity();
			entity.setConceptSource(ConceptSource.ONTOLOGICAL);
			entity.setLanguage(inputParameterdetectMeaningLanguageSpecific.getLanguage());
			entity.setUrl(concept.getUri());
			entity.setTranslatedURL(concept.getLabel().get(prefixLanguage));
			entity.setHidden(false);
			result.add(entity);
		}

		//

		return result;
	}

	public String getLogicalView(InputParamterForGetLogicalView paramterForGetLogicalView) {
		// TODO Auto-generated method stub

		OutputForGetLogicalView outputStructure = new OutputForGetLogicalView();
		LocalOntologyView completeStructure = new LocalOntologyView();
		eu.nimble.service.catalog.search.impl.dao.Entity concept = new eu.nimble.service.catalog.search.impl.dao.Entity();
		concept.setUrl(paramterForGetLogicalView.getConcept());

		String url = urlForClassInformation + "?uri=" + URLEncoder.encode(paramterForGetLogicalView.getConcept());
		String resultString = invokeHTTPMethod(url);
		System.out.println(resultString);
		Gson gson = new Gson();
		ClassType r = gson.fromJson(resultString, ClassType.class);
		String prefixLanguage = Language.toOntologyPostfix(paramterForGetLogicalView.getLanguageAsLanguage())
				.replaceAll("@", "");

		concept.setUrl(paramterForGetLogicalView.getConcept());
		concept.setTranslatedURL(r.getLabel().get(prefixLanguage));
		concept.setLanguage(paramterForGetLogicalView.getLanguageAsLanguage());
		completeStructure.setConcept(concept);

		List<String> uriPath = new ArrayList<String>();
		uriPath.add(concept.getUrl());
		completeStructure.setConceptURIPath(uriPath);

		for (String propertyURL : r.getProperties()) {

			eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = requestPropertyInfos(gson,
					propertyURL);
			eu.nimble.service.catalog.search.impl.dao.Entity entity = new eu.nimble.service.catalog.search.impl.dao.Entity();
			entity.setUrl(propertyURL);
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

		outputStructure.setCompleteStructure(completeStructure);
		LocalOntologyView structureForView = completeStructure.getVisibleLocalOntologyViewStructure();
		outputStructure.setViewStructure(structureForView);
		outputStructure.setCurrentSelections(paramterForGetLogicalView.getCurrentSelections());

		String result = gson.toJson(outputStructure);
		return result;

	}

	private eu.nimble.service.catalog.search.impl.dao.PropertyType requestPropertyInfos(Gson gson, String propertyURL) {
		String url;
		eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = null;
		url = urlForPropertyInformation + "?uri=" + URLEncoder.encode(propertyURL);
		String propertyInfo = invokeHTTPMethod(url);
		try {
			propertyType = gson.fromJson(propertyInfo, eu.nimble.service.catalog.search.impl.dao.PropertyType.class);
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"There is no propertyInfos available for: " + propertyURL + "URL: " + url);
		}
		return propertyType;
	}

	private boolean isItADatatypeProperty(eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType) {
		if (propertyType.getRange().contains("http://www.w3.org/2001/XMLSchema#")) {
			return true;
		}
		return false;
	}

	public List<String> getAllDifferentValuesForAProperty(String conceptURL, String propertyURL) {
		Gson gson = new Gson();
		List<String> allValues = new ArrayList<String>();
		eu.nimble.service.catalog.search.impl.dao.PropertyType propertyType = requestPropertyInfos(gson, propertyURL);

		String url = urlForItemInformation + "/select?fq=commodityClassficationUri:" + URLEncoder.encode(conceptURL);
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
			// propertyType.getItemFieldNames()?
			return allValues;
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot get property values from: " + propertyURL);
			return Collections.EMPTY_LIST;
		}

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

		}
	}

	public OutputForExecuteSelect createOPtionalSPARQLAndExecuteIT(
			InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect) {
		String uri = inputParamaterForExecuteOptionalSelect.getUuid();
		String url = urlForItemInformation + "/select?fq=uri:" + uri;
		OutputForExecuteSelect result = new OutputForExecuteSelect();

		String response = invokeHTTPMethod(url);

		JSONObject jsonObject = new JSONObject(response);
		JSONArray results = jsonObject.getJSONArray("result");
		if (results != null) {

			for (int i = 0; i < results.length(); i++) {
				JSONObject ob = (JSONObject) results.get(i);
				updateCacheForFieldNames(ob);

				for (String fieldName : ob.keySet()) {
					if (propertyInformationCache.isNameFieldAlreadyContained(fieldName)) {
						PropertyType propertyType = propertyInformationCache.getPropertyTypeForANameField(fieldName);
						String currentLabel = getLanguageLabel(propertyType,
								inputParamaterForExecuteOptionalSelect.getLanguage());
						List<String> allValues = new ArrayList<String>();
						extractValuesOfAFieldName(allValues, fieldName, ob);
						result.getColumns().add(currentLabel);
						result.getRows().add((ArrayList<String>) allValues);
					}
				}
			}

		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot get property values from: " + uri);
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
		System.out.println(r);
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
		String response = invokeHTTPMethod(url);
		System.out.println(response);
		Gson gson = new Gson();
		SOLRResult result = gson.fromJson(response, SOLRResult.class);
		List<String> columns = new ArrayList<String>();
		columns.addAll(inputParamaterForExecuteSelect.getParameters());
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();

		outputForExecuteSelect.setColumns(columns);
		outputForExecuteSelect.setInput(inputParamaterForExecuteSelect);
		List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
		outputForExecuteSelect.setRows(rows);
		

		for (ItemType itemType : result.getResult()) {
			outputForExecuteSelect.getUuids().add(itemType.getUri());
			ArrayList<String> row = new ArrayList<String>();
			rows.add(row);
			for (String propertyURL : inputParamaterForExecuteSelect.getParametersURL()) {
				
				boolean contained = false;
				String value = "";
				if (itemType.getBooleanValue() != null && itemType.getBooleanValue().containsKey(propertyURL)){
					contained = true;
					value = String.valueOf(itemType.getBooleanValue().get(propertyURL));
				}
				
				if (itemType.getDoubleValue()!= null && itemType.getDoubleValue().containsKey(propertyURL)){
					contained = true;
					value = String.valueOf(itemType.getDoubleValue().get(propertyURL));
				}
				
				if (itemType.getStringValue() != null &&itemType.getStringValue().containsKey(propertyURL)){
					contained = true;
					value = String.valueOf(itemType.getStringValue().get(propertyURL));
				}
				
				if (itemType.getCustomProperties()!= null && itemType.getCustomProperties().containsKey(propertyURL)){
					contained = true;
					value = String.valueOf(itemType.getCustomProperties().get(propertyURL));
				}
				
				if (contained){
					row.add(value);
				}
				else{
					Logger.getAnonymousLogger().log(Level.SEVERE, "found no value for requested property: " + propertyURL);
					row.add("NUll");
				}
			}

		}


		System.out.println(result);
		return outputForExecuteSelect;
	}

}
