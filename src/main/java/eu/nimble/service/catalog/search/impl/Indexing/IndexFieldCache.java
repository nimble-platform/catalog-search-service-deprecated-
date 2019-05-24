package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.ArrayList;
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
public class IndexFieldCache extends IndexingServiceConstant{

	private static final String HTTP_UNKNOWN_SOURCE_DESCRIPTION = "http://UnknownSource#description";
	private static final String HTTP_UNKNOWN_SOURCE_PRICE = "http://UnknownSource#price";
	private List<IndexFields> allndexFields = new ArrayList<IndexFields>();
	private Map<String, IndexFields> indexFieldPerPropertyURL = new HashMap<String, IndexFields> ();
	private Map<String, IndexFields> indexFieldPerPropertyURLManuallyAdded = new HashMap<String, IndexFields> ();
	//private Map<String, PropertyRelevance> propertyRelevanceByPropertyURL  = new HashMap<String, PropertyRelevance>();
	private Map<String, Map<String, PropertyRelevance>> propertyRelevancesByConceptURL = new HashMap<String, Map<String, PropertyRelevance>>();
	
	
	
	public IndexFieldCache() {
		super();
		addManualFilters();
	}


	public void addManualFilters(){
		//("http://UnknownSource#name");
		//http://UnknownSource#description");
		//("http://UnknownSource#price");
		
		IndexFields fieldsName = new IndexFields();
		fieldsName.setDataType(this.HTTP_WWW_W3_ORG_2001_XML_SCHEMA + "string");
		fieldsName.setFieldName("allLabels");
		fieldsName.setUri("http://UnknownSource#name");
		indexFieldPerPropertyURLManuallyAdded.put("http://UnknownSource#name", fieldsName);
		
		
		IndexFields fieldsDecription = new IndexFields();
		fieldsDecription.setDataType(this.HTTP_WWW_W3_ORG_2001_XML_SCHEMA + "string");
		fieldsDecription.setFieldName("en_desc");
		fieldsDecription.setUri(HTTP_UNKNOWN_SOURCE_DESCRIPTION);
		indexFieldPerPropertyURLManuallyAdded.put(HTTP_UNKNOWN_SOURCE_DESCRIPTION, fieldsDecription);
		
		IndexFields fieldsPrice = new IndexFields();
		fieldsPrice.setDataType(this.HTTP_WWW_W3_ORG_2001_XML_SCHEMA + "double");
		fieldsPrice.setFieldName("_price");
		fieldsPrice.setUri(HTTP_UNKNOWN_SOURCE_PRICE);
		indexFieldPerPropertyURLManuallyAdded.put(HTTP_UNKNOWN_SOURCE_PRICE, fieldsPrice);
		
		IndexFields fieldsCatalogueID = new IndexFields();
		fieldsCatalogueID.setDataType(this.HTTP_WWW_W3_ORG_2001_XML_SCHEMA + "string");
		fieldsCatalogueID.setFieldName("catalogueId");
		fieldsCatalogueID.setUri(HTTP_UNKNOWN_SOURCE_PRICE);
		indexFieldPerPropertyURLManuallyAdded.put(HTTP_UNKNOWN_SOURCE_PRICE, fieldsCatalogueID);
		
		
	}
	
	
	public void insertAllIndexFields(Collection<IndexFields> indexFields){
		allndexFields.clear();
		Iterator<com.google.gson.internal.LinkedTreeMap> iterator = (Iterator<LinkedTreeMap>) ((Object)indexFields.iterator());
		while (iterator.hasNext()){
			com.google.gson.internal.LinkedTreeMap map  = iterator.next();
			
			IndexFields field = new IndexFields();
			field.setUri((String) map.get("uri"));
			field.setFieldName((String) map.get("fieldName"));
			field.setDataType((String)map.get("dataType"));
			field.setDynamicBase((String)map.get("dynamicBase"));
			field.setMappedName((String)map.get("mappedName"));
			field.setDynamicPart((String)map.get("dynamicPart"));
			
			allndexFields.add(field);
			
			if (field.getUri() != null && field.getUri().length() > 0){
			indexFieldPerPropertyURL.put(field.getUri(), field);
			}
		}
		
		adaptPriceProperty(allndexFields);
		adaptDescriptionProperty(allndexFields);
	}
	
	private void adaptPriceProperty(List<IndexFields> allndexFields2) {
		List<String>  relevantPriceFieldNames = new ArrayList<String>();
		for (IndexFields indexFields: allndexFields2){
			
			if (indexFields.getDynamicBase()!= null && indexFields.getDynamicBase().equals("*_price")){
				relevantPriceFieldNames.add(indexFields.getFieldName());
			}
			
		}
		
		if (relevantPriceFieldNames.size() > 0){
			indexFieldPerPropertyURLManuallyAdded.get(HTTP_UNKNOWN_SOURCE_PRICE).getDifferentFieldNames().addAll(relevantPriceFieldNames);
		}
		
	}


	private void adaptDescriptionProperty(List<IndexFields> allndexFields2) {
		List<String>  relevantPriceFieldNames = new ArrayList<String>();
		for (IndexFields indexFields: allndexFields2){
			if (indexFields.getDynamicBase()!= null && indexFields.getDynamicBase().equals("*_desc")){
				relevantPriceFieldNames.add(indexFields.getFieldName());
			}
			
		}
		
		if (relevantPriceFieldNames.size() > 0){
			indexFieldPerPropertyURLManuallyAdded.get(HTTP_UNKNOWN_SOURCE_DESCRIPTION).getDifferentFieldNames().addAll(relevantPriceFieldNames);
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
	
	
	
	
	
	public String getIndexFieldForAnyKindOfProperty( String propertyURL){
		if (propertyURL != null && propertyURL.length() > 0 && indexFieldPerPropertyURL.containsKey(propertyURL)){
			return indexFieldPerPropertyURL.get(propertyURL).getFieldName();
		}
		
		if (propertyURL != null && propertyURL.length() > 0 && indexFieldPerPropertyURLManuallyAdded.containsKey(propertyURL)){
			return indexFieldPerPropertyURLManuallyAdded.get(propertyURL).getFieldName();
		}
		
		Logger.getAnonymousLogger().log(Level.WARNING, "Found no fieldname for: " + propertyURL);
		return null;
	}
	
	
	public IndexFields getIndexFieldForAnyKindOfPropertyAsIndexFields( String propertyURL){
		if (propertyURL != null && propertyURL.length() > 0 && indexFieldPerPropertyURL.containsKey(propertyURL)){
			return indexFieldPerPropertyURL.get(propertyURL);
		}
		
		if (propertyURL != null && propertyURL.length() > 0 && indexFieldPerPropertyURLManuallyAdded.containsKey(propertyURL)){
			return indexFieldPerPropertyURLManuallyAdded.get(propertyURL);
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
