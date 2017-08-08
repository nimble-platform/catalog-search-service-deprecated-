package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.Language;

public class InputParamterForGetLogicalView {

	private String concept ="";
	private int stepRange = 0;
	private String language ="";
	
	private String frozenConcept ="";
	private int distanceToFrozenConcept = 0;
	private String oldJsonLogicalView = "";
	
	// Path to the current concept from the frozen concept
	private List<String> conceptURIPath = new ArrayList<String>();
	
	@Override
	public String toString() {
		return "InputParamterForGetLogicalView [concept=" + concept + ", stepRange=" + stepRange + ", language=" + language + ", frozenConcept="
				+ frozenConcept +  ", distanceToFrozenConcept=" + distanceToFrozenConcept + ", oldJsonLogicalView=" + oldJsonLogicalView + "]";
	}
	
	public String getLanguage() {
		return language;
	}
	
	public Language getLanguageAsLanguage() {
		return Language.fromString(language);
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getConcept() {
		return concept;
	}
	public void setConcept(String concept) {
		this.concept = concept;
	}
	public int getStepRange() {
		return stepRange;
	}
	public void setStepRange(int stepRange) {
		this.stepRange = stepRange;
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
	public String getOldJsonLogicalView() {
		return oldJsonLogicalView;
	}
	public void setOldJsonLogicalView(String oldJsonLogicalView) {
		this.oldJsonLogicalView = oldJsonLogicalView;
	}

	
	/**
	 * Get parent concept of the given concept. 
	 * @param conceptURI
	 * @return URI of the parent concept; empty string, when it is not found.
	 */
	public String getParentConceptURI(String conceptURI)
	{	
		int pos = this.getConceptURIPath().indexOf(conceptURI);
		
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
		if(conceptURIPath.isEmpty())
		{
			conceptURIPath.add(frozenConcept);
		}
			
		return conceptURIPath;
	}

	public void setConceptURIPath(List<String> conceptURIPath) {
		this.conceptURIPath = conceptURIPath;
	}
	
}
