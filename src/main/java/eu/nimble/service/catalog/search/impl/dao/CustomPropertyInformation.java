package eu.nimble.service.catalog.search.impl.dao;

public class CustomPropertyInformation {

	private String propertyName;
	private String typeDescription;
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public String getTypeDescription() {
		return typeDescription;
	}
	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}
	@Override
	public String toString() {
		return "CustomPropertyInformation [propertyName=" + propertyName + ", typeDescription=" + typeDescription + "]";
	}
	
	
	
}
