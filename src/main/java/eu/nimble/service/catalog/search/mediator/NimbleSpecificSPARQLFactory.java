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

			sparql += addPropertyForMarmotta(inputParamaterForExecuteSelect.getParametersURL().get(counter));

			sparql += "Filter  regex( ?codeValue , \"" + urlOfconcept + "\").";
			sparql += "}";
			result.add(sparql);
			counter++;
		}
		return result;
	}

	private String addPropertyForMarmotta(String propertyURL) {

		String result = "";

		PropertySource propertySource = detectPropertySource(propertyURL);
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
			result += extendForDimension();
			break;
		case MANUFACTURER_ITEMS_IDENTIFICATION:
			break;
		case MANUFACTURER_PARTY:
			break;
		}

		
		if (propertySource != PropertySource.DOMAIN_SPECIFIC_PROPERTY){
		result += "?instance ?property  ?hasValue .";
		}
		else{
			result += "?code2 <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?property.";
		}

		return result;
	}

	private String extendForDomainSpecificProperty(String propertyURL) {

		//this is the old query which uses the classificationCode to refer to an domain specific property
		//String sparql =  " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?hasValue. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code2. ?code2 <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?value";
		String sparql =  " ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#linkedResourceURI> ?value";
		sparql += " Filter  regex( ?value , \"" + propertyURL + "\").";
		return sparql;
	}

	private String extendForDimension() {
		return "?instance"
				+ "<urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Dimension> ?hasValue .";
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

	private PropertySource detectPropertySource(String propertyURL) {
		// TODO Auto-generated method stub
		String propertyName = propertyURL.toLowerCase();
		int index = propertyName.indexOf("#");
		propertyName = propertyName.substring(index + 1);
		if (propertyName.contains("name")) {
			return PropertySource.NAME;
		}
		if (propertyName.contains("description")) {
			return PropertySource.DESCRIPTION;
		}
		if (propertyName.contains("dimension")) {
			return PropertySource.DIMENSION;
		}
		if (propertyName.contains("certi")) {
			return PropertySource.CERTIFICATE;
		}

		if (nimbleSpecificSPARQLDeriviation.isItADomainSpecificPropertyWhichHasValues(propertyURL)){
			return PropertySource.DOMAIN_SPECIFIC_PROPERTY;
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
