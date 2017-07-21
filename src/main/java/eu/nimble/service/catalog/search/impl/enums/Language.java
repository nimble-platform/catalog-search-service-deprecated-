package eu.nimble.service.catalog.search.impl.enums;

public enum Language {

	GERMAN, ENGLISH, SPANISH, UNKNOWN;
	
	
	public static Language fromString(String lanuage){
		if (lanuage.equals("de")){
			return Language.GERMAN;
		}
		if (lanuage.equals("en")){
			return Language.ENGLISH;
		}
		if (lanuage.equals("es")){
			return Language.SPANISH;
		}
		
		return UNKNOWN;
		
	}
}
