package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.List;
import java.util.Map;

import de.biba.triple.store.access.dmo.Entity;
import eu.nimble.service.catalog.search.impl.dao.enums.TypeOfDataSource;



public class MeaningResult {

	private String searchTyp;
	private TypeOfDataSource source = TypeOfDataSource.UNKOWN;
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
	public TypeOfDataSource getSource() {
		return source;
	}
	public void setSource(TypeOfDataSource source) {
		this.source = source;
	}
	@Override
	public String toString() {
		return "MeaningResult [searchTyp=" + searchTyp + ", source=" + source + ", conceptOverview=" + conceptOverview
				+ "]";
	}
	
	
}
