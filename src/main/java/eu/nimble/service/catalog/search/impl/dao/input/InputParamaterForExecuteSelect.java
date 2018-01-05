package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.impl.dao.Filter;

public class InputParamaterForExecuteSelect {

	String concept;
	String language;
	List<String> parameters = new ArrayList<String>();
	List<String> parametersURL = new ArrayList<String>();
	List<Parameter> parametersIncludingPath = new ArrayList<Parameter>();
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

	public Language getLanguage() {
		if (language != null) {
			return Language.fromString(language);
		} else {
			return null;
		}
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Parameter> getParametersIncludingPath() {
		return parametersIncludingPath;
	}

	public void setParametersIncludingPath(List<Parameter> parametersIncludingPath) {
		this.parametersIncludingPath = parametersIncludingPath;
	}

	public List<String> getParametersURL() {
		return parametersURL;
	}

	public void setParametersURL(List<String> parametersURL) {
		this.parametersURL = parametersURL;
	}

}
