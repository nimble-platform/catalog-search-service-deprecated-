package eu.nimble.service.catalog.search.impl.dao.output;

public class OutputForPropertyValuesFromOrangeGroup extends OutputForPropertyValuesFromGreenGroup{

	private boolean isItAIndividual = false;
	private boolean isITASimpleValue = true;
	private String belongsToTheFollowingConcept = "";
	public boolean isItAConcept() {
		return isItAIndividual;
	}
	public void setItAConcept(boolean isItAConcept) {
		this.isItAIndividual = isItAConcept;
	}
	public boolean isITASimpleValue() {
		return isITASimpleValue;
	}
	public void setITASimpleValue(boolean isITASimpleValue) {
		this.isITASimpleValue = isITASimpleValue;
	}
	
	
	public boolean isItAIndividual() {
		return isItAIndividual;
	}
	public void setItAIndividual(boolean isItAIndividual) {
		this.isItAIndividual = isItAIndividual;
	}
	public String getBelongsToTheFollowingConcept() {
		return belongsToTheFollowingConcept;
	}
	public void setBelongsToTheFollowingConcept(String belongsToTheFollowingConcept) {
		this.belongsToTheFollowingConcept = belongsToTheFollowingConcept;
	}
	@Override
	public String toString() {
		return "OutputForPropertyValuesFromOrangeGroup [isItAIndividual=" + isItAIndividual + ", isITASimpleValue="
				+ isITASimpleValue + ", belongsToTheFollowingConcept=" + belongsToTheFollowingConcept + "]";
	}
	
	
	
}
