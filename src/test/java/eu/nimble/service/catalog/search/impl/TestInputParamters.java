package eu.nimble.service.catalog.search.impl;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;


import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.MeaningResult;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameter;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForgetPropertyValuesDiscretised;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;

public class TestInputParamters {

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
	public void doJson_InputParamaterForExecuteSelect(){
		InputParamaterForExecuteSelect test = new InputParamaterForExecuteSelect();
		test.setConcept("AirBed");
		test.getParameters().add("hieght");
		test.getParameters().add("size");
		
		Filter f1 = new Filter();
		f1.setProperty("hasHeight");
		f1.setMax(5.2f);
		f1.setMin(3.0f);
		
		test.getFilters().add(f1);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(test));
	}
	
	@Test
	public void doJson_InputParamaterForExecuteOptionalSelect(){
		InputParamaterForExecuteOptionalSelect test = new InputParamaterForExecuteOptionalSelect();
		test.setUuid(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural"));
		
		try {
			String result = URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural", "UTF-8");
			System.out.println(result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Gson gson = new Gson();
		System.out.println(gson.toJson(test));
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
	
	@Test
	public void OutputForExecuteSelect() {
		
		InputParamaterForExecuteSelect test = new InputParamaterForExecuteSelect();
		test.setConcept("AirBed");
		test.getParameters().add("hieght");
		test.getParameters().add("size");
		Filter f1 = new Filter();
		f1.setMax(5.2f);
		f1.setMin(3.0f);
		test.getFilters().add(f1);
		
		eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect output = new eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect();
		output.setInput(test);
		output.getColumns().add("height");
		output.getColumns().add("size");
		
		ArrayList<String> row1 = new ArrayList<String>();
		row1.add("100");
		row1.add("34");
		
		ArrayList<String> row2 = new ArrayList<String>();
		row2.add("1009");
		row2.add("37");
		
		output.getRows().add(row1);
		output.getRows().add(row2);
		
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(output));
	}
}
