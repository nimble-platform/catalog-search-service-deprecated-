package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;

import de.biba.triple.store.access.dmo.Entity;

public class LocalOntologyView {

	Entity concept;
	List<Entity> dataproperties = new ArrayList<Entity>();
	// key is URI of the object property, value is LocalOntologyView
	Map<String, LocalOntologyView> objectproperties = new HashMap<String, LocalOntologyView>();
	
	private String frozenConcept ="";
	private int distanceToFrozenConcept = 0;
	
	// Path to the current concept from the frozen concept
	private List<String> conceptURIPath = new ArrayList<String>();
	
	// Flag to indict whether its diret parent is hidden
	private boolean hasHiddenDirectParent = false;
	
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
	 * Delete all child invisible local ontology views from the the current local ontology view.
	 * When there is visible local ontology view within invisible local ontology view, only the visible ontology view will be kept.
	 * 
	 * e.g. Given Ontology View "HighChair(not hidden)-->Manufacture (hidden) --> Legislation (not hidden)
	 *      Return: "HighChair(not hidden) --> Legislation (not hidden)
	 *      
	 * @return local ontology view for the displaying of the circle structure in the explorative search
	 */
	public LocalOntologyView getVisibleLocalOntologyViewStructure()
	{
		Gson gson = new Gson();
		String jsonStr = gson.toJson(this);
		LocalOntologyView deepcopy = gson.fromJson(jsonStr, LocalOntologyView.class);
		
		return deepcopy.convertToVisibleLocalOntologyViewStructure();
	}

	
	/**
	 * Delete all child invisible local ontology views from the the current local ontology view.
	 * When there is visible local ontology view within invisible local ontology view, only the visible ontology view will be kept.
	 * 
	 * Avoid duplicate names that uers cannot distinct between them:   
	 * 
	 * Example 0. Given Ontology View "HighChair(not hidden) --> Legislation (not hidden)
	 *            Return: "HighChair(not hidden) --> Legislation (not hidden)
	 * 
	 * Example 1. Given Ontology View "HighChair(not hidden)-->Manufacture (hidden) --> Legislation (not hidden)
	 *            Return: "HighChair(not hidden) --> Manufacture/Legislation (not hidden)
	 *            Rather than Return: "HighChair(not hidden) --> Legislation (not hidden)
	 *            Because otherwise the user cannot know where the Legislations come from in the example 2.
	 *            
	 * Example 2. Given Ontology View "HighChair(not hidden)-->Manufacture (hidden) --> Legislation (not hidden)"
	 *                                "HighChair(not hidden)-->Material (hidden) --> Legislation (not hidden)"
	 *      Return: "HighChair(not hidden) --> Manufacture/Legislation (not hidden)   
	 *     		    "HighChair(not hidden) --> Material/Legislation (not hidden)   
	 *      Rather than:  
	 *       		"HighChair(not hidden) --> Legislation (not hidden)   
	 *     		    "HighChair(not hidden) --> Material/Legislation (not hidden)      
	 *        
	 * Example 3. Given Ontology View "HighChair(not hidden) --> Legislation (not hidden)"
	 * 							"HighChair(not hidden)-->Manufacture (hidden) --> Legislation (not hidden)"
	 *                          "HighChair(not hidden)-->Material (hidden) --> Legislation (not hidden)"
	 *      Return: "HighChair(not hidden) --> Legislation (not hidden)   
	 *      		"HighChair(not hidden) --> Manufacture/Legislation (not hidden)   
	 *     		    "HighChair(not hidden) --> Material/Legislation (not hidden)   
	 *      
	 * WARNING：   This method will make update on the current LocalOntologyView. 
	 * If the update is not expected, please make a deep copy of the current LocalOntologyView, and then use the deep copy to call the method.
	 *      
	 * @return local ontology view for the displaying of the circle structure in the explorative search
	 */
	private LocalOntologyView convertToVisibleLocalOntologyViewStructure()
	{
		LocalOntologyView viewStructcture = null;
		
		if(this.concept.isHidden())
		{
			viewStructcture = this.findVisibleChildLocalOntologyView();
		}
		else
		{
			viewStructcture = this;
			Set<String> oldObjProps = new HashSet<String>();
			oldObjProps.addAll(this.getObjectproperties().keySet());
			for(String objProp : oldObjProps)
			{
				LocalOntologyView temp = this.getObjectproperties().get(objProp);
				LocalOntologyView tempVisible = temp.convertToVisibleLocalOntologyViewStructure();
				
				viewStructcture.getObjectproperties().remove(objProp);
				if(tempVisible!= null)
				{
					String visibleObjPropName = tempVisible.getConcept().getUrl();
					
					if(visibleObjPropName.equals(objProp))
					{
						viewStructcture.getObjectproperties().put(tempVisible.getConcept().getUrl(), tempVisible);
					}
					else
					{
						// Get new name for the visible LocalOntologyView with some path information for the purpose of distinction
						String newVisibleObjPropName = getConceptNameWithPath(tempVisible);
						viewStructcture.getObjectproperties().put(newVisibleObjPropName, tempVisible);
					}	
				}
			}
		}
		
		return viewStructcture;
	}
	
	/**
	 * It is only used by the method "convertToVisibleLocalOntologyViewStructure", 
	 * to get the name for visible LocalOntologyView that has some invisible parent node
	 * 
	 * e.g. In case of "HighChair(not hidden)-->Material (hidden) --> Legislation (not hidden)"
	 *   The return name for "Legislation" is "Material/Legislation". 
	 *   The name include name of all parent nodes except the root node, because the root node shows always in the circle.
	 * 
	 * @param view visible LocalOntologyView
	 * @return new name of the visible LocalOntologyView with some path information for the purpose of distinction
	 */
	private static String getConceptNameWithPath(LocalOntologyView view)
	{
		String name = "";
		
		List<String> pathWithoutRoot = new ArrayList<String>();
		
		pathWithoutRoot.addAll(view.getConceptURIPath());
		String rootElementURI = pathWithoutRoot.get(0);
	
		String namespace = rootElementURI.substring(0, rootElementURI.indexOf("#"));
		// the first element is always the root, and is not necessary to be shown in the path
		pathWithoutRoot.remove(0);
		
		for(String el:pathWithoutRoot)
		{
			name = name + el.substring(el.indexOf("#") + 1) + "/";
		}
		if(name.endsWith("/"))
		{
			name = name.substring(0, name.length() - 1);
		}
		
		name = namespace + "#" + name;
		
		return name;
	}
	
	/**
	 * Find the single visible child local ontology view from the current hidden local ontology view.
	 * At the moment, under a invisible local ontology view e.g. Manufactuer, only one child local ontology view is visible e.g. Legislation.
	 * 
	 * @return visible child local ontology view 
	 */
	public LocalOntologyView findVisibleChildLocalOntologyView()
	{
		if (!this.concept.isHidden())
		{
			return this;
		}
		else
		{
			for(LocalOntologyView localView : this.getObjectproperties().values())
			{
				LocalOntologyView temp = localView.findVisibleChildLocalOntologyView();
				if(temp != null)
				{
					return temp;
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
		// todo....
		this.concept.setTranslatedURL(parentLocalView.getConcept().getTranslatedURL()+ "/" +this.concept.getTranslatedURL());
		this.setHasHiddenDirectParent(true);
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
	public String getParentConceptURI(String conceptURI)
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

	public List<String> getConceptURIPath() {
		return conceptURIPath;
	}

	public void setConceptURIPath(List<String> conceptURIPath) {
		this.conceptURIPath = conceptURIPath;
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
	public boolean isHasHiddenDirectParent() {
		return hasHiddenDirectParent;
	}
	public void setHasHiddenDirectParent(boolean hasHiddenDirectParent) {
		this.hasHiddenDirectParent = hasHiddenDirectParent;
	}


	
	
	

}
