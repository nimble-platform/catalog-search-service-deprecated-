package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.enums.ConceptSource;
import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;

public class InputParamterForGetLogicalView {

	private String concept ="";
	private int stepRange = 0;
	private String language ="";
	private ConceptSource conceptSource = ConceptSource.ONTOLOGICAL;
	
	
	private String frozenConcept ="";
	private int distanceToFrozenConcept = 0;
	private LocalOntologyView oldJsonLogicalView = null;
	
	// Path to the current concept from the frozen concept
	private List<String> conceptURIPath = new ArrayList<String>();
	
	// List of paths to the selected properties
	private List<ArrayList<String>> currentSelections = new ArrayList<ArrayList<String>>();

	@Override
	public String toString() {
		return "InputParamterForGetLogicalView [concept=" + concept + ", stepRange=" + stepRange + ", language="
				+ language + ", conceptSource=" + conceptSource + ", frozenConcept=" + frozenConcept
				+ ", distanceToFrozenConcept=" + distanceToFrozenConcept + ", oldJsonLogicalView=" + oldJsonLogicalView
				+ ", conceptURIPath=" + conceptURIPath + ", currentSelections=" + currentSelections + "]";
	}
	
	public String getLanguage() {
		return language;
	}
	
	public ConceptSource getConceptSource() {
		return conceptSource;
	}

	public void setConceptSource(ConceptSource conceptSource) {
		this.conceptSource = conceptSource;
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
	public LocalOntologyView getOldJsonLogicalView() {
		return oldJsonLogicalView;
	}
	public void setOldJsonLogicalView(LocalOntologyView oldJsonLogicalView) {
		this.oldJsonLogicalView = oldJsonLogicalView;
	}
	
	public List<ArrayList<String>> getCurrentSelections() {
		return currentSelections;
	}
	public void setCurrentSelections(List<ArrayList<String>> currentSelections) {
		this.currentSelections = currentSelections;
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
