package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

public class MediatorSPARQLDerivationTest {

	@Test
	@Ignore
	public void testGroupingOfPropertyValues(){
		MediatorSPARQLDerivation mediatorSPARQLDerivation = new MediatorSPARQLDerivation();
		
		String concept = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Bed";
		String property = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#price";
		
		Map<String, List<Group>> results = mediatorSPARQLDerivation.generateGroup(3, concept, property);
		System.out.println(results);
		
	}
	
	@Test
	@Ignore
	public void testgetViewForOneStepRange(){
		MediatorSPARQLDerivation mediatorSPARQLDerivation = new MediatorSPARQLDerivation("C:/ontology/FurnitureTaxonomy-v1.4-biba.owl");
		LocalOntologyView helper = new LocalOntologyView();
		helper.setConcept("HighChair");
		LocalOntologyView result = mediatorSPARQLDerivation.getViewForOneStepRange(helper.getConcept(), helper);
		assertTrue(result.getDataproperties().size() > 0);
		System.out.println(result.getDataproperties());
		System.out.println(result.getObjectproperties());
	}
	
}
