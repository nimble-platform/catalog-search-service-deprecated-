package eu.nimble.service.catalog.search.impl.dao;

public interface IPropertyType extends INamed {
	String COLLECTION = "props";
	String TYPE_FIELD = "doctype";
	String TYPE_VALUE = "property";
	String IS_FACET_FIELD = "isFacet";
	String BOOST_FIELD = "boost";
	String RANGE_FIELD = "range";
	String VALUE_QUALIFIER_FIELD = "valueQualifier";
	String USED_WITH_FIELD = "used_in";
	String IDX_FIELD_NAME_FIELD = "idxField";
}
	
