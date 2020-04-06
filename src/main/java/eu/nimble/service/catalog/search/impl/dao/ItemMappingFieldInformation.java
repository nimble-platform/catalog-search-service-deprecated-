package eu.nimble.service.catalog.search.impl.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ItemMappingFieldInformation {

	private String fieldName;
	private String dataType;
	private String dynamicBase;
	private String dynamicPart;
	private String mappedName;
	private int  docCount;
	private String uri; 
	protected Map<String, String> label;
	
	
	
	
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

	public String getDynamicPart() {
		return dynamicPart;
	}

	public void setDynamicPart(String dynamicPart) {
		this.dynamicPart = dynamicPart;
	}

	public String getMappedName() {
		return mappedName;
	}

	public void setMappedName(String mappedName) {
		this.mappedName = mappedName;
	}

	public int getDocCount() {
		return docCount;
	}

	public void setDocCount(int docCount) {
		this.docCount = docCount;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, String> getLabel() {
		return label;
	}

	public void addLabel(String language, String label) {
		if ( this.label == null) {
			this.label = new HashMap<>();
		}
		this.label.put(language, label);
		// 
		
	}
	
	public void setLabel(Map<String, String> labelMap) {
		if ( labelMap != null ) {
			for ( String key : labelMap.keySet() ) {
				addLabel(key, labelMap.get(key));
			}
		}
		else {
			this.label = null;
		}
	}

	@Override
	public String toString() {
		return "ItemMappingFieldInformation [fieldName=" + fieldName + ", dataType=" + dataType + ", dynamicBase="
				+ dynamicBase + ", dynamicPart=" + dynamicPart + ", mappedName=" + mappedName + ", docCount=" + docCount
				+ ", uri=" + uri + ", label=" + label + "]";
	}
	
}
