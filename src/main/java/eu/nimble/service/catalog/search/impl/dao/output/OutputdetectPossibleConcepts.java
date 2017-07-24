package eu.nimble.service.catalog.search.impl.dao.output;

public class OutputdetectPossibleConcepts {

	private String conceptName ="";
	private String conceptLabel ="";
	public String getConceptName() {
		return conceptName;
	}
	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}
	public String getConceptLabel() {
		return conceptLabel;
	}
	public void setConceptLabel(String conceptLabel) {
		this.conceptLabel = conceptLabel;
	}
	@Override
	public String toString() {
		return "outputdetectPossibleConcepts [conceptName=" + conceptName + ", conceptLabel=" + conceptLabel + "]";
	}
	
	
}
