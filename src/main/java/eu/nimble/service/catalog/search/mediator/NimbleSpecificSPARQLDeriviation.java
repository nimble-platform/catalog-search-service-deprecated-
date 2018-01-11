package eu.nimble.service.catalog.search.mediator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.enums.PropertyType;
import de.biba.triple.store.access.jena.Reader;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromGreenGroup;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForPropertyValuesFromOrangeGroup;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPConfiguration;
import eu.nimble.service.catalog.search.impl.dao.sqp.SQPMapping;
import eu.nimble.service.catalog.search.services.SQPDerivationService;

public class NimbleSpecificSPARQLDeriviation {

	private IReader reader = null;
	private SQPDerivationService sqpDerivationService = null;

	public NimbleSpecificSPARQLDeriviation(IReader marmottaReader, SQPDerivationService sqpDerivationService) {
		super();
		this.reader = marmottaReader;
		this.sqpDerivationService = sqpDerivationService;
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

	public OutputForPropertyValuesFromOrangeGroup getPropertyValuesForOrangeGroup(String command, String concept) {
		OutputForPropertyValuesFromOrangeGroup propertyValuesFromOrangeGroup = new OutputForPropertyValuesFromOrangeGroup();
		String sparql = createSPARQLForAOrangeCommand(command, concept);
		if (sparql != null) {
			Logger.getAnonymousLogger().log(Level.INFO, "Request: " + sparql + " by: " + reader.getClass().getName());
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
			if (!value.contains("<")){
				value = "<" + value + ">";
			}
			//A individual UUID cannot have a space inside
			if (value.contains(" ")){
				return null;
			}
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>SELECT  ?subject ?predicate ?object WHERE { "
					+ value + " rdf:type ?object.}	";
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

}
