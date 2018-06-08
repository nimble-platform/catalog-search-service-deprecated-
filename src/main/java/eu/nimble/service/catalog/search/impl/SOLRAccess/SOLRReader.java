package eu.nimble.service.catalog.search.impl.SOLRAccess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
//import org.apache.solr.client.solrj.impl.HttpSolrClient;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.dmo.Filter;
import de.biba.triple.store.access.dmo.IndividualInformation;
import de.biba.triple.store.access.dmo.ObjectPropertyToDatatypePropertyMapping;
import de.biba.triple.store.access.dmo.PropertyConceptAssignment;
import de.biba.triple.store.access.dmo.PropertyInformation;
import de.biba.triple.store.access.enums.ConceptSource;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.factories.ValueGroupingFactory;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForGetReferencesFromAConcept;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.TranslationResult;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SOLRReader implements IReader {

	private static final String LMF_URI = "lmf.uri";
	private static final String FIELD_FOR_PROPERTY_URI = LMF_URI;
	private HttpSolrClient client = null;
	private HttpSolrClient clientForIntensionalQueriesProperties = null;
	private HttpSolrClient clientForIntensionalQueriesConcepts = null;
	private String url = "https://nimble-platform.salzburgresearch.at/marmotta/solr/catalogue2";
	private String urlForIntensionalQueriesProperties = "https://nimble-platform.salzburgresearch.at/marmotta/solr/properties";
	private String urlForIntensionalQueriesConcepts = "https://nimble-platform.salzburgresearch.at/marmotta/solr/Concepts";

	private final String labelFieldForSpanish = "label_es";
	private final String labelFieldForEnglish = "label_en";
	private final String labelFieldForGerman = "label_de";

	private EntityMappingService entityMappingService;

	public SOLRReader() {
		init();
	}

	public void init() {
		SystemDefaultHttpClient httpClient = new SystemDefaultHttpClient();
		// client = new HttpSolrClient.Builder(url).build();
		// clientForIntensionalQueries = new
		// HttpSolrClient.Builder(urlForIntensionalQueries).build();
		client = new HttpSolrClient(url, httpClient);
		clientForIntensionalQueriesProperties = new HttpSolrClient(urlForIntensionalQueriesProperties, httpClient);
		clientForIntensionalQueriesConcepts = new HttpSolrClient(urlForIntensionalQueriesConcepts, httpClient);

		entityMappingService = new EntityMappingService();
	}

	public SOLRReader(String url) {
		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}

		urlForIntensionalQueriesProperties = url + "/" + "properties";
		urlForIntensionalQueriesConcepts = url + "/" + "Concepts";
		this.url = url + "/" + "catalogue2";

		this.url = url;

		init();
	}

	public SOLRReader(String url, String urlForIntensionalQueriesProperties, String urlForIntensionalQueriesConcepts) {
		super();
		this.url = url;
		this.urlForIntensionalQueriesProperties = urlForIntensionalQueriesProperties;
		this.urlForIntensionalQueriesConcepts = urlForIntensionalQueriesConcepts;
		init();
	}

	public Map<String, String> createKeyValueOfAllPropertiesFromResultList(Object resultSet, Language language) {
		QueryResponse response = (QueryResponse) resultSet;
		SolrDocumentList results = response.getResults();
		Map<String, String> valueMap = new HashMap<String, String>();
		for (int i = 0; i < results.size(); ++i) {
			SolrDocument solrDocument = results.get(i);
			Collection<String> columns = solrDocument.getFieldNames();
			for (String column : columns) {
				if (entityMappingService.isAPropertySpecificField(column)) {
					String propertyURI = entityMappingService.mapFieldNameToProperty(column);
					String propertyTranslated = translateProperty(propertyURI, language);
					valueMap.put(propertyURI, String.valueOf(solrDocument.getFieldValue(column)));
				}
			}
		}
		return valueMap;
	}

	@Override
	public List<String> createResultList(Object arg0, String arg1) {
		List<String> result = new ArrayList<String>();
		QueryResponse response = (QueryResponse) arg0;
		SolrDocumentList results = response.getResults();
		for (int i = 0; i < results.size(); ++i) {
			SolrDocument solrDocument = (results.get(i));
			String value = String.valueOf(solrDocument.getFieldValue(arg1));
			String[] allValues = value.split(",");
			for (String oneValue : allValues) {
				oneValue = postProcessSolrValue(oneValue);
				if (!result.contains(oneValue)) {
					result.add(oneValue);
				}
			}

		}
		return result;
	}

	public String postProcessSolrValue(String oneValue) {
		oneValue = oneValue.replace("[", "");
		oneValue = oneValue.replace("]", "");
		if (oneValue.length() > 1) {
			if (oneValue.charAt(0) == ' ') {
				oneValue = oneValue.substring(1);
			}
			if (oneValue.charAt(oneValue.length() - 1) == ' ') {
				oneValue = oneValue.substring(0, oneValue.length() - 1);
			}
		}

		return oneValue;
	}

	@Override
	public Model getBaseModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataSetURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSupportedLangauges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrlForQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean loadOntologyModel(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object query(String arg0) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		// query.addFilterQuery("cat:electronics","store:amazon.com");
		query.setFields("*");
		query.setStart(0);
		// query.set("defType", "edismax");
		try {
			QueryResponse response = client.query(query);
			return response;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	public Object query(String arg0, String field) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		query.setFields(field);
		// query.addFilterQuery("cat:electronics","store:amazon.com");
		query.setStart(0);
		// query.set("defType", "edismax");
		try {
			QueryResponse response = client.query(query);
			return response;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	public Object query(String arg0, String field, String... filter) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		query.setFields(field);
		query.setFilterQueries(filter);
		query.setStart(0);
		try {
			QueryResponse response = client.query(query);
			return response;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	public Object queryIntensionalProperties(String arg0) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		// query.addFilterQuery("cat:electronics","store:amazon.com");
		query.setFields("*");
		query.setStart(0);
		// query.set("defType", "edismax");
		try {
			QueryResponse response = clientForIntensionalQueriesProperties.query(query);
			return response;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	public Object queryIntensionalConcepts(String arg0) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		// query.addFilterQuery("cat:electronics","store:amazon.com");
		query.setFields("*");
		query.setStart(0);
		// query.set("defType", "edismax");
		try {
			QueryResponse response = clientForIntensionalQueriesConcepts.query(query);
			return response;
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
		}
		return null;
	}

	@Override
	public ResultSet queryLocal(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryRemote(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTypeDescriptionInValues(List<String> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String removeTypeDescriptionInValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLanguageLabel(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setModeToLocal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setModeToRemote() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String[]> createResultListArray(Object arg0, String[] arg1) {
		List<String[]> result = new ArrayList<String[]>();
		QueryResponse response = (QueryResponse) arg0;
		SolrDocumentList results = response.getResults();
		for (int i = 0; i < results.size(); ++i) {
			SolrDocument solrDocument = (results.get(i));
			String[] values = new String[arg1.length];
			result.add(values);
			for (int a = 0; a < arg1.length; a++) {
				String value = String.valueOf(solrDocument.getFieldValue(arg1[a]));
				values[a] = value;
			}

		}
		return result;
	}

	@Override
	public Model describe(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model describeAllProperties(String arg0, Model arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllConcepts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllConcepts(String arg0) {
		String query = "class: *\"" + arg0 + "\"*";
		Object response = queryIntensionalProperties(query);
		List<String> result = createResultList(response, "class");
		List<String> finalResult = new ArrayList<String>();
		String searchTermLowerCase = arg0.toLowerCase();
		result.forEach(str -> {
			String strLowerCase = str.toLowerCase();
			if (strLowerCase.contains(searchTermLowerCase)) {
				System.out.println(strLowerCase + "->" + searchTermLowerCase);
				finalResult.add(str);
			}
		});
		return finalResult;
	}

	@Override
	public List<Entity> getAllConceptsFocusOnlyOnURI(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO not supported yet. Therefore, it is used the old one
	@Override
	public List<Entity> getAllConceptsLanguageSpecific(String arg0, Language arg1) {

		// String query = createQueryForLanguageLabel(arg1, arg0);
		// Object response

		List<String> concepts = getAllConcepts(arg0);
		List<Entity> entities = new ArrayList<Entity>();
		for (String cocnept : concepts) {
			Entity entity = new Entity();
			entity.setConceptSource(ConceptSource.ONTOLOGICAL);
			entity.setLanguage(Language.UNKNOWN);
			entity.setUrl(cocnept);
			
			String shortName = "";
			if (cocnept.contains("#")){
				shortName  = cocnept.substring(cocnept.lastIndexOf("#") + 1);	
			}
			else{
				shortName  = cocnept.substring(cocnept.lastIndexOf("/") + 1);
			}
			entity.setTranslatedURL(shortName);
			Logger.getAnonymousLogger().log(Level.WARNING, "No translation available, using URL als translation label!!!");
			entities.add(entity);
		}
		return entities;
	}

	private String createQueryForLanguageLabel(Language arg1, String token) {
		String queryPrefix = deriveFieldFromLanguage(arg1);

		String query = queryPrefix + ":" + "*\"" + token + "\"";

		return query;
	}

	@Override
	public List<String> getAllDirectInstances(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllDirectProperties(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllDirectSubConcepts(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllFunctionalProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllIndividualsWhichHasAreferenceToASpeciifcOne(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> getAllObjectPropertiesIncludingEverythingAndReturnItsRange(String conceptURL) {
		String query = "class: " + "\"" + conceptURL + "\""+ " And lmf.type : \"http://www.w3.org/2002/07/owl#ObjectProperty\"";
		Object response = queryIntensionalProperties(query);
		
		List<String[]> allProperties = createResultListArray(response, new String[] { "lmf.uri", "class" });
		return allProperties;
	}

	@Override
	public List<String> getAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllProperties(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * arg0 have to be a concept URL
	 */
	@Override
	public List<String> getAllPropertiesIncludingEverything(String arg0) {
		String query = "class: \"" + arg0 + "\"";
		Object response = queryIntensionalProperties(query);
		List<String> propertyURLS = createResultList(response, FIELD_FOR_PROPERTY_URI);
		return propertyURLS;
	}

	@Override
	public List<String> getAllPropertiesIncludingEverythingWhichHasValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveInstances(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<PropertyInformation>> getAllTransitiveInstances(String arg0, List<String> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> getAllTransitiveInstancesAndClasses(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveProperties(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitivePropertiesDirectionParents(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveSubConcepts(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDomainOfProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getHandleFromAElement(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getLabelFromAElement(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method checks for a predefined list of languages whether there are
	 * labels available
	 */

	@Override
	public List<Language> getNativeSupportedLangauges() {
		List<Language> languges = new ArrayList<Language>();
		String query = labelFieldForEnglish + ":*";
		Object response = queryIntensionalProperties(query);
		List<String> result = createResultList(response, "id");
		if (result.size() > 0) {
			languges.add(Language.ENGLISH);
		}

		query = labelFieldForGerman + ":*";
		response = queryIntensionalProperties(query);
		result = createResultList(response, "id");
		if (result.size() > 0) {
			languges.add(Language.GERMAN);
		}

		query = labelFieldForSpanish + ":*";
		response = queryIntensionalProperties(query);
		result = createResultList(response, "id");
		if (result.size() > 0) {
			languges.add(Language.SPANISH);
		}

		return languges;
	}

	@Override
	public PropertyType getPropertyType(String arg0) {
		String query = "lmf.uri:\"" + arg0 + "\"";
		Object response = queryIntensionalProperties(query);
		List<String> result = createResultList(response, "lmf.type");
		if (result.size() > 0) {

			for (String pType : result) {
				pType = pType.replace(" ", "");
				if (pType.equals("http://www.w3.org/2002/07/owl#ObjectProperty")) {
					return PropertyType.OBJECTPROPERTY;
				}
				if (pType.equals("http://www.w3.org/2002/07/owl#DatatypeProperty")) {
					return PropertyType.DATATYPEPROPERTY;
				}
				Logger.getAnonymousLogger().log(Level.WARNING, "Found no propertyType for: " + arg0);
				return PropertyType.UNKNOWN;
			}
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Found no propertyType for: " + arg0);
		}

		return PropertyType.UNKNOWN;
	}

	@Override
	public String getPropertyTypeRange(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String arg0, List<ObjectPropertyToDatatypePropertyMapping> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String arg0, List<ObjectPropertyToDatatypePropertyMapping> arg1, List<Filter> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiumsAndApplyFilter(
			String arg0, List<ObjectPropertyToDatatypePropertyMapping> arg1, List<Filter> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPropertyValuesOfAIndividium(String uuid) {
		// TODO Auto-generated method stub
		String query = LMF_URI + ":" + uuid;
		Object resposne = query(query);
		return createKeyValueOfAllPropertiesFromResultList(resposne, Language.ENGLISH);
	}
	
	
	public Map<String, String> getPropertyValuesOfAIndividium(String uuid, Language language) {
		// TODO Auto-generated method stub
		String query = LMF_URI + ":" + "\""+ uuid+ "\"";
		Object resposne = query(query);
		return createKeyValueOfAllPropertiesFromResultList(resposne, language);
	}

	@Override
	public Map<String, PropertyInformation> getPropertyValuesOfAIndividium(String arg0, List<String[]> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getPropertyValuesOfAIndividiumForMultipleValues(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, PropertyInformation> getPropertyValuesOfAIndividiumIncludingType(String arg0,
			List<ObjectPropertyToDatatypePropertyMapping> arg1, List<Filter> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiums(String arg0,
			List<String[]> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String arg0, List<String[]> arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String arg0, List<String[]> arg1, List<Filter> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRangeOfProperty(String propertyURL) {
		String query = LMF_URI + ": " + "\"" + propertyURL + "\"";
		Object response = queryIntensionalProperties(query);
		List<String> result = createResultList(response, "range");
		
		
		return result;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getSpecificPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String arg0, List<String> arg1, List<Filter> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getTheOverallOntology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTransitiveParentConceptsToAGivenConcept(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyConceptAssignment> getTransitivePropertiesInDirectionOfParent(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyConceptAssignment> getTransitivePropertiesIncludingTypeInDirectionOfParent(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPropertyFunctional(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String translateProperty(String propertyURL, Language language) {
		String query = FIELD_FOR_PROPERTY_URI + ":\"" + propertyURL + "\"";
		String fieldOfInterest = deriveFieldFromLanguage(language);
		Object response = queryIntensionalProperties(query);
		List<String> results = createResultList(response, fieldOfInterest);
		if (results.size() > 0) {
			if (results.get(0) != null && !results.get(0).equals("null")){
				return  results.get(0);
			}
			
		}

		if (propertyURL.contains("#")) {
			propertyURL = propertyURL.substring(propertyURL.indexOf("#") + 1);
		} else {
			propertyURL = propertyURL.substring(propertyURL.lastIndexOf("/") + 1);
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Cannot translate proeprty: " + propertyURL);
		return propertyURL;
	}

	public String translateConcept(String conceptURL, Language language) {
		String query = LMF_URI + ":\"" + conceptURL + "\"";
		String fieldOfInterest = deriveFieldFromLanguage(language);
		Object response = queryIntensionalConcepts(query);
		if (response == null){
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot translate the concept...." + conceptURL);
			if (conceptURL.contains("#")){
				return conceptURL.substring(conceptURL.indexOf("#")+1);
			}
			else{
				return conceptURL.substring(conceptURL.indexOf("/")+1);
			}
		}
		
		List<String> results = createResultList(response, fieldOfInterest);
		if (results.size() > 0) {
			return results.get(0);
		}

		if (conceptURL.contains("#")) {
			conceptURL = conceptURL.substring(conceptURL.indexOf("#") + 1);
		} else {
			conceptURL = conceptURL.substring(conceptURL.lastIndexOf("/") + 1);
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Cannot translate cocnept: " + conceptURL);
		return conceptURL;
	}

	private String deriveFieldFromLanguage(Language language) {
		String queryPrefix = "";
		switch (language) {
		case ENGLISH:
			queryPrefix = labelFieldForEnglish;
			break;
		case GERMAN:
			queryPrefix = labelFieldForGerman;
			break;
		case SPANISH:
			queryPrefix = labelFieldForSpanish;
			break;

		default:
			Logger.getAnonymousLogger().log(Level.WARNING,
					"Received no language information. Use the English translation");
			queryPrefix = labelFieldForEnglish;
			break;
		}

		return queryPrefix;
	}

	public List<String> getAllValuesForAGivenProperty(String concept, String property, PropertySource propertySource) {
		String query = "item_commodity_classification_uri:" + "\"" + concept + "\"";
		Object response = query(query);
		if (response == null || ((QueryResponse)response).getResults().size()==0){
			Logger.getAnonymousLogger().log(Level.WARNING, "No connection or item_commodity_classification_uri doesn't exists. Try without concept binding");
		response = query ("*:*");
		}
		property = entityMappingService.mapPropertyURIToFieldName(property);
		List<String> values = createResultList(response, property);
		return values;
	}

	public OutputForExecuteSelect createSPARQLAndExecuteIT(
			InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {
		EntityMappingService entityMappingService = new EntityMappingService();
		String concept = inputParamaterForExecuteSelect.getConcept();
		String query = "item_commodity_classification_uri:" + "\"" + concept + "\"";
		if (concept.equals("*")) {
			query = "*:*";
			Logger.getAnonymousLogger().log(Level.WARNING, "Have no concept information. Set to all!!!");
		}
		String fq = "";
		for (eu.nimble.service.catalog.search.impl.dao.Filter filter : inputParamaterForExecuteSelect.getFilters()) {
			if ((filter.getExactValue() != null) && (filter.getExactValue().length() > 0)) {
				String proeprtyUI = filter.getProperty();
				fq += " " + entityMappingService.mapPropertyURIToFieldName(proeprtyUI);
				fq += " : ";
				fq += filter.getExactValue();
			}

			if (filter.isHasMaxBeenSet() && filter.isHasMinBeenSet()) {

				String proeprtyUI = filter.getProperty();
				fq += " " + entityMappingService.mapPropertyURIToFieldName(proeprtyUI);
				fq += " : ";

				// min max

				proeprtyUI = filter.getProperty();
				fq += " +" + entityMappingService.mapPropertyURIToFieldName(proeprtyUI);
				fq += " : ";
				fq += "[" + filter.getMinAsInt() + " TO " + filter.getMaxAsInt() + "]";

			} else {

				String proeprtyUI = filter.getProperty();
				fq += " " + entityMappingService.mapPropertyURIToFieldName(proeprtyUI);
				fq += " : ";

				if (filter.isHasMaxBeenSet()) {
					fq += "[" + "*" + " TO " + filter.getMaxAsInt() + "]";
				} else {
					if (filter.isHasMinBeenSet()) {
						fq += "[" + filter.getMinAsInt() + " TO " + "*" + "]";
					}
				}

			}

			fq += " ";

		}

		String fl = "";
		for (String uri : inputParamaterForExecuteSelect.getParametersURL()) {
			fl += entityMappingService.mapPropertyURIToFieldName(uri) + " , ";
		}
		;
		if (fl.length() > 2) {
			fl += LMF_URI;
		}
		Object response = query(query, fl, fq);
		String[] columns = new String[inputParamaterForExecuteSelect.getParametersURL().size() + 1];
		int index = 0;
		for (String uri : inputParamaterForExecuteSelect.getParametersURL()) {
			String field = entityMappingService.mapPropertyURIToFieldName(uri);
			columns[index] = field;
			index++;
		}
		columns[index] = LMF_URI;
		List<String[]> result = createResultListArray(response, columns);
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		outputForExecuteSelect.setInput(inputParamaterForExecuteSelect);
		List<String> columnsInTargetLangauge = new ArrayList<String>();
		// last element will not be translated, because it is the id
		for (int i = 0; i < columns.length - 1; i++) {
			String propertyURL = inputParamaterForExecuteSelect.getParametersURL().get(i);
			columnsInTargetLangauge.add(translateProperty(propertyURL, inputParamaterForExecuteSelect.getLanguage()));
		}

		outputForExecuteSelect.setColumns(columnsInTargetLangauge);

		for (String[] row : result) {

			// 0..n-1 property values
			List<String> dataRow = new ArrayList<String>();
			for (int i = 0; i < row.length - 1; i++) {
				dataRow.add(row[i]);
			}

			// n-1 uuid
			outputForExecuteSelect.getUuids().add(row[row.length - 1]);
			outputForExecuteSelect.getRows().add((ArrayList<String>) dataRow);
		}

		return outputForExecuteSelect;
	}

	/**
	 * get all details for a specific product.
	 * 
	 * @param inputParamaterForExecuteSelect
	 * @return
	 */
	public OutputForExecuteSelect createOPtionalSPARQLAndExecuteIT(
			InputParamaterForExecuteOptionalSelect inputParamaterForExecuteSelect) {
		// This is/ necessary to get the uuid for each instance
		Map<String, String> result = getPropertyValuesOfAIndividium(inputParamaterForExecuteSelect.getUuid(), inputParamaterForExecuteSelect.getLanguage());

		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		
		outputForExecuteSelect.getColumns().addAll(result.keySet());
		ArrayList<String> row = new ArrayList<String>();
		row.addAll(result.values());
		outputForExecuteSelect.getRows().add(row);
		

		return outputForExecuteSelect;
	}

	public LocalOntologyView getViewForOneStepRange(String conceptAsUri, LocalOntologyView instance,
			LocalOntologyView parentInstance, Language language) {
		
		LocalOntologyView localOntologyView = null;
		
		if (instance == null) {
			localOntologyView = new LocalOntologyView();
		} else {
			localOntologyView = instance;
		}
		
		List<String> properties = getAllPropertiesIncludingEverything(conceptAsUri);
		for (String proeprty : properties) {
			PropertyType pType = getPropertyType(proeprty);
			if (pType == PropertyType.DATATYPEPROPERTY) {
				String translatedName = translateProperty(proeprty, language);
				eu.nimble.service.catalog.search.impl.dao.Entity entity = new eu.nimble.service.catalog.search.impl.dao.Entity();
				entity.setUrl(proeprty);
				entity.setTranslatedURL(translatedName);
				entity.setPropertySource(PropertySource.DOMAIN_SPECIFIC_PROPERTY);

				localOntologyView.addDataproperties(entity);
			} else {
				// It is a object property which means I must return the
				// name of
				// the concept
				addObjectPropertyToLogicalView(instance, language, localOntologyView, proeprty);
			}

		}
		
		return localOntologyView;
	}
	
	public void addObjectPropertyToLogicalView(LocalOntologyView instance, Language language,
			LocalOntologyView localOntologyView, String proeprty) {
		List<String> ranges = getRangeOfProperty(proeprty);
		for (int i = 0; i < ranges.size(); i++) {
			String range = ranges.get(i);
			String rangeReduced = translateConcept(range, language);
			LocalOntologyView localOntologyView2 = new LocalOntologyView();

			eu.nimble.service.catalog.search.impl.dao.Entity conceptRange = new eu.nimble.service.catalog.search.impl.dao.Entity();
			conceptRange.setUrl(range);
			String label = translateConcept(range, language);
			conceptRange.setTranslatedURL(rangeReduced);
			// conceptRan

			localOntologyView2.setConcept(conceptRange);
			localOntologyView2.setObjectPropertySource(proeprty);
			localOntologyView2.setFrozenConcept(instance.getFrozenConcept());
			localOntologyView2.setDistanceToFrozenConcept(instance.getDistanceToFrozenConcept() + 1);
			List<String> newPaht = new ArrayList<String>(localOntologyView.getConceptURIPath());
			newPaht.add(range);
			localOntologyView2.setConceptURIPath(newPaht);
			localOntologyView.getObjectproperties().put(range, localOntologyView2);
		}
	}

	
	public Map<String, List<Group>> generateGroup(int amountOfGroups, String conceptURL, String propertyURL) {
		
		String shortPropertyName = "";
		if (propertyURL.contains("#")){
			shortPropertyName = propertyURL.substring(propertyURL.indexOf("#")+1);
		}
		else{
			if (propertyURL.contains("/")){
				shortPropertyName = propertyURL.substring(propertyURL.lastIndexOf("/")+1);
			}	
		}
		
		List<String> values = getAllValuesForAGivenProperty(conceptURL, propertyURL, null);
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

	public List<String[]> getAllObjectPropertiesIncludingEverythingAndReturnItsRange(
			InputParameterForGetReferencesFromAConcept inputParameterForGetReferencesFromAConcept) {
		return getAllObjectPropertiesIncludingEverythingAndReturnItsRange(inputParameterForGetReferencesFromAConcept.getConceptURL());
	}

	public TranslationResult translateProperty(String value, Language language, String languageLabel) {
		// TODO Auto-generated method stub
		String translation = translateProperty(value, language);
		TranslationResult translationResult = new TranslationResult();
		translationResult.setOriginal(value);
		translationResult.setTranslation(translation);
		translationResult.setSuccess(true);
		return translationResult;
	}
}
