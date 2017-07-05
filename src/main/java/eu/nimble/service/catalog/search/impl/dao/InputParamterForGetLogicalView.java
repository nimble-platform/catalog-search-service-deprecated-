package eu.nimble.service.catalog.search.impl.dao;

public class InputParamterForGetLogicalView {

	private String concept ="";
	private int stepRange = 0;
	private String frozenConcept ="";
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
	
	
}
