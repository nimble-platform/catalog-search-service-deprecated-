package eu.nimble.service.catalog.search.impl.dao.item;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import eu.nimble.service.catalog.search.impl.dao.Concept;
import eu.nimble.service.catalog.search.impl.dao.IPropertyType;
import eu.nimble.service.catalog.search.impl.dao.ValueQualifier;

public class PropertyType extends Concept implements IPropertyType {
	/**
	 * The uri of the property including namespace
	 */
	private String type = TYPE_VALUE;

	private String range;
	
	private ValueQualifier valueQualifier;
	
	private Collection<String> product;
	
	private Collection<String> items;

	private Collection<String> itemFieldNames;
	
	private boolean facet = true;
	
	private Double boost;
	
	private String propertyType;
	
	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

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
	public void addItemFieldName(String idxField) {
		if (itemFieldNames==null) {
			itemFieldNames=new HashSet<>();
		}
		else if (! (itemFieldNames instanceof Set)) {
			// ensure to have a new set (to avoid duplicates)
			itemFieldNames = itemFieldNames.stream().collect(Collectors.toSet());
		}
		this.itemFieldNames.add(idxField);
	}

	public ValueQualifier getValueQualifier() {
		return valueQualifier;
	}

	public void setValueQualifier(ValueQualifier valueQualifier) {
		this.valueQualifier = valueQualifier;
	}
	public boolean isFacet() {
		return facet;
	}

	public void setFacet(boolean facet) {
		this.facet = facet;
	}
	public Double getBoost() {
		return boost;
	}

	public void setBoost(Double boost) {
		this.boost = boost;
	}

	
	public Collection<String> getItems() {
		return items;
	}

	public void setItems(Collection<String> items) {
		this.items = items;
	}
	public void addItem(String uri) {
		if ( items == null ) {
			items = new HashSet<>();
		}
		items.add(uri);
	}
	public boolean removeItem(String item) {
		if ( items == null ) {
			return false;
		}
		return items.remove(item);
		
	}

}
