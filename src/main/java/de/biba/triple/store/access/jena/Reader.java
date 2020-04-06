package de.biba.triple.store.access.jena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.graph.Node_Variable.VariableName;
import org.apache.jena.query.DatasetAccessor;
import org.apache.jena.query.DatasetAccessorFactory;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import de.biba.triple.store.access.AbstractQueryExecutor;
import de.biba.triple.store.access.ConfigurationService;
import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.dmo.Filter;
import de.biba.triple.store.access.dmo.IndividualInformation;
import de.biba.triple.store.access.dmo.ObjectPropertyToDatatypePropertyMapping;
import de.biba.triple.store.access.dmo.PropertyConceptAssignment;
import de.biba.triple.store.access.dmo.PropertyInformation;
import de.biba.triple.store.access.dmo.Value;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.Operator;
import de.biba.triple.store.access.enums.PropertyType;

public class Reader extends AbstractQueryExecutor implements IReader {

	String urlForModelRetrival = dataSetURL + "/data";
	Model retrivedModel = null;

	public Reader() {
		super(ConfigurationService.getInstance().getDataSetUrl(), null);
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
		// TODO Auto-generated method stub
		return null;
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