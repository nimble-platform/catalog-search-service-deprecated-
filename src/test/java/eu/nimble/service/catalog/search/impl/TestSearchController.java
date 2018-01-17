package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.Parameter;
import eu.nimble.service.catalog.search.impl.dao.input.Tuple;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;

public class TestSearchController {

	private static final String SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML = "./src/main/resources/sqpConfiguration.xml";

	SearchController serachController;

	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

	@Before
	public void setUp() {
		serachController = new SearchController();
		serachController.setOntologyFile(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		serachController.setSqpConfigurationPath(SRC_MAIN_RESOURCES_SQP_CONFIGURATION_XML);
		serachController.init();

	}

	@Ignore
	@Test
	public void testgetPropertyValuesFromOrangeGroup() {
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
	@Ignore
	public void testExecuteSPARQLSelectWithOrange() {
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

}