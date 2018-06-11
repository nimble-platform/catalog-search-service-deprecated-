package eu.nimble.service.catalog.search.impl.dao;

public class Filter {

	private String property;
	private float min;
	private float max;
	private String exactValue ="0";
	private boolean hasMinBeenSet = false;
	private boolean hasMaxBeenSet = false;
	
	public Filter(){
		
	}
	
	
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
		checkWhetherValuesHasBeenSetPerreflection();
		return hasMinBeenSet;
	}
	
	public boolean isHasMaxBeenSet() {
		checkWhetherValuesHasBeenSetPerreflection();
		
		return hasMaxBeenSet;
	}
	private void checkWhetherValuesHasBeenSetPerreflection() {
		if (min != 0 || max != 0){
			hasMaxBeenSet = true;
			hasMinBeenSet = true;
		}
		
		
	}


	
	
	@Override
	public String toString() {
		return "Filter [property=" + property + ", min=" + min + ", max=" + max + ", exactValue=" + exactValue
				+ ", hasMinBeenSet=" + hasMinBeenSet + ", hasMaxBeenSet=" + hasMaxBeenSet + "]";
	}
	
	
	
}
