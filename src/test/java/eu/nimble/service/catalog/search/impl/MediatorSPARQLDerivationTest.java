package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

public class MediatorSPARQLDerivationTest extends MediatorSPARQLDerivation {

	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureTaxonomy-v1.4-biba.owl";

	@Test
	@Ignore
	public void testGroupingOfPropertyValues() {
		MediatorSPARQLDerivation mediatorSPARQLDerivation = new MediatorSPARQLDerivation();

		String concept = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Bed";
		String property = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#price";

		Map<String, List<Group>> results = mediatorSPARQLDerivation.generateGroup(3, concept, property);
		System.out.println(results);

	}

	@Test
	@Ignore
	public void testgetViewForOneStepRange() {
		MediatorSPARQLDerivation mediatorSPARQLDerivation = new MediatorSPARQLDerivation(
				C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		LocalOntologyView helper = new LocalOntologyView();
		helper.setConcept("HighChair");
		LocalOntologyView result = mediatorSPARQLDerivation.getViewForOneStepRange(helper.getConcept(), helper);
		assertTrue(result.getDataproperties().size() > 0);
		System.out.println(result.getDataproperties());
		System.out.println(result.getObjectproperties());
	}

	@Test
	@Ignore
	public void testCreateSPARQL() {
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		
		Filter f1 = new Filter();
		f1.setProperty("hasHeight");
		f1.setMax(5.2f);
		f1.setMin(3.0f);
		
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.getFilters().add(f1);
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		String sparql = createSparql(inputParamaterForExecuteSelect);
		assertTrue(sparql.length() > 10);
		System.out.println(sparql);
		
	}
	
	@Test
	@Ignore
	public void testcreateSPARQLAndExecuteIT(){
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		OutputForExecuteSelect result = createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		assertTrue(result.getColumns().size() > 0);
		System.out.println(result);
		
	}

}