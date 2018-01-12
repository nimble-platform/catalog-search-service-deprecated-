package eu.nimble.service.catalog.search.impl;

import java.net.URLEncoder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
	//@Ignore 
	@Test
	public void testgetPropertyValuesFromOrangeGroup(){
		String inputAsJson = "{\"conceptURL\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair\",\"orangeCommand\":\"companyName\"}";
		System.out.println(URLEncoder.encode(inputAsJson));
		HttpEntity<Object>  result = serachController.getPropertyValuesFromOrangeGroup(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}
	
	//@Ignore
	@Test
	public void testgetSQP(){
		String inputAsJson = "{\"concept\":\"http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair\",\"stepRange\":1,\"language\":\"en\",\"frozenConcept\":\"HighChair\",\"distanceToFrozenConcept\":0,\"conceptURIPath\":[],\"currenSelections\":[]}";
		System.out.println(URLEncoder.encode(inputAsJson));
		
		HttpEntity<Object>  result = serachController.getSQP(inputAsJson);
		String r = result.getBody().toString();
		System.out.println(r);
	}
}
