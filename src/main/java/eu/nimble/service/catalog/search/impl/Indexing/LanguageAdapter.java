package eu.nimble.service.catalog.search.impl.Indexing;

import de.biba.triple.store.access.enums.Language;

public class LanguageAdapter {

	private static final String EN = "en";


	public static String createPrefix(Language language){
		
		if(language==null){
			return getEnglishPrefix();
		}
		
		if (language.equals(Language.ENGLISH)){
			return EN;
		}
		if (language.equals(Language.GERMAN)){
			return "de";
		}
		if (language.equals(Language.SPANISH)){
			return "es";
		}
		return EN;
	}
	
	
	public static String getEnglishPrefix(){
		return EN;
	}
	
}
