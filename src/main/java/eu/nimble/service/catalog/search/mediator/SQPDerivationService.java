package eu.nimble.service.catalog.search.mediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This service will be used to dtermine the sqps for a given concepts
 * 
 * @author marco_000
 *
 */
public class SQPDerivationService {

	private Map<String, List<String>> availableSQPs = new HashMap<String, List<String>>();
	private MediatorSPARQLDerivation sparqlDerivation = null;

	/**
	 * Must be extenbded for using a database for lookup
	 */
	public SQPDerivationService(MediatorSPARQLDerivation sparqlDerivation) {
		this.sparqlDerivation = sparqlDerivation;
		List<String> list = new ArrayList<String>();
		list.add("hasSupplier");
		availableSQPs.put("HighChair", list);
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
				result.addAll(availableSQPs.get(dependantConcept));
			}
		}
		return result;
	}

}
