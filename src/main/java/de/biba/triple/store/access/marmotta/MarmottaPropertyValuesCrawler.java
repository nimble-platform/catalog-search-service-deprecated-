package de.biba.triple.store.access.marmotta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.marmotta.client.ClientConfiguration;
import org.apache.marmotta.client.MarmottaClient;
import org.apache.marmotta.client.clients.SPARQLClient;
import org.apache.marmotta.client.exception.MarmottaClientException;
import org.apache.marmotta.client.model.rdf.RDFNode;
import org.apache.marmotta.client.model.sparql.SPARQLResult;

import de.biba.triple.store.access.IPropertyValuesCrawler;
import de.biba.triple.store.access.enums.ExecutionMode;
import de.biba.triple.store.access.jena.PropertyValuesCrawler;

public class MarmottaPropertyValuesCrawler extends PropertyValuesCrawler implements IPropertyValuesCrawler{

	private ClientConfiguration config = null;
	private MarmottaClient client = null;
	private String marmottaUri = "http://134.168.33.237:8080/marmotta";
	private SPARQLClient sparqlClient  = null;
	
	public MarmottaPropertyValuesCrawler() {
		init(ExecutionMode.REMOTE);
	}

	public MarmottaPropertyValuesCrawler(String marmottaUri) {
		init(marmottaUri);
	}
	
	
	private void init(String uri) {
		if (uri == null) {
			config = new ClientConfiguration(marmottaUri);
		} else {
			config = new ClientConfiguration(uri);

		}
		client = new MarmottaClient(config);
		 sparqlClient = client.getSPARQLClient();
	
	}
	
	
	public SPARQLResult query(String sparql){
		try {
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
		for (int i =0; i < result2.size(); i++){
		
			RDFNode node = result2.get(i).get(propertyName);
			allProperties.add(node.toString());
		}
		return allProperties;
	}
	
}
