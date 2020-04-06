package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxonomyCache {

	private Map<String, List<String>>  cacheUPPerConceptURL = new HashMap<String, List<String>>();
	private Map<String, List<String>>  cacheDownPerConceptURL = new HashMap<String, List<String>>();
	
	
	public void addAllSubConcepts(String conceptURL, List<String> children){
		
		List<String> copy = new ArrayList<String>();
		copy.addAll(children);
		
		cacheDownPerConceptURL.put(conceptURL, copy);
	}
	
	public void addAllUpperConcepts(String conceptURL, List<String> parents){

		
		List<String> copy = new ArrayList<String>();
		copy.addAll(parents);

		cacheUPPerConceptURL.put(conceptURL, copy);
	}
	
	
	/**
	 * Returns all children to a given concept
	 * @param conceptURL the unique uel of a cocnept/category
	 * @return in any case a list. If there is no result it returns an empty list
	 */
	public List<String> getAllChildrenConcepts(String conceptURL){
		List<String> result = new ArrayList<String>();
		if (cacheDownPerConceptURL.containsKey(conceptURL)){
			result.addAll(cacheDownPerConceptURL.get(conceptURL));
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Concept is not part of the Taxonomy Cache");
		}
		return result;
	}
	
	
	/**
	 * Returns all parents to a given concept
	 * @param conCeptURL the unique uel of a cocnept/category
	 * @return in any case a list. If there is no result it returns an empty list
	 */
	public List<String> getAllParentsConcepts(String conceptURL){
		List<String> result = new ArrayList<String>();
		if (cacheUPPerConceptURL.containsKey(conceptURL)){
			result.addAll(cacheUPPerConceptURL.get(conceptURL));
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Concept is not part of the Taxonomy Cache");
		}
		return result;
	}
	
}
