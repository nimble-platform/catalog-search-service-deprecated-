package eu.nimble.service.catalog.search.impl.SOLRAccess;

import java.util.ArrayList;
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
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class SOLRReader implements IReader {

	private HttpSolrClient client = null;
	private HttpSolrClient clientForIntensionalQueries = null;
	private String url = "https://nimble-platform.salzburgresearch.at/marmotta/solr/catalogue2";
	private String urlForIntensionalQueries = "https://nimble-platform.salzburgresearch.at/marmotta/solr/properties";

	public SOLRReader() {
		init();
	}

	public void init() {
		SystemDefaultHttpClient httpClient = new SystemDefaultHttpClient();
//		client = new HttpSolrClient.Builder(url).build();
//		clientForIntensionalQueries = new HttpSolrClient.Builder(urlForIntensionalQueries).build();
		client = new  HttpSolrClient(url, httpClient);
		clientForIntensionalQueries = new  HttpSolrClient(urlForIntensionalQueries, httpClient);
	}

	public SOLRReader(String url) {
		this.url = url;
		init();
	}

	@Override
	public List<String> createResultList(Object arg0, String arg1) {
		List<String> result = new ArrayList<String>();
		QueryResponse response = (QueryResponse) arg0;
		SolrDocumentList results = response.getResults();
		for (int i = 0; i < results.size(); ++i) {
			SolrDocument solrDocument = (results.get(i));
			System.out.println(solrDocument.getFieldNames());
			System.out.println(solrDocument.getFieldNames());
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
		oneValue = oneValue.replace(" ", "");
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

	public Object queryIntensional(String arg0) {
		SolrQuery query = new SolrQuery();
		query.setQuery(arg0);
		// query.addFilterQuery("cat:electronics","store:amazon.com");
		query.setFields("*");
		query.setStart(0);
		// query.set("defType", "edismax");
		try {
			QueryResponse response = clientForIntensionalQueries.query(query);
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
		String query = "class: *\""+arg0+"\"*";
		Object response = queryIntensional(query);
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
		List<String> concepts = getAllConcepts(arg0);
		List<Entity> entities = new ArrayList<Entity>();
		for (String cocnept : concepts) {
			Entity entity = new Entity();
			entity.setConceptSource(ConceptSource.ONTOLOGICAL);
			entity.setLanguage(Language.UNKNOWN);
			entity.setUrl(cocnept);
			String shortName = cocnept.substring(cocnept.lastIndexOf("#") + 1);
			entities.add(entity);
		}
		return entities;
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
	public List<String[]> getAllObjectPropertiesIncludingEverythingAndReturnItsRange(String arg0) {
		// TODO Auto-generated method stub
		return null;
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
		String query = "class: \""+arg0+"\"";
		Object response = queryIntensional(query);
		List<String> propertyURLS = createResultList(response, "lmf.uri");
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

	@Override
	public List<Language> getNativeSupportedLangauges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyType getPropertyType(String arg0) {
		String query = "lmf.uri:\"" + arg0 + "\"";
		Object response = queryIntensional(query);
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
	public Map<String, String> getPropertyValuesOfAIndividium(String arg0) {
		// TODO Auto-generated method stub
		return null;
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
	public List<String> getRangeOfProperty(String arg0) {
		// TODO Auto-generated method stub
		return null;
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

}
