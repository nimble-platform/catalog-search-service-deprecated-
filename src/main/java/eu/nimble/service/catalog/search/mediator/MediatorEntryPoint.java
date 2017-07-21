package eu.nimble.service.catalog.search.mediator;

import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.input.InputParameter;
import eu.nimble.service.catalog.search.mediator.datatypes.MediatorResult;

public class MediatorEntryPoint {

	private String configPath;
private InputParameter parameter;

	public MediatorEntryPoint(String configPath, InputParameter parameter) {
		// TODO Auto-generated constructor stub
		this.configPath = configPath;
		this.parameter = parameter;
	}

	public MediatorResult query() {
		MediatorWebservice mediatorWebservice = new MediatorWebservice(configPath);
		String query = parameter.getUserData()[0];
		String namespace = parameter.getUserData()[1];
		String query2 = "{\"namespace\":\"http://www.semanticweb.org/dqu/ontologies/2016/1/untitled-ontology-43\",\"query\":\"select ?Customer ?CustomerID  where { ?Customer a \u003cCustomer\u003e. ?Customer \u003cCustomerID\u003e ?CustomerID.}\"}";
		query = "{\"namespace\":\""+namespace + "\",\"query\":\"" + query + "\"}";
		System.out.println(query);
		System.out.println(query2);
		return mediatorWebservice.query(query, false);
	}

}
