package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import de.biba.triple.store.access.jena.Reader;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivationAndExecution;
import eu.nimble.service.catalog.search.mediator.NimbleSpecificSPARQLDeriviationAndExecution;
import eu.nimble.service.catalog.search.services.SQPDerivationService;

public class TestNimbleSpecificSPARQLDeriviation {

	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureSectorTaxonomy-v1.8_BIBA.owl";

	@Ignore
	@Test
	public void testcreateSPARQLForAllDomainSpecificProperties() {
		NimbleSpecificSPARQLDeriviationAndExecution deriviation = new NimbleSpecificSPARQLDeriviationAndExecution(null, null,null);
		String sparql = deriviation.createSPARQLForAllDomainSpecificProperties();
		System.out.println(sparql);

	}

	@Ignore
	@Test
	public void testgetPropertyValuesForOrangeGroup(){
		File file = new File("./src/main/resources/sqpConfiguration.xml");
		if (file.exists()){
		Reader reader = new Reader();
		reader.setModeToLocal();
		reader.loadOntologyModel(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		
		SQPDerivationService sqpDerivationService = new SQPDerivationService(null, "./src/main/resources/sqpConfiguration.xml");
		NimbleSpecificSPARQLDeriviationAndExecution deriviation = new NimbleSpecificSPARQLDeriviationAndExecution(reader, sqpDerivationService,null);
		String command = "companyName";
		String concept = "http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair";
		eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromOrangeGroup result = deriviation.getPropertyValuesForOrangeGroup(command, concept);
		System.out.println(result.getAllValues());
		
		
		
		}
		else{
			fail("No configuration file found");
		}
	}

	@Ignore
	@Test
	public void testgetAllDifferentValuesForAProperty(){
		MarmottaReader reader = new MarmottaReader("https://nimble-platform.salzburgresearch.at/marmotta");
		MediatorSPARQLDerivationAndExecution  mediatorSPARQLDerivationAndExecution  = null;
		SQPDerivationService sqpDerivationService = new SQPDerivationService(mediatorSPARQLDerivationAndExecution, "./src/main/resources/sqpConfiguration.xml");
		  mediatorSPARQLDerivationAndExecution = new MediatorSPARQLDerivationAndExecution("https://nimble-platform.salzburgresearch.at/marmotta", true, sqpDerivationService);
		
		  NimbleSpecificSPARQLDeriviationAndExecution deriviation = new NimbleSpecificSPARQLDeriviationAndExecution(reader,sqpDerivationService, mediatorSPARQLDerivationAndExecution);
		  List<String> result = deriviation.getAllDifferentValuesForAProperty("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish", "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name");
		  System.out.println(result);
	}
	
}
