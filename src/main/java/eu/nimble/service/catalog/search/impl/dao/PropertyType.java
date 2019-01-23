package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.HashSet;


import com.fasterxml.jackson.annotation.JsonIgnore;
/**
 * SOLR Document holding the properties out of 
 * any ontologies including label, range and 
 * the usage in distinct classes
 * 
 * @author dglachs
 *
 */
public class PropertyType extends Named implements IPropertyType {
	/**
	 * The uri of the property including namespace
	 */
	private String type = TYPE_VALUE;

	private String range;
	
	private String valueQualifier;
	
	private Collection<String> product;
	
	private Collection<String> itemFieldNames;
	
	private boolean facet = true;
	
	private Double boost;

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Collection<String> getProduct() {
		if (product == null ) {
			product = new HashSet<String>(); 
		}
		return product;
	}
	public void addProduct(String className) {
		if ( this.product == null ) {
			this.product = new HashSet<>();
		}
	}
	public void setProduct(Collection<String> className) {
		this.product = className;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Collection<String> getItemFieldNames() {
		return itemFieldNames;
	}

	public void setItemFieldNames(Collection<String> idxFieldNames) {
		this.itemFieldNames = idxFieldNames;
	}

	public String getValueQualifier() {
		return valueQualifier;
	}

	public void setValueQualifier(String valueQualifier) {
		this.valueQualifier = valueQualifier;
	}
	@JsonIgnore
	public boolean isFacet() {
		return facet;
	}

	public void setFacet(boolean facet) {
		this.facet = facet;
	}
	@JsonIgnore
	public Double getBoost() {
		return boost;
	}

	public void setBoost(Double boost) {
		this.boost = boost;
	}


}
