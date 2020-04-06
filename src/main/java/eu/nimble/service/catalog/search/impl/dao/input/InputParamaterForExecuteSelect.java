package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;

public class InputParamaterForExecuteSelect {

	private String concept;
	private String language;
	private List<String> parameters = new ArrayList<String>();
	private List<String> parametersURL = new ArrayList<String>();
	private List<Parameter> parametersIncludingPath = new ArrayList<Parameter>();
	private List<Filter> filters = new ArrayList<Filter>();
	private OrangeCommands orangeCommandSelected = new OrangeCommands();
	private List<PropertySource> propertySources = new ArrayList<PropertySource>();

	
	
	

	public List<PropertySource> getPropertySources() {
		return propertySources;
	}

	public void setPropertySources(List<PropertySource> propertySources) {
		this.propertySources = propertySources;
	}

	public OrangeCommands getOrangeCommandSelected() {
		return orangeCommandSelected;
	}

	public void setOrangeCommandSelected(OrangeCommands orangeCommandSelected) {
		this.orangeCommandSelected = orangeCommandSelected;
	}

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

	@Override
	public String toString() {
		return "InputParamaterForExecuteSelect [concept=" + concept + ", language=" + language + ", parameters="
				+ parameters + ", parametersURL=" + parametersURL + ", parametersIncludingPath="
				+ parametersIncludingPath + ", filters=" + filters + ", orangeCommandSelected=" + orangeCommandSelected
				+ ", propertySources=" + propertySources + "]";
	}

}
