package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.internal.LinkedTreeMap;

import eu.nimble.service.catalog.search.impl.dao.IndexFields;
import eu.nimble.service.catalog.search.impl.dao.PropertyRelevance;

/**
 * This cache is required to know which indexFields can be sued for which solr query
 * @author fma
 *
 */
public class IndexFieldCache {

	private Map<String, IndexFields> indexFieldPerPropertyURL = new HashMap<String, IndexFields> ();
	//private Map<String, PropertyRelevance> propertyRelevanceByPropertyURL  = new HashMap<String, PropertyRelevance>();
	private Map<String, Map<String, PropertyRelevance>> propertyRelevancesByConceptURL = new HashMap<String, Map<String, PropertyRelevance>>();
	
	public void insertAllIndexFields(Collection<IndexFields> indexFields){
		
		Iterator<com.google.gson.internal.LinkedTreeMap> iterator = (Iterator<LinkedTreeMap>) ((Object)indexFields.iterator());
		while (iterator.hasNext()){
			com.google.gson.internal.LinkedTreeMap map  = iterator.next();
			
			IndexFields field = new IndexFields();
			field.setUri((String) map.get("uri"));
			field.setFieldName((String) map.get("fieldName"));
			
			
			if (field.getUri() != null && field.getUri().length() > 0){
			indexFieldPerPropertyURL.put(field.getUri(), field);
			}
		}
	}
	
	public boolean isIndexFieldInfoContained(String propoertyUrl){
		return indexFieldPerPropertyURL.containsKey(propoertyUrl);
	}
	
	public boolean isPropertyRelevanceInfoContained(String concept, String propoertyUrl){
		if( propertyRelevancesByConceptURL.containsKey(concept) && propertyRelevancesByConceptURL.get(concept).containsKey(propoertyUrl)){
			return true;
		}
		return false;
	}
	
	
	public boolean isPropertyRelevanceGiven(String concept, String propoertyUrl){
		if( propertyRelevancesByConceptURL.containsKey(concept) && propertyRelevancesByConceptURL.get(concept).containsKey(propoertyUrl)){
			return propertyRelevancesByConceptURL.get(concept).get(propoertyUrl).isItRelevant();
		}
		return false;
	}
	
	public void addProopertyRelevance(String conceptURL, String propertyURL, PropertyRelevance pRelevance){
		Map<String, PropertyRelevance> pRRelevanceMap = null;
		if (propertyRelevancesByConceptURL.containsKey(conceptURL)){
			pRRelevanceMap = propertyRelevancesByConceptURL.get(conceptURL);
		}
		else{
			pRRelevanceMap = new HashMap<String, PropertyRelevance>();
			propertyRelevancesByConceptURL.put(conceptURL, pRRelevanceMap);
		}
		pRRelevanceMap.put(propertyURL, pRelevance);
	}
	
	public String getIndexFieldForAOntologicalProperty( String propertyURL){
		if (propertyURL != null && propertyURL.length() > 0 && indexFieldPerPropertyURL.containsKey(propertyURL)){
			return indexFieldPerPropertyURL.get(propertyURL).getFieldName();
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Found no fieldname for: " + propertyURL);
		return null;
	}
	
	public IndexFields getIndexFieldDAOForAOntologicalProperty( String propertyURL){
		if (propertyURL != null && propertyURL.length() > 0){
			return indexFieldPerPropertyURL.get(propertyURL);
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Found no fieldname for: " + propertyURL);
		return null;
	}
	
	
	
	
}
