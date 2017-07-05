package eu.nimble.service.catalog.search.impl.dao;

import java.util.List;
import java.util.Map;

public class MeaningResult {

	private String searchTyp;
	private  List<String> conceptOverview;
	public String getSearchTyp() {
		return searchTyp;
	}
	public void setSearchTyp(String searchTyp) {
		this.searchTyp = searchTyp;
	}
	public  List<String> getConceptOverview() {
		return conceptOverview;
	}
	public void setConceptOverview( List<String> conceptOverview) {
		this.conceptOverview = conceptOverview;
	}
	
	
}
