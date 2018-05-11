package eu.nimble.service.catalog.search.impl.SOLRAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
/*import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;*/

public class SOLRReader implements IReader {

	//HttpSolrClient client = null;
	String url = "https://nimble-platform.salzburgresearch.at/marmotta/solr/catalogue2";

	public SOLRReader() {
		init();
	}

	public void init() {
		//client = new HttpSolrClient.Builder(url).build();
	}

	public SOLRReader(String url) {
		this.url = url;
		init();
	}

	@Override
	public List<String> createResultList(Object arg0, String arg1) {
		List<String> result = new ArrayList<String>();
//		QueryResponse response = (QueryResponse) arg0;
//		SolrDocumentList results = response.getResults();
//		for (int i = 0; i < results.size(); ++i) {
//			SolrDocument solrDocument = (results.get(i));
//			System.out.println(solrDocument.getFieldNames());
//			String value = String.valueOf(solrDocument.getFieldValue(arg1));
//			if (!result.contains(value)) {
//				result.add(value);
//			}
//
//		}
		return result;
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
//		SolrQuery query = new SolrQuery();
//		query.setQuery(arg0);
//		// query.addFilterQuery("cat:electronics","store:amazon.com");
//		query.setFields("*");
//		query.setStart(0);
//		// query.set("defType", "edismax");
//		try {
//			QueryResponse response = client.query(query);
//			return response;
//		} catch (Exception e) {
//			Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
//		}
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
//		QueryResponse response = (QueryResponse) arg0;
//		SolrDocumentList results = response.getResults();
//		for (int i = 0; i < results.size(); ++i) {
//			SolrDocument solrDocument = (results.get(i));
//			String[] values = new String[arg1.length];
//			result.add(values);
//			for (int a = 0; a < arg1.length; a++) {
//				String value = String.valueOf(solrDocument.getFieldValue(arg1[a]));
//				values[a] = value;
//			}
//
//		}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getAllConceptsFocusOnlyOnURI(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getAllConceptsLanguageSpecific(String arg0, Language arg1) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public List<String> getAllPropertiesIncludingEverything(String arg0) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
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
