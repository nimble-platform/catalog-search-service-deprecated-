package eu.nimble.service.catalog.search.impl.dao;

import eu.nimble.service.catalog.search.impl.dao.enums.TypeOfDataSource;

public class HybridConfiguration {

	private String detectMeaningLanguageSpecific;
	private String executeSPARQLWithOptionalSelect;
	private String executeSPARQLSelect;
	private String getPropertyValuesDiscretised;
	private String getReferencesFromAConcept;
	private String getPropertyValuesFromGreenGroup;
	private String getPropertyValuesFromOrangeGroup;
	private String getLogicalView;
	private String getPropertyFromConcept;
	private String getInstantiatedPropertiesFromConcept;
	
	public HybridConfiguration(){
		detectMeaningLanguageSpecific = "MARMOTTA";
		executeSPARQLWithOptionalSelect  = "MARMOTTA";
		executeSPARQLSelect  = "MARMOTTA";
		getPropertyValuesDiscretised  = "MARMOTTA";
		getReferencesFromAConcept  = "MARMOTTA";
		getPropertyValuesFromGreenGroup  = "MARMOTTA";
		getPropertyValuesFromOrangeGroup  = "MARMOTTA";
		getLogicalView  = "MARMOTTA";
		getPropertyFromConcept  = "MARMOTTA";
		getInstantiatedPropertiesFromConcept  = "MARMOTTA";
		
	}

	public TypeOfDataSource getExecuteSPARQLWithOptionalSelect() {
		return TypeOfDataSource.fromString(executeSPARQLWithOptionalSelect);
	}

	public void setExecuteSPARQLWithOptionalSelect(String executeSPARQLWithOptionalSelect) {
		this.executeSPARQLWithOptionalSelect = executeSPARQLWithOptionalSelect;
	}

	public TypeOfDataSource getExecuteSPARQLSelect() {
		return TypeOfDataSource.fromString(executeSPARQLSelect);
	}

	public void setExecuteSPARQLSelect(String executeSPARQLSelect) {
		this.executeSPARQLSelect = executeSPARQLSelect;
	}

	public TypeOfDataSource getGetPropertyValuesDiscretised() {
		return TypeOfDataSource.fromString(getPropertyValuesDiscretised);
	}

	public void setGetPropertyValuesDiscretised(String getPropertyValuesDiscretised) {
		this.getPropertyValuesDiscretised = getPropertyValuesDiscretised;
	}

	public TypeOfDataSource getGetReferencesFromAConcept() {
		return TypeOfDataSource.fromString(getReferencesFromAConcept);
	}

	public void setGetReferencesFromAConcept(String getReferencesFromAConcept) {
		this.getReferencesFromAConcept = getReferencesFromAConcept;
	}

	public TypeOfDataSource getGetPropertyValuesFromGreenGroup() {
		return TypeOfDataSource.fromString(getPropertyValuesFromGreenGroup);
	}

	public void setGetPropertyValuesFromGreenGroup(String getPropertyValuesFromGreenGroup) {
		this.getPropertyValuesFromGreenGroup = getPropertyValuesFromGreenGroup;
	}

	public TypeOfDataSource getGetPropertyValuesFromOrangeGroup() {
		return TypeOfDataSource.fromString(getPropertyValuesFromOrangeGroup);
	}

	public void setGetPropertyValuesFromOrangeGroup(String getPropertyValuesFromOrangeGroup) {
		this.getPropertyValuesFromOrangeGroup = getPropertyValuesFromOrangeGroup;
	}

	public TypeOfDataSource getGetLogicalView() {
		return TypeOfDataSource.fromString(getLogicalView);
	}

	public void setGetLogicalView(String getLogicalView) {
		this.getLogicalView = getLogicalView;
	}

	public TypeOfDataSource getGetPropertyFromConcept() {
		return TypeOfDataSource.fromString(getPropertyFromConcept);
	}

	public void setGetPropertyFromConcept(String getPropertyFromConcept) {
		this.getPropertyFromConcept = getPropertyFromConcept;
	}

	public TypeOfDataSource getGetInstantiatedPropertiesFromConcept() {
		return TypeOfDataSource.fromString(getInstantiatedPropertiesFromConcept);
	}

	public void setGetInstantiatedPropertiesFromConcept(String getInstantiatedPropertiesFromConcept) {
		this.getInstantiatedPropertiesFromConcept = getInstantiatedPropertiesFromConcept;
	}

	public TypeOfDataSource getDetectMeaningLanguageSpecific() {
		return TypeOfDataSource.fromString(detectMeaningLanguageSpecific);
	}

	public void setDetectMeaningLanguageSpecific(String detectMeaningLanguageSpecific) {
		this.detectMeaningLanguageSpecific = detectMeaningLanguageSpecific;
	}

	@Override
	public String toString() {
		return "HybridConfiguration [detectMeaningLanguageSpecific=" + detectMeaningLanguageSpecific
				+ ", executeSPARQLWithOptionalSelect=" + executeSPARQLWithOptionalSelect + ", executeSPARQLSelect="
				+ executeSPARQLSelect + ", getPropertyValuesDiscretised=" + getPropertyValuesDiscretised
				+ ", getReferencesFromAConcept=" + getReferencesFromAConcept + ", getPropertyValuesFromGreenGroup="
				+ getPropertyValuesFromGreenGroup + ", getPropertyValuesFromOrangeGroup="
				+ getPropertyValuesFromOrangeGroup + ", getLogicalView=" + getLogicalView + ", getPropertyFromConcept="
				+ getPropertyFromConcept + ", getInstantiatedPropertiesFromConcept="
				+ getInstantiatedPropertiesFromConcept + "]";
	} 
	
	
	
}
