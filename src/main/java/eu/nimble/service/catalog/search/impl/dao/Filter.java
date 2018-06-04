package eu.nimble.service.catalog.search.impl.dao;

public class Filter {

	String property;
	float min;
	float max;
	String exactValue;
	boolean hasMinBeenSet = false;
	boolean hasMaxBeenSet = false;
	
	
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
	
	public int getMinAsInt() {
		return (int) min;
	}
	
	public void setMin(float min) {
		hasMinBeenSet = true;
		this.min = min;
	}
	public float getMax() {
		return max;
	}
	
	
	public int getMaxAsInt() {
		return (int) max;
	}
	
	public void setMax(float max) {
		hasMinBeenSet = true;
		this.max = max;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public boolean isHasMinBeenSet() {
		return hasMinBeenSet;
	}
	public void setHasMinBeenSet(boolean hasMinBeenSet) {
		this.hasMinBeenSet = hasMinBeenSet;
	}
	public boolean isHasMaxBeenSet() {
		return hasMaxBeenSet;
	}
	public void setHasMaxBeenSet(boolean hasMaxBeenSet) {
		this.hasMaxBeenSet = hasMaxBeenSet;
	}
	
	@Override
	public String toString() {
		return "Filter [property=" + property + ", min=" + min + ", max=" + max + ", exactValue=" + exactValue
				+ ", hasMinBeenSet=" + hasMinBeenSet + ", hasMaxBeenSet=" + hasMaxBeenSet + "]";
	}
	
	
	
}
