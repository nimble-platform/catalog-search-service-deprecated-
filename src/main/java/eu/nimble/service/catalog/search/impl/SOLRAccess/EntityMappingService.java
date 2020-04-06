package eu.nimble.service.catalog.search.impl.SOLRAccess;



public class EntityMappingService {
	
	private SOLRReader solrReader = null;
	
	
	
	public EntityMappingService(SOLRReader solrReader) {
		super();
		this.solrReader = solrReader;
	}

	public String mapPropertyURIToFieldName(String propertyUrl, String conceptURL){
		return solrReader.getNameForIdxField(propertyUrl, conceptURL);
	}

	public boolean isAPropertySpecificField(String column) {
		//TODO the following assumption doesn't hold. Have to be more specific. 
		return column.contains("lmf.")?false:true;
	}

	public String mapFieldNameToProperty(String propertyUrl, String conceptURL){
		return solrReader.getPropertyURLBasedOnIdxField(propertyUrl, conceptURL);
	}
	
	
}
