package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;
import java.util.List;

public class InputParamaterForExecuteSelect {

	String concept;
	List<String> parameters = new ArrayList<String>();
	List<Filter> filters = new ArrayList<Filter>();
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void setParameters(List<String> parameters) {
		this.parameters = parameters;
	}
	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	
	
}
