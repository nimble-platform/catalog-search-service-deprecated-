package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import eu.nimble.service.catalog.search.impl.dao.PropertyType;

public class PropertyInformationCache extends IndexingServiceConstant {

	private static final String REQUESTED_PROPS_WHICH_ARE_NOT_IN_THE_CACHE = "Requested props which are not in the cache";
	private Map<String, List<PropertyType>> cachedProeprtiesPerConceptURI = new HashMap<String, List<PropertyType>>();
	private Map<String, PropertyType> cachedPropertiesPerNamefield = new HashMap<String, PropertyType>();
	private List<PropertyType> allStandardProps = new ArrayList<PropertyType>();
	private List<PropertyType> allUBLSpeciifcProperties = new ArrayList<PropertyType>();
	private Map<String, PropertyType> allPropertytype = new HashMap<String, PropertyType>(); 

	public boolean isConceptAlreadyContained(String conceptUri) {
		return cachedProeprtiesPerConceptURI.containsKey(conceptUri);
	}

	public void addConcept(String concept, List<PropertyType> properties) {
		cachedProeprtiesPerConceptURI.put(concept, properties);
		
		//Store it in the overall propertyMap
		properties.forEach( x-> allPropertytype.put(x.getUri(), x));
		
		//Store it in the standard propoerties
		properties.forEach( x-> {
			if (x.getUri().contains(namespace)){
				boolean canBeAdded = true;
				for (PropertyType ptype: allStandardProps){
					if (ptype.getItemFieldNames().equals(x.getItemFieldNames())){
						canBeAdded = false;
						break;
					}
				}
				if (canBeAdded){
					allStandardProps.add(x);
				}
			}
		});
		
		
		//Store it in the ubl propoerties
				properties.forEach( x-> {
					if (x.getUri().contains(nameSPACEUBL)){
						boolean canBeAdded = true;
						for (PropertyType ptype: allUBLSpeciifcProperties){
							if (ptype.getItemFieldNames().equals(x.getItemFieldNames())){
								canBeAdded = false;
								break;
							}
						}
						if (canBeAdded){
							allUBLSpeciifcProperties.add(x);
						}
					}
				});
		
		for (PropertyType propertyType : properties) {
			if (propertyType != null && propertyType.getItemFieldNames() != null) {
				propertyType.getItemFieldNames().forEach(x -> {
					if (x != null) {
						cachedPropertiesPerNamefield.put(x, propertyType);
					}
				});
			} else {
				if (propertyType != null) {
					Logger.getAnonymousLogger().log(Level.WARNING,
							"There is no fieldnames for: " + propertyType.getUri());
				} else {
					Logger.getAnonymousLogger().log(Level.WARNING,
							"There is no property information available for: " + concept);
				}

			}
		}

	}

	public List<PropertyType> getAllStandardProps() {
		if (allStandardProps.size()==0){
			Logger.getAnonymousLogger().log(Level.WARNING, REQUESTED_PROPS_WHICH_ARE_NOT_IN_THE_CACHE);
		}
		return allStandardProps;
	}

	public void setAllStandardProps(List<PropertyType> allStandardProps) {

		this.allStandardProps.clear();
		this.allStandardProps.addAll(allStandardProps);
	}

	public List<PropertyType> getAllUBLSpeciifcProperties() {
		if (allUBLSpeciifcProperties.size()==0){
			Logger.getAnonymousLogger().log(Level.WARNING, REQUESTED_PROPS_WHICH_ARE_NOT_IN_THE_CACHE);
		}
		return allUBLSpeciifcProperties;
	}

	public void setAllUBLSpeciifcProperties(List<PropertyType> allUBLSpeciifcProperties) {
		this.allUBLSpeciifcProperties.clear();
		this.allUBLSpeciifcProperties.addAll(allUBLSpeciifcProperties);
	}

	public boolean isNameFieldAlreadyContained(String nameField) {
		return cachedPropertiesPerNamefield.containsKey(nameField);
	}

	public PropertyType getPropertyTypeForANameField(String nameField) {
		return cachedPropertiesPerNamefield.get(nameField);
	}

	public PropertyType getPropertyTypeForASingleProperty(String conceptURL, String propertyURL) {
		List<PropertyType> r = getAllCachedPropertiesForAConcept(conceptURL);
		List<PropertyType> filteredResult = r.stream().filter(x -> x.getUri().equals(propertyURL))
				.collect(Collectors.toList());
		if (filteredResult != null && filteredResult.size() > 0) {
			return filteredResult.get(0);
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"Expect to find the property: " + propertyURL + " for the concept:  " + conceptURL);
			;
			return null;
		}
	}
	
	
	public PropertyType getPropertyTypeForASingleProperty( String propertyURL) {
		PropertyType result = allPropertytype.get(propertyURL);
		if (result != null) {
			return result;
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"Expect to find the property: " + propertyURL + " for  any the concept:  ");
			;
			return null;
		}
	}
	

	public List<PropertyType> getAllCachedPropertiesForAConcept(String conceptURL) {
		List<PropertyType> result = new ArrayList<PropertyType>();
		if (cachedProeprtiesPerConceptURI.containsKey(conceptURL)) {
			cachedProeprtiesPerConceptURI.get(conceptURL).forEach(x -> result.add(x));
		}

		return result;
	}

	
	
	

	@Override
	public String toString() {
		return "PropertyInformationCache [cachedProeprtiesPerConceptURI=" + cachedProeprtiesPerConceptURI
				+ ", cachedPropertiesPerNamefield=" + cachedPropertiesPerNamefield + ", allStandardProps="
				+ allStandardProps + ", allUBLSpeciifcProperties=" + allUBLSpeciifcProperties + "]";
	}

}
