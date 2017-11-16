package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

public class OutputForPropertyValuesFromGreenGroup {

	private List<String> allValues = new ArrayList<String>();

	public List<String> getAllValues() {
		return allValues;
	}

	public void setAllValues(List<String> allValues) {
		this.allValues = allValues;
	}

	@Override
	public String toString() {
		return "OutputForPropertyValuesFromGreenGroup [allValues=" + allValues + "]";
	}
	
	
	
}
