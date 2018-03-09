package eu.nimble.service.catalog.search.impl.dao;

public class Filter {

	String property;
	float min;
	float max;
	String exactValue;
	
	public String getExactValue() {
		return exactValue;
	}
	public void setExactValue(String exactValue) {
		this.exactValue = exactValue;
	}
	public String getProperty() {
		return property;
	}
	public float getMin() {
		return min;
	}
	public void setMin(float min) {
		this.min = min;
	}
	public float getMax() {
		return max;
	}
	public void setMax(float max) {
		this.max = max;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	@Override
	public String toString() {
		return "Filter [property=" + property + ", min=" + min + ", max=" + max + ", exactValue=" + exactValue + "]";
	}
	
	
	
	
}
