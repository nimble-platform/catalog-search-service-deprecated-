package eu.nimble.service.catalog.search.impl.dao;

public class InputParameterForgetPropertyValuesDiscretised {

	String concept = "";
	String property = "";
	String language ="";
	int amountOfGroups = 0;

	public String getConcept() {
		return concept;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public int getAmountOfGroups() {
		return amountOfGroups;
	}

	public void setAmountOfGroups(int amountOfGroups) {
		this.amountOfGroups = amountOfGroups;
	}

	@Override
	public String toString() {
		return "InputParameterForgetPropertyValuesDiscretised [concept=" + concept + ", property=" + property
				+ ", language=" + language + ", amountOfGroups=" + amountOfGroups + "]";
	}

}
