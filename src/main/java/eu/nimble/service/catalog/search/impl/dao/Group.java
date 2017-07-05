package eu.nimble.service.catalog.search.impl.dao;

public class Group {

	private String description ="";
	private String property ="";
	private float min =0;
	private float max =0;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
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
	@Override
	public String toString() {
		return "Group [description=" + description + ", property=" + property + ", min=" + min + ", max=" + max + "]";
	}
	
	
	
	
}
