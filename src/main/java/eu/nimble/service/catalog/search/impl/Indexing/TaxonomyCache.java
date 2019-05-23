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
	
	
	public List<String> getAllChildrenConcepts(String cocneptURL){
		List<String> result = new ArrayList<String>();
		if (cacheDownPerConceptURL.containsKey(cocneptURL)){
			result.addAll(cacheDownPerConceptURL.get(cocneptURL));
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Concept is not part of the Taxonomy Cache");
		}
		return result;
	}
	
	
	public List<String> getAllParentsConcepts(String cocneptURL){
		List<String> result = new ArrayList<String>();
		if (cacheUPPerConceptURL.containsKey(cocneptURL)){
			result.addAll(cacheUPPerConceptURL.get(cocneptURL));
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Concept is not part of the Taxonomy Cache");
		}
		return result;
	}
	
}
