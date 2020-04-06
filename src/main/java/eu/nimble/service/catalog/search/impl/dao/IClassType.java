package eu.nimble.service.catalog.search.impl.dao;

public interface IClassType extends INamed {
	String TYPE_FIELD = "doctype";
	String TYPE_VALUE = "class";
	String PROPERTIES_FIELD = "properties";
	String PARENTS_FIELD = "parents";
	String ALL_PARENTS_FIELD = "allParents";
	String CHILDREN_FIELD ="children";
	String ALL_CHILDREN_FIELD ="allChildren";
	
	String LEVEL_FIELD = "level";
	
}