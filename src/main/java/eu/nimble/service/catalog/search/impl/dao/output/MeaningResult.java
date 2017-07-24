package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.List;
import java.util.Map;

import de.biba.triple.store.access.dmo.Entity;

public class MeaningResult {

	private String searchTyp;
	private  List<Entity> conceptOverview;
	public String getSearchTyp() {
		return searchTyp;
	}
	public void setSearchTyp(String searchTyp) {
		this.searchTyp = searchTyp;
	}
	public  List<Entity> getConceptOverview() {
		return conceptOverview;
	}
	public void setConceptOverview( List<Entity> conceptOverview) {
		this.conceptOverview = conceptOverview;
	}
	
	
}
