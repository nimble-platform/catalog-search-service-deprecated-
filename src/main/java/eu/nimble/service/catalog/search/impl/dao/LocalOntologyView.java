package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalOntologyView {

	String concept;
	List<String> dataproperties = new ArrayList<String>();
	Map<String, LocalOntologyView> objectproperties = new HashMap<String, LocalOntologyView>();
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public List<String> getDataproperties() {
		return dataproperties;
	}
	public void setDataproperties(List<String> dataproperties) {
		this.dataproperties.clear();
		this.dataproperties.addAll(dataproperties);
	}
	public void addDataproperties(String dataproperties) {
		this.dataproperties.add(dataproperties);
	}
	public Map<String, LocalOntologyView> getObjectproperties() {
		return objectproperties;
	}
	public void setObjectproperties(Map<String, LocalOntologyView> objectproperties) {
		this.objectproperties = objectproperties;
	}
	
	
	

}
