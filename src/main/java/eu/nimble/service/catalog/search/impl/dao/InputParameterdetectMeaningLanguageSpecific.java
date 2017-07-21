package eu.nimble.service.catalog.search.impl.dao;

import eu.nimble.service.catalog.search.impl.enums.Language;

public class InputParameterdetectMeaningLanguageSpecific {

	String keyword;
	String language;
	
	public Language getLanguage(){
		return Language.fromString(language);
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
}
