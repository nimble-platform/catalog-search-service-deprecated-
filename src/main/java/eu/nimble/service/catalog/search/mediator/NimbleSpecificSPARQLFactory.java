package eu.nimble.service.catalog.search.mediator;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.services.NimbleAdaptionServiceOfSearchResults;
import eu.nimble.service.catalog.search.services.SQPDerivationService;

public class NimbleSpecificSPARQLFactory {

	private MediatorSPARQLDerivationAndExecution sparqlDerivation;
	private NimbleSpecificSPARQLDeriviationAndExecution nimbleSpecificSPARQLDeriviation;
	private IReader reader;
	private SQPDerivationService sqpDerivationService;

	public NimbleSpecificSPARQLFactory(MediatorSPARQLDerivationAndExecution sparqlDerivation, SQPDerivationService sqpDerivationService) {
		super();
		this.sparqlDerivation = sparqlDerivation;
		this.sqpDerivationService = sqpDerivationService;

	}

	public List<String> createSparql(InputParamaterForExecuteSelect inputParamaterForExecuteSelect, IReader reader) {
		this.reader = reader;

		if (!(reader instanceof MarmottaReader)) {
			return createJenaSPARQLQuery(inputParamaterForExecuteSelect);
		} else {
			return createMarmottaSPARQLQuery(inputParamaterForExecuteSelect);
		}
	}

	private List<String> createMarmottaSPARQLQuery(InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {

		nimbleSpecificSPARQLDeriviation = new NimbleSpecificSPARQLDeriviationAndExecution((MarmottaReader) reader,sqpDerivationService,sparqlDerivation);

		List<String> result = new ArrayList<String>();
		int counter = 0;
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";

			sparql += " ?property";
			sparql += " ?hasValue";

			// Define it is a ItemType
			sparql += " where{";
			sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?codeValue.";

			String urlOfconcept = inputParamaterForExecuteSelect.getConcept();
			if (sparqlDerivation != null) {
				urlOfconcept = sparqlDerivation.getURIOfConcept(inputParamaterForExecuteSelect.getConcept());
			}

			sparql += addPropertyForMarmotta(inputParamaterForExecuteSelect.getParametersURL().get(counter), inputParamaterForExecuteSelect.getPropertySources().get(counter));

			sparql += "Filter  regex( ?codeValue , \"" + urlOfconcept + "\").";
			sparql += "}";
			result.add(sparql);
			counter++;
		}
		return result;
	}

	private String addPropertyForMarmotta(String propertyURL, PropertySource propertySource2) {

		String result = "";

		PropertySource propertySource = detectPropertySource(propertyURL, propertySource2);
		switch (propertySource) {
		case ADDITIONAL_ITEM_PROPERTY:

			break;
		case DOMAIN_SPECIFIC_PROPERTY:
			result += extendForDomainSpecificProperty(propertyURL);
			break;
			
		case DESCRIPTION:
			result += extendForDescriptionProperty();
			break;
		case NAME:
			result += extendForNameProperty();
			break;
		case CERTIFICATE:
			result += extendForCertificate();
			break;
		case DIMENSION:
			result += extendForDimension(propertyURL);
			return result;
		case MANUFACTURER_ITEMS_IDENTIFICATION:
			result+= extendForManufacturerIdentification();
			return result;
		case MANUFACTURER_PARTY:
			result+= extendForManufacturerParty(propertyURL);
			return result;
			
		case CUSTOM_STRING:
			result += extendForCustomString(propertyURL);
			return result;
		case CUSTOM_DECIMAL:
			result += extendForCustomDecimal(propertyURL);
			return result;
		
	}

		
		if (propertySource != PropertySource.DOMAIN_SPECIFIC_PROPERTY){
		result += "?instance ?property  ?hasValue .";
		}
		else{
			result += "?code2 <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?property.";
		}

		return result;
	}

	private String extendForManufacturerIdentification() {
		String sparql = "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ManufacturersItemIdentification> ?ManufacturerParty. ?ManufacturerParty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ID> ?hasValue.";
		sparql+= "?instance ?property  ?ManufacturerParty .";
		return sparql;
	}

	private String extendForManufacturerParty(String propertyURL) {
		String sparql = "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ManufacturerParty> ?ManufacturerParty. ?ManufacturerParty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?hasValue.";
		sparql+= "?instance ?property  ?ManufacturerParty .";
		return sparql;
	}

	private String extendForCustomString(String propertyName) {
		String sparql= "?instance"
				+ "  <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue.  ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?codeOfProperty. ?codeOfProperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#listID> ?value.";
		
		sparql += "Filter  (regex( ?property , \"" + propertyName + "\") && regex (?value, \"Custom\", \" i\")).";
		
		return sparql;
	}
	
	private String extendForCustomDecimal(String propertyName) {
		String sparql= "?instance"
				+ "  <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQuantity> ?valueQ. ?valueQ <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue.  ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?codeOfProperty. ?codeOfProperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#listID> ?value.";
		
		sparql += "Filter  (regex( ?property , \"" + propertyName + "\") && regex (?value, \"Custom\", \" i\")).";
		
		return sparql;
	}

	private String extendForDomainSpecificProperty(String propertyURL) {

		//this is the old query which uses the classificationCode to refer to an domain specific property
		//String sparql =  " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code2. ?code2 <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?value";
		String sparql =  " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#linkedResourceURI> ?value";
		sparql += " Filter  regex( ?value , \"" + propertyURL + "\").";
		return sparql;
	}

	private String extendForDimension(String propertyName) {
		String sparql= "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Dimension> ?dimension. ?dimension <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#AttributeID> ?property. ?dimension <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Measure> ?measure. ?measure <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue";
		sparql += " Filter  regex( ?property , \"" + propertyName + "\").";
		return sparql;
	}

	private String extendForCertificate() {
		return "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Certificate> ?hasValue .";
	}

	private String extendForDescriptionProperty() {
		return "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Description> ?hasValue .";

	}

	private String extendForNameProperty() {
		// TODO Auto-generated method stub
		return "?instance" + "<"+NimbleAdaptionServiceOfSearchResults.propertyURLForName+">  ?hasValue .";
	}

	private PropertySource detectPropertySource(String propertyURL, PropertySource propertySource2) {
		
		switch (propertySource2) {
		case DIMENSION:
			return PropertySource.DIMENSION;
			
		case CUSTOM_DECIMAL:
			return PropertySource.CUSTOM_DECIMAL;
			
		case CUSTOM_STRING:
			return PropertySource.CUSTOM_STRING;
			
		default:
			
		
		
		String propertyName = propertyURL.toLowerCase();
		int index = propertyName.indexOf("#");
		propertyName = propertyName.substring(index + 1);
		if (propertyName.contains("name")) {
			return PropertySource.NAME;
		}
		if (propertyName.contains("description")) {
			return PropertySource.DESCRIPTION;
		}
		
		if (propertyName.contains("certi")) {
			return PropertySource.CERTIFICATE;
		}
		
		if (propertyName.contains("manufacturerparty")) {
			return PropertySource.MANUFACTURER_PARTY;
		}
		
		if (propertyName.contains("manufacturersitemidentification" )){
			return PropertySource.MANUFACTURER_ITEMS_IDENTIFICATION;
		}

		if (nimbleSpecificSPARQLDeriviation.isItADomainSpecificPropertyWhichHasValues(propertyURL)){
			return PropertySource.DOMAIN_SPECIFIC_PROPERTY;
		}
		}
		return PropertySource.ADDITIONAL_ITEM_PROPERTY;
	}

	private List<String> createJenaSPARQLQuery(InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {
		// TODO Auto-generated method stub
		return null;
	}

	public String extractNameOfURL(String param) {
		if (param.contains("#")) {
			param = param.substring(param.indexOf("#") + 1);
		}
		return param;
	}
}
