package de.biba.triple.store.access.jena;


import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import de.biba.triple.store.access.AbstractQueryExecutor;
import de.biba.triple.store.access.ConfigurationService;
import de.biba.triple.store.access.IPropertyValuesCrawler;

public class PropertyValuesCrawler extends AbstractQueryExecutor implements IPropertyValuesCrawler {

	String urlForModelRetrival = dataSetURL + "/data";
	Model retrivedModel = null;

	public PropertyValuesCrawler() {
		super(ConfigurationService.getInstance().getDataSetUrl(),null);
	}
	
	public PropertyValuesCrawler(String dataSetURL) {
		super(dataSetURL, null);
		this.dataSetURL = dataSetURL;
		urlForQueries = dataSetURL + "/query";
		urlForModelRetrival = dataSetURL + "/data";

	}

	/* (non-Javadoc)
	 * @see de.biba.triple.store.access.jena.IPropertyValuesCrawler#getAllDifferentValuesForAProperty(java.lang.String)
	 */
	@Override
	public List<String> getAllDifferentValuesForAProperty(String propertyURL) {
		
		List<String> result = new ArrayList<String>();
		String sparQL = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct  ?value  where { ?instance <" +propertyURL+"> ?value.}";
		
		Object resultSet = query(sparQL);
		
		result = createResultList(resultSet, "value");
		
		return result;
		
	}

	
/* (non-Javadoc)
 * @see de.biba.triple.store.access.jena.IPropertyValuesCrawler#getAllDifferentValuesForAProperty(java.lang.String, java.lang.String)
 */
@Override
public List<String> getAllDifferentValuesForAProperty(String concept, String propertyURL) {
		
		List<String> result = new ArrayList<String>();
		String sparQL = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct  ?value  where { ?instance a ?class. ?class rdfs:subClassOf* <"+concept+"> .?instance <" +propertyURL+"> ?value.}";
		
		Object resultSet = query(sparQL);
		
		result = createResultList(resultSet, "value");
		
		return result;
		
	}

	
}
