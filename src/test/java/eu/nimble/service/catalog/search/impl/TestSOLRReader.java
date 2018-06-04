package eu.nimble.service.catalog.search.impl;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.impl.SOLRAccess.SOLRReader;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;

public class TestSOLRReader {

	@Test
	@Ignore
	public void testgetConnection() {
		SOLRReader solrReader = new SOLRReader();
		Object response = solrReader.query("*.*");
		System.out.println(solrReader.createResultList(response, "item_manufacturer_name"));
		// :*&rows=0&wt=csv

	}

	@Test
	@Ignore
	public void testgetConcepts() {
		SOLRReader solrReader = new SOLRReader();
		List<String> concepts = solrReader.getAllConcepts("PieceOfFurniture");
		System.out.println(concepts);
	}

	// getPropertyType
	@Test
	@Ignore
	public void testgetPropertyType() {
		SOLRReader solrReader = new SOLRReader();
		PropertyType properType = solrReader
				.getPropertyType("http://www.aidimme.es/FurnitureSectorOntology.owl#hasCatalogue");
		System.out.println(properType);
	}

	// getAllPropertiesIncludingEverything
	@Test
	@Ignore
	public void testgetgetAllPropertiesIncludingEverything() {
		SOLRReader solrReader = new SOLRReader();
		List<String> properType = solrReader
				.getAllPropertiesIncludingEverything("http://www.aidimme.es/FurnitureSectorOntology.owl#Company");
		System.out.println(properType);
	}

	@Test
	@Ignore
	public void testgetNativeSupportedLangauges() {
		SOLRReader reader = new SOLRReader();
		List<Language> result = reader.getNativeSupportedLangauges();
		System.out.println(result);
	}

	@Test
	@Ignore
	public void testtranslateProperty() {
		SOLRReader reader = new SOLRReader();
		String label = reader.translateProperty("http://www.w3.org/ns/org#memberOf", Language.ENGLISH);
		System.out.println(label);

		String label2 = reader.translateProperty("http://www.w3.org/ns/org#memberOf", Language.SPANISH);
		System.out.println(label2);
	}

	@Test
	@Ignore
	public void testgetAllValuesForAGivenProperty() {
		SOLRReader reader = new SOLRReader(
				"http://nimble-staging.salzburgresearch.at/marmotta/solr/catalogue_semantic_search/", "", "");
		List<String> result = reader.getAllValuesForAGivenProperty(
				"http://www.aidimme.es/FurnitureSectorOntology.owl#MDFBoard", "item_price",
				PropertySource.ADDITIONAL_ITEM_PROPERTY);
	}

	@Test
	@Ignore
	public void testcreateSPARQLAndExecuteIT() {
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept("*");
		inputParamaterForExecuteSelect.setLanguage("en");
		inputParamaterForExecuteSelect.getParametersURL().add("item_name");
		inputParamaterForExecuteSelect.getParametersURL().add("item_price");

		SOLRReader reader = new SOLRReader();
		OutputForExecuteSelect executeSelect = reader.createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		System.out.println(executeSelect);
	}

	@Test
	@Ignore
	public void testcreateSPARQLAndExecuteITMinValue() {
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
		OutputForExecuteSelect executeSelect = reader.createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		System.out.println(executeSelect);
	}

	@Test
	@Ignore
	public void testcreateOPtionalSPARQLAndExecuteIT() {
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect = new InputParamaterForExecuteOptionalSelect();
		inputParamaterForExecuteOptionalSelect.setLanguage("en");
		inputParamaterForExecuteOptionalSelect.setUuid(
				"urn:oasis:names:specification:ubl:schema:xsd:Catalogue-2#INS776a79b6-5e86-4ebd-9b71-eaca8e92a257_CatalogueLineType_12");
		SOLRReader reader = new SOLRReader();
		OutputForExecuteSelect executeSelect = reader
				.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteOptionalSelect);
		System.out.println(executeSelect);
	}

}
