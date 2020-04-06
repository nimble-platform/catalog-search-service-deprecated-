package de.biba.triple.store.access;

import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Model;

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.dmo.Filter;
import de.biba.triple.store.access.dmo.IndividualInformation;
import de.biba.triple.store.access.dmo.ObjectPropertyToDatatypePropertyMapping;
import de.biba.triple.store.access.dmo.PropertyConceptAssignment;
import de.biba.triple.store.access.dmo.PropertyInformation;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;

public interface IReader extends IAbstractQueryExecutor{

	List<String> getAllDirectSubConcepts(String urlOfClass);

	List<String> getAllTransitiveSubConcepts(String urlOfClass);

	List<String> getAllConcepts();

	/**
	 * Try to find a appropriate concept name which meet the given searchterm.
	 * 
	 * @param searchTerm
	 *            could be a regex or just a string
	 * @return list of concepts which meet the regex in its uuid
	 */
	List<String> getAllConcepts(String searchTerm);

	List<String> getAllTransitiveInstances(String urlOfClass);

	/**
	 * Get All instances by concept url and collects also the requested
	 * 
	 * WARNING: Methods assumes that the proeprty exists!!! Otherwise it will
	 * crash parameter values
	 * 
	 * @param urlOfClass
	 * @param properties
	 * @return
	 */
	Map<String, List<PropertyInformation>> getAllTransitiveInstances(String urlOfClass, List<String> properties);

	List<String[]> getAllTransitiveInstancesAndClasses(String urlOfClass);

	List<String> getAllDirectInstances(String urlOfClass);

	List<String> getDomainOfProperty(String urlOfProperty);

	List<String> getRangeOfProperty(String urlOfProperty);

	List<String> getAllProperties();

	List<String> getAllProperties(String searchTerm);

	List<String> getAllDirectProperties(String urlOfClass);

	List<String> getAllTransitiveProperties(String urlOfClass);

	Model describeAllProperties(String urlOfClass, Model model);

	/**
	 * Returns all a properties of the instance as a hashMap
	 * 
	 * @param urlOfIndividuum
	 *            the url of the instance. Example is:
	 *            http://biba.uni-bremen.de/InnovationGateway#3d_printer
	 * @return a HashMap in which the key is the property url and the value is
	 *         the object
	 */
	Map<String, String> getPropertyValuesOfAIndividium(String urlOfIndividuum);
	
	

	/*
	 * * Returns all a properties of the instance as a hashMap
	 * 
	 * @param urlOfIndividuum the url of the instance. Example is:
	 * http://biba.uni-bremen.de/InnovationGateway#3d_printer
	 * 
	 * @return a HashMap in which the key is the property url and the value is
	 * the object
	 */
	Map<String, List<String>> getPropertyValuesOfAIndividiumForMultipleValues(String urlOfIndividuum);

	/**
	 * Is is faster than the other merhod but deliver not all informaiton.
	 * 
	 * @param urlOfConcept
	 * @param objectproperties
	 * @return
	 */
	Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String[]> objectproperties);

	Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiums(String urlOfConcept,
			List<String[]> objectproperties);

	Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filter);

	Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiums(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties);

	Map<IndividualInformation, Map<String, PropertyInformation>> getPropertyValuesAndDatatypePropertyTypeOfAllIndividiumsAndApplyFilter(
			String urlOfConcept, List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filter);

	/**
	 * PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:
	 * <http://www.w3.org/2002/07/owl#>PREFIX rdfs:
	 * <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd:
	 * <http://www.w3.org/2001/XMLSchema#> SELECT ?predicate ?c ?object WHERE {
	 * {
	 * <http://biba.uni-bremen.de/InnovationGateway#asset_af9dab49-6316-4853-b4c2-4f6f600d4206>
	 * <http://biba.uni-bremen.de/InnovationGateway#mainContact> ?c. ?c
	 * <https://schema.org/givenName> ?object.} UNION {
	 * <http://biba.uni-bremen.de/InnovationGateway#asset_af9dab49-6316-4853-b4c2-4f6f600d4206>
	 * <http://biba.uni-bremen.de/InnovationGateway#mainCompany> ?c. ?c
	 * <http://biba.uni-bremen.de/InnovationGateway#Name> ?object.}}
	 * 
	 * @param result
	 * @param propertyName
	 * @return
	 */
	Map<String, PropertyInformation> getPropertyValuesOfAIndividium(String urlOfIndividuum,
			List<String[]> objectproperties);

	Map<String, PropertyInformation> getPropertyValuesOfAIndividiumIncludingType(String urlOfIndividuum,
			List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filters);

	List<String[]> createResultListArray(Object result, String[] propertyNames);

	Model getTheOverallOntology();

	void clearCache();

	/**
	 * This methods used a cached ontology model for describe. The cached
	 * ontology model is requested once from the triple store. If you want to
	 * reload the cached ontology you must call clear
	 * 
	 * @param describeStr
	 *            The complete URL of a concept
	 *            (http://biba.uni-bremen.de/InnovationGateway#Person)
	 * @return the model
	 */
	Model describe(String describeStr);

	/**
	 * Returns the information whether the property is a datatype or
	 * objectproperty. This information could be used to determine whether the
	 * value of the property is jaust a value or a individual reference
	 * 
	 * @param urlOfProperty
	 *            the url of the property, like
	 *            http://biba.uni-bremen.de/InnovationGateway#Description
	 * @return a enum containing datatypeproperty, objectproperty and unknown
	 */
	PropertyType getPropertyType(String urlOfProperty);

	String getPropertyTypeRange(String urlOfProperty);

	/**
	 * this method collects recursively all parent concepts to a given concept
	 * 
	 * @param urlConcept
	 *            The url of the cocnept
	 * @return List containing the urls for parent cocnepts
	 */
	List<String> getTransitiveParentConceptsToAGivenConcept(String urlConcept);

	/**
	 * This method collects recursively all properties from all parent classes
	 * until it reach Thing and returns it (
	 * 
	 * @param urlConcept
	 *            The url of the concept
	 * @return A list containing all properties containing the information which
	 *         property belongs to which concept without type of property
	 */
	List<PropertyConceptAssignment> getTransitivePropertiesInDirectionOfParent(String urlConcept);

	/**
	 * This method collects recursively all properties from all parent classes
	 * until it reach Thing and returns it (
	 * 
	 * @param urlConcept
	 *            The url of the concept
	 * @return A list containing all properties containing the information which
	 *         property belongs to which concept + type of property
	 */
	List<PropertyConceptAssignment> getTransitivePropertiesIncludingTypeInDirectionOfParent(String urlConcept);

	/**
	 * Checks to a given uuid of the propertty whether it is functional
	 * 
	 * @param uuidOfProperty
	 * @return
	 */
	boolean isPropertyFunctional(String uuidOfProperty);

	/**
	 * returns all functional properties including both datatype and object
	 * properties
	 * 
	 * @return
	 */
	List<String> getAllFunctionalProperties();

	/**
	 * This methos can ask the label for concept, property and individuals. A
	 * element could have more than one label.
	 * 
	 * @param uuid
	 *            uuid of the ontological element
	 * @return list of labels
	 */
	List<String> getLabelFromAElement(String uuid);

	// <http://biba.uni-bremen.de/InnovationGateway#ig:handle>
	/**
	 * select ?a ?b ?c where {
	 * <http://biba.uni-bremen.de/InnovationGateway#assetName>
	 * <http://biba.uni-bremen.de/InnovationGateway#ig:handle> ?c}
	 */

	List<String> getHandleFromAElement(String uuid);

	/**
	 * Returns the individuals which have a reference to a given individua
	 */
	List<String> getAllIndividualsWhichHasAreferenceToASpeciifcOne(String uuid);

	/**
	 * select distinct ?instance ?class ?predicate ?c ?object ?error where
	 * {?instance a ?class. ?class rdfs:subClassOf*
	 * <http://www.falcon.org/ontologies/Hightech-concepts#Error> . ?instance
	 * <http://www.falcon.org/ontologies/Hightech-concepts#ErrorCode> ?error.
	 * FILTER(?error = " 2005"). ?instance ?predicate ?object.}
	 * 
	 */
	Map<String, Map<String, PropertyInformation>> getPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String[]> objectproperties, List<Filter> filters);

	/**
	 * select distinct ?instance ?class ?predicate ?c ?object ?error where
	 * {?instance a ?class. ?class rdfs:subClassOf*
	 * <http://www.falcon.org/ontologies/Hightech-concepts#Error> . ?instance
	 * <http://www.falcon.org/ontologies/Hightech-concepts#ErrorCode> ?error.
	 * FILTER(?error = " 2005"). ?instance ?predicate ?object.}
	 * 
	 */
	Map<String, Map<String, PropertyInformation>> getSpecificPropertyValuesOfAllIndividiumsFastButWithoutObjectPropertyTranslation(
			String urlOfConcept, List<String> properties, List<Filter> filters);

	List<String> getAllTransitivePropertiesDirectionParents(String urlOfClass);

	List<String> getAllPropertiesIncludingEverything(String urlOfClass);
	

	List<Entity> getAllConceptsLanguageSpecific(String searchTerm, Language language);

	List<Entity> getAllConceptsFocusOnlyOnURI(String searchTerm);
	
	public List<Language> getNativeSupportedLangauges();

	List<String[]> getAllObjectPropertiesIncludingEverythingAndReturnItsRange(String urlOfClass);

	List<String> getAllPropertiesIncludingEverythingWhichHasValues(String urlOfClass);

}