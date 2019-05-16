package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import eu.nimble.service.catalog.search.impl.dao.PropertyType;

public class PropertyInformationCache {

	private Map<String, List<PropertyType>> cachedProeprtiesPerConceptURI = new HashMap<String, List<PropertyType>>();
	private Map<String, PropertyType> cachedPropertiesPerNamefield = new HashMap<String, PropertyType>();

	public boolean isConceptAlreadyContained(String conceptUri) {
		return cachedProeprtiesPerConceptURI.containsKey(conceptUri);
	}

	public void addConcept(String concept, List<PropertyType> properties) {
		cachedProeprtiesPerConceptURI.put(concept, properties);
		for (PropertyType propertyType : properties) {
			if (propertyType != null && propertyType.getItemFieldNames() != null){
			propertyType.getItemFieldNames().forEach(x -> {
				if (x != null){
					cachedPropertiesPerNamefield.put(x, propertyType);
				}
			});
			}
			else{
				if (propertyType != null){
				Logger.getAnonymousLogger().log(Level.WARNING, "There is no fieldnames for: " + propertyType.getUri());
				}
				else{
					Logger.getAnonymousLogger().log(Level.WARNING, "There is no property information available for: " + concept);
				}
				
			}
		}

	}

	public boolean isNameFieldAlreadyContained(String nameField) {
		return cachedPropertiesPerNamefield.containsKey(nameField);
	}

	public PropertyType getPropertyTypeForANameField(String nameField) {
		return cachedPropertiesPerNamefield.get(nameField);
	}

	
	public PropertyType getPropertyTypeForASingleProperty(String conceptURL, String propertyURL){
		List<PropertyType> r = getAllCachedPropertiesForAConcept(conceptURL);
		List<PropertyType> filteredResult = r.stream().filter(x -> x.getUri().equals(propertyURL)).collect(Collectors.toList());
		if (filteredResult != null && filteredResult.size() > 0){
			return filteredResult.get(0);
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Expect to find the property: " + propertyURL + " for the concept:  " + conceptURL);;
			return null;
		}
	}
	
	public List<PropertyType> getAllCachedPropertiesForAConcept(String conceptURL){
		List<PropertyType> result = new ArrayList<PropertyType>();
		if(cachedProeprtiesPerConceptURI.containsKey(conceptURL)){
			cachedProeprtiesPerConceptURI.get(conceptURL).forEach( x-> result.add(x));
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		return "PropertyInformationCache [cachedProeprtiesPerConceptURI=" + cachedProeprtiesPerConceptURI
				+ ", cachedPropertiesPerNamefield=" + cachedPropertiesPerNamefield + "]";
	}

	
}
