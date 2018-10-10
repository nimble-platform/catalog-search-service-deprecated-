package eu.nimble.service.catalog.search.impl.SOLRAccess;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityMappingService {
	
	public String mapPropertyURIToFieldName(String proeprtyUI){
		Logger.getAnonymousLogger().log(Level.WARNING, "No entity mapping available. Return URI: " + proeprtyUI);
		return proeprtyUI;
	}

	public boolean isAPropertySpecificField(String column) {
		//TODO the following assumption doesn't hold. Have to be more specific. 
		return column.contains("lmf.")?false:true;
	}

	public String mapFieldNameToProperty(String fieldName){
		Logger.getAnonymousLogger().log(Level.WARNING, "No entity mapping available. Return URI: " + fieldName);
		return fieldName;
	}
	
	
}
