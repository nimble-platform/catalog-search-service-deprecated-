package eu.nimble.service.catalog.search.mediator;

import java.util.List;

import de.biba.triple.store.access.marmotta.MarmottaReader;

public class NimbleSpecificSPARQLDeriviation {

	private MarmottaReader marmottaReader = null;

	public NimbleSpecificSPARQLDeriviation(MarmottaReader marmottaReader) {
		super();
		this.marmottaReader = marmottaReader;
	}
	
	
	public boolean isItADomainSpecificPropertyWhichHasValues(String propertyURL){
		
		String sparql = createSPARQLForAllDomainSpecificProperties();
		if (marmottaReader != null){
		Object result = marmottaReader.query(sparql);
		List<String> allproperties = marmottaReader.createResultList(result, "value");
		
		return allproperties.contains(propertyURL);
		}
		if (propertyURL.contains("Varnish")){
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
	
	

	
}
