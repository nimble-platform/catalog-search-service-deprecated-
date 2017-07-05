package eu.nimble.service.catalog.search.impl;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

public class MediatorSPARQLDerivationTest {

	@Test
	public void testGroupingOfPropertyValues(){
		MediatorSPARQLDerivation mediatorSPARQLDerivation = new MediatorSPARQLDerivation();
		
		String concept = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Bed";
		String property = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#price";
		
		Map<String, List<Group>> results = mediatorSPARQLDerivation.generateGroup(3, concept, property);
		System.out.println(results);
		
	}
	
}
