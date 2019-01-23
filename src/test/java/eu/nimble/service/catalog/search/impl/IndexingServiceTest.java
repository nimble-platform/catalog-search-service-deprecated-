package eu.nimble.service.catalog.search.impl;

import  java.util.List;

import org.junit.Test;

import eu.nimble.service.catalog.search.impl.Indexing.IndexingServiceReader;

public class IndexingServiceTest {

	@Test
	public void justCheckWhetherEndpointIsAvailable(){
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#StoragePieceOfFurniture";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		List<String> r = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
		System.out.println(r.get(0));
		
	}
	
	
}
