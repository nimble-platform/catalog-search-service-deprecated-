package de.biba.triple.store.access.dmo;

import de.biba.triple.store.access.enums.ConceptSource;
import de.biba.triple.store.access.enums.Language;

public class Entity {

	private String url;
	private String translatedURL;
	private Language language;
	private ConceptSource conceptSource = ConceptSource.ONTOLOGICAL;
	// Flag to indict if the entity should be hidden in the local ontological view
	private boolean isHidden = false;
	
	public ConceptSource getConceptSource() {
		return conceptSource;
	}
	public void setConceptSource(ConceptSource conceptSource) {
		this.conceptSource = conceptSource;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTranslatedURL() {
		return translatedURL;
	}
	public void setTranslatedURL(String translatedURL) {
		this.translatedURL = translatedURL;
	}
	public Language getLanguage() {
		return language;
	}
	public void setLanguage(Language language) {
		this.language = language;
	}
	public boolean isHidden() {
		return isHidden;
	}
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	@Override
	public String toString() {
		return "Entity [url=" + url + ", translatedURL=" + translatedURL + ", language=" + language + ", conceptSource="
				+ conceptSource + ", isHidden=" + isHidden + "]";
	}

	
	
	
}
