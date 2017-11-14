package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

public class OutputForPropertiesFromConcept {

	List<OutputForPropertyFromConcept>  outputForPropertiesFromConcept = new ArrayList<OutputForPropertyFromConcept>();

	public List<OutputForPropertyFromConcept> getOutputForPropertiesFromConcept() {
		return outputForPropertiesFromConcept;
	}

	@Override
	public String toString() {
		return "OutputForPropertiesFromConcept [outputForPropertiesFromConcept=" + outputForPropertiesFromConcept + "]";
	}
	
	
	
}
