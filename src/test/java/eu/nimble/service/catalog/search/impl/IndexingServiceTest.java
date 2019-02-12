package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.*;

import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.Test;

import eu.nimble.service.catalog.search.impl.Indexing.IndexingServiceReader;
import eu.nimble.service.catalog.search.impl.dao.Entity;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;

public class IndexingServiceTest {

	@Test
	public void justCheckWhetherEndpointIsAvailable() {
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#StoragePieceOfFurniture";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		List<String> r = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
		System.out.println(r.get(0));

	}

	@Test
	public void testgetLogicalView() {
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#Shelf";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamterForGetLogicalView input = new InputParamterForGetLogicalView();
		input.setConcept(urlForClas);
		input.setLanguage("en");
		input.setDistanceToFrozenConcept(0);
		input.setStepRange(2);
		// input.setOldJsonLogicalView(new LocalOntologyView());
		String r = indexingServiceReader.getLogicalView(input);
		System.out.println(r);

	}

	/**
	 * The test just serach for any valid result
	 */
	@Test
	public void testdetectPossibleConceptsLanguageSpecific() {
		String serach = "Shelf";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific = new InputParameterdetectMeaningLanguageSpecific();

		inputParameterdetectMeaningLanguageSpecific.setLanguage("en");
		inputParameterdetectMeaningLanguageSpecific.setKeyword(serach);

		List<de.biba.triple.store.access.dmo.Entity> r = indexingServiceReader
				.detectPossibleConceptsLanguageSpecific(inputParameterdetectMeaningLanguageSpecific);
		assertTrue(r.size() > 0);
		System.out.println(r.get(0));

	}

	@Test
	public void testgetAllDifferentValuesForAProperty() {
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#Product";
		String property = "http://www.aidimme.es/FurnitureSectorOntology.owl#hasPrice";

		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property);
		System.out.println(test);
		assertTrue(test.size() > 0);
	}

	@Test
	public void testcreateOPtionalSPARQLAndExecuteIT(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		String uuid = "5407";
		
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect = new InputParamaterForExecuteOptionalSelect();
		inputParamaterForExecuteOptionalSelect.setLanguage("en");
		inputParamaterForExecuteOptionalSelect.setUuid(uuid);
		
		
		
		OutputForExecuteSelect r = indexingServiceReader.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(r);
		System.out.println("##########################");
		r = indexingServiceReader.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(r);
	}
	
}
