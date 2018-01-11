package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Test;

import de.biba.triple.store.access.jena.Reader;
import eu.nimble.service.catalog.search.mediator.NimbleSpecificSPARQLDeriviation;
import eu.nimble.service.catalog.search.services.SQPDerivationService;

public class TestNimbleSpecificSPARQLDeriviation {

	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

	@Test
	public void testcreateSPARQLForAllDomainSpecificProperties() {
		NimbleSpecificSPARQLDeriviation deriviation = new NimbleSpecificSPARQLDeriviation(null, null);
		String sparql = deriviation.createSPARQLForAllDomainSpecificProperties();
		System.out.println(sparql);

	}

	@Test
	public void testgetPropertyValuesForOrangeGroup(){
		File file = new File("./src/main/resources/sqpConfiguration.xml");
		if (file.exists()){
		Reader reader = new Reader();
		reader.setModeToLocal();
		reader.loadOntologyModel(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		
		SQPDerivationService sqpDerivationService = new SQPDerivationService(null, "./src/main/resources/sqpConfiguration.xml");
		NimbleSpecificSPARQLDeriviation deriviation = new NimbleSpecificSPARQLDeriviation(reader, sqpDerivationService);
		String command = "companyName";
		String concept = "http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair";
		eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromOrangeGroup result = deriviation.getPropertyValuesForOrangeGroup(command, concept);
		System.out.println(result.getAllValues());
		
		
		
		}
		else{
			fail("No configuration file found");
		}
	}

}
