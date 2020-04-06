package de.biba.triple.store.access.dmo;

public class PropertyConceptAssignment {

	private String urlOfProperty;
	private String urlOfConcept;
	private String type;
	
	
	public String getUrlOfProperty() {
		return urlOfProperty;
	}
	public void setUrlOfProperty(String urlOfProperty) {
		this.urlOfProperty = urlOfProperty;
	}
	public String getUrlOfConcept() {
		return urlOfConcept;
	}
	public void setUrlOfConcept(String urlOfConcept) {
		this.urlOfConcept = urlOfConcept;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "PropertyConceptAssignment [urlOfProperty=" + urlOfProperty + ", urlOfConcept=" + urlOfConcept
				+ ", type=" + type + "]";
	}
	
	
	
	
}
