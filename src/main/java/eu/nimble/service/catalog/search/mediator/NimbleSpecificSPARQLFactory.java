package eu.nimble.service.catalog.search.mediator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	public NimbleSpecificSPARQLFactory(MediatorSPARQLDerivationAndExecution sparqlDerivation,
			SQPDerivationService sqpDerivationService) {
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

	public String createSPARQLForFinalValue(String uuidOfProperty, String propertyname, IReader reader) {

		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?hasValue ?property where {";

		PropertySource source = detectSourceOnBasisOfName(propertyname);

		switch (source) {
		case DIMENSION:
			sparql += extendSPARQLForDimensionToGivenInstance(uuidOfProperty);
			break;
		case CERTIFICATE:
			sparql += extendSPARQLForCertificateToGivenInstance(uuidOfProperty);
			break;
		case ADDITIONAL_ITEM_PROPERTY:
			sparql += extendSPARQLForDomainAndCustomPropertiesToGivenInstance(uuidOfProperty, reader);
			break;

		case MANUFACTURER_PARTY:
			sparql += extendSPARQLForManufacturerPartyToGivenInstance(uuidOfProperty);
			break;

		case MANUFACTURER_ITEMS_IDENTIFICATION:
			sparql += extendSPARQLForManufacturersItemIdentificationToGiven(uuidOfProperty);
			break;
			
		case CATALOGUE_DOCUMENT_REFERENCE:
			sparql +=extendSPARQLForCatalogueDocumentReferenceToGiven(uuidOfProperty);
			break;
			
		default:
			Logger.getAnonymousLogger().log(Level.WARNING, "Found no specific property mapping. ");
			
			return null;
		}

		sparql += "}";
		return sparql;
	}

	private String extendSPARQLForCatalogueDocumentReferenceToGiven(String uuidOfProperty) {
		String sparql = "<" + uuidOfProperty + ">"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ID> ?hasValue.";
		return sparql;
	}

	private String extendSPARQLForManufacturersItemIdentificationToGiven(String uuidOfProperty) {
		String sparql = "<" + uuidOfProperty + ">"
				+
		"<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ID> ?hasValue.";
		return sparql;
	}

	private String extendSPARQLForManufacturerPartyToGivenInstance(String uuidOfProperty) {
		String sparql = "<" + uuidOfProperty + ">"
				+ " <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?hasValue.";
		return sparql;
	}

	private String extendSPARQLForDomainAndCustomPropertiesToGivenInstance(String uuidOfProperty, IReader reader) {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?hasValue ?property ?quality where {";
		sparql += "<" + uuidOfProperty + ">"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property.  <"
				+ uuidOfProperty
				+ "> <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQualifier> ?quality.";
		sparql += "}";
		Object result = reader.query(sparql);
		List<String> quantifier = reader.createResultList(result, "quality");
		if (!quantifier.isEmpty()) {
			String type = quantifier.get(0);
			sparql = "<" + uuidOfProperty + ">"
					+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property.";

			if (type.contains("REAL_MEASURE")) {
				sparql += "<" + uuidOfProperty + ">"
						+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue";
			}

			if (type.contains("QUANTITY")) {
				sparql += "<" + uuidOfProperty + ">"
						+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQuantity> ?qType. ?qType  <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue.";
			}
			if (type.contains("STRING")) {
				sparql += "<" + uuidOfProperty + ">"
						+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue.";
			}

		}
		return sparql;
	}

	private String extendSPARQLForCertificateToGivenInstance(String uuidOfProperty) {
		return "<" + uuidOfProperty + ">"
				+ " <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#CertificateType> ?hasValue";
	}

	private String extendSPARQLForDimensionToGivenInstance(String uuidOfProperty) {

		return "<" + uuidOfProperty + ">"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#AttributeID> ?property. " + "<"
				+ uuidOfProperty + ">"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Measure> ?measure. ?measure <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue";
	}

	private PropertySource detectSourceOnBasisOfName(String propertyname) {

		if (propertyname.contains("Additional")) {
			return PropertySource.ADDITIONAL_ITEM_PROPERTY;
		}
		PropertySource propertySource = detectSubsetofPotentialPropertySource(propertyname);
		if (propertySource != PropertySource.UNKNOWN) {
			return propertySource;
		}

		return PropertySource.UNKNOWN;
	}

	private List<String> createMarmottaSPARQLQuery(InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {

		nimbleSpecificSPARQLDeriviation = new NimbleSpecificSPARQLDeriviationAndExecution((MarmottaReader) reader,
				sqpDerivationService, sparqlDerivation);

		List<String> result = new ArrayList<String>();
		int counter = 0;
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";

			sparql += " ?property";
			sparql += " ?hasValue";

			// Define it is a ItemType
			sparql += " where{";
			sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";

			String urlOfconcept = inputParamaterForExecuteSelect.getConcept();
			if (sparqlDerivation != null) {
				urlOfconcept = sparqlDerivation.getURIOfConcept(inputParamaterForExecuteSelect.getConcept());
			}


			sparql += addPropertyForMarmotta(inputParamaterForExecuteSelect.getParametersURL().get(counter),
					inputParamaterForExecuteSelect.getPropertySources().get(counter));


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
			return result;

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

			result += extendForManufacturerIdentification();
			return result;
		case MANUFACTURER_PARTY:
			result += extendForManufacturerParty(propertyURL);
			return result;


		case CATALOGUE_DOCUMENT_REFERENCE:
			result += extendForCatalogueDocuemntReference(propertyURL);
			return result;	
			
		case CUSTOM_STRING:
			result += extendForCustomString(propertyURL);
			return result;
		case CUSTOM_DECIMAL:
			result += extendForCustomDecimal(propertyURL);
			return result;
		default:
			Logger.getAnonymousLogger().log(Level.WARNING, "Found no specific property mapping, all properties will be requested");
			break;



		}

		if (propertySource != PropertySource.DOMAIN_SPECIFIC_PROPERTY) {
			result += "?instance ?property  ?hasValue .";
		} else {
			result += "?code2 <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?property.";
		}

		return result;
	}

	private String extendForCatalogueDocuemntReference(String propertyURL) {
		String sparql = "?instance  <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CatalogueDocumentReference> ?documentReference. ?documentReference <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ID> ?hasValue.";
		sparql += "?instance ?property  ?documentReference .";
		return sparql;
	}

	private String extendForManufacturerIdentification() {
		String sparql = "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ManufacturersItemIdentification> ?ManufacturerParty. ?ManufacturerParty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ID> ?hasValue.";

		sparql += "?instance ?property  ?ManufacturerParty .";

		return sparql;
	}

	private String extendForManufacturerParty(String propertyURL) {
		String sparql = "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ManufacturerParty> ?ManufacturerParty. ?ManufacturerParty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?hasValue.";

		sparql += "?instance ?property  ?ManufacturerParty .";

		return sparql;
	}

	private String extendForCustomString(String propertyName) {

		String sparql = "?instance"
				+ "  <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue.  ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?codeOfProperty. ?codeOfProperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#listID> ?value.";

		sparql += "Filter  (regex( ?property , \"" + propertyName + "\") && regex (?value, \"Custom\", \" i\")).";

		return sparql;
	}

	private String extendForCustomDecimal(String propertyName) {
		String sparql = "?instance"
				+ "  <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?property. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQuantity> ?valueQ. ?valueQ <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue.  ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?codeOfProperty. ?codeOfProperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#listID> ?value.";

		sparql += "Filter  (regex( ?property , \"" + propertyName + "\") && regex (?value, \"Custom\", \" i\")).";


		return sparql;
	}

	private String extendForDomainSpecificProperty(String propertyURL) {

		String sparql ="";
		String sparqlForValueQuanitity= "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance  ?property  where{ ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?property. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQuantity> ?hasValue. Filter  regex( ?property , \"" + propertyURL + "\").}";
		Object result = reader.query(sparqlForValueQuanitity);
		List<String> content = reader.createResultList(result, "instance");
		
		//if the value is represented as DECIMAL
		if (content.size() > 0){
			 sparql = " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?property. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQuantity> ?qt. ?qt <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue.";
			sparql += " Filter  regex( ?property , \"" + propertyURL + "\").";
			
		}
		else{
			//if the value is represented as Value
		sparql = " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?property. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue.";
		sparql += " Filter  regex( ?property , \"" + propertyURL + "\").";
		}
		return sparql;
	}

	private String extendForDimension(String propertyName) {

		String sparql = "?instance"

				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Dimension> ?dimension. ?dimension <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#AttributeID> ?property. ?dimension <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Measure> ?measure. ?measure <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueDecimal> ?hasValue";
		sparql += " Filter  regex( ?property , \"" + propertyName + "\").";
		return sparql;
	}

	private String extendForCertificate() {
		return "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Certificate> ?cert. ?cert <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#CertificateType> ?hasValue .";
	}

	private String extendForDescriptionProperty() {
		return "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Description> ?hasValue .";

	}

	private String extendForNameProperty() {
		// TODO Auto-generated method stub
		return "?instance" + "<" + NimbleAdaptionServiceOfSearchResults.propertyURLForName + ">  ?hasValue .";
	}

	private PropertySource detectPropertySource(String propertyURL, PropertySource propertySource2) {


		switch (propertySource2) {
		case DIMENSION:
			return PropertySource.DIMENSION;

		case CUSTOM_DECIMAL:
			return PropertySource.CUSTOM_DECIMAL;

		case CUSTOM_STRING:
			return PropertySource.CUSTOM_STRING;

		case DOMAIN_SPECIFIC_PROPERTY:
			return PropertySource.DOMAIN_SPECIFIC_PROPERTY;

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

			PropertySource propertySource = detectSubsetofPotentialPropertySource(propertyName);
			if (propertySource != PropertySource.UNKNOWN) {
				return propertySource;
			}

			if (nimbleSpecificSPARQLDeriviation.isItADomainSpecificPropertyWhichHasValues(propertyURL)) {
				return PropertySource.DOMAIN_SPECIFIC_PROPERTY;
			}

		}
		return PropertySource.ADDITIONAL_ITEM_PROPERTY;
	}

	public PropertySource detectSubsetofPotentialPropertySource(String propertyName) {
		propertyName = propertyName.toLowerCase();
		if (propertyName.contains("certi")) {
			return PropertySource.CERTIFICATE;
		}


		if (propertyName.contains("dimension")) {
			return PropertySource.DIMENSION;
		}

		if (propertyName.contains("manufacturerparty")) {
			return PropertySource.MANUFACTURER_PARTY;

		}
		
		if (propertyName.contains("manufacturerparty")) {
			return PropertySource.MANUFACTURER_PARTY;
		}
		
		if (propertyName.contains("manufacturersitemidentification" )){
			return PropertySource.MANUFACTURER_ITEMS_IDENTIFICATION;
		}

		if (propertyName.contains("manufacturersitemidentification")) {
			return PropertySource.MANUFACTURER_ITEMS_IDENTIFICATION;
		}

		if (propertyName.contains("cataloguedocumentreference")){
			return PropertySource.CATALOGUE_DOCUMENT_REFERENCE;
		}

		
		return PropertySource.UNKNOWN;
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
