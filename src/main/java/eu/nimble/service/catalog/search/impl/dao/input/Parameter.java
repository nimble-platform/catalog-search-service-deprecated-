package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;


public class Parameter {

	private String urlOfProperty;
	private List<Tuple> path = new ArrayList<Tuple>();
	public String getUrlOfProperty() {
		return urlOfProperty;
	}
	public void setUrlOfProperty(String urlOfProperty) {
		this.urlOfProperty = urlOfProperty;
	}
	public List<Tuple> getPath() {
		return path;
	}
	public void setPath(List<Tuple> path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return "Parameter [urlOfProperty=" + urlOfProperty + ", path=" + path + "]";
	}
	
	
	
}
