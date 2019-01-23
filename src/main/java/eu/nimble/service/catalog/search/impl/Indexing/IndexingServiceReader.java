package eu.nimble.service.catalog.search.impl.Indexing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import com.google.gson.Gson;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.dmo.Filter;
import de.biba.triple.store.access.dmo.IndividualInformation;
import de.biba.triple.store.access.dmo.ObjectPropertyToDatatypePropertyMapping;
import de.biba.triple.store.access.dmo.PropertyConceptAssignment;
import de.biba.triple.store.access.dmo.PropertyInformation;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.impl.dao.ClassType;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameter;

public class IndexingServiceReader implements IReader {

	private String url = "";
	private String urlForClassInformation = "";
	private String urlForPropertyInformation = "";
	private String urlForItemInformation = "";

	public IndexingServiceReader(String url) {
		super();
		this.url = url;
		if (url.charAt(url.length() - 1) != '/') {
			url += "/";
		}
		urlForClassInformation = url + "class";
		urlForPropertyInformation = url + "property";
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

	@Override
	public String getUrlForQueries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModeToRemote() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getDataSetURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> createResultList(Object result, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModeToLocal() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean loadOntologyModel(String ontoFile) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object query(String sparqlStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object queryRemote(String sparqlStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet queryLocal(String sparqlStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTypeDescriptionInValues(List<String> allValuesForProperty) {
		// TODO Auto-generated method stub

	}

	@Override
	public String removeTypeDescriptionInValues(String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getBaseModel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getSupportedLangauges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLanguageLabel(String languageLabel) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getAllDirectSubConcepts(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveSubConcepts(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllConcepts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllConcepts(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveInstances(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<PropertyInformation>> getAllTransitiveInstances(String urlOfClass,
			List<String> properties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> getAllTransitiveInstancesAndClasses(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllDirectInstances(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getDomainOfProperty(String urlOfProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getRangeOfProperty(String urlOfProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllProperties(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllDirectProperties(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitiveProperties(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model describeAllProperties(String urlOfClass, Model model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPropertyValuesOfAIndividium(String urlOfIndividuum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, List<String>> getPropertyValuesOfAIndividiumForMultipleValues(String urlOfIndividuum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String[]> objectproperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiums(
			String urlOfConcept, List<String[]> objectproperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiumsAndApplyFilter(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, PropertyInformation> getPropertyValuesOfAIndividium(String urlOfIndividuum,
			List<String[]> objectproperties) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, PropertyInformation> getPropertyValuesOfAIndividiumIncludingType(String urlOfIndividuum,
			List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> createResultListArray(Object result, String[] propertyNames) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model getTheOverallOntology() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearCache() {
		// TODO Auto-generated method stub

	}

	@Override
	public Model describe(String describeStr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyType getPropertyType(String urlOfProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPropertyTypeRange(String urlOfProperty) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getTransitiveParentConceptsToAGivenConcept(String urlConcept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyConceptAssignment> getTransitivePropertiesInDirectionOfParent(String urlConcept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PropertyConceptAssignment> getTransitivePropertiesIncludingTypeInDirectionOfParent(String urlConcept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPropertyFunctional(String uuidOfProperty) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> getAllFunctionalProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getLabelFromAElement(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getHandleFromAElement(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllIndividualsWhichHasAreferenceToASpeciifcOne(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String[]> objectproperties, List<Filter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Map<String, PropertyInformation>> getSpecificPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String> properties, List<Filter> filters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllTransitivePropertiesDirectionParents(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllPropertiesIncludingEverything(String urlOfClass) {

		String httpGetURL = urlForClassInformation + "?uri=" + URLEncoder.encode(urlOfClass);
		String result = invokeHTTPMethod(httpGetURL);

		List<String> allProperties = new ArrayList<String>();
		Gson gson = new Gson();
		ClassType r = gson.fromJson(result, ClassType.class);
		r.getProperties().forEach(x -> allProperties.add(x));
		allProperties.add(result); 
									
		return allProperties;
	}

	@Override
	public List<Entity> getAllConceptsLanguageSpecific(String searchTerm, Language language) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Entity> getAllConceptsFocusOnlyOnURI(String searchTerm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Language> getNativeSupportedLangauges() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String[]> getAllObjectPropertiesIncludingEverythingAndReturnItsRange(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllPropertiesIncludingEverythingWhichHasValues(String urlOfClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
