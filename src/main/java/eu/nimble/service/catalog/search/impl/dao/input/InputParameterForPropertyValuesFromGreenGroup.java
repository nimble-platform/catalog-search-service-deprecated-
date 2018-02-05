package eu.nimble.service.catalog.search.impl.dao.input;

import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;

public class InputParameterForPropertyValuesFromGreenGroup {

	private String conceptURL ;
	private String propertyURL;
	private PropertySource propertySource;
	
	public String getConceptURL() {
		return conceptURL;
	}
	public void setConceptURL(String conceptURL) {
		this.conceptURL = conceptURL;
	}
	public String getPropertyURL() {
		return propertyURL;
	}
	public void setPropertyURL(String propertyURL) {
		this.propertyURL = propertyURL;
	}
	@Override
	public String toString() {
		return "InputForPropertyValuesFromGreenGroup [conceptURL=" + conceptURL + ", propertyURL=" + propertyURL + "]";
	}
	public PropertySource getPropertySource() {
		return propertySource;
	}
	public void setPropertySource(PropertySource propertySource) {
		this.propertySource = propertySource;
	}
	
	
	
}
