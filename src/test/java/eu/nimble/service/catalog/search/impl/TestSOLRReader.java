package eu.nimble.service.catalog.search.impl;

import java.util.List;

import org.junit.Test;

import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.impl.SOLRAccess.SOLRReader;

public class TestSOLRReader {

	@Test
	public void getConnection(){
		SOLRReader solrReader = new SOLRReader();
		Object response = solrReader.query("*.*");
		System.out.println(solrReader.createResultList(response, "item_manufacturer_name"));
		//:*&rows=0&wt=csv
		
	}
	
	
	@Test
	public void getConcepts(){
		SOLRReader solrReader = new SOLRReader();
		List<String> concepts = solrReader.getAllConcepts("PieceOfFurniture");
		System.out.println(concepts);
	}
	
	//getPropertyType
	@Test
	public void getPropertyType(){
		SOLRReader solrReader = new SOLRReader();
		PropertyType properType  = solrReader.getPropertyType("http://www.aidimme.es/FurnitureSectorOntology.owl#hasCatalogue");
		System.out.println(properType);
	}
	
	//getAllPropertiesIncludingEverything
	@Test
	public void getgetAllPropertiesIncludingEverything(){
		SOLRReader solrReader = new SOLRReader();
		List<String> properType  = solrReader.getAllPropertiesIncludingEverything("http://www.aidimme.es/FurnitureSectorOntology.owl#Company");
		System.out.println(properType);
	}
}
