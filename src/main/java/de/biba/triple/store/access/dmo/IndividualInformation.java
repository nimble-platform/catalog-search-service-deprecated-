package de.biba.triple.store.access.dmo;

public class IndividualInformation {

	String urlOfIndividual;
	String urlOfClass;
	public String getUrlOfIndividual() {
		return urlOfIndividual;
	}
	public void setUrlOfIndividual(String urlOfIndividual) {
		this.urlOfIndividual = urlOfIndividual;
	}
	public String getUrlOfClass() {
		return urlOfClass;
	}
	public void setUrlOfClass(String urlOfClass) {
		this.urlOfClass = urlOfClass;
	}
	@Override
	public String toString() {
		return "IndividualInformation [urlOfIndividual=" + urlOfIndividual + ", urlOfClass=" + urlOfClass + "]";
	}
	
	
	
}
