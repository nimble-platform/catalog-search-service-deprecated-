package de.biba.triple.store.access.dmo;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.Operator;

/**
 * This class implements a filter which has the following details: urlOfProperty
 * is the subject of the filter A filter can define a set of values for a
 * specific property An operator is required. If no operator is given , = is set
 * as default
 * 
 * @author marco_000
 *
 */
public class Filter {

	String urlOfProperty;
	List<String> value = new ArrayList<String>();
	Operator operator = Operator.EQUAL; // default is equal
	boolean objectproperty = false;

	public boolean isObjectproperty() {
		return objectproperty;
	}

	public void assignAsObjectproperty(boolean objectproperty) {
		this.objectproperty = objectproperty;
	}

	public String getUrlOfProperty() {
		return urlOfProperty;
	}

	public void setUrlOfProperty(String urlOfProperty) {
		this.urlOfProperty = urlOfProperty;
	}

	public List<String> getValues() {
		return value;
	}

	// @Deprecated
	// public String getValue() {
	// return value.get(0);
	// }

	public void addValue(String value) {
		this.value.add(value);
	}

	@Deprecated
	public void setValue(String value) {
		this.value.add(value);
	}

	@Override
	public String toString() {
		return "Filter [urlOfProperty=" + urlOfProperty + ", value=" + value + ", objectproperty=" + objectproperty
				+ "]";
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

}
