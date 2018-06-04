package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForPropertyValuesFromGreenGroup;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.input.Parameter;
import eu.nimble.service.catalog.search.impl.dao.input.Tuple;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;

public class TestSearchController {

	private static final String SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML = "./src/main/resources/sqpConfiguration.xml";

	SearchController serachController;

	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

	// @Ignore
	// @Before
	// public void setUp() {
	// serachController = new SearchController();
	// serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
	// serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
	// serachController.init();
	//
	// }

	// @Ignore
	@Test
	public void testgetPropertyValuesFromOrangeGroup() {
		serachController = new SearchController();
		serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();
		String inputAsJson = "{\"conceptURL\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair\",\"orangeCommand\":\"companyName\"}";
		System.out.println(URLEncoder.encode(inputAsJson));
		HttpEntity<Object> result = serachController.getPropertyValuesFromOrangeGroup(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Ignore
	@Test
	public void testgetSQP() {
		String inputAsJson = "{\"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair\",\"stepRange\":1,\"language\":\"en\",\"frozenConcept\":\"HighChair\",\"distanceToFrozenConcept\":0,\"conceptURIPath\":[],\"currenSelections\":[]}";
		System.out.println(URLEncoder.encode(inputAsJson));

		HttpEntity<Object> result = serachController.getSQP(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	public void testgetLogicalView_Marmotta() {

		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		InputParamterForGetLogicalView inputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		inputParamterForGetLogicalView.setConcept("http://www.nimble-project.org/resource/eclass/22292803");
		inputParamterForGetLogicalView.setStepRange(1);
		inputParamterForGetLogicalView.setLanguage("en");

		HttpEntity<Object> result = serachController.getLogicalView(inputParamterForGetLogicalView);
		String r = result.getBody().toString();
		System.out.println(r);

	}

	@Test
	public void testgetPropertyFromConcept() {
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		InputParamterForGetLogicalView inputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		inputParamterForGetLogicalView.setConcept("http://www.nimble-project.org/resource/eclass/22292803");
		inputParamterForGetLogicalView.setStepRange(1);
		inputParamterForGetLogicalView.setLanguage("en");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamterForGetLogicalView);
		HttpEntity<Object> result = serachController.getPropertyFromConcept(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);

	}
	
	@Test
	public void testgetPropertyFromConceptII() {
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		InputParamterForGetLogicalView inputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		inputParamterForGetLogicalView.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParamterForGetLogicalView.setStepRange(1);
		inputParamterForGetLogicalView.setLanguage("en");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamterForGetLogicalView);
		HttpEntity<Object> result = serachController.getPropertyFromConcept(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);

	}

	@Test
	public void testgetPropertyValuesFromGreenGroup_BUG_NIMBLE93() {
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();
		
		InputParameterForPropertyValuesFromGreenGroup inputParameterForPropertyValuesFromGreenGroup = new InputParameterForPropertyValuesFromGreenGroup();
		inputParameterForPropertyValuesFromGreenGroup.setConceptURL("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParameterForPropertyValuesFromGreenGroup.setPropertySource(PropertySource.CUSTOM_DECIMAL);
		inputParameterForPropertyValuesFromGreenGroup.setPropertyURL("dryingTime");
		
		Gson gson = new Gson();
		
		String inputAsJson = gson.toJson(inputParameterForPropertyValuesFromGreenGroup);
		System.out.println(inputAsJson);
		HttpEntity<Object> result = serachController.getPropertyValuesFromGreenGroup(inputAsJson);
		
		String r = result.getBody().toString();
		System.out.println(r);
		
		assertTrue(r.contains("20"));
		
	}
	
	@Test
	public void testgetPropertyValuesFromGreenGroup_BUG_DomainSpecificProperties() {
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();
		
		InputParameterForPropertyValuesFromGreenGroup inputParameterForPropertyValuesFromGreenGroup = new InputParameterForPropertyValuesFromGreenGroup();
		inputParameterForPropertyValuesFromGreenGroup.setConceptURL("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParameterForPropertyValuesFromGreenGroup.setPropertySource(PropertySource.DOMAIN_SPECIFIC_PROPERTY);
		inputParameterForPropertyValuesFromGreenGroup.setPropertyURL("http://www.aidimme.es/FurnitureSectorOntology.owl#hasViscosity");
		
		Gson gson = new Gson();
		
		String inputAsJson = gson.toJson(inputParameterForPropertyValuesFromGreenGroup);
		System.out.println(inputAsJson);
		HttpEntity<Object> result = serachController.getPropertyValuesFromGreenGroup(inputAsJson);
		
		String r = result.getBody().toString();
		System.out.println(r);
		
		assertTrue(r.contains("20"));
		
	}

	
	
	
	
	
	@Test
	@Ignore
	public void testExecuteSPARQLSelect_Bug_NIMBLE_81() {

		/**
		 * { "parametersIncludingPath": [ { "urlOfProperty":
		 * "http%3A%2F%2Fwww.aidimme.es%2FFurnitureSectorOntology.owl%23hasHeight",
		 * "path": [ { "concept":
		 * "http%3A%2F%2Fwww.aidimme.es%2FFurnitureSectorOntology.owl%23HighChair"
		 * } ] } ], "parameters": [ "tieneAltura" ], "parametersURL": [
		 * "http%3A%2F%2Fwww.aidimme.es%2FFurnitureSectorOntology.owl%23hasHeight"
		 * ], "filters": [], "orangeCommandSelected": { "names": [] },
		 * "concept":
		 * "http%3A%2F%2Fwww.aidimme.es%2FFurnitureSectorOntology.owl%23HighChair",
		 * "language": "es" }
		 */
		
		serachController = new SearchController();
		serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair");
		inputParamaterForExecuteSelect.getParameters().add("tieneAltura");
		inputParamaterForExecuteSelect.setLanguage("es");
		inputParamaterForExecuteSelect.getParametersURL().add("http://www.aidimme.es/FurnitureSectorOntology.owl#hasHeight");
		
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair");
		t1.setUrlOfProperty(null);

		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(t1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		
		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);

		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	@Ignore
	public void testExecuteSPARQLSelectWithOrange() {
		
		
		
		serachController = new SearchController();
		serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();
		
		String ontology = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		String prop3 = "hasLegislationName";
		String prop4 = "companyName";

		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		// inputParamaterForExecuteSelect.getParameters().add(prop3);
		inputParamaterForExecuteSelect.getParameters().add(prop4);
		inputParamaterForExecuteSelect.setLanguage("en");

		Parameter parameter1 = new Parameter();
		Parameter parameter2 = new Parameter();
		Parameter parameter3 = new Parameter();
		Parameter parameter4 = new Parameter();
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair");
		t1.setUrlOfProperty(null);

		parameter1.getPath().add(t1);
		parameter2.getPath().add(t1);
		// parameter3.getPath().add(t1);

		Tuple t2 = new Tuple();
		t2.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Manufacturer");
		t2.setUrlOfProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#isManufacturedBy");

		Tuple t3 = new Tuple();
		t3.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Legislation");
		t3.setUrlOfProperty(
				"http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#compliesWithLegislation");

		parameter3.getPath().add(t2);
		parameter3.getPath().add(t3);

		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter2);
		// inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter3);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter4);
		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);

		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);

	}

	@Test
	@Ignore
	public void testExecuteSPARQLSelectWithOrangeII() {
		
		
		serachController = new SearchController();
		serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();
		
		String ontology = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		String prop3 = "hasLegislationName";

		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		// inputParamaterForExecuteSelect.getParameters().add(prop3);

		inputParamaterForExecuteSelect.setLanguage("en");

		Parameter parameter1 = new Parameter();
		Parameter parameter2 = new Parameter();
		Parameter parameter3 = new Parameter();

		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair");
		t1.setUrlOfProperty(null);

		parameter1.getPath().add(t1);
		parameter2.getPath().add(t1);
		// parameter3.getPath().add(t1);

		Tuple t2 = new Tuple();
		t2.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Manufacturer");
		t2.setUrlOfProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#isManufacturedBy");

		Tuple t3 = new Tuple();
		t3.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Legislation");
		t3.setUrlOfProperty(
				"http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#compliesWithLegislation");

		parameter3.getPath().add(t2);
		parameter3.getPath().add(t3);

		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter2);
		// inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter3);

		inputParamaterForExecuteSelect.getOrangeCommandSelected().getNames().add("companyName");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		System.out.println(URLEncoder.encode(inputAsJson));

		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);

	}

	@Test
	public void testdetectMeaningLanguageSpecific() {

		InputParameterdetectMeaningLanguageSpecific input = new InputParameterdetectMeaningLanguageSpecific();
		input.setLanguage("en");
		input.setKeyword("Varnish");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(input);

		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();
		HttpEntity<Object> result = serachController.detectMeaningLanguageSpecific(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	public void testdetectMeaningLanguageSpecificII() {

		InputParameterdetectMeaningLanguageSpecific input = new InputParameterdetectMeaningLanguageSpecific();
		input.setLanguage("en");
		//input.setKeyword("Fruit");
		input.setKeyword("MDF laminated");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(input);

		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		// MarmottaReader marmottaReader = new
		// MarmottaReader("https://nimble-platform.salzburgresearch.at/marmotta");
		// marmottaReader.setModeToRemote();
		// List<Entity> entities =
		// marmottaReader.getAllConceptsLanguageSpecific("Fruit",
		// Language.ENGLISH);
		// System.out.println(entities);

		HttpEntity<Object> result = serachController.detectMeaningLanguageSpecific(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	public void testExecuteSPARQLSelectAgainstEClass() {

		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		String concept = "http://www.nimble-project.org/resource/eclass/22292803";
		String prop1 = "name";
		String prop2 = "hasColour";
		String prop3 = "custom_dimensione";

		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		// inputParamaterForExecuteSelect.getParameters().add(prop3);

		inputParamaterForExecuteSelect.getParametersURL()
				.add("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name");
		inputParamaterForExecuteSelect.getParametersURL()
				.add("http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		inputParamaterForExecuteSelect.getParametersURL().add("custom_dimensione");

		inputParamaterForExecuteSelect.getPropertySources().add(PropertySource.DIRECT_PROPERTIES);
		inputParamaterForExecuteSelect.getPropertySources().add(PropertySource.DOMAIN_SPECIFIC_PROPERTY);

		inputParamaterForExecuteSelect.setLanguage("en");

		Parameter parameter1 = new Parameter();
		Parameter parameter2 = new Parameter();
		Parameter parameter3 = new Parameter();

		// Tuple t1 = new Tuple();
		// t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair");
		// t1.setUrlOfProperty(null);
		//
		// parameter1.getPath().add(t1);
		// parameter2.getPath().add(t1);
		// // parameter3.getPath().add(t1);
		//
		// Tuple t2 = new Tuple();
		// t2.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Manufacturer");
		// t2.setUrlOfProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#isManufacturedBy");
		//
		// Tuple t3 = new Tuple();
		// t3.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Legislation");
		// t3.setUrlOfProperty(
		// "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#compliesWithLegislation");
		//
		// parameter3.getPath().add(t2);
		// parameter3.getPath().add(t3);

		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter2);
		// inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter3);

		// inputParamaterForExecuteSelect.getOrangeCommandSelected().getNames().add("companyName");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		System.out.println(URLEncoder.encode(inputAsJson));

		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	public void testExecuteOptionalSPARQLSelectAgainstEClass() {

		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();

		String indi = "urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2#INS2b1b4356-0457-4cf5-8f57-8663857b3c40_ItemType_1";
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteOptionalSelect();
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.setUuid(indi);

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		System.out.println(URLEncoder.encode(inputAsJson));

		HttpEntity<Object> result = serachController.executeSPARQLWithOptionalSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	@Ignore
	public void testexecuteSPARQLWithOptionalSelect() {
		String indi = "http://www.aidimme.es/FurnitureSectorOntology.owl#T950_Plus_Natural";

		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteOptionalSelect();
		String translationLabel = "http://www.aidimme.es/FurnitureSectorOntology.owl#translation";
		// this.setLanguagelabel(translationLabel);
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.setUuid(indi);
		inputParamaterForExecuteSelect.getOrangeCommandSelected().getNames().add("companyName");

		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		System.out.println(URLEncoder.encode(inputAsJson));

		HttpEntity<Object> result = serachController.executeSPARQLWithOptionalSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}

	@Test
	public void testExecuteSparQLSeelct_Bug_NIMBLE_94(){
		//String inputAsJson = "{\"input\":{ \"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\", \"language\":\"en\", \"parameters\":[\"CatalogueDocumentReference\"], \"parametersURL\":[\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\"], \"parametersIncludingPath\":[  {   \"urlOfProperty\":\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\",   \"path\":[{\"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\"}]} } ],\"filters\":[],\"orangeCommandSelected\":{\"names\":[]}, \"propertySources\":[\"DIRECT_PROPERTIES\"]]}";
	
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParamaterForExecuteSelect.getParameters().add("CatalogueDocumentReference");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference");
		inputParamaterForExecuteSelect.getPropertySources().add(PropertySource.DIRECT_PROPERTIES);
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		t1.setUrlOfProperty(null);

		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(t1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		
		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		
		
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();
		
		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	
	}
	
	@Test
	public void testExecuteSparQLSeelct_Bug_NIMBLE_94_Filter(){
		//String inputAsJson = "{\"input\":{ \"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\", \"language\":\"en\", \"parameters\":[\"CatalogueDocumentReference\"], \"parametersURL\":[\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\"], \"parametersIncludingPath\":[  {   \"urlOfProperty\":\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\",   \"path\":[{\"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\"}]} } ],\"filters\":[],\"orangeCommandSelected\":{\"names\":[]}, \"propertySources\":[\"DIRECT_PROPERTIES\"]]}";
	
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParamaterForExecuteSelect.getParameters().add("CatalogueDocumentReference");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference");
		inputParamaterForExecuteSelect.getPropertySources().add(PropertySource.DIRECT_PROPERTIES);
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		t1.setUrlOfProperty(null);

		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(t1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		
		Filter filter = new Filter();
		filter.setProperty("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference");
		filter.setExactValue("db46244e-8e43-48a6-b523-51b4da3a82f2");
		inputParamaterForExecuteSelect.getFilters().add(filter);
		
		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		
		
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();
		
		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	
	}
	
	@Test
	public void testExecuteSparQLSeelct_Bug_NIMBLE_94_Filter_DomainSpecific(){
		//String inputAsJson = "{\"input\":{ \"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\", \"language\":\"en\", \"parameters\":[\"CatalogueDocumentReference\"], \"parametersURL\":[\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\"], \"parametersIncludingPath\":[  {   \"urlOfProperty\":\"urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference\",   \"path\":[{\"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish\"}]} } ],\"filters\":[],\"orangeCommandSelected\":{\"names\":[]}, \"propertySources\":[\"DIRECT_PROPERTIES\"]]}";
	
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		inputParamaterForExecuteSelect.getParameters().add("hasViscosity");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("http://www.aidimme.es/FurnitureSectorOntology.owl#hasViscosity");
		inputParamaterForExecuteSelect.getPropertySources().add(PropertySource.DOMAIN_SPECIFIC_PROPERTY);
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		t1.setUrlOfProperty(null);

		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(t1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		
		Filter filter = new Filter();
		filter.setProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#hasViscosity");
		filter.setExactValue("15");
		inputParamaterForExecuteSelect.getFilters().add(filter);
		
		Gson gson = new Gson();
		String inputAsJson = gson.toJson(inputParamaterForExecuteSelect);
		
		
		SearchController serachController = new SearchController();
		serachController.setMarmottaUri("https://nimble-platform.salzburgresearch.at/marmotta");
		serachController.setOntologyFile("null");
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.setLanguageLabel("http://www.w3.org/2004/02/skos/core#prefLabel");
		serachController.init();
		
		HttpEntity<Object> result = serachController.executeSPARQLSelect(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	
	}
	
	@Test
	public void testgetInstantiatedPropertiesFromConceptForSOLR(){
		
		//pre
		SearchController serachController = new SearchController();
		serachController.setUseSOLRIndex(true);
		serachController.init();
		
		InputParamterForGetLogicalView inputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		inputParamterForGetLogicalView.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Seat");
		inputParamterForGetLogicalView.setLanguage("es");
		
		Gson gson = new Gson();
		String input = gson.toJson(inputParamterForGetLogicalView);
		System.out.println(URLEncoder.encode(input));
		HttpEntity<Object> result = serachController.getInstantiatedPropertiesFromConcept(input);
		
		
		String r = result.getBody().toString();
		System.out.println(r);
		
		
	}
	
	@Test
	public void testgetLogicalViewForSOLR(){
		
		//pre
		InputParamterForGetLogicalView inputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		inputParamterForGetLogicalView.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#PieceOfFurniture");
		inputParamterForGetLogicalView.setLanguage("es");
		inputParamterForGetLogicalView.setStepRange(2);
		
		
		SearchController serachController = new SearchController();
		serachController.setUseSOLRIndex(true);
		serachController.init();
		
		
		
		Gson gson = new Gson();
		String input = gson.toJson(inputParamterForGetLogicalView);
		System.out.println(URLEncoder.encode(input));
		HttpEntity<Object> result = serachController.getLogicalView(inputParamterForGetLogicalView);
		
		
		String r = result.getBody().toString();
		System.out.println(r);
		
		
		
	}	
	
	
	
}
