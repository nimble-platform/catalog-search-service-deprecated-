package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

public class Reference {

	private String objectPropertyURL;
	private String translatedProperty;
	private List<TranslationResult> range = new ArrayList<TranslationResult>();
	public String getObjectPropertyURL() {
		return objectPropertyURL;
	}
	public void setObjectPropertyURL(String objectPropertyURL) {
		this.objectPropertyURL = objectPropertyURL;
	}
	public List<TranslationResult> getRange() {
		return range;
	}
	
	@Override
	public String toString() {
		return "Reference [objectPropertyURL=" + objectPropertyURL + ", range=" + range + "]";
	}
	public String getTranslatedProperty() {
		return translatedProperty;
	}
	public void setTranslatedProperty(String translatedProperty) {
		this.translatedProperty = translatedProperty;
	}
	
	
	
}
