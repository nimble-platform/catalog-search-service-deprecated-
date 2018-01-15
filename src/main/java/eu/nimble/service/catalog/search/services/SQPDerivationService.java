package eu.nimble.service.catalog.search.services;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import eu.nimble.service.catalog.search.impl.dao.sqp.SQPConfiguration;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPConfigurations;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPMapping;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivationAndExecution;

/**
 * This service will be used to dtermine the sqps for a given concepts
 * 
 * @author marco_000
 *
 */
public class SQPDerivationService {

	private Map<String, List<SQPConfiguration>> availableSQPsUsingConceptsAsKey = new HashMap<String, List<SQPConfiguration>>();
	private Map<String, SQPConfiguration> availableSQPsUsingNameAsKey = new HashMap<String, SQPConfiguration>();
	private MediatorSPARQLDerivationAndExecution sparqlDerivation = null;

	/**
	 * Must be extenbded for using a database for lookup
	 * 
	 * @param sqpConfigurationPath
	 */
	public SQPDerivationService(MediatorSPARQLDerivationAndExecution sparqlDerivation, String sqpConfigurationPath) {

		this.sparqlDerivation = sparqlDerivation;
		init(sqpConfigurationPath);
	}

	public SQPConfiguration getSpecificSQPConfiguration(String command) {
		return availableSQPsUsingNameAsKey.get(command);
	}

	public boolean isITSAQPCommand(String command) {
		return availableSQPsUsingNameAsKey.containsKey(command) ? true : false;
	}

	public void init(String sqpConfigurationPath) {
		File file = new File(sqpConfigurationPath);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SQPConfigurations.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SQPConfigurations configurations = (SQPConfigurations) jaxbUnmarshaller.unmarshal(file);
			for (SQPConfiguration sqpConfiguration : configurations.getSQPConfiguration()) {
				String conceptURL = sqpConfiguration.getSQPMapping().getSource().getSourceConcept();
				String SQPName = sqpConfiguration.getSQPName();
				
				availableSQPsUsingNameAsKey.put(SQPName, sqpConfiguration);
				
				if (availableSQPsUsingConceptsAsKey.containsKey(conceptURL)) {
					availableSQPsUsingConceptsAsKey.get(conceptURL).add(sqpConfiguration);
				} else {
					List<SQPConfiguration> sqps = new ArrayList<SQPConfiguration>();
					sqps.add(sqpConfiguration);
					availableSQPsUsingConceptsAsKey.put(conceptURL, sqps);
				}
			}
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getMessage());
		}
	}

	/**
	 * Get all parent concepts and ask for each of them the available set
	 * 
	 * @param concept
	 * @return
	 */
	public List<String> getListOfAvailableSQPs(String concept) {
		List<String> result = new ArrayList<String>();
		List<String> allDerivedConcepts = sparqlDerivation.getAllDerivedConcepts(concept);
		for (String dependantConcept : allDerivedConcepts) {
			if (availableSQPsUsingConceptsAsKey.containsKey(dependantConcept)) {
				for (SQPConfiguration sqpConfiguration : availableSQPsUsingConceptsAsKey.get(concept)) {
					result.add(sqpConfiguration.getSQPName());
				}

			}
		}
		return result;
	}

	public SQPConfiguration getSQPConfiguration(String concept, String orangeCommand) {
		if (availableSQPsUsingConceptsAsKey.containsKey(concept)) {
			for (SQPConfiguration sqpConfiguration : availableSQPsUsingConceptsAsKey.get(concept)) {

				if (sqpConfiguration.getSQPName().equals(orangeCommand)) {
					return sqpConfiguration;
				}

			}

		}
		return null;
	}
	
	public String[] splitTargetConfiguration(SQPConfiguration sqpConfiguration){
		return  sqpConfiguration.getSQPMapping().getTarget().getTargetPathFromSource().split(";");
	}
	
}
