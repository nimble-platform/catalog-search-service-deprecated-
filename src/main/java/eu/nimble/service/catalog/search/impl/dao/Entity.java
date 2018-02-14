package eu.nimble.service.catalog.search.impl.dao;

import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;

public class Entity extends de.biba.triple.store.access.dmo.Entity{

	private PropertySource propertySource;

	@Override
	public String toString() {
		return "Entity [propertySource=" + propertySource + ", getConceptSource()=" + getConceptSource() + ", getUrl()="
				+ getUrl() + ", getTranslatedURL()=" + getTranslatedURL() + ", getLanguage()=" + getLanguage()
				+ ", isHidden()=" + isHidden() + ", toString()=" + super.toString() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + "]";
	}

	public PropertySource getPropertySource() {
		return propertySource;
	}

	public void setPropertySource(PropertySource propertySource) {
		this.propertySource = propertySource;
	}
	
	
	
}
