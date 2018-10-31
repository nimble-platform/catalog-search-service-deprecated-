package eu.nimble.service.catalog.search.impl.dao.enums;

public enum TypeOfDataSource {

	LOCAL_ONTOLOGY, MARMOTTA, SOLR, UNKOWN;
	
	public static TypeOfDataSource fromString(String str){
		if (str.equals(LOCAL_ONTOLOGY.toString())){
			return LOCAL_ONTOLOGY;
		}
		if (str.equals(MARMOTTA.toString())){
			return MARMOTTA;
		}
		if (str.equals(SOLR.toString())){
			return SOLR;
		}
		return UNKOWN;
	}
}
