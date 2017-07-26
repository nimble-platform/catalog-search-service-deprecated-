package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

public class OutoutForGetSupportedLanguages {
	
	List<String> languages = new ArrayList<String>();

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}

	@Override
	public String toString() {
		return "IntputParamterForGetAvailableLanguages [languages=" + languages + "]";
	}
	
	
	

}
