package de.biba.triple.store.access.enums;

public enum Language {

	GERMAN, ENGLISH, SPANISH, SWEDISH, SAMI, UNKNOWN;

	public static Language fromString(String lanuage) {
		if (lanuage.equals("de")) {
			return Language.GERMAN;
		}
		if (lanuage.equals("en")) {
			return Language.ENGLISH;
		}
		if (lanuage.equals("es")) {
			return Language.SPANISH;
		}

		if (lanuage.equals("sv")) {
			return Language.SWEDISH;
		}
		
		if (lanuage.equals("se")) {
			return Language.SAMI;
		}

		return ENGLISH;

	}

	public static String replaceLanguageStringToOldNamingAsAttribute(String inputAsJson) {
		inputAsJson = inputAsJson.replace("ENGLISH", "en");
		inputAsJson = inputAsJson.replace("SPANISH", "es");
		inputAsJson = inputAsJson.replace("GERMAN", "de");
		inputAsJson = inputAsJson.replace("SWEDISH", "sv");
		return inputAsJson;
	}
	
	
	public static String replaceLanguageStringToOldNamingInJSON(String inputAsJson) {
		inputAsJson = inputAsJson.replace("\"ENGLISH\"", "\"en\"");
		inputAsJson = inputAsJson.replace("\"SPANISH\"", "\"es\"");
		inputAsJson = inputAsJson.replace("\"GERMAN\"", "\"de\"");
		inputAsJson = inputAsJson.replace("\"SWEDISH\"", "\"sv\"");
		return inputAsJson;
	}
	
	public static String toOntologyPostfix(Language language) {
		switch (language) {
		case GERMAN:
			return "@de";
		case ENGLISH:
			return "@en";
		case SPANISH:
			return "@es";
		case SWEDISH:
			return "@sv";
		case SAMI:
		return "@se";
		}
		return "@en";
	}
}
