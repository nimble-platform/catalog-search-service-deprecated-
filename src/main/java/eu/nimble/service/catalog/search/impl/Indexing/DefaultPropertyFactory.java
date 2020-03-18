package eu.nimble.service.catalog.search.impl.Indexing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.nimble.service.catalog.search.impl.dao.PropertyType;

public class DefaultPropertyFactory extends IndexingServiceConstant{

	

	public List<PropertyType> createProperties() {

		List<PropertyType> result = new ArrayList<PropertyType>();

		result.add(createNameProperty());
		result.add(createDescriptionProperty());
		result.add(createPriceProperty());
		result.add(createCatalogieIdProperty());

		return result;
	}

	private PropertyType createPriceProperty() {
		PropertyType r = new PropertyType();
		r.setVisible(true);
		r.setUri(namespace + "price");
		Collection<String> idxFieldNames = new ArrayList<String>();
		idxFieldNames.add("price");
		r.setItemFieldNames(idxFieldNames);
		Map<String, String> labelMap = new HashMap<String, String>();
		labelMap.put("en", "Price");
		labelMap.put("es", "Precios");
		labelMap.put("sv", "Pris");
		r.setLabel(labelMap);
		
		r.setRange(HTTP_WWW_W3_ORG_2001_XML_SCHEMA+ "float");
		return r;
	}

	private PropertyType createDescriptionProperty() {
		PropertyType r = new PropertyType();
		r.setVisible(true);
		r.setUri(namespace + "description");
		Collection<String> idxFieldNames = new ArrayList<String>();
		idxFieldNames.add("description");
		r.setItemFieldNames(idxFieldNames);
		Map<String, String> labelMap = new HashMap<String, String>();
		labelMap.put("en", "Description");
		labelMap.put("es", "Descripción");
		labelMap.put("sv", "beskrivning");
		r.setLabel(labelMap);
		r.setRange(HTTP_WWW_W3_ORG_2001_XML_SCHEMA+ "string");
		return r;
	}

	private PropertyType createNameProperty() {
		PropertyType r = new PropertyType();
		r.setVisible(true);
		r.setUri(namespace + "name");
		Collection<String> idxFieldNames = new ArrayList<String>();
		idxFieldNames.add("label");
		r.setItemFieldNames(idxFieldNames);
		Map<String, String> labelMap = new HashMap<String, String>();
		labelMap.put("en", "Name");
		labelMap.put("es", "Nombre");
		labelMap.put("sv", "Namn");
		r.setRange(HTTP_WWW_W3_ORG_2001_XML_SCHEMA+ "string");
		r.setLabel(labelMap);
		return r;
	}
	
	private PropertyType createCatalogieIdProperty() {
		PropertyType r = new PropertyType();
		r.setVisible(true);
		r.setUri(namespace + "catalogueId");
		Collection<String> idxFieldNames = new ArrayList<String>();
		idxFieldNames.add("catalogueId");
		r.setItemFieldNames(idxFieldNames);
		Map<String, String> labelMap = new HashMap<String, String>();
		labelMap.put("en", "CatalogueId");
		labelMap.put("es", "ID de catálogo");
		labelMap.put("sv", "Katalog ID");
		r.setRange(HTTP_WWW_W3_ORG_2001_XML_SCHEMA+ "string");
		r.setLabel(labelMap);
		return r;
	}

}
