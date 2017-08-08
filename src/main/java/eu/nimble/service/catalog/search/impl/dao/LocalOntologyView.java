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
	
	private String frozenConcept ="";
	private int distanceToFrozenConcept = 0;
	
	// Path to the current concept from the frozen concept
	private List<String> conceptURIPath = new ArrayList<String>();
	
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
	
	public String getFrozenConcept() {
		return frozenConcept;
	}
	public void setFrozenConcept(String frozenConcept) {
		this.frozenConcept = frozenConcept;
	}
	public int getDistanceToFrozenConcept() {
		return distanceToFrozenConcept;
	}
	public void setDistanceToFrozenConcept(int distanceToFrozenConcept) {
		this.distanceToFrozenConcept = distanceToFrozenConcept;
	}
	
	
	/**
	 * Find a local ontology view for a concept. The concept could from any path start from the current local ontology view.
	 * 
	 * @param conceptURI
	 * @return LocalOntologyView
	 */
	public LocalOntologyView findLocalOntologyViewForConcept(String conceptURI)
	{		
		if(this.getConcept().getUrl().equals(conceptURI))
		{
			return this;
		}
		else
		{
			for(LocalOntologyView localView : this.getObjectproperties().values())
			{
				LocalOntologyView temp = localView.findLocalOntologyViewForConcept(conceptURI);
				if(temp != null)
				{
					return temp;
				}
			}	
		}
			 	
		return null;
	}
	
	/**
	 * Find a local ontology view for a concept from root local ontology view.
	 * 
	 * @param conceptURI
	 * @return LocalOntologyView
	 */
	public LocalOntologyView findLocalOntologyViewForConceptFromRoot(String conceptURI, List<String> conceptURIPath, LocalOntologyView rootOntologyView)
	{			
		LocalOntologyView view = rootOntologyView;
		for(int i=0; i<conceptURIPath.size(); i++)
		{
			if(view.getConcept().getUrl().equals(conceptURI))
			{
				return view;
			}
			else
			{
				if(conceptURIPath.size()>i+1)
				{
					String nextConceptURI = conceptURIPath.get(i+1);
					view = view.getObjectproperties().get(nextConceptURI);
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Hidden all elements of parent ontology view except the current concept.
	 * @param parentLocalView
	 */
	public void hiddenElementsOfParentView(LocalOntologyView parentLocalView)
	{
		parentLocalView.concept.setHidden(true);
		
		for(Entity dataProp : parentLocalView.getDataproperties())
		{
			dataProp.setHidden(true);
		}
			
		for(LocalOntologyView view : parentLocalView.getObjectproperties().values())
		{
			view.concept.setHidden(true);
		}
		
		this.concept.setHidden(false);
	}
	
	/**
	 * Get parent concept URI of current concept
	 * @return URI of parent concept; empty string, when it is not found.
	 */
	public String getParentConceptURI()
	{	
		int pos_parentConcept = this.conceptURIPath.size()-2;
		
		if(pos_parentConcept>=0)
		{
			return this.conceptURIPath.get(pos_parentConcept);
		}
		
		return "";
	}
	
	
	public boolean hasParentConcept()
	{
		String uri = this.getParentConceptURI();
		
		if(uri.isEmpty())
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Get parent concept of the given concept. 
	 * @param conceptURI
	 * @return URI of the parent concept; empty string, when it is not found.
	 */
	public String getConceptURI(String conceptURI)
	{	
		int pos = this.conceptURIPath.indexOf(conceptURI);
		
		if(pos>0)
		{
			return this.conceptURIPath.get(pos-1);
		}
		else
		{
			return "";
		}
	}

	public List<String> getParentConceptURIPath() {
		return conceptURIPath;
	}

	public void setConceptURIPath(List<String> parentConceptURIPath) {
		this.conceptURIPath = parentConceptURIPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public LocalOntologyView hiddenMiddleLayer()
	{
		return null;
	}
	
	@Override
	public String toString() {
		return "LocalOntologyView [concept=" + concept + ", dataproperties=" + dataproperties + ", objectproperties="
				+ objectproperties + ", frozenConcept="	+ frozenConcept +  ", distanceToFrozenConcept=" + distanceToFrozenConcept +  "]";
	}


	
	
	

}
