package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;


public class Parameter {

	private String urlOfProperty;
	private List<String> path = new ArrayList<String>();
	public String getUrlOfProperty() {
		return urlOfProperty;
	}
	public void setUrlOfProperty(String urlOfProperty) {
		this.urlOfProperty = urlOfProperty;
	}
	public List<String> getPath() {
		return path;
	}
	public void setPath(List<String> path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Parameter [urlOfProperty=" + urlOfProperty + ", path=" + path + "]";
	}
	
	
	
}
