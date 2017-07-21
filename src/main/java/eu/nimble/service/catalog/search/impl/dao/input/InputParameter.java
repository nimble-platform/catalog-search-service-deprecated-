package eu.nimble.service.catalog.search.impl.dao.input;

public class InputParameter {

	String[] userData;
	String typeOfQuery;
	String typeOfOutput;

	public String[] getUserData() {
		return userData;
	}

	public void setUserData(String[] userData) {
		this.userData = userData;
	}

	public String getTypeOfQuery() {
		return typeOfQuery;
	}

	public void setTypeOfQuery(String typeOfQuery) {
		this.typeOfQuery = typeOfQuery;
	}

	public String getTypeOfOutput() {
		return typeOfOutput;
	}

	public void setTypeOfOutput(String typeOfOutput) {
		this.typeOfOutput = typeOfOutput;
	}

}
