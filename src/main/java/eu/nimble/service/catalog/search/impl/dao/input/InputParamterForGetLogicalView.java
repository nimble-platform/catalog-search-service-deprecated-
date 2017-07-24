package eu.nimble.service.catalog.search.impl.dao.input;

import de.biba.triple.store.access.enums.Language;

public class InputParamterForGetLogicalView {

	private String concept ="";
	private int stepRange = 0;
	private String frozenConcept ="";
	private String language ="";
	@Override
	public String toString() {
		return "InputParamterForGetLogicalView [concept=" + concept + ", stepRange=" + stepRange + ", frozenConcept="
				+ frozenConcept + ", language=" + language + "]";
	}
	public String getLanguage() {
		return language;
	}
	
	public Language getLanguageAsLanguage() {
		return Language.fromString(language);
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public int getStepRange() {
		return stepRange;
	}
	public void setStepRange(int stepRange) {
		this.stepRange = stepRange;
	}
	public String getFrozenConcept() {
		return frozenConcept;
	}
	public void setFrozenConcept(String frozenConcept) {
		this.frozenConcept = frozenConcept;
	}
	
	
}
