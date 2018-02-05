package eu.nimble.service.catalog.search.impl.dao.output;

import de.biba.triple.store.access.enums.PropertyType;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;

public class OutputForPropertyFromConcept {

	private String propertyURL;
	private boolean datatypeProperty;
	private boolean objectProperty;
	private String translatedProperty;
	private PropertySource propertySource;
	
	
	public PropertySource getPropertySource() {
		return propertySource;
	}
	public void setPropertySource(PropertySource propertySource) {
		this.propertySource = propertySource;
	}
	public String getTranslatedProperty() {
		return translatedProperty;
	}
	public void setTranslatedProperty(String translatedProperty) {
		this.translatedProperty = translatedProperty;
	}
	public String getPropertyURL() {
		return propertyURL;
	}
	public void setPropertyURL(String propertyURL) {
		this.propertyURL = propertyURL;
	}
	public boolean isDatatypeProperty() {
		return datatypeProperty;
	}
	public void setDatatypeProperty(boolean datatypeProperty) {
		this.datatypeProperty = datatypeProperty;
	}
	public boolean isObjectProperty() {
		return objectProperty;
	}
	public void setObjectProperty(boolean objectProperty) {
		this.objectProperty = objectProperty;
	}
	@Override
	public String toString() {
		return "OutputForPropertyFromConcept [propertyURL=" + propertyURL + ", datatypeProperty=" + datatypeProperty
				+ ", objectProperty=" + objectProperty + ", translatedProperty=" + translatedProperty
				+ ", propertySource=" + propertySource + "]";
	}
	
	
	
}
