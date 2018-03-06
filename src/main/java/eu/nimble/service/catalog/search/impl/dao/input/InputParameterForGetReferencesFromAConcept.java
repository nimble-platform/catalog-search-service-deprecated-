package eu.nimble.service.catalog.search.impl.dao.input;

import de.biba.triple.store.access.enums.Language;

public class InputParameterForGetReferencesFromAConcept {

	public String conceptURL;
	public String language ="";

	public String getConceptURL() {
		return conceptURL;
	}

	public void setConceptURL(String conceptURL) {
		this.conceptURL = conceptURL;
	}

	
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "InputForgetReferencesFromAConcept [conceptURL=" + conceptURL + "]";
	}
	
	
	
	
}
