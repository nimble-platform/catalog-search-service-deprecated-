package eu.nimble.service.catalog.search.mediator;

import java.util.List;

public class WrapperMessage {
	String namespace;
	String query;
	List<WrapperProperty> propList;

	public WrapperMessage() {
	}

	public WrapperMessage(String namespace, String query,
			List<WrapperProperty> propList) {
		this.namespace = namespace;
		this.query = query;
		this.propList = propList;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<WrapperProperty> getPropList() {
		return propList;
	}

	public void setPropList(List<WrapperProperty> propList) {
		this.propList = propList;
	}
}

class WrapperProperty {
	int wrapperID;

	public int getWrapperID() {
		return wrapperID;
	}

	public void setWrapperID(int wrapperID) {
		this.wrapperID = wrapperID;
	}
}

class WrapperHTTPProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;

	public WrapperHTTPProperty() {
	}

	public WrapperHTTPProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperSQLProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;

	public WrapperSQLProperty() {
	}

	public WrapperSQLProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperCSVProperty extends WrapperProperty {
	String propertyFile;
	String csvFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;
	String datatypeDefinitionFile;

	public WrapperCSVProperty() {
	}

	public WrapperCSVProperty(String csvFile, int wrapperID,
			String propertyFile, String mappingFile, String mappingSchema,
			String ontologyFile, String datatypeDefinitionFile) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(String csvFile) {
		this.csvFile = csvFile;
	}

	public String getDatatypeDefinitionFile() {
		return datatypeDefinitionFile;
	}

	public void setDatatypeDefinitionFile(String datatypeDefinitionFile) {
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}
}

class WrapperXLSProperty extends WrapperProperty {
	String propertyFile;
	String mappingFile;
	String mappingSchema;
	String ontologyFile;
	String datatypeDefinitionFile;
	String configFolder;

	public WrapperXLSProperty() {
	}

	public WrapperXLSProperty(int wrapperID, String propertyFile,
			String mappingFile, String mappingSchema, String ontologyFile,
			String datatypeDefinitionFile, String configFolder) {
		this.wrapperID = wrapperID;
		this.propertyFile = propertyFile;
		this.mappingFile = mappingFile;
		this.mappingSchema = mappingSchema;
		this.ontologyFile = ontologyFile;
		this.datatypeDefinitionFile = datatypeDefinitionFile;
		this.configFolder = configFolder;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getDatatypeDefinitionFile() {
		return datatypeDefinitionFile;
	}

	public void setDatatypeDefinitionFile(String datatypeDefinitionFile) {
		this.datatypeDefinitionFile = datatypeDefinitionFile;
	}

	public String getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public String getMappingSchema() {
		return mappingSchema;
	}

	public void setMappingSchema(String mappingSchema) {
		this.mappingSchema = mappingSchema;
	}

	public String getOntologyFile() {
		return ontologyFile;
	}

	public void setOntologyFile(String ontologyFile) {
		this.ontologyFile = ontologyFile;
	}

	public String getConfigFolder() {
		return configFolder;
	}

	public void setConfigFolder(String configFolder) {
		this.configFolder = configFolder;
	}
}