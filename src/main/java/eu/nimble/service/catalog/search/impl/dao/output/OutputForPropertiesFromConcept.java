package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.Language;

public class OutputForPropertiesFromConcept {

	private Language language;
	List<OutputForPropertyFromConcept>  outputForPropertiesFromConcept = new ArrayList<OutputForPropertyFromConcept>();

	public List<OutputForPropertyFromConcept> getOutputForPropertiesFromConcept() {
		return outputForPropertiesFromConcept;
	}

	
	
	public Language getLanguage() {
		return language;
	}



	public void setLanguage(Language language) {
		this.language = language;
	}



	@Override
	public String toString() {
		return "OutputForPropertiesFromConcept [language=" + language + ", outputForPropertiesFromConcept="
				+ outputForPropertiesFromConcept + "]";
	}
	
	
	
}
