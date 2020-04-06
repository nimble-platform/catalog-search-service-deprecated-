package de.biba.triple.store.access.dmo;

import java.util.ArrayList;
import java.util.List;

public class ObjectPropertyToDatatypePropertyMapping {

	String urlObjectProperty = "";
	List<String> datatypeProperties = new ArrayList<String>();
	String seperator = ";";

	public String getUrlObjectProperty() {
		return urlObjectProperty;
	}

	public void addDatatypeProperty(String urlOfProperty) {

		if (!datatypeProperties.contains(urlOfProperty)) {
			datatypeProperties.add(urlOfProperty);
		}
	}

	public void setUrlObjectProperty(String urlObjectProperty) {
		this.urlObjectProperty = urlObjectProperty;
	}

	public String getSeperator() {
		return seperator;
	}

	public void setSeperator(String seperator) {
		this.seperator = seperator;
	}

	public List<String> getDatatypeProperties() {
		return datatypeProperties;
	}

}
