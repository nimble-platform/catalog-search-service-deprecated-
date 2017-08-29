package eu.nimble.service.catalog.search.impl.dao.input;

public class Tuple {

	String urlOfProperty;
	String concept;
	public String getUrlOfProperty() {
		return urlOfProperty;
	}
	public void setUrlOfProperty(String urlOfProperty) {
		this.urlOfProperty = urlOfProperty;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	@Override
	public String toString() {
		return "Tuple [urlOfProperty=" + urlOfProperty + ", concept=" + concept + "]";
	}
	
	
}
