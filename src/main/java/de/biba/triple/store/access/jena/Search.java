package de.biba.triple.store.access.jena;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;

import de.biba.triple.store.access.ConfigurationService;
import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Filter;
import de.biba.triple.store.access.dmo.IndividualInformation;
import de.biba.triple.store.access.dmo.ObjectPropertyToDatatypePropertyMapping;
import de.biba.triple.store.access.dmo.PropertyInformation;

public class Search extends Reader {

	String dataSetURL = "";
	String urlForQueries = dataSetURL + "/query";

	public Search(){
		dataSetURL = ConfigurationService.getInstance().getDataSetUrl();
		urlForQueries = dataSetURL + "/query";
	}
	
	/**
	 * Search all individual which satisfy the searchTerms for at least one
	 * serachProeprty and given concept( join for subclasses)
	 * 
	 * @param searchTerm
	 *            list of keywords (AND)
	 * @param searchProperties
	 *            list of properties (UNION)
	 * @param searchConcept
	 * @param objectproperties
	 *            mapping between uid and corresponding data property to be
	 *            replaced
	 * @return a map
	 */
	public Map<IndividualInformation, Map<String, PropertyInformation>> searchAndReturnWholeIndividuals(
			List<String> searchTerm, List<String> searchProperties, String searchConcept,
			List<ObjectPropertyToDatatypePropertyMapping> objectproperties) {
		List<String[]> indis = search(searchTerm, searchProperties, searchConcept);
		Map<IndividualInformation, Map<String, PropertyInformation>> result = new HashMap<IndividualInformation, Map<String, PropertyInformation>>();
		for (String[] indi : indis) {
			IndividualInformation individualInformation = new IndividualInformation();
			individualInformation.setUrlOfClass(indi[1]);
			individualInformation.setUrlOfIndividual(indi[0]);
			result.put(individualInformation,
					getPropertyValuesOfAIndividiumIncludingType(indi[0], objectproperties, null));
		}
		return result;
	}

	/**
	 * Search all individual which satisfy the searchTerms for at least one
	 * serachProeprty and given concept( join for subclasses)
	 * 
	 * @param searchTerm
	 *            list of keywords (AND)
	 * @param searchProperties
	 *            list of properties (UNION)
	 * @param searchConcept
	 * @param objectproperties
	 *            mapping between uid and corresponding data property to be
	 *            replaced
	 * @return a map
	 */
	public Map<IndividualInformation, Map<String, PropertyInformation>> searchAndReturnWholeIndividuals(
			List<String> searchTerm, List<String> searchProperties, String searchConcept,
			List<ObjectPropertyToDatatypePropertyMapping> objectproperties, List<Filter> filters) {
		List<String[]> indis = search(searchTerm, searchProperties, searchConcept);
		Map<IndividualInformation, Map<String, PropertyInformation>> result = new HashMap<IndividualInformation, Map<String, PropertyInformation>>();
		for (String[] indi : indis) {
			IndividualInformation individualInformation = new IndividualInformation();
			individualInformation.setUrlOfClass(indi[1]);
			individualInformation.setUrlOfIndividual(indi[0]);
			result.put(individualInformation,
					getPropertyValuesOfAIndividiumIncludingType(indi[0], objectproperties, filters));
		}
		return result;
	}

	public Map<String, List<String>> searchTransitiveAllValuesForPropertiesForAGivenConcept(List<String> searchTerm,
			List<String> searchProperties, String urlOfConcept) {

		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();
		List<String[]> allIndis = search(searchTerm, searchProperties, urlOfConcept);
		urlOfConcept = "<" + urlOfConcept + ">";
		for (String[] indi : allIndis) {
			String individuum = "<" + indi[0] + ">";
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain> ?x. ?x rdfs:subClassOf* "
					+ urlOfConcept + ". " + individuum + " ?property ?value.}";
			System.out.println(sparql);
			createResultForSearchingPropertyValues(allPropertyValues, sparql);
		}
		return allPropertyValues;
	}

	public Map<String, List<String>> searchTransitiveParantsAllValuesForPropertiesForAGivenConcept(
			List<String> searchTerm, List<String> searchProperties, String urlOfConcept) {

		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();
		List<String[]> allIndis = search(searchTerm, searchProperties, urlOfConcept);
		urlOfConcept = "<" + urlOfConcept + ">";
		for (String[] indi : allIndis) {
			String individuum = "<" + indi[0] + ">";
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain> ?x. "
					+ urlOfConcept + "  rdfs:subClassOf* ?x." + individuum + " ?property ?value.}";
			System.out.println(sparql);
			createResultForSearchingPropertyValues(allPropertyValues, sparql);
		}
		return allPropertyValues;
	}

	public Map<String, List<String>> searchDirectAllValuesForPropertiesForAGivenConcept(List<String> searchTerm,
			List<String> searchProperties, String urlOfConcept) {

		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();
		List<String[]> allIndis = search(searchTerm, searchProperties, urlOfConcept);
		urlOfConcept = "<" + urlOfConcept + ">";
		for (String[] indi : allIndis) {
			String individuum = "<" + indi[0] + ">";
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain> "
					+ urlOfConcept + ". " + individuum + " ?property ?value.}";
			System.out.println(sparql);
			createResultForSearchingPropertyValues(allPropertyValues, sparql);
		}
		return allPropertyValues;
	}

	/**
	 * This methods search all properties to a given concept (transitive) and
	 * its values. This methods has no restrictions
	 * 
	 * @param urlOfConcept
	 * @return Map (Key is propertyUUID, Value List of Values)
	 */
	public Map<String, List<String>> searchTransitiveAllValuesForPropertiesForAGivenConcept(String urlOfConcept) {
		urlOfConcept = "<" + urlOfConcept + ">";
		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain> ?x. ?x rdfs:subClassOf* "
				+ urlOfConcept + ". ?instance ?property ?value.}";
		return createResultForSearchingPropertyValues(allPropertyValues, sparql);
	}

	/**
	 * This methods search all properties to a given concept (transitive) and
	 * its values. This methods has no restrictions
	 * 
	 * @param urlOfConcept
	 * @return Map (Key is propertyUUID, Value List of Values)
	 */
	public Map<String, List<String>> searchTransitiveParentsAllValuesForPropertiesForAGivenConcept(
			String urlOfConcept) {
		IReader read = new Reader();
		List<String> allIndis = read.getAllTransitiveInstances(urlOfConcept);
		urlOfConcept = "<" + urlOfConcept + ">";
		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();

		for (String indi : allIndis) {
			String individuum = "<" + indi + ">";
			String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain> ?x. "
					+ urlOfConcept + "  rdfs:subClassOf* ?x." + individuum + " ?property ?value.}";
			System.out.println(sparql);
			createResultForSearchingPropertyValues(allPropertyValues, sparql);
		}
		return allPropertyValues;

	}

	/**
	 * This methods search all properties to a given concept (direct) and its
	 * values. This methods has no restrictions
	 * 
	 * @param urlOfConcept
	 * @return Map (Key is propertyUUID, Value List of Values)
	 */
	public Map<String, List<String>> searchDirectAllValuesForPropertiesForAGivenConcept(String urlOfConcept) {
		urlOfConcept = "<" + urlOfConcept + ">";
		Map<String, List<String>> allPropertyValues = new HashMap<String, List<String>>();
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?property ?value  where {   ?property <http://www.w3.org/2000/01/rdf-schema#domain>  "
				+ urlOfConcept + ". ?instance ?property ?value.}";
		return createResultForSearchingPropertyValues(allPropertyValues, sparql);
	}

	private Map<String, List<String>> createResultForSearchingPropertyValues(
			Map<String, List<String>> allPropertyValues, String sparql) {
		Object result = query(sparql);
		List<String[]> allValues = createResultListArray(result, new String[] { "property", "value" });
		for (String[] row : allValues) {
			String propertyName = row[0];
			if (allPropertyValues.containsKey(propertyName)) {
				allPropertyValues.get(propertyName).add(row[1]);
			} else {
				List<String> allValuesForProperty = new ArrayList<String>();
				allValuesForProperty.add(row[1]);
				removeTypeDescriptionInValues(allValuesForProperty);
				allPropertyValues.put(propertyName, allValuesForProperty);
			}
		}

		return allPropertyValues;
	}


	protected List<String[]> search(List<String> searchTerm, List<String> searchProperties, String searchConcept) {
		//
		// PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs:
		// <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd:
		// <http://www.w3.org/2001/XMLSchema#>SELECT distinct ?instance WHERE {
		// {?instance a ?x. ?x rdfs:subClassOf*
		// <http://biba.uni-bremen.de/InnovationGateway#Funding_Opportunity>.
		// ?instance ?a ?object
		// .?instance<http://biba.uni-bremen.de/InnovationGateway#Website>?value0.
		// FILTER regex(?value0,"www.h2020.eu"). } UNION {?instance a ?x. ?x
		// rdfs:subClassOf*
		// <http://biba.uni-bremen.de/InnovationGateway#Funding_Opportunity>.
		// ?instance ?a ?object.
		// ?instance<http://biba.uni-bremen.de/InnovationGateway#assetName>?value1.
		// FILTER regex(?value1,"20").} }

		searchConcept = "<" + searchConcept + ">";
		String sparql = "PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>SELECT distinct ?instance ?x WHERE { ";

		int counter = 0;
		if (searchProperties != null) {
			for (String property : searchProperties) {
				property = "<" + property + ">";
				sparql += "{?instance  a ?x. ?x rdfs:subClassOf* " + searchConcept + ". ?instance ?a ?object .";

				// Now the filter
				String filter = "";

				String variable = "?value" + counter;

				filter += "?instance" + property + "" + variable + ". ";
				if (searchTerm != null) {
					sparql = addAnfFilterForProperties(searchTerm, sparql, filter, variable);
				}
				if (counter < searchProperties.size() - 1) {
					sparql += " UNION";
				}
				counter++;
			}
		}

		sparql += "}";
		System.out.println(sparql);
		Object result = query(sparql);
		return

		createResultListArray(result, new String[] { "instance", "x" });
	}


	private String addAnfFilterForProperties(List<String> searchTerm, String sparql, String filter, String variable) {
		for (String str : searchTerm) {
			filter += " FILTER regex( lcase(";
			filter += variable + ")," + "lcase( \"" + str + "\")).";

		}
		sparql += filter + "}";
		return sparql;
	}

	private String createFilterForValues(List<String> filterValues, String urlPropeety) {
		String result = "";
		for (int i = 0; i < filterValues.size(); i++) {
			String value = filterValues.get(i);

			result += "regex( lcase(" + "<" + urlPropeety + ">" + ")," + "lcase( \"" + value + "\"))";

			if (i < filterValues.size() - 1) {
				result += " || ";
			}
		}

		return result;
	}

	private String extendFilters(String sparql, List<Filter> filters) {
		for (Filter filter : filters) {

			sparql += " FILTER (" + createFilterForValues(filter.getValues(), filter.getUrlOfProperty() + ").");

		}
		return sparql;

	}

	public String getUrlForQueries() {
		return urlForQueries;
	}
}
