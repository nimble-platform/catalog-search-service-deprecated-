package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.biba.triple.store.access.dmo.Entity;

public class LocalOntologyView {

	Entity concept;
	List<Entity> dataproperties = new ArrayList<Entity>();
	Map<String, LocalOntologyView> objectproperties = new HashMap<String, LocalOntologyView>();
	public Entity getConcept() {
		return concept;
	}
	public void setConcept(Entity concept) {
		this.concept = concept;
	}
	public List<Entity> getDataproperties() {
		return dataproperties;
	}
	public void setDataproperties(List<Entity> dataproperties) {
		this.dataproperties.clear();
		this.dataproperties.addAll(dataproperties);
	}
	public void addDataproperties(Entity dataproperties) {
		this.dataproperties.add(dataproperties);
	}
	public Map<String, LocalOntologyView> getObjectproperties() {
		return objectproperties;
	}
	public void setObjectproperties(Map<String, LocalOntologyView> objectproperties) {
		this.objectproperties = objectproperties;
	}
	@Override
	public String toString() {
		return "LocalOntologyView [concept=" + concept + ", dataproperties=" + dataproperties + ", objectproperties="
				+ objectproperties + "]";
	}
	
	
	

}
