package eu.nimble.service.catalog.search.impl.SOLRAccess;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EntityMappingService {
	
	public String mapPropertyURIToFieldName(String proeprtyUI){
		Logger.getAnonymousLogger().log(Level.WARNING, "No entity mapping available. Return URI: " + proeprtyUI);
		return proeprtyUI;
	}

}
