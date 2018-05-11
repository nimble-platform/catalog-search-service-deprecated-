package eu.nimble.service.catalog.search.impl;

import org.junit.Test;

import eu.nimble.service.catalog.search.impl.SOLRAccess.SOLRReader;

public class TestSOLRReader {

	@Test
	public void getConnection(){
		SOLRReader solrReader = new SOLRReader();
		Object response = solrReader.query("");
		System.out.println(solrReader.createResultList(response, "item_manufacturer_name"));
		//:*&rows=0&wt=csv
		
	}
	
}
