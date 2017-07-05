package eu.nimble.service.catalog.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.InputParameter;
import eu.nimble.service.catalog.search.impl.dao.InputParameterForgetPropertyValuesDiscretised;
import eu.nimble.service.catalog.search.impl.dao.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.MeaningResult;

public class TestINputParameter {

	@Test
	public void doJson() {
		InputParameter parameter = new InputParameter();
		parameter.setUserData(new String[] { "select ?Customer ?CustomerID  where { ?Customer a \u003cCustomer\u003e. ?Customer \u003cCustomerID\u003e ?CustomerID.}", "http://www.semanticweb.org/dqu/ontologies/2016/1/untitled-ontology-43" });
		parameter.setTypeOfQuery("sparql");
		parameter.setTypeOfOutput("xml");

		Gson gson = new Gson();
		System.out.println(gson.toJson(parameter));
	}
	@Test
	public void doJson2() {
		InputParameter parameter = new InputParameter();
		parameter.setUserData(new String[] { "select ?S ?took  where { ?S a \u003cSomething\u003e. ?S \u003ctook\u003e ?id.}", "http://NIMBLE.eu" });
		parameter.setTypeOfQuery("sparql");
		parameter.setTypeOfOutput("xml");

		Gson gson = new Gson();
		System.out.println(gson.toJson(parameter));
	}
	
	@Test
	public void doLocalOntologyView() {
		LocalOntologyView parameter = new LocalOntologyView();
		parameter.setConcept("blupp");
		List<String> pros = new ArrayList<>();
		pros.add("price");
		parameter.setDataproperties(pros);
		
		Map<String,LocalOntologyView> map   = new HashMap<String,LocalOntologyView>();
		map.put("conceprt2", new LocalOntologyView());
		parameter.setObjectproperties(map);
		

		Gson gson = new Gson();
		System.out.println(gson.toJson(parameter));
	}
	
	@Test
	public void doJson_InputParamterForGetLogicalView(){
		InputParamterForGetLogicalView InputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		InputParamterForGetLogicalView.setConcept("ContactPerson");
		InputParamterForGetLogicalView.setFrozenConcept("ddd");
		InputParamterForGetLogicalView.setStepRange(2);
		Gson gson = new Gson();
		System.out.println(gson.toJson(InputParamterForGetLogicalView));
	}
	
	
	//InputParameterForgetPropertyValuesDiscretised
	@Test
	public void doJson_InputParameterForgetPropertyValuesDiscretised(){
		InputParameterForgetPropertyValuesDiscretised inputParameterForgetPropertyValuesDiscretised = new InputParameterForgetPropertyValuesDiscretised();
		inputParameterForgetPropertyValuesDiscretised.setAmountOfGroups(3);
		inputParameterForgetPropertyValuesDiscretised.setConcept("Bed");
		inputParameterForgetPropertyValuesDiscretised.setProperty("price");
		Gson gson = new Gson();
		System.out.println(gson.toJson(inputParameterForgetPropertyValuesDiscretised));
	}
	
	@Test
	public void doJson_MeaningResult() {
		MeaningResult parameter = new MeaningResult();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> properties = new ArrayList<String>();
		properties.add("size");
		properties.add("weight");
		
		map.put("Bed", properties);
		//parameter.setConceptOverview(map);
		parameter.setSearchTyp("ExplorativeSearch");

		Gson gson = new Gson();
		System.out.println(gson.toJson(parameter));
	}
}
