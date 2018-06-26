package eu.nimble.service.catalog.search.mediator;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.ConceptSource;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import de.biba.triple.store.access.jena.Reader;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.CustomPropertyInformation;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertiesFromConcept;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyFromConcept;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromGreenGroup;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromOrangeGroup;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPConfiguration;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPMapping;
import eu.nimble.service.catalog.search.services.SQPDerivationService;

public class NimbleSpecificSPARQLDeriviationAndExecution {

	private static final String HTTP_WWW_W3_ORG_2004_02_SKOS_CORE_PREF_LABEL = "http://www.w3.org/2004/02/skos/core#prefLabel";
	private static final String URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_DIMENSION = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Dimension";
	private static final String URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_ADDITIONAL_ITEM_PROPERTY = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty";
	private static final String URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_COMMODITY_CLASSIFICATION = "urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification";
	private static final String HTTP_WWW_W3_ORG_1999_02_22_RDF_SYNTAX_NS_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	private IReader reader = null;
	private SQPDerivationService sqpDerivationService = null;
	private MediatorSPARQLDerivationAndExecution mediatorSPARQLDerivationAndExecution = null;

	public NimbleSpecificSPARQLDeriviationAndExecution(IReader marmottaReader,
			SQPDerivationService sqpDerivationService,
			MediatorSPARQLDerivationAndExecution mediatorSPARQLDerivationAndExecution) {
		super();
		this.reader = marmottaReader;
		this.sqpDerivationService = sqpDerivationService;
		this.mediatorSPARQLDerivationAndExecution = mediatorSPARQLDerivationAndExecution;
	}

	public boolean isItADomainSpecificPropertyWhichHasValues(String propertyURL) {

		String sparql = createSPARQLForAllDomainSpecificProperties();
		if (reader != null) {
			Object result = reader.query(sparql);
			List<String> allproperties = reader.createResultList(result, "value");

			return allproperties.contains(propertyURL);
		}
		if (propertyURL.contains("Varnish")) {
			return true;
		}

		return false;
	}

	public String createSPARQLForAllDomainSpecificProperties() {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";
		sparql += " ?value";
		// Define it is a ItemType
		sparql += " where{";
		sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?aproperty. ?aproperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Value> ?value";
		sparql += "}";
		return sparql;
	}

	private String createSPARQLForAOrangeCommand(String command, String concept) {

		SQPConfiguration sqpConfiguration = sqpDerivationService.getSQPConfiguration(concept, command);
		if (sqpConfiguration == null) {
			Logger.getAnonymousLogger().log(Level.WARNING, "Found no orangeCommand for: " + command);
			return null;
		}
		if (!concept.contains("<")) {
			concept = "<" + concept + ">";
		}
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";
		sparql += " ?value";
		// Define it is a ItemType
		sparql += " where{";
		sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " + concept + ".";
		SQPMapping sqpMapping = sqpConfiguration.getSQPMapping();
		String path = sqpMapping.getTarget().getTargetPathFromSource();
		String[] pathTokens = path.split(";");
		String lastToken = "?instance";
		String propertyName = "";
		for (String token : pathTokens) {
			propertyName = "?" + token.substring(token.indexOf("#") + 1);
			sparql += lastToken + " <" + token + "> " + propertyName + ". ";

			lastToken = propertyName;
		}
		sparql += propertyName + "<" + sqpMapping.getTarget().getTargetProperty() + ">" + "?value. ";
		sparql += "}";
		return sparql;
	}

	private String createSPARQLForAOrangeCommandWithMamrottaSpecificStructure(String command, String concept) {

		SQPConfiguration sqpConfiguration = sqpDerivationService.getSQPConfiguration(concept, command);
		if (sqpConfiguration == null) {
			Logger.getAnonymousLogger().log(Level.WARNING, "Found no orangeCommand for: " + command);
			return null;
		}
		if (concept.contains("<")) {
			concept = concept.replace("<", "");
			concept = concept.replace(">", "");
		}
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";
		sparql += " ?value";
		// Define it is a ItemType
		sparql += " where{";
		sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";
		SQPMapping sqpMapping = sqpConfiguration.getSQPMapping();
		String path = sqpMapping.getTarget().getTargetPathFromSource();
		String[] pathTokens = path.split(";");
		String lastToken = "?instance";
		String propertyName = "";
		for (String token : pathTokens) {
			propertyName = "?" + token.substring(token.indexOf("#") + 1);
			sparql += lastToken + " <" + token + "> " + propertyName + ". ";

			lastToken = propertyName;
		}
		sparql += propertyName + "<" + sqpMapping.getTarget().getTargetProperty() + ">" + "?value. ";
		sparql += "Filter  regex( ?codeValue , \"" + concept + "\").";
		sparql += "}";
		return sparql;
	}

	public OutputForPropertyValuesFromOrangeGroup getPropertyValuesForOrangeGroup(String command, String concept,
			boolean nimbleAndMarmottaSpecific) {
		OutputForPropertyValuesFromOrangeGroup propertyValuesFromOrangeGroup = new OutputForPropertyValuesFromOrangeGroup();
		String sparql = "";
		if (nimbleAndMarmottaSpecific) {
			sparql = createSPARQLForAOrangeCommandWithMamrottaSpecificStructure(command, concept);

		} else {

			sparql = createSPARQLForAOrangeCommand(command, concept);
		}
		if (sparql != null) {
			Logger.getAnonymousLogger().log(Level.INFO, "Request: " + sparql + " by: " + reader.getClass().getName());
			Logger.getAnonymousLogger().log(Level.INFO, sparql);
			Object result = reader.query(sparql);
			List<String> values = reader.createResultList(result, "value");
			propertyValuesFromOrangeGroup.getAllValues().addAll(values);
			String conceptOfTarget = getTypeOfValue(values);
			if (conceptOfTarget != null) {
				propertyValuesFromOrangeGroup.setBelongsToTheFollowingConcept(concept);
				propertyValuesFromOrangeGroup.setItAConcept(true);
				propertyValuesFromOrangeGroup.setITASimpleValue(false);
			} else {
				propertyValuesFromOrangeGroup.setItAConcept(false);
				propertyValuesFromOrangeGroup.setITASimpleValue(true);
			}
		}
		return propertyValuesFromOrangeGroup;
	}

	/**
	 * INtersection is not allowed here
	 * 
	 * @param values
	 * @return the concept if the input is a individual otherwise null
	 */
	private String getTypeOfValue(List<String> values) {
		if (values != null && values.size() > 0) {
			String value = values.get(0);
			if (!value.contains("<")) {
				value = "<" + value + ">";
			}
			// A individual UUID cannot have a space inside
			if (value.contains(" ")) {
				return null;
			}
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>SELECT  ?subject ?predicate ?object WHERE { "
					+ value + " rdf:type ?object.}	";
			Logger.getAnonymousLogger().log(Level.INFO, sparql);
			Object result = reader.query(sparql);
			List<String> allTypes = reader.createResultList(result, "object");
			if (allTypes != null && allTypes.size() > 0) {

				for (String type : allTypes) {
					if (!type.contains("NamedIndividual")) {
						return type;
					}
				}

			}
		}
		return null;
	}

	public void removeInternalConceptsToHideItForTheUser(List<Entity> concepts) {

		List<Entity> toBeRemoved = new ArrayList<Entity>();
		for (Entity entity : concepts) {

			if (entity.getUrl().contains("ItemType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("AmountType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("CatalogueType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("CertificateType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("ComodityClassificationType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("DeliveryTermsType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("DimensionType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("DocumentReferenceType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("GoodsItemType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("ItemIdentificationType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("ItemLocationQuantityType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("ItemLPropertyType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("PackageType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("PartyType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("PeriodType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("PriceType")) {
				toBeRemoved.add(entity);
			}
			if (entity.getUrl().contains("QuantityType")) {
				toBeRemoved.add(entity);
			}
		}

		for (Entity entity : toBeRemoved) {
			concepts.remove(entity);
		}
	}

	public List<String> getAdditionalPropertiesWhichAreDerivedFromAbox(String conceptURL) {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ";
		sparql += " ?property";
		sparql += " where{";
		sparql += "?instance ?property ?propertyValue. ?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";
		sparql += "Filter  regex( ?codeValue , \"" + conceptURL + "\").";
		sparql += "}";
		Logger.getAnonymousLogger().log(Level.INFO, sparql);
		Object result = reader.query(sparql);
		List<String> resultAsList = reader.createResultList(result, "property");
		removeNimbleSpecificInternalProperties(resultAsList);
		return resultAsList;

	}

	public List<String> getAllAvailableDimensionsWhichAreDerivedFromAbox(String conceptURL) {
		List<String> resultAsList = null;
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct  ";
		sparql += " ?value";
		sparql += " where{";

		sparql += "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#Dimension> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#AttributeID> ?value. ?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";

		sparql += "Filter  regex( ?codeValue , \"" + conceptURL + "\").";
		sparql += "}";
		Logger.getAnonymousLogger().log(Level.INFO, sparql);
		Object result = reader.query(sparql);
		resultAsList = reader.createResultList(result, "value");

		return resultAsList;
	}

	public List<CustomPropertyInformation> getAllAvailableCustomPropertiesWhichAreDerivedFromAbox(String conceptURL) {
		List<String[]> resultAsList = null;
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ";
		sparql += " ?value ?name ?typeDes";
		sparql += " where{";

		sparql += "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ValueQualifier> ?typeDes.?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?name. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?codeOfProperty. ?codeOfProperty <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#listID> ?value. ?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";
		sparql += "Filter  (regex( ?codeValue , \"" + conceptURL + "\") && regex (?value, \"Custom\", \", i\")).";
		sparql += "}";
		Logger.getAnonymousLogger().log(Level.INFO, sparql);
		Object result = reader.query(sparql);
		resultAsList = reader.createResultListArray(result, new String[] { "name", "typeDes" });

		List<CustomPropertyInformation> customPropertyInformations = new ArrayList<CustomPropertyInformation>();
		resultAsList.forEach(element -> {
			String name = element[0];
			String propertyDescription = element[1];
			CustomPropertyInformation customPropertyInformation = new CustomPropertyInformation();
			customPropertyInformation.setPropertyName(name);
			customPropertyInformation.setTypeDescription(propertyDescription);
			customPropertyInformations.add(customPropertyInformation);
		});

		return customPropertyInformations;
	}

	public void removeNimbleSpecificInternalProperties(List<String> resultAsList) {
		resultAsList.remove(HTTP_WWW_W3_ORG_1999_02_22_RDF_SYNTAX_NS_TYPE);
		resultAsList.remove(
				URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_COMMODITY_CLASSIFICATION);
		resultAsList.remove(
				URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_ADDITIONAL_ITEM_PROPERTY);
		resultAsList.remove(URN_OASIS_NAMES_SPECIFICATION_UBL_SCHEMA_XSD_COMMON_AGGREGATE_COMPONENTS_2_DIMENSION);
	}

	public List<String> getAllDifferentValuesForAProperty(String concept, String propertyURL,
			PropertySource propertySource) {

		NimbleSpecificSPARQLFactory factory = new NimbleSpecificSPARQLFactory(mediatorSPARQLDerivationAndExecution,
				sqpDerivationService);
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		String property = propertyURL.substring(propertyURL.indexOf("#") + 1);
		inputParamaterForExecuteSelect.getParameters().add(property);
		inputParamaterForExecuteSelect.getParametersURL().add(propertyURL);
		inputParamaterForExecuteSelect.getPropertySources().add(propertySource);
		List<String> sparqls = factory.createSparql(inputParamaterForExecuteSelect, reader,false,false);
		if (sparqls != null && sparqls.size() > 0) {
			String sparql = sparqls.get(0);
			Logger.getAnonymousLogger().log(Level.INFO, sparql);
			Object result = reader.query(sparql);
			List<String> values = reader.createResultList(result, "hasValue");
			List<String> valuesCleanedUP = new ArrayList<String>();
			for (String str: values){
				if (!valuesCleanedUP.contains(str)){
					valuesCleanedUP.add(str);
				}
			}
			
			return valuesCleanedUP;
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING,
					"Couldn't create sparql queries for: " + concept + " & " + propertyURL);
		}
		return Collections.EMPTY_LIST;
	}

	/**
	 * This method use the e-class to dtermine possible concepts
	 * 
	 * @param keyword
	 * @param translationLabel
	 * @param language
	 * @return
	 */

	public List<Entity> detectNimbleSpecificMeaningFromAKeyword(String keyword, String translationLabel,
			Language language) {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT ?subject ?value WHERE {  ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2004/02/skos/core#Concept>. ?subject <"
				+ translationLabel + "> ?value FILTER regex( str(?value),\"" + keyword + "\",\"i\").}";
		Object result = reader.query(sparql);
		String[] columns = new String[] { "subject", "value" };
		List<String[]> allConcepts = reader.createResultListArray(result, columns);
		List<Entity> resultOfSerachTerm = new ArrayList<Entity>();
		String languagepostfix = language.toOntologyPostfix(language);
		for (String[] row : allConcepts) {
			String value = row[1];
			if (value != null && value.length() > 1 && value.contains(languagepostfix)) {
				value = value.substring(0, value.indexOf(languagepostfix));

				Entity entity = new Entity();
				entity.setUrl(row[0]);
				entity.setTranslatedURL(value);
				entity.setConceptSource(ConceptSource.CUSTOM);
				entity.setLanguage(language);
				resultOfSerachTerm.add(entity);
			} else {
				if (!value.contains("@")) {

					Entity entity = new Entity();
					entity.setUrl(row[0]);
					entity.setTranslatedURL(value);
					entity.setConceptSource(ConceptSource.CUSTOM);
					entity.setLanguage(Language.UNKNOWN);
					resultOfSerachTerm.add(entity);
				}
			}
		}
		return resultOfSerachTerm;
	}

	/**
	 * This method use the e-class to dtermine possible concepts
	 * 
	 * @param keyword
	 * @param translationLabel
	 * @param language
	 * @param useSimplifiedSPARQL
	 * @return
	 */
	public List<Entity> detectNimbleSpecificMeaningFromAKeywordReferringToInstances(String keyword,
			String translationLabel, Language language, boolean useSimplifiedSPARQL) {
		// String sparql = "PREFIX rdf:
		// <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl:
		// <http://www.w3.org/2002/07/owl#>PREFIX rdfs:
		// <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd:
		// <http://www.w3.org/2001/XMLSchema#> SELECT ?subject ?value WHERE {
		// ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
		// <http://www.w3.org/2004/02/skos/core#Concept>. ?subject <"
		// + translationLabel
		// + "> ?value. ?instance
		// <http://www.w3.org/1999/02/22-rdf-syntax-ns#type>
		// <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>.
		// ?instance
		// <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification>
		// ?type. ?type
		// <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode>
		// ?code. ?code
		// <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI>
		// ?codeValue.FILTER regex( str(?value),\""
		// + keyword + "\",\"i\"). FILTER regex(str(?subject),
		// str(?codeValue))}";
		// Object result = reader.query(sparql);
		// String[] columns = new String[] { "subject", "value" };
		// List<String[]> allConcepts = reader.createResultListArray(result,
		// columns);
		//
		// List<Entity> resultOfSerachTerm = new ArrayList<Entity>();
		// String languagepostfix = language.toOntologyPostfix(language);
		// for (String[] row : allConcepts) {
		// String value = row[1];
		// if (value != null && value.length() > 1 &&
		// value.contains(languagepostfix)) {
		// value = value.substring(0, value.indexOf(languagepostfix));
		//
		// Entity entity = new Entity();
		// entity.setUrl(row[0]);
		// entity.setTranslatedURL(value);
		// entity.setConceptSource(ConceptSource.CUSTOM);
		// entity.setLanguage(language);
		// resultOfSerachTerm.add(entity);
		// } else {
		// if (!value.contains("@")) {
		//
		// Entity entity = new Entity();
		// entity.setUrl(row[0]);
		// entity.setTranslatedURL(value);
		// entity.setConceptSource(ConceptSource.CUSTOM);
		// entity.setLanguage(Language.UNKNOWN);
		// resultOfSerachTerm.add(entity);
		// }
		// }
		// }
		List<Entity> resultOfSerachTerm = new ArrayList<Entity>();
		requestBasedOnConceptsURI(resultOfSerachTerm, keyword);
		if (!useSimplifiedSPARQL) {
			requestBasedOnTranslationLabel(resultOfSerachTerm, keyword, language);
		} else {
			requestBasedOnTranslationLabelSimplfied(resultOfSerachTerm, keyword, language);
		}
		return resultOfSerachTerm;
	}
	
	
	private void requestBasedOnTranslationLabelSimplfied(List<Entity> allConcepts, String keyword, Language language) {
		MarmottaReader readerMarmotta = (MarmottaReader) reader;
		readerMarmotta.setLanguageLabel(HTTP_WWW_W3_ORG_2004_02_SKOS_CORE_PREF_LABEL);
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT distinct ?subject  ?translation WHERE {  ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://www.nimble-project.org/onto/eclass#CodeConcept>. ?subject <http://www.w3.org/2004/02/skos/core#prefLabel> ?translation.  ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue. FILTER (regex( str(?translation),\""+keyword+ "\",\"i\") && regex (str(?codeValue),str(?subject))).}";
		Logger.getAnonymousLogger().log(Level.INFO, query);
				
				Object result = readerMarmotta.query(query);
		List<String[]> allProperties = readerMarmotta.createResultListArray(result, new String[]{"subject", "translation"});
		List<Entity> resultOfSearchTerm = new ArrayList<Entity>();
		for (String[] element : allProperties) {

			Entity entity = new Entity();
			entity.setUrl(element[0]);
			String value = element[1];
			entity.setTranslatedURL(value);
			entity.setLanguage(Language.UNKNOWN);
			resultOfSearchTerm.add(entity);
		}		
				
		for (Entity entity : resultOfSearchTerm) {
			boolean contained = false;
			for (Entity e : allConcepts) {
				if (e.getUrl().equals(entity.getUrl())) {
					contained = true;
					break;
				}
			}
			if (!contained) {
				allConcepts.add(entity);
			}
		}

	}
	
	

	private void requestBasedOnTranslationLabel(List<Entity> allConcepts, String keyword, Language language) {
		MarmottaReader readerMarmotta = (MarmottaReader) reader;
		readerMarmotta.setLanguageLabel(HTTP_WWW_W3_ORG_2004_02_SKOS_CORE_PREF_LABEL);
		List<Entity> entities = readerMarmotta.getAllConceptsLanguageSpecific(keyword, language);
		for (Entity entity : entities) {
			boolean contained = false;
			for (Entity e : allConcepts) {
				if (e.getUrl().equals(entity.getUrl())) {
					contained = true;
					break;
				}
			}
			if (!contained) {
				allConcepts.add(entity);
			}
		}

	}

	private void requestBasedOnConceptsURI(List<Entity> allConcepts, String keyword) {
		MarmottaReader readerMarmotta = (MarmottaReader) reader;
		List<Entity> entities = readerMarmotta.getAllConceptsFocusOnlyOnURI(keyword);
		for (Entity entity : entities) {
			boolean contained = false;
			for (Entity e : allConcepts) {
				if (e.getUrl().equals(entity.getUrl())) {
					contained = true;
					break;
				}
			}
			if (!contained) {
				allConcepts.add(entity);
			}
		}

	}

	public List<String> getAllAvailableEClassOrDomainPropertiesFromAbox(String eclassOrconceptURL) {
		List<String> resultList = null;

		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";
		sparql += " ?value ?name";
		sparql += " where{";
		sparql += "?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#AdditionalItemProperty> ?propertyValue. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name> ?name. ?propertyValue <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI>  ?value. ?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";

		sparql += "Filter  (regex( ?codeValue , \"" + eclassOrconceptURL + "\") ).";
		sparql += "}";
		Object result = reader.query(sparql);
		resultList = reader.createResultList(result, "value");

		return resultList;
	}

	private OutputForPropertyFromConcept createDefaultOutputForPropertyFromConcept(String urlOfProperty,
			OutputForPropertiesFromConcept result) {

		OutputForPropertyFromConcept outputForPropertyFromConcept = new OutputForPropertyFromConcept();
		outputForPropertyFromConcept.setPropertyURL(urlOfProperty);
		outputForPropertyFromConcept.setDatatypeProperty(true);
		outputForPropertyFromConcept.setObjectProperty(false);
		if ((urlOfProperty.contains("http:") || (urlOfProperty.contains("https")))){
		PropertyType propertyType = reader.getPropertyType(urlOfProperty);

		if (propertyType == PropertyType.OBJECTPROPERTY) {

			outputForPropertyFromConcept.setDatatypeProperty(false);
			outputForPropertyFromConcept.setObjectProperty(true);
		}
		}
		else{
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot determine type{datatype, object property} of property:  " + urlOfProperty);
		}
		result.getOutputForPropertiesFromConcept().add(outputForPropertyFromConcept);

		return outputForPropertyFromConcept;
	}

	public OutputForPropertiesFromConcept getAllPropertiesIncludingEverything(String conceptURL) {
		OutputForPropertiesFromConcept result = new OutputForPropertiesFromConcept();

		List<String> allAdditionalProperties = getAdditionalPropertiesWhichAreDerivedFromAbox(conceptURL);
		allAdditionalProperties.forEach(str -> {
			OutputForPropertyFromConcept outputForPropertyFromConcept = createDefaultOutputForPropertyFromConcept(str,
					result);
			outputForPropertyFromConcept.setPropertySource(PropertySource.DIRECT_PROPERTIES);
		});

		allAdditionalProperties = getAllAvailableDimensionsWhichAreDerivedFromAbox(conceptURL);
		allAdditionalProperties.forEach(str -> {
			OutputForPropertyFromConcept outputForPropertyFromConcept = createDefaultOutputForPropertyFromConcept(str,
					result);
			outputForPropertyFromConcept.setPropertySource(PropertySource.DIMENSION);
		});

		List<CustomPropertyInformation> customPropertyInformations = getAllAvailableCustomPropertiesWhichAreDerivedFromAbox(
				conceptURL);
		customPropertyInformations.forEach(element -> {
			OutputForPropertyFromConcept outputForPropertyFromConcept = createDefaultOutputForPropertyFromConcept(
					element.getPropertyName(), result);
			if (element.getTypeDescription().toLowerCase().contains("string")) {
				outputForPropertyFromConcept.setPropertySource(PropertySource.CUSTOM_STRING);
			} else {
				outputForPropertyFromConcept.setPropertySource(PropertySource.CUSTOM_DECIMAL);
			}
		});

		allAdditionalProperties = getAllAvailableEClassOrDomainPropertiesFromAbox(conceptURL);
		allAdditionalProperties.forEach(str -> {
			OutputForPropertyFromConcept outputForPropertyFromConcept = createDefaultOutputForPropertyFromConcept(str,
					result);
			outputForPropertyFromConcept.setPropertySource(PropertySource.DOMAIN_SPECIFIC_PROPERTY);
		});

		
		
		return result;
	}

	public Map<String, String> resolveOPtionalSPARQLAndExecuteITToFinalPropertyValues(Map<String, String> result,
			String uuid) {
		// ODO Auto-generated method stub
		NimbleSpecificSPARQLFactory factory = new NimbleSpecificSPARQLFactory(mediatorSPARQLDerivationAndExecution,
				sqpDerivationService);
		Map<String, String> resultFinal = new HashMap<String, String>();
		result.keySet().forEach(key -> {
			String sparql = factory.createSPARQLForFinalValue(result.get(key), key, reader);
			if (sparql != null) {
				Object r = reader.query(sparql);
				List<String> properties = reader.createResultList(r, "property");
				List<String> values = reader.createResultList(r, "hasValue");
				String name = key;
				String value = "";
				if (!properties.isEmpty()) {
					name = properties.get(0);
				}
				if (!values.isEmpty()) {
					value = values.get(0);
				}
				resultFinal.put(name, value);
			} else {
				if (!key.contains("CommodityClassification")) {
					resultFinal.put(key, result.get(key));
				}
			}
		});

		return resultFinal;

	}

}
