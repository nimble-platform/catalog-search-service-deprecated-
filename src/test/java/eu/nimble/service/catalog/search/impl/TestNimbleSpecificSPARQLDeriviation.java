package eu.nimble.service.catalog.search.impl;

import org.junit.Test;

import eu.nimble.service.catalog.search.mediator.NimbleSpecificSPARQLDeriviation;

public class TestNimbleSpecificSPARQLDeriviation {

	@Test
	public void testcreateSPARQLForAllDomainSpecificProperties(){
		NimbleSpecificSPARQLDeriviation deriviation = new NimbleSpecificSPARQLDeriviation(null);
		String sparql =  deriviation.createSPARQLForAllDomainSpecificProperties();
		System.out.println(sparql);
		
	}
	
}
