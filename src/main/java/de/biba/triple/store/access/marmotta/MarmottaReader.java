package de.biba.triple.store.access.marmotta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.marmotta.client.ClientConfiguration;
import org.apache.marmotta.client.MarmottaClient;
import org.apache.marmotta.client.clients.SPARQLClient;
import org.apache.marmotta.client.exception.MarmottaClientException;
import org.apache.marmotta.client.model.rdf.Literal;
import org.apache.marmotta.client.model.rdf.RDFNode;
import org.apache.marmotta.client.model.rdf.URI;
import org.apache.marmotta.client.model.sparql.SPARQLResult;

import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.jena.Reader;

public class MarmottaReader extends Reader implements IReader {

	private ClientConfiguration config = null;
	private MarmottaClient client = null;
	private String marmottaUri = "http://134.168.33.237:8080/marmotta";
	private SPARQLClient sparqlClient = null;

	public MarmottaReader() {
		init(marmottaUri);
	}

	public MarmottaReader(String marmottaUri) {
		init(marmottaUri);
	}

	private void init(String uri) {
		marmottaUri = uri;
		if (uri == null) {
			config = new ClientConfiguration(marmottaUri);
		} else {
			config = new ClientConfiguration(uri);

		}
		
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);
        config.setConectionManager(cm);

		
		client = new MarmottaClient(config);
		sparqlClient = client.getSPARQLClient();

	}

	public SPARQLResult query(String sparql) {
		try {
			// sparql =
			// sparql.replace("http://www.aidimme.es/FurnitureSectorOntology.owl",
			// arg1)
			SPARQLResult result = sparqlClient.select(sparql);
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MarmottaClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String> createResultList(Object result, String propertyName) {
		SPARQLResult result2 = (SPARQLResult) result;
		List<String> allProperties = new ArrayList<String>();
		if (result2 != null) {
			for (int i = 0; i < result2.size(); i++) {

				RDFNode node = result2.get(i).get(propertyName);
				if (node instanceof URI || node instanceof Literal) {
					if (node instanceof URI){
						URI uri = (URI) node;
						allProperties.add(uri.toString());
					}
					else{
					Literal literal = (Literal) node;
					String language = literal.getLanguage();
					if (language != null){
						allProperties.add(node.toString() + "@" + language);
					}
					else{
						
						allProperties.add(node.toString());
					}
					}
				}
			}
		}
		return allProperties;
	}

	@Override
	public List<String[]> createResultListArray(Object result, String[] propertyNames) {

		for (int i = 0; i < propertyNames.length; i++) {
			if (propertyNames[i].contains("?")) {

				propertyNames[i] = propertyNames[i].replace("?", "");

			}
		}

		List<String[]> allProperties = new ArrayList<String[]>();
		if (result != null) {
			SPARQLResult result2 = (SPARQLResult) result;
			if (result2 != null) {
				for (int i = 0; i < result2.size(); i++) {
					List<String> line = new ArrayList<String>();
					for (String pn : propertyNames) {
						RDFNode node = result2.get(i).get(pn);
						if (node instanceof URI || node instanceof Literal) {
							line.add(node.toString());
						}
					}
					String[] row = new String[line.size()];
					line.toArray(row);
					allProperties.add(row);

				}
			}
		}
		return allProperties;
	}

	
	
	@Override
	public List<String> getAllPropertiesIncludingEverything(String urlOfClass) {

		if (!urlOfClass.contains("<")) {
			urlOfClass = "<" + urlOfClass + ">";
		}
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>			PREFIX owl: <http://www.w3.org/2002/07/owl#>				PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>				PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>				PREFIX mic: <http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#>				select distinct ?property ?type where {				  { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna> {				    ?property rdfs:range ?type .				    				    ?property (rdfs:domain/owl:unionOf/rdf:rest*/rdf:first | rdfs:domain) ?x .				  	filter isIri(?x) .				    {				        select ?parents where {				          { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna>				               { 				                    ?uri rdfs:subClassOf*/rdfs:subClassOf ?parents .				                    {				                        select ?uri where {				                            values ?uri { "
				+ urlOfClass
				+ " }				                        }				                    }				               }				                        				          } UNION {				          { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna>				               { 				                 select ?parents where {				                   values ?parents { "
				+ urlOfClass
				+ " }				                 }				               }				             }        				          }				        }				     }				       filter (?x in (?parents) ) .  				  }				}				}   order by ?property";

		Object result = query(sparql);
		Logger.getAnonymousLogger().log(Level.INFO, sparql);
		List<String> allProperties = createResultList(result, "property");
		return allProperties;
	}
	
	
	@Override
	public List<String> getAllPropertiesIncludingEverythingWhichHasValues(String urlOfClass) {

		if (!urlOfClass.contains("<")) {
			urlOfClass = "<" + urlOfClass + ">";
		}
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>			PREFIX owl: <http://www.w3.org/2002/07/owl#>				PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>				PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>				PREFIX mic: <http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#>				select distinct ?property ?type where {				  { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna> {				    ?property rdfs:range ?type .				    				    ?property (rdfs:domain/owl:unionOf/rdf:rest*/rdf:first | rdfs:domain) ?x .				  	filter isIri(?x) .				    {				        select ?parents where {				          { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna>				               { 				                    ?uri rdfs:subClassOf*/rdfs:subClassOf ?parents .				                    {				                        select ?uri where {				                            values ?uri { "
				+ urlOfClass
				+ " }				                        }				                    }				               }				                        				          } UNION {				          { GRAPH <http://134.168.33.237:8080/marmotta/context/micuna>				               { 				                 select ?parents where {				                   values ?parents { "
				+ urlOfClass
				+ " }				                 }				               }				             }        				          }				        }				     }				       filter (?x in (?parents) ) .  				  }				}				}   order by ?property";

		Object result = query(sparql);
		Logger.getAnonymousLogger().log(Level.INFO, sparql);
		List<String> allProperties = createResultList(result, "property");
		return allProperties;
	}


	@Override
	public Map<String, String> getPropertyValuesOfAIndividium(String urlOfIndividuum) {
		int counter =0;
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT ?predicate ?object WHERE {  <"
				+ urlOfIndividuum + ">"
				+ "?predicate ?object. FILTER (?predicate != <http://www.w3.org/2000/01/rdf-schema#type>)}";
		Logger.getAnonymousLogger().log(Level.INFO, "Request: " + sparql);
		Object result = query(sparql);
		Map<String, String> allProperties = new HashMap<String, String>();
		if (result != null && result instanceof SPARQLResult) {
			SPARQLResult result2 = (SPARQLResult) result;
			if (result2 != null) {
				for (int i = 0; i < result2.size(); i++) {

					RDFNode nodePredicate = result2.get(i).get("predicate");
					RDFNode ndoeObject = result2.get(i).get("object");
					if (ndoeObject instanceof URI || ndoeObject instanceof Literal) {
						String predicate = nodePredicate.toString();
						String value = ndoeObject.toString();
						if (!((predicate.contains("rdfs:type"))
								|| (predicate.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")))) {
							if (allProperties.containsKey(predicate)){
							counter++;	
							allProperties.put(predicate+counter, value);
							}
							else{
							allProperties.put(predicate, value);
							}
						}
					}
				}
			}
		}
		return allProperties;
	}

	@Override
	public Object queryRemoteHelper(String sparqlStr) {
		try {
			SPARQLResult results = sparqlClient.select(sparqlStr);
			return results; // Passes the result set out of the try-resources
		} catch (Exception e) {
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getLocalizedMessage());
			System.out.println("Exception: " + e.getMessage());
		}
		return null;
	}

	public static void main(String[] arvs) {
		MarmottaReader marmottaReader = new MarmottaReader();
		SPARQLResult result = marmottaReader.query("SELECT * WHERE { ?subject		 ?property ?object}LIMIT 10");
		// System.out.println(marmottaReader.getAllConcepts());
		// http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair
		System.out.println(marmottaReader
				.getAllPropertiesIncludingEverything("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair"));
		System.out.println("#################################");
		System.out.println(marmottaReader.getAllPropertiesIncludingEverything(
				"http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));

	}

	
	@Override
	public List<Entity> getAllConceptsFocusOnlyOnURI(String searchTerm) {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT distinct ?subject ?codeValue WHERE {  ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://www.w3.org/2002/07/owl#Class>. ";
		
		sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";
		
		String filter = " FILTER (regex( str(";
		filter += "?subject" + ")," + "\"" + searchTerm + "\",\"i\") && regex (str(?codeValue),"+  "\"" + searchTerm + "\",\"i\")).";
		sparql += filter + "}";
		System.out.println(sparql);
		Object result = query(sparql);
		List<String> allProperties = createResultList(result, "codeValue");
		List<Entity> resultOfSearchTerm = new ArrayList<Entity>();
		for (String concept : allProperties) {

			Entity entity = new Entity();
			entity.setUrl(concept);
			String value = concept.substring(concept.indexOf("#") + 1);
			entity.setTranslatedURL(value);
			entity.setLanguage(Language.UNKNOWN);
			resultOfSearchTerm.add(entity);
		}
		return resultOfSearchTerm;
	}
	
	/**
	 * 
	 */
	@Override
	public List<Entity> getAllConceptsLanguageSpecific(String searchTerm, Language language) {
		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#>PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> SELECT distinct ?subject  ?translation WHERE {  ?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type><http://www.nimble-project.org/onto/eclass#CodeConcept>. ";
		sparql += "?subject <"+getLanguageLabel()+"> ?translation. "; 
		sparql += "?instance <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#ItemType>. ?instance <urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2#CommodityClassification> ?type. ?type <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#ItemClassificationCode> ?code. ?code <urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#URI> ?codeValue.";
		
		String filter = " FILTER (regex( str(";
		filter += "?translation" + ")," + "\"" + searchTerm + "\",\"i\") && regex (str(?codeValue),"+  "str(?subject)" + ")).";
		sparql += filter + "}";
		System.out.println(sparql);
		Object result = query(sparql);
		List<String[]> allProperties = createResultListArray(result, new String[]{"subject", "translation"});
		List<Entity> resultOfSearchTerm = new ArrayList<Entity>();
		for (String[] element : allProperties) {

			Entity entity = new Entity();
			entity.setUrl(element[0]);
			String value = element[1];
			entity.setTranslatedURL(value);
			entity.setLanguage(Language.UNKNOWN);
			resultOfSearchTerm.add(entity);
		}
		return resultOfSearchTerm;
	}
	
}
