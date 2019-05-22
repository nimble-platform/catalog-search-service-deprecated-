package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

import eu.nimble.service.catalog.search.impl.dao.*;
import eu.nimble.service.catalog.search.impl.Indexing.IndexingServiceReader;
import eu.nimble.service.catalog.search.impl.dao.Entity;
import eu.nimble.service.catalog.search.impl.dao.ItemMappingFieldInformation;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
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
	public void filterProperties(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		
		boolean result = indexingServiceReader.checkWhetherPropertyIsRelevant(null, urlForClas);
		
		System.out.println(result);
	}
	
	@Test
	public void testgetLogicalView() {
		//String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#Shelf";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamterForGetLogicalView input = new InputParamterForGetLogicalView();
		input.setConcept(urlForClas);
		input.setLanguage("en");
		input.setDistanceToFrozenConcept(0);
		input.setStepRange(2);
		// input.setOldJsonLogicalView(new LocalOntologyView());
		String r = indexingServiceReader.getLogicalView(input);
		System.out.println(r);
		
		String r2 = indexingServiceReader.getLogicalView(input);
		
		assertEquals(r, r2);

	}
	
	
	
	
	@Ignore
	@Test
	public void testcreateSPARQLAndExecuteITFILTEROntologicalProperty(){
		
		
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		//urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#TableTop";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamaterForExecuteSelect executeSelect = new InputParamaterForExecuteSelect();
		executeSelect.getParametersURL().add("http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		
		executeSelect.setConcept(urlForClas);
		
		Filter filter = new Filter();
		filter.setProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		filter.setExactValue("Natural");
		executeSelect.getFilters().add(filter);
		
		
//		Filter filter2 = new Filter();
//		filter2.setProperty("http://UnknownSource#price");
//		filter2.setExactValue("18.0");
//		executeSelect.getFilters().add(filter2);
//		executeSelect.getFilters().add(filter2);
		
//		String desc =" MEDILAND XL is made of 100% of Landes Pine selected wood fibres (MDF).\n All the products of the Mediland range are manufactured by FINSA France (Morcenx), the only MDF factory set in the heart of the Landes de Gascogne. The Landes forest is the biggest forest massif of Maritime Pine (Pinus Pinaster) located in the South of Europe. Mediland boards are well-known for their quality. They have a characteristical light colour that helps improve the final results of the product, especially for applications of lacquer, paint or varnish. E1 classification: low formaldehyde content.";
//		Filter filter3 = new Filter();
//		filter3.setProperty("http://UnknownSource#description");
//		filter3.setExactValue(desc);
//		executeSelect.getFilters().add(filter3);
		
		//precondition getProperties to laod the proeprtyCache
				List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
				List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(urlForClas, "http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		System.out.println(indexingServiceReader.createSPARQLAndExecuteIT(executeSelect));
	}
	
	
	
	@Test
	public void testcreateSPARQLAndExecuteITFILTERPrice(){
		
		
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		//urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#TableTop";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamaterForExecuteSelect executeSelect = new InputParamaterForExecuteSelect();
		executeSelect.getParametersURL().add("http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		executeSelect.getParametersURL().add("http://UnknownSource#name");
		executeSelect.getParametersURL().add("http://UnknownSource#description");
		executeSelect.getParametersURL().add("http://UnknownSource#price");
		executeSelect.setConcept(urlForClas);
		
		Filter filter = new Filter();
		filter.setProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		filter.setExactValue("Natural");
		executeSelect.getFilters().add(filter);
		
		
		Filter filter2 = new Filter();
		filter2.setProperty("http://UnknownSource#price");
		filter2.setExactValue("18.0");
		executeSelect.getFilters().add(filter2);
		executeSelect.getFilters().add(filter2);
		
//		String desc =" MEDILAND XL is made of 100% of Landes Pine selected wood fibres (MDF).\n All the products of the Mediland range are manufactured by FINSA France (Morcenx), the only MDF factory set in the heart of the Landes de Gascogne. The Landes forest is the biggest forest massif of Maritime Pine (Pinus Pinaster) located in the South of Europe. Mediland boards are well-known for their quality. They have a characteristical light colour that helps improve the final results of the product, especially for applications of lacquer, paint or varnish. E1 classification: low formaldehyde content.";
//		Filter filter3 = new Filter();
//		filter3.setProperty("http://UnknownSource#description");
//		filter3.setExactValue(desc);
//		executeSelect.getFilters().add(filter3);
		
		//precondition getProperties to laod the proeprtyCache
				List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
				List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(urlForClas, "http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour");
		System.out.println(indexingServiceReader.createSPARQLAndExecuteIT(executeSelect));
	}
	
	
	@Test
	public void testcreateSPARQLAndExecuteIT(){
		
		
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		//urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#TableTop";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamaterForExecuteSelect executeSelect = new InputParamaterForExecuteSelect();
		executeSelect.getParametersURL().add("http://UnknownSource#name");
		executeSelect.getParametersURL().add("http://UnknownSource#description");
		executeSelect.getParametersURL().add("http://UnknownSource#price");
		executeSelect.setConcept(urlForClas);
		
		Filter filter = new Filter();
		filter.setProperty("http://UnknownSource#name");
		filter.setExactValue("Mediland XL");
		executeSelect.getFilters().add(filter);
		
		
		Filter filter2 = new Filter();
		filter2.setProperty("http://UnknownSource#price");
		filter2.setExactValue("18.0");
		executeSelect.getFilters().add(filter2);
		executeSelect.getFilters().add(filter2);
		
//		String desc =" MEDILAND XL is made of 100% of Landes Pine selected wood fibres (MDF).\n All the products of the Mediland range are manufactured by FINSA France (Morcenx), the only MDF factory set in the heart of the Landes de Gascogne. The Landes forest is the biggest forest massif of Maritime Pine (Pinus Pinaster) located in the South of Europe. Mediland boards are well-known for their quality. They have a characteristical light colour that helps improve the final results of the product, especially for applications of lacquer, paint or varnish. E1 classification: low formaldehyde content.";
//		Filter filter3 = new Filter();
//		filter3.setProperty("http://UnknownSource#description");
//		filter3.setExactValue(desc);
//		executeSelect.getFilters().add(filter3);
		
		//precondition getProperties to laod the proeprtyCache
				List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
		
		System.out.println(indexingServiceReader.createSPARQLAndExecuteIT(executeSelect));
	}
	
	
	@Ignore
	@Test
	public void testcreateSPARQLAndExecuteITPropertyUnknownSource(){
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		//urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#TableTop";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamaterForExecuteSelect executeSelect = new InputParamaterForExecuteSelect();
		executeSelect.getParametersURL().add("http://UnknownSource#name");
		//executeSelect.getParametersURL().add("http://UnknownSource#description");
		executeSelect.getParametersURL().add("http://UnknownSource#price");
		executeSelect.setConcept(urlForClas);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
		
		eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect r = indexingServiceReader.createSPARQLAndExecuteIT(executeSelect);
		System.out.println(r);
	}
	
@Ignore
	@Test
	public void testcreateSPARQLAndExecuteITPropertyUBLSource(){
		String urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		//urlForClas = "http://www.aidimme.es/FurnitureSectorOntology.owl#TableTop";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParamaterForExecuteSelect executeSelect = new InputParamaterForExecuteSelect();
		executeSelect.getParametersURL().add("http://www.nimble-project.org/resource/ubl#freeOfCharge");
		//executeSelect.getParametersURL().add("http://UnknownSource#description");
		//executeSelect.getParametersURL().add("http://UnknownSource#price");
		executeSelect.setConcept(urlForClas);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(urlForClas);
		
		eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect r = indexingServiceReader.createSPARQLAndExecuteIT(executeSelect);
		System.out.println(r);
	}
	
	
	/**
	 * The test just serach for any valid result
	 */
	@Ignore
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

	@Ignore
	@Test
	public void testdetectPossibleConceptsLanguageSpecificII() {
		String serach = "Mdf";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific = new InputParameterdetectMeaningLanguageSpecific();

		inputParameterdetectMeaningLanguageSpecific.setLanguage("en");
		inputParameterdetectMeaningLanguageSpecific.setKeyword(serach);

		List<de.biba.triple.store.access.dmo.Entity> r = indexingServiceReader
				.detectPossibleConceptsLanguageSpecific(inputParameterdetectMeaningLanguageSpecific);
		assertTrue(r.size() > 0);
		System.out.println(r);

	}
	
	
	@Test
	public void testdetectPossibleConceptsLanguageSpecificIII() {
		//Auxiliary supply, additive, cleaning agent
		//http://www.nimble-project.org/resource/eclass#0173-1#01-AAC350#007
		String serach = "agent";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific = new InputParameterdetectMeaningLanguageSpecific();

		inputParameterdetectMeaningLanguageSpecific.setLanguage("en");
		inputParameterdetectMeaningLanguageSpecific.setKeyword(serach);

		List<de.biba.triple.store.access.dmo.Entity> r = indexingServiceReader
				.detectPossibleConceptsLanguageSpecific(inputParameterdetectMeaningLanguageSpecific);
		assertTrue(r.size() > 0);
		System.out.println(r);
		System.out.println(r);

	}
	
	
	@Ignore
	@Test
	public void testgetAllDifferentValuesForAnInvalidProperty() {
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#Product";
		String property = "http://www.aidimme.es/FurnitureSectorOntology.owl#hasPrice";

		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		
		
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		
		List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property);
		System.out.println(test);
		assertTrue(test.size() == 0);
	}

	@Test
	public void testgetAllDifferentValuesForAUnknownPropertySource() {
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		String property = "http://UnknownSource#price";

		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		
		List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property);
		System.out.println(test);
		assertTrue(test.size() > 0);
	}
	
	@Ignore
	@Test
	public void testgetAllDifferentValuesForAOntologynPropertySource() {
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		String property = "http://www.aidimme.es/FurnitureSectorOntology.owl#hasColour";

		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		
		List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property);
		System.out.println(test);
		assertTrue(test.size() > 0);
	}
	
	@Ignore
	@Test
	public void testgetAllDifferentValuesForAUnknownPropertySourceComplexJson() {
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		String property = "http://UnknownSource#price";

		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";

		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		
		//precondition getProperties to laod the proeprtyCache
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		
		List<String> test = indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property);
		System.out.println(test);
		assertTrue(test.size() > 0);
	}
	
	@Ignore
	@Test
	public void testcreateOPtionalSPARQLAndExecuteIT(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		String uuid = "257423";
		
		System.out.println(URLEncoder.encode("localName:\"540*\""));
		
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect = new InputParamaterForExecuteOptionalSelect();
		inputParamaterForExecuteOptionalSelect.setLanguage("en");
		inputParamaterForExecuteOptionalSelect.setUuid(uuid);
		
//		String gg= "http://nimble-staging.salzburgresearch.at/index/item/select?fq=commodityClassficationUri:%22http://www.aidimme.es/FurnitureSectorOntology.owl%23Product%22&fq=certificateType:%5B*%20TO%20*%5D&facet.field=certificateType";
//		System.out.println(URLDecoder.decode(gg));
		
		OutputForExecuteSelect r = indexingServiceReader.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(r);
		System.out.println("##########################");
		r = indexingServiceReader.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(r);
	}
	
	
	@Test
	public void testcreateOPtionalSPARQLAndExecuteITI(){
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		String uuid = "255841";
		
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect = new InputParamaterForExecuteOptionalSelect();
		inputParamaterForExecuteOptionalSelect.setLanguage("en");
		inputParamaterForExecuteOptionalSelect.setUuid(uuid);
		
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		System.out.println("##########################");
		OutputForExecuteSelect  r = indexingServiceReader.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(r);
		System.out.println(r);
	}
	@Ignore
	@Test
	public void testPropertiesFromfTheGreenGroupI(){
		//http://www.aidimme.es/FurnitureSectorOntology.owl#hasEAN
		String cocnept = "http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard";
		String property = "http://www.aidimme.es/FurnitureSectorOntology.owl#hasEAN";
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		List<String> properties = indexingServiceReader.getAllPropertiesIncludingEverything(cocnept);
		System.out.println(indexingServiceReader.getAllDifferentValuesForAProperty(cocnept, property));
		
	}
	@Ignore
	@Test
	public void testgetAllMappableFields(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		List<ItemMappingFieldInformation> r = indexingServiceReader.getAllMappableFields();
	}
	@Ignore
	@Test
	public void testUBLProperties(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		String urlForPropertyInformationUBL = "https://nimble-platform.salzburgresearch.at/nimble/indexing-service/";
		indexingServiceReader.setUrlForPropertyInformationUBL(urlForPropertyInformationUBL );
		System.out.println(indexingServiceReader.requestStandardPropertiesFromUBL());
	}
	@Ignore
	@Test
	public void testrequestAllIndexFields(){
		String urlIndexingService = "http://nimble-staging.salzburgresearch.at/index/";
		IndexingServiceReader indexingServiceReader = new IndexingServiceReader(urlIndexingService);
		System.out.println(indexingServiceReader.requestAllIndexFields());
		
	}
	
}
