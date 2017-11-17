package eu.nimble.service.catalog.search.impl.dao.input;

import de.biba.triple.store.access.enums.Language;

public class InputParameterForGetReferencesFromAConcept {

	private String conceptURL;
	private Language language;

	public String getConceptURL() {
		return conceptURL;
	}

	public void setConceptURL(String conceptURL) {
		this.conceptURL = conceptURL;
	}

	
	
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	@Override
	public String toString() {
		return "InputForgetReferencesFromAConcept [conceptURL=" + conceptURL + "]";
	}
	
	
	
	
}
