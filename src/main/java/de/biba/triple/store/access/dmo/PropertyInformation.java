package de.biba.triple.store.access.dmo;

import java.util.ArrayList;
import java.util.List;

public class PropertyInformation {
	String name;
	boolean isItAObjectProperty;
	
	
	public boolean isItAObjectProperty() {
		return isItAObjectProperty;
	}
	public void setItAObjectProperty(boolean isItAObjectProperty) {
		this.isItAObjectProperty = isItAObjectProperty;
	}
	public String getName() {
		return name;
	}
	public void setName(String urlOfProperty) {
		this.name = urlOfProperty;
	}
	String type;
	List<Value> values = new ArrayList<Value>();
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Value> getValues() {
		return values;
	}
	public void setValues(List<Value> values) {
		this.values = values;
	}
	@Override
	public String toString() {
		return "PropertyInformation [name=" + name + ", isItAObjectProperty=" + isItAObjectProperty + ", type=" + type
				+ ", values=" + values + "]";
	}
	
	
	
}
