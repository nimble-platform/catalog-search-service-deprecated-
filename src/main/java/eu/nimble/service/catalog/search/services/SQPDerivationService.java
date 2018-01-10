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
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

/**
 * This service will be used to dtermine the sqps for a given concepts
 * 
 * @author marco_000
 *
 */
public class SQPDerivationService {

	private Map<String, List<SQPConfiguration>> availableSQPs = new HashMap<String, List<SQPConfiguration>>();
	private MediatorSPARQLDerivation sparqlDerivation = null;

	/**
	 * Must be extenbded for using a database for lookup
	 * 
	 * @param sqpConfigurationPath
	 */
	public SQPDerivationService(MediatorSPARQLDerivation sparqlDerivation, String sqpConfigurationPath) {

		this.sparqlDerivation = sparqlDerivation;
		init(sqpConfigurationPath);
	}

	public void init(String sqpConfigurationPath) {
		File file = new File(sqpConfigurationPath);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(SQPConfigurations.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			SQPConfigurations configurations = (SQPConfigurations) jaxbUnmarshaller.unmarshal(file);
			for (SQPConfiguration sqpConfiguration : configurations.getSQPConfiguration()) {
				String conceptURL = sqpConfiguration.getSQPMapping().getSource().getSourceConcept();
				if (availableSQPs.containsKey(conceptURL)) {
					availableSQPs.get(conceptURL).add(sqpConfiguration);
				} else {
					List<SQPConfiguration> sqps = new ArrayList<SQPConfiguration>();
					sqps.add(sqpConfiguration);
					availableSQPs.put(conceptURL, sqps);
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
			int index = dependantConcept.indexOf("#") + 1;
			dependantConcept = dependantConcept.substring(index);
			if (availableSQPs.containsKey(dependantConcept)) {
				for (SQPConfiguration sqpConfiguration : availableSQPs.get(concept)) {
					result.add(sqpConfiguration.getSQPName());
				}
				
			}
		}
		return result;
	}

}