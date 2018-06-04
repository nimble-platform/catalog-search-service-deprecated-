package eu.nimble.service.catalog.search.impl;

import java.util.List;

import org.junit.Test;

import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.impl.SOLRAccess.SOLRReader;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;

public class TestSOLRReader {

	@Test
	public void testgetConnection(){
		SOLRReader solrReader = new SOLRReader();
		Object response = solrReader.query("*.*");
		System.out.println(solrReader.createResultList(response, "item_manufacturer_name"));
		//:*&rows=0&wt=csv
		
	}
	
	
	@Test
	public void testgetConcepts(){
		SOLRReader solrReader = new SOLRReader();
		List<String> concepts = solrReader.getAllConcepts("PieceOfFurniture");
		System.out.println(concepts);
	}
	
	//getPropertyType
	@Test
	public void testgetPropertyType(){
		SOLRReader solrReader = new SOLRReader();
		PropertyType properType  = solrReader.getPropertyType("http://www.aidimme.es/FurnitureSectorOntology.owl#hasCatalogue");
		System.out.println(properType);
	}
	
	//getAllPropertiesIncludingEverything
	@Test
	public void testgetgetAllPropertiesIncludingEverything(){
		SOLRReader solrReader = new SOLRReader();
		List<String> properType  = solrReader.getAllPropertiesIncludingEverything("http://www.aidimme.es/FurnitureSectorOntology.owl#Company");
		System.out.println(properType);
	}
	
	@Test
	public void testgetNativeSupportedLangauges(){
		SOLRReader reader = new SOLRReader();
		List<Language> result = reader.getNativeSupportedLangauges();
		System.out.println(result);
	}
	
	@Test
	public void testtranslateProperty(){
		SOLRReader reader = new SOLRReader();
		String label = reader.translateProperty("http://www.w3.org/ns/org#memberOf", Language.ENGLISH);
		System.out.println(label);
		
		String label2 = reader.translateProperty("http://www.w3.org/ns/org#memberOf", Language.SPANISH);
		System.out.println(label2);
	}
	
	@Test
	public void testgetAllValuesForAGivenProperty(){
		SOLRReader reader = new SOLRReader("http://nimble-staging.salzburgresearch.at/marmotta/solr/catalogue_semantic_search/", "", "");
		List<String> result = reader.getAllValuesForAGivenProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard", "item_price", PropertySource.ADDITIONAL_ITEM_PROPERTY);
	}
	
	@Test
	public void createSPARQLAndExecuteIT(){
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("*");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("item_name");
		inputParamaterForExecuteSelect.getParametersURL().add("item_price");
		
		SOLRReader reader = new SOLRReader();
		OutputForExecuteSelect executeSelect =  reader.createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		System.out.println(executeSelect);
	}
	
	@Test
	public void createSPARQLAndExecuteITMinValue(){
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("*");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("item_name");
		inputParamaterForExecuteSelect.getParametersURL().add("item_price");
		
		Filter filter = new Filter();
		filter.setMin(100);
		filter.setProperty("item_price");
		inputParamaterForExecuteSelect.getFilters().add(filter);
		
		SOLRReader reader = new SOLRReader();
		OutputForExecuteSelect executeSelect =  reader.createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		System.out.println(executeSelect);
	}
}
