package eu.nimble.service.catalog.search.impl.dao.item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.google.common.base.CaseFormat;

import eu.nimble.service.catalog.search.impl.dao.Concept;
import eu.nimble.service.catalog.search.impl.dao.ICatalogueItem;
import eu.nimble.service.catalog.search.impl.dao.PartyType;
import eu.nimble.service.catalog.search.impl.dao.ValueQualifier;

public class ItemType extends Concept implements ICatalogueItem, Serializable {
	public static String QUALIFIED_DELIMITER ="@";
	private static final long serialVersionUID = -3631731059281154372L;

	/**
	 * The indexed item must have a type value assigned for
	 * proper handling of nested documents
	 */
	
	private String type = TYPE_VALUE;
	/**
	 * The ID of the catalogue the item belongs to
	 */
	
	private String catalogueId;
	
	// PRICE & Currency
	
	private Map<String, String> currencyMap = new HashMap<>();
	
	
	private Map<String, Double> currencyValue = new HashMap<>();
	
	
	private Set<String> applicableCountries;
	// FREE of charge indicator
	
	private Boolean freeOfCharge;
	// certification types 
	
	private Set<String> certificateType;

	// delivery time(s)
	
	private Map<String, String> deliveryTimeUnit = new HashMap<>();
	
	private Map<String, Double> deliveryTime = new HashMap<>();
	/**
	 * Map holding a list of used Unit's for packaging
	 * The 
	 */
	
	private Map<String, String> packageUnit = new HashMap<>();
	/**
	 * Map holding the amounts per package unit
	 */
	
	private Map<String, List<Double>> packageAmounts =new HashMap<>();
	/**
	 * Id of the corresponding manufacturer
	 */
	
	private String manufacturerId;
	
	private String manufactuerItemId;
	// Transportation Service Details
	
	private Set<String> serviceType;
	
	private String supportedProductNature;
	
	private String supportedCargoType;
	
	private String emissionStandard;
	

	/**
	 * Possibility for joining to product class index
	 */
	
	private List<String> classificationUri;
	//
	
	private Map<String, String> propertyMap = new HashMap<>();
	// 

	private Map<String, Collection<String>> stringValue = new HashMap<>();
	
	private Map<String, Boolean> booleanValue = new HashMap<>();
	
	private Map<String, Collection<Double>> doubleValue = new HashMap<>();
	
	
	
	private Collection<String> imgageUri;
	
	private Collection<String> incoterms;
	
	private Double minimumOrderQuantity;
	
	private Double warrantyValidityPeriod;
	/**
	 * List containing multilingual labels for product classification
	 * 
	 */
	
	private List<Concept> classification;
	/**
	 * Read only field - used to provide the manufacturer's details
	 * in a search result
	 */
	
	private PartyType manufacturer;
	
	
	private Map<String,PropertyType> customProperties;
	/**
	 * Setter for the dynamic string properties.
	 * 
	 * Method is for deserializing from JSON, do not use directly
	 * @param qualifier
	 * @param values
	 */
	public void setStringProperty(String qualifier, Collection<String> values) {
		this.stringValue.put(dynamicKey(qualifier, propertyMap), values);
	}
	/**
	 * Add a new string based property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The value of the property
	 */
	public void addProperty(String qualifier, String value) {
		String key = dynamicKey(qualifier, propertyMap);
		Collection<String> values = stringValue.get(key);
		if ( values == null ) {
			values = new HashSet<String>();
			this.stringValue.put(key, values);
		}
		//
		values.add(value);
	}
	/**
	 * Add a new string based property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The value of the property
	 * @param meta A detailed property description holding multi lingual labels
	 */
	public void addProperty(String qualifier, String value, PropertyType meta) {
		addProperty(qualifier, value);
		if ( meta != null) {
			// ensure the proper valueQualifier
			addCustomProperty(qualifier, meta, ValueQualifier.STRING);
		}		
	}
	/**
	 * Add a new numeric (double) property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier
	 * @param value The value of the property
	 */
	public void addProperty(String qualifier, String unit, Double value) {
		String key = dynamicKey(propertyMap, qualifier, unit);
		Collection<Double> values = doubleValue.get(key);
		if ( values == null ) {
			values = new HashSet<Double>();
			this.doubleValue.put(key, values);
		}
		//
		values.add(value);
	}
	/**
	 * Add a new numeric (double) property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier
	 * @param value The value of the property
	 * @param meta A detailed property description holding multi lingual labels
	 */
	public void addProperty(String qualifier, String unit, Double value, PropertyType meta) {
		addProperty(qualifier, unit, value);
		if ( meta != null) {
			// ensure the proper valueQualifier
			addCustomProperty(qualifier, unit, meta, ValueQualifier.QUANTITY);
		}		
	}
	/**
	 * Add a new string property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier, such as the language
	 * @param value The value of the property
	 * @param meta A detailed property description holding multi lingual labels
	 * @deprecated Use {@link #addMultiLingualProperty(String, String, String, PropertyType)}
	 */
	@Deprecated
	public void addProperty(String qualifier, String unit, String value, PropertyType meta) {
		addMultiLingualProperty(qualifier, unit, value, meta);
	}
	/**
	 * Setter for the double properties, used for deserializing from JSON. 
	 * Do not use directly
	 * @param qualifier
	 * @param values
	 */
	private void setDoubleProperty(String qualifier, Collection<Double> values) {
		if ( qualifier.contains("@") ) {
			String qualifiedValue = qualifiedValue(qualifier);
			String qualifierUnit = qualifiedUnit(qualifier);
			for ( Double d : values) {
				addProperty(qualifiedValue, qualifierUnit, d);
			}
		}
		else {
			for (Double d : values) {
				addProperty(qualifier, d);
			}
		}
	}
	/**
	 * Add a new numeric double property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The value of the property
	 */
	public void addProperty(String qualifier, Double value) {
		String key = dynamicKey(qualifier, propertyMap);
		Collection<Double> values = doubleValue.get(key);
		if ( values == null ) {
			values = new HashSet<Double>();
			this.doubleValue.put(key, values);
		}
		//
		values.add(value);
	}
	/**
	 * Add a new numeric double property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The value of the property
	 * @param meta A detailed property description holding multi lingual labels
	 */
	public void addProperty(String qualifier, Double value, PropertyType meta) {
		addProperty(qualifier, value);
		if ( meta != null) {
			// 
			addCustomProperty(qualifier, meta, ValueQualifier.NUMBER);
		}
	}
	/**
	 * Helper method maintaining the list of custom property descriptions
	 * @param qualifier The qualifier to use for the index name and for the mapping to the custom property
	 * @param unit The unit of the actual usage - full qualifier including unit is used for mapping as well
	 * @param meta The detailed property description holding multilingual labels
	 * @param valueQualfier The basic type of the property, see {@link ValueQualifier} for complete list
	 */
	private void addCustomProperty(String qualifier, String unit, PropertyType meta, ValueQualifier valueQualfier) {
		String key = dynamicFieldPart(qualifier);
		String full = dynamicFieldPart(qualifier, unit);
		if ( customProperties == null ) {
			customProperties = new HashMap<>();
		}
//		String idxField = dynamicFieldPart(qualifier, unit);
//		meta.addItemFieldName(idxField);
		//
		if ( ! customProperties.containsKey(key)) {
			customProperties.put(key, meta);
		}
		// add the qualifier "including" the unit
		PropertyType pt = customProperties.get(key);
		pt.setValueQualifier(valueQualfier);
		pt.addItemFieldName(key);
		pt.addItemFieldName(full);
		
	}
	/**
	 * Helper method maintaining the list of custom property descriptions
	 * @param qualifier The qualifier to use for the index name and for the mapping to the custom property
	 * @param meta The detailed property description holding multilingual labels
	 * @param valueQualfier The basic type of the property, see {@link ValueQualifier} for complete list
	 */
	private void addCustomProperty(String qualifier, PropertyType meta, ValueQualifier valueQualifier) {
		String part = dynamicFieldPart(qualifier);
		if ( customProperties == null ) {
			customProperties = new HashMap<>();
		}
		if (! customProperties.containsKey(part)) {
			customProperties.put(part, meta);
		}
		// add the qualifier "including" the unit
		PropertyType pt = customProperties.get(part);
		pt.setValueQualifier(valueQualifier);
		pt.addItemFieldName(part);
	}
	/**
	 * Add a new string property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier, such as the language
	 * @param value The value of the property
	 * @deprecated Use {@link #addMultiLingualProperty(String, String, String)}
	 */
	@Deprecated
	public void addProperty(String qualifier, String unit, String value) {
		addMultiLingualProperty(qualifier, unit, value);
	}
	/**
	 * Add a new string property, the text and the language are are combined to <i>text@language</i>.
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier, such as the language
	 * @param value The value of the property
	 */
	public void addMultiLingualProperty(String qualifier, String language, String text) {
		String key = dynamicKey(propertyMap, qualifier);
		Collection<String> values = this.stringValue.get(key);
		if ( values == null ) {
			values = new HashSet<String>();
			this.stringValue.put(key, values);
		}
		//
		values.add(String.format("%s@%s", text, language));
		
	}
	/**
	 * Add a new string property, the text and the language are are combined to <i>text@language</i>.
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param unit Additional qualifier, such as the language
	 * @param value The value of the property
	 * @param meta A detailed description of the property including multi lingual labels
	 */
	public void addMultiLingualProperty(String qualifier, String language, String text, PropertyType meta) {
		addMultiLingualProperty(qualifier, language, text);
		if ( meta != null) {
			// ensure the proper valueQualifier
			addCustomProperty(qualifier, meta, ValueQualifier.STRING);
		}		
	}
	/**
	 * Obtain a multi-lingual property for the given qualifier and the desired language, <code>null</code> if 
	 * no value can be found. 
	 * @param qualifier The qualifier used when storing  the multi lingual property value
	 * @param language The language code, such as <i>en</i>, <i>es</i>
	 * @return The multi lingual property or <code>null</code>
	 */
	public String getMultiLingualProperty(String qualifier, String language) {
		String key = dynamicFieldPart(qualifier);
		if ( this.stringValue.get(key)!=null && ! this.stringValue.get(key).isEmpty()) {
			Optional<String> prop = this.stringValue.get(key).stream()
					.filter(new Predicate<String>() {
						
						@Override
						public boolean test(String t) {
							if (language.equalsIgnoreCase(qualifiedUnit(t))) {
								return true;
							}
							return false;
						}
						
					})
					.map(new Function<String, String>() {
						
						@Override
						public String apply(String t) {
							return qualifiedValue(t);
						}})
					.findFirst();
			return prop.orElse(null);
		}
		return null;

	}
	/**
	 * Obtain a list of multilingual property values
	 * @param qualifier The qualifier used when storing  the multi lingual property value
	 * @param language The language code, such as <i>en</i>, <i>es</i>
	 * @return A list of multi lingual property values for the desired languag, empty list when no value present
	 */
	public List<String> getMultiLingualProperties(String qualifier, String language) {
		String key = dynamicFieldPart(qualifier);
		if ( this.stringValue.get(key)!=null && ! this.stringValue.get(key).isEmpty()) {
			List<String> prop = this.stringValue.get(key).stream()
					.filter(new Predicate<String>() {
						
						@Override
						public boolean test(String t) {
							if (language.equalsIgnoreCase(qualifiedUnit(t))) {
								return true;
							}
							return false;
						}
						
					})
					.map(new Function<String, String>() {
						
						@Override
						public String apply(String t) {
							return qualifiedValue(t);
						}})
					.collect(Collectors.toList());
			return prop;
		}
		return new ArrayList<>();
	}
	/**
	 * Helper method to extract the value portion of the multilingual label
	 * @param t
	 * @return
	 */
	public static String qualifiedValue(String t) {
		int delim = t.lastIndexOf("@");
		if ( delim > 0 ) {
			return t.substring(0,delim);
		}
		return t;
		
	}
	/**
	 * Helper method to extract the language portion of the multilingual label
	 * @param t
	 * @return
	 */
	public static String qualifiedUnit(String t) {
		int delim = t.lastIndexOf("@");
		if ( delim > 0 && t.length()>delim+1) {
			return t.substring(delim+1);
		}
		return null;
		
	}
	/**
	 * Obtain the collection of numeric properties stored with the provided <i>qualifier</i>
	 * @param qualifier The qualifier used when storing the numeric values
	 * @param unit The unit extension used when storing the numeric values
	 * @return The list of stored values, may return null when not found!
	 */
	public Collection<Double> getProperty(String qualifier, String unit) {
		String key = dynamicFieldPart(qualifier, unit);
		return this.doubleValue.get(key);
	}
	/**
	 * Getter for the boolean properties, used for serializing. The qualifiers originally used
	 * when storing are restored and used as keys.
	 * @return
	 */
	public Map<String, Boolean> getBooleanValue() {
		Map<String, Boolean> result = new HashMap<>();
		for ( String dynUnitKey : this.propertyMap.keySet()) {
			if ( booleanValue.containsKey(dynUnitKey)) {
				result.put(propertyMap.get(dynUnitKey), booleanValue.get(dynUnitKey));
			}
		}
		return result;
	}
	/**
	 * Setter for boolean properties, used for deserializing JSON, not intended to be
	 * used directly
	 * @param booleanValue
	 */
	public void setBooleanValue(Map<String, Boolean> booleanValue) {
		if ( booleanValue != null ) {
			for (String key :  booleanValue.keySet()) {
				this.booleanValue.put(dynamicKey(key, propertyMap), booleanValue.get(key));
			}
		}
		else {
			this.booleanValue = booleanValue;
		}
	}
	/**
	 * Getter for the string properties, used for serializing. The qualifiers originally used 
	 * when storing are restored and used as keys.
	 * @return
	 */
	public Map<String, Collection<String>> getStringValue() {
		Map<String, Collection<String>> result = new HashMap<>();
		for ( String dynUnitKey : this.propertyMap.keySet()) {
			if ( stringValue.containsKey(dynUnitKey)) {
				result.put(propertyMap.get(dynUnitKey), stringValue.get(dynUnitKey));
			}
		}
		return result;
	}
	/**
	 * Getter for the double values, used for serializing. The qualifiers originally used
	 * when storing are restored and used as key.
	 * @return
	 */
	public Map<String, Collection<Double>> getDoubleValue() {
		Map<String, Collection<Double>> result = new HashMap<>();
		for ( String dynUnitKey : this.propertyMap.keySet()) {
			if ( doubleValue.containsKey(dynUnitKey)) {
				result.put(propertyMap.get(dynUnitKey), doubleValue.get(dynUnitKey));
			}
		}
		return result;
	}
	/**
	 * Setter for the string values, used for deserializing, do not use directly
	 * @param stringValue
	 */
	public void setStringValue(Map<String, Collection<String>> stringValue) {
		if ( stringValue != null ) {
			for (String key :  stringValue.keySet()) {
				setStringProperty(key, stringValue.get(key));
			}
		}
		else {
			this.stringValue = stringValue;
		}
	}
	/**
	 * Setter for the double values, used for deserializing, do not use directly
	 * @param doubleValue
	 */
	public void setDoubleValue(Map<String, Collection<Double>> doubleValue) {
		if ( doubleValue != null ) {
			for (String key :  doubleValue.keySet()) {
				setDoubleProperty(key, doubleValue.get(key));
			}
		}
		else {
			this.doubleValue = doubleValue;
		}
	}
	
	/**
	 * Setter for a boolean property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The boolean value to store
	 */
	public void setProperty(String qualifier, Boolean value) {
		this.booleanValue.put(dynamicKey(qualifier,propertyMap), value);
	}
	/**
	 * Setter for a boolean property
	 * @param qualifier To be used as dynamic field name in index, see {@link #dynamicFieldPart(String)}
	 * @param value The boolean value to store
	 * @param meta A detailed description of the property holding multilingual labels
	 */
	public void setProperty(String qualifier, Boolean value, PropertyType meta) {
		setProperty(qualifier, value);
		if ( meta != null) {
			// 
			addCustomProperty(qualifier, meta, ValueQualifier.BOOLEAN);
		}
	}
	/**
	 * For proper distinction of catalogue items and nested
	 * documents, the item must hava a type field 
	 * @see #TYPE_VALUE
	 * @return
	 */
	
	public String getTypeValue() {
		return type;
	}
	/**
	 * Setter for the type value
	 * @param type
	 */
	public void setTypeValue(String type) {
		this.type = type;
	}
	/**
	 * Getter for the catalog id
	 * @return
	 */
	public String getCatalogueId() {
		return catalogueId;
	}
	/**
	 * Setter for the catalog id
	 * @param catalogueId
	 */
	public void setCatalogueId(String catalogueId) {
		this.catalogueId = catalogueId;
	}
//		this.languages = language;
	public Boolean getFreeOfCharge() {
		return freeOfCharge;
	}
	public void setFreeOfCharge(Boolean freeOfCharge) {
		this.freeOfCharge = freeOfCharge;
	}
	public Set<String> getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(Set<String> certificateType) {
		this.certificateType = certificateType;
	}
	public Set<String> getApplicableCountries() {
		return applicableCountries;
	}
	public void setApplicableCountries(Set<String> applicableCountries) {
		this.applicableCountries = applicableCountries;
	}


	public String getManufacturerId() {
		if (manufacturer != null && manufacturer.getId() != null) {
			return manufacturer.getId();
		}
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public Set<String> getServiceType() {
		return serviceType;
	}
	public void setServiceType(Set<String> serviceType) {
		this.serviceType = serviceType;
	}
	public String getSupportedProductNature() {
		return supportedProductNature;
	}
	public void setSupportedProductNature(String supportedProductNature) {
		this.supportedProductNature = supportedProductNature;
	}
	public String getSupportedCargoType() {
		return supportedCargoType;
	}
	public void setSupportedCargoType(String supportedCargoType) {
		this.supportedCargoType = supportedCargoType;
	}
	public String getEmissionStandard() {
		return emissionStandard;
	}
	public void setEmissionStandard(String emissionStandard) {
		this.emissionStandard = emissionStandard;
	}
	public void addPrice(String currency, Double price) {
		this.currencyValue.put(dynamicKey(currency, this.currencyMap), price);
	}
	public Collection<String> getCurrency() {
		return this.currencyMap.values();
	}

	public void setCurrency(Collection<String> currency) {
		this.currencyMap.clear();
		for ( String c : currency) {
			dynamicKey(c, this.currencyMap);
		}
	}
	public Map<String, Double> getPrice() {
		Map<String, Double> ret = new HashMap<>();
		for ( String key : currencyMap.keySet()) {
			ret.put(currencyMap.get(key), currencyValue.get(key));
		}
		return ret;
	}
	public void setPrice(Map<String, Double> price) {
		this.currencyValue.clear();
		for ( String c : price.keySet()) {
			addPrice(c, price.get(c));
		}
	}
	public Map<String, String> getDeliveryTimeUnit() {
		return deliveryTimeUnit;
	}
	public Collection<String> getDeliveryTimeUnits() {
		return this.deliveryTimeUnit.values();
	}
	public void setDeliveryTimeUnits(Collection<String> units) {
		this.deliveryTimeUnit.clear();
		for ( String unit : units ) {
			// update the packageUnit
			dynamicKey(unit,  this.deliveryTimeUnit);
		}
	}
	/**
	 * Add a new delivery time to the item. 
	 * @param unit The unit such as <i>Week(s)</i>, <i>Day(s)</i>
	 * @param time The amount of the delivery time unit
	 */
	public void addDeliveryTime(String unit, Double time) {
		this.deliveryTime.put(dynamicKey(unit, this.deliveryTimeUnit), time);
	}
	/**
	 * Getter for the delivery times per unit
	 * @return
	 */
	public Map<String, Double> getDeliveryTime() {
		Map<String, Double> result = new HashMap<>();
		for ( String dynUnitKey : this.deliveryTimeUnit.keySet()) {
			result.put(deliveryTimeUnit.get(dynUnitKey), deliveryTime.get(dynUnitKey));
		}
		return result;
	}
	public void setDeliveryTime(Map<String, Double> deliveryTime) {
		this.deliveryTime.clear();
		for ( String c : deliveryTime.keySet()) {
			addDeliveryTime(c, deliveryTime.get(c));
		}
	}

	/**
	 * Internally the package units hold
	 * @return
	 */
	public Map<String, String> getPackageUnit() {
		return packageUnit;
	}
	public Collection<String> getPackageUnits() {
		return this.packageUnit.values();
	}
	public void setPackageUnits(Collection<String> units) {
		this.packageUnit.clear();
		for ( String unit : units ) {
			// update the packageUnit
			dynamicKey(unit,  this.packageUnit);
		}
	}
	public void addPackageAmounts(String unit, List<Double> amounts) {
		this.packageAmounts.put(dynamicKey(unit, this.packageUnit), amounts);;
	}
	/**
	 * Getter for the package amounts per unit
	 * @return
	 */
	public Map<String, List<Double>> getPackageAmounts() {
		Map<String, List<Double>> result = new HashMap<>();
		for ( String dynUnitKey : this.packageUnit.keySet()) {
			result.put(packageUnit.get(dynUnitKey), packageAmounts.get(dynUnitKey));
		}
		return result;
	}
	public void setPackageAmounts(Map<String, List<Double>> packageAmountPerUnit) {
		this.packageAmounts.clear();
		for ( String key : packageAmountPerUnit.keySet()) {
			addPackageAmounts(key, packageAmountPerUnit.get(key));
		}
	}
	public PartyType getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(PartyType manufacturer) {
		this.manufacturer = manufacturer;
	}
	public List<Concept> getClassification() {
		if ( classification == null) {
			classification = new ArrayList<>();
		}
		return classification;
	}
	public void addClassification(Concept c) {
		getClassification().add(c);
	}
	public void setClassification(List<Concept> classification) {
		this.classification = classification;
	}
	public List<String> getClassificationUri() {
		return classificationUri;
	}
	public void setClassificationUri(List<String> classificationUri) {
		this.classificationUri = classificationUri;
	}
	public Collection<String> getImgageUri() {
		return imgageUri;
	}
	public void setImgageUri(Collection<String> imgageUri) {
		this.imgageUri = imgageUri;
	}
	/**
	 * Getter for custom properties, used for serializing to JSON
	 * @return
	 */
	public Map<String, PropertyType> getCustomProperties() {
		return customProperties;
	}
	/**
	 * Setter for custom properties, used for deserializing from JSON. Do not use directly - instead 
	 * use the addProperty ... methods
	 * <ul>
	 * <li>{@link #addProperty(String, Double, PropertyType)}
	 * <li>{@link #addProperty(String, String, PropertyType)}
	 * <li>{@link #addProperty(String, String, Double, PropertyType)}
	 * <li>{@link #addMultiLingualProperty(String, String, String, PropertyType)}
	 * </li>
	 * @param customProperties
	 */
	public void setCustomProperties(Map<String, PropertyType> customProperties) {
		this.customProperties = customProperties;
	}
	/**
	 * Helper method to create the index field's name part and
	 * to maintain the label for the corresponding name map
	 * @param keyVal
	 * @param keyMap
	 * @return
	 */
	private String dynamicKey(String keyVal, Map<String, String> keyMap) {
		String key = dynamicFieldPart(keyVal);
		keyMap.put(key, keyVal);
		return key;
	}
	/**
	 * Helper method combining multiple key parts, e.q. qualifier and unit
	 * @param keyMap
	 * @param keyPart
	 * @return
	 */
	private String dynamicKey(Map<String, String> keyMap, String ... keyPart) {
		String key = dynamicFieldPart(keyPart);
		// use @ as delimiter
		keyMap.put(key, String.join("@", keyPart));
		return key;
	}
	/**
	 * Static helper method transforming a qualifier into a valid dynamic field part
	 * @param qualifier The qualifier used when adding dynamic properties
	 */
	public static String dynamicFieldPart(String qualifier) {
		if (! StringUtils.hasText(qualifier)) {
			// when no unit code specified - use "undefined";
			return "undefined";
		}
		String dynamicFieldPart = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, qualifier);
		dynamicFieldPart = dynamicFieldPart.replaceAll("[^a-zA-Z0-9_ ]", "");
		dynamicFieldPart = dynamicFieldPart.trim().replaceAll(" ", "_").toUpperCase();
		dynamicFieldPart = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, dynamicFieldPart);
		return dynamicFieldPart;
		
	}
	/**
	 * Static helper method transforming multiple qualifier into a valid dynamic field part 
	 * @param strings The qualifiers used when adding dynamic properties
	 * @return
	 */
	public static String dynamicFieldPart(String ...strings) {
		List<String> parts = new ArrayList<>();
		for ( String part : strings ) {
			parts.add(dynamicFieldPart(part));
		}
		return dynamicFieldPart(String.join("_", parts));
	}
	public Map<String, String> getPropertyMap() {
		return propertyMap;
	}
	public Collection<String> getIncoterms() {
		return incoterms;
	}
	public void setIncoterms(Collection<String> incoterms) {
		this.incoterms = incoterms;
	}
	public Double getMinimumOrderQuantity() {
		return minimumOrderQuantity;
	}
	public void setMinimumOrderQuantity(Double minimumOrderQuantity) {
		this.minimumOrderQuantity = minimumOrderQuantity;
	}
	public Double getWarrantyValidityPeriod() {
		return warrantyValidityPeriod;
	}
	public void setWarrantyValidityPeriod(Double warrantyValidityPeriod) {
		this.warrantyValidityPeriod = warrantyValidityPeriod;
	}
	public String getManufactuerItemId() {
		return manufactuerItemId;
	}
	public void setManufactuerItemId(String manufactuerItemId) {
		this.manufactuerItemId = manufactuerItemId;
	}

}
