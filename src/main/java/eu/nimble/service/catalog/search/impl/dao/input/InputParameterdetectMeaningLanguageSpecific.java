package eu.nimble.service.catalog.search.impl.dao.input;

import de.biba.triple.store.access.enums.Language;

public class InputParameterdetectMeaningLanguageSpecific {

	String keyword;
	String language;
	long userID =0;
	
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

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	@Override
	public String toString() {
		return "InputParameterdetectMeaningLanguageSpecific [keyword=" + keyword + ", language=" + language
				+ ", userID=" + userID + "]";
	}
	
	
}
