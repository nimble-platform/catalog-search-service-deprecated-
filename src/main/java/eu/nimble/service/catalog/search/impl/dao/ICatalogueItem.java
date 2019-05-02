package eu.nimble.service.catalog.search.impl.dao;

public interface ICatalogueItem extends IConcept {
	String COLLECTION = "item";
	String ID_FIELD = "id";
	String TYPE_FIELD ="doctype";
	/**
	 * the item entries must have a type value assigned
	 */
	public static final String TYPE_VALUE = "item";
	/**
	 * The id of the catalogue the item is contained in
	 */
	String CATALOGUE_ID_FIELD = "catalogueId";
	/**
	 * The curreny label, the dynamic part is the encoded label string such as eUR for EUR
	 */
	String CURRENCY_FIELD = "*_currency";
	/**
	 * The currency based price field, e.g. <code>price_eur</code>, <code>price_usd</code> 
	 */
	String PRICE_FIELD = "*_price";
	/**
	 * The image field with qualifier (thumbnail, midsize)
	 */
	String IMAGE_URI_FIELD ="image";
	/**
	 * Free of charge indicator
	 */
	String FREE_OF_CHARGE_FIELD = "freeOfCharge";
	/**
	 * Certificate types
	 */
	String CERTIFICATE_TYPE_FIELD = "certificateType";
	/**
	 * List of applicable countries the item is available
	 */
	String APPLICABLE_COUNTRIES_FIELD = "applicableCountries";
	/**
	 * The delivery time, numeric
	 */
	String ESTIMATED_DELIVERY_TIME_FIELD = "*_deliveryTime";
	/**
	 * The delivery time unit (weeks, days) 
	 */
	String ESTIMATED_DELIVERY_TIME_UNIT_FIELD = "*_deliveryTimeUnit";
	/**
	 * The id of the manufacturer's party type
	 */
	String MANUFACTURER_ID_FIELD = "manufacturerId";
	/**
	 * The item's id in the manufacturer's context or system
	 */
	String MANUFACTURER_ITEM_ID_FIELD = "manufacturerItemId";
	/**
	 * Service type, such as <b>Port to Port</b>, <b>Door to door</b>
	 */
	String SERVICE_TYPE_FIELD = "serviceType";
	/**
	 * The supported product nature (when delivering), such as <b>Euro Pallet</b>
	 */
	String SUPPORTED_PRODUCT_NATURE_FIELD = "supportedProductNature";
	/** 
	 * Supported cargo type
	 */
	String SUPPORTED_CARGO_TYPE_FIELD = "supportedCargoType";
	// DYNAMIC
	/**
	 * Dynamic! The packaging unit, e.g. the field name <code>package_unit_box</code> for the package type <b>Box</b>
	 */
	String PACKAGE_UNIT_FIELD = "*_packageUnit";
	// DYNAMIC
	/**
	 * Dynamic The packaging amount per unit, e.g. the field name <code>package_amount_box</code> for the package type <b>Box</b>
	 */
	String PACKAGE_AMOUNT_FILED = "*_package";
	/**
	 * The package type, such as
	 */
	/**
	 * The label for any item classification
	 */
	String COMMODITY_CLASSIFICATION_LABEL_FIELD ="commodityClassificationLabel";
	/**
	 * The URI for any item classification
	 */
	String COMMODITY_CLASSIFICATION_URI_FIELD = "commodityClassficationUri";
	/**
	 * A combination of uri and label
	 */
	String COMMODITY_CLASSIFICATION_MIX_FIELD = "commodityClassficationMix";
	/**
	 * The total capacity
	 */
	String TOTAL_CAPACITY_FIELD = "totalCapacity";
	/**
	 * The total capacity's unit
	 */
	String Total_CAPACITY_UNIT_FIELD ="totalCapacityUnit";
	/**
	 * The transport mode
	 */
	String TRANSPORT_MODE = "transportMode";
	/**
	 * Emission type
	 */
	String EMISSION_TYPE_FIELD = "emissionType";
	/**
	 * Emission standard
	 */
	String EMISSION_STANDARD_FIELD = "emissionStandard";
	/**
	 * Estimated delivery duration
	 */
	String ESTIMATED_DURATION_FIELD = "estimatedDuration";
	/**
	 * Duration of the warranty period
	 */
	String WARRANTY_VALIDITY_PERIOD_FIELD = "warrantyValidityPeriod";
	/**
	 * Minimum Order Quantity
	 */
	String MINIMUM_ORDER_QUANTITY_FIELD = "minimumOrderQuantity";
	/**
	 * Applicable INCOTERMS
	 *
	 */
	String INCOTERMS_FIELD = "incoterm";
	// additional property attributes
	String VALUE_QUALIFIER_FIELD = "valueQualifier";
	/**
	 * Additional Properties
	 */
	String QUALIFIED_KEY_FIELD = "*_key";
	String QUALIFIED_STRING_FIELD = "*_svalues";
	String QUALIFIED_DOUBLE_FIELD = "*_dvalues";
	String QUALIFIED_BOOLEAN_FIELD = "*_bvalue";
	//
	static boolean isQualifiedDynamic(String string) {
		switch (string) {
		case QUALIFIED_BOOLEAN_FIELD:
		case QUALIFIED_DOUBLE_FIELD:
		case QUALIFIED_STRING_FIELD:
		case QUALIFIED_KEY_FIELD:
			return true;
		default:
			return false;
		}
	}
	static boolean isFixedDynamic(String string) {
		switch (string) {
		case PACKAGE_AMOUNT_FILED:
		case PACKAGE_UNIT_FIELD:
		case CURRENCY_FIELD:
		case PRICE_FIELD:
		case ESTIMATED_DELIVERY_TIME_FIELD:
		case LABEL_FIELD:
		case DESCRIPTION_FIELD:
		case ALTERNATE_LABEL_FIELD:
		case HIDDEN_LABEL_FIELD:
		case LANGUAGE_TXT_FIELD:
			return true;
		default:
			return false;
		}
	}

}
