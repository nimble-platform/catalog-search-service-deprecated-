package eu.nimble.service.catalog.search.impl.dao.output;

public class OutputForPropertyFromConcept {

	private String propertyURL;
	private boolean datatypeProperty;
	private boolean objectProperty;
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
				+ ", objectProperty=" + objectProperty + "]";
	}
	
	
	
}
