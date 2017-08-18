package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;

public class OutputForGetLogicalView {
	public LocalOntologyView getViewStructure() {
		return viewStructure;
	}
	public void setViewStructure(LocalOntologyView viewStructure) {
		this.viewStructure = viewStructure;
	}
	public LocalOntologyView getCompleteStructure() {
		return completeStructure;
	}
	public void setCompleteStructure(LocalOntologyView completeStructure) {
		this.completeStructure = completeStructure;
	}
	public List<ArrayList<String>> getCurrentSelections() {
		return currentSelections;
	}
	public void setCurrentSelections(List<ArrayList<String>> currentSelections) {
		this.currentSelections = currentSelections;
	}
	
	private LocalOntologyView viewStructure;
	private LocalOntologyView completeStructure;
	private List<ArrayList<String>> currentSelections = new ArrayList<ArrayList<String>>();
	
}
