package eu.nimble.service.catalog.search.impl.dao;

public class IndexFields {

	private String fieldName;
	private String dataType;
	private String dynamicBase;
	private String uri;
	private String mappedName;
	private String dynamicPart;
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDynamicBase() {
		return dynamicBase;
	}
	public void setDynamicBase(String dynamicBase) {
		this.dynamicBase = dynamicBase;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getMappedName() {
		return mappedName;
	}
	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}
	public String getDynamicPart() {
		return dynamicPart;
	}
	public void setDynamicPart(String dynamicPart) {
		this.dynamicPart = dynamicPart;
	}
	@Override
	public String toString() {
		return "IndexFields [fieldName=" + fieldName + ", dataType=" + dataType + ", dynamicBase=" + dynamicBase
				+ ", uri=" + uri + ", mappedName=" + mappedName + ", dynamicPart=" + dynamicPart + "]";
	}
	
	
	
}
