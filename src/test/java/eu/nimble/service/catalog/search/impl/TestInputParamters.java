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

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameter;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForGetReferencesFromAConcept;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForPropertyValuesFromGreenGroup;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForgetPropertyValuesDiscretised;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.input.Parameter;
import eu.nimble.service.catalog.search.impl.dao.input.Tuple;
import eu.nimble.service.catalog.search.impl.dao.output.MeaningResult;

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
		Entity cocnept = new Entity();
		cocnept.setUrl("http://blupp");
		cocnept.setTranslatedURL("bla2");
		
		
		parameter.setConcept(cocnept);
		List<Entity> pros = new ArrayList<>();
		
		
		Entity entity = new Entity();
		entity.setUrl("http://blupp");
		entity.setTranslatedURL("bla2");
		
		pros.add(entity);
		parameter.setDataproperties(pros);
		
		Map<String,LocalOntologyView> map   = new HashMap<String,LocalOntologyView>();
		map.put("conceprt2", new LocalOntologyView());
		parameter.setObjectproperties(map);
		

		Gson gson = new Gson();
		System.out.println(URLEncoder.encode(gson.toJson(parameter)));
	}
	
	@Test
	public void doJson_InputParamterForGetLogicalView(){
		InputParamterForGetLogicalView InputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		InputParamterForGetLogicalView.setConcept(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));
		InputParamterForGetLogicalView.setFrozenConcept("ddd");
		InputParamterForGetLogicalView.setStepRange(2);
		Gson gson = new Gson();
		System.out.println(gson.toJson(InputParamterForGetLogicalView));
	}
	
	//http://www.aidimme.es/FurnitureSectorOntology.owl
	
	@Test
	public void do_Json_InputParameterForPropertyValuesFromGreenGroup(){
		InputParameterForPropertyValuesFromGreenGroup inputParameterForPropertyValuesFromGreenGroup = new InputParameterForPropertyValuesFromGreenGroup();
		inputParameterForPropertyValuesFromGreenGroup.setConceptURL(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));
		inputParameterForPropertyValuesFromGreenGroup.setPropertyURL(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#hasWidth"));
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(inputParameterForPropertyValuesFromGreenGroup));
	}
	
	@Test
	public void do_Json_InputParameterForGetReferencesFromAConcept(){
		InputParameterForGetReferencesFromAConcept InputParameterForGetReferencesFromAConcept = new InputParameterForGetReferencesFromAConcept();
		InputParameterForGetReferencesFromAConcept.setConceptURL(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));
		InputParameterForGetReferencesFromAConcept.setLanguage(Language.SPANISH);
		Gson gson = new Gson();
		System.out.println(gson.toJson(InputParameterForGetReferencesFromAConcept));
	}
	
	@Test
	public void do_Json_InputParameterForPropertyValuesFromGreenGroupForHydra(){
		InputParameterForPropertyValuesFromGreenGroup inputParameterForPropertyValuesFromGreenGroup = new InputParameterForPropertyValuesFromGreenGroup();
		inputParameterForPropertyValuesFromGreenGroup.setConceptURL(URLEncoder.encode("http://www.aidimme.es/FurnitureSectorOntology.owl#HighChair"));
		inputParameterForPropertyValuesFromGreenGroup.setPropertyURL(URLEncoder.encode("http://www.aidimme.es/FurnitureSectorOntology.owl#hasWidth"));
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(inputParameterForPropertyValuesFromGreenGroup));
	}
	
	@Test
	public void doJson_InputParamterForGetLogicalView2(){
		InputParamterForGetLogicalView InputParamterForGetLogicalView = new InputParamterForGetLogicalView();
		InputParamterForGetLogicalView.setConcept(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));
		InputParamterForGetLogicalView.setFrozenConcept("HighChair");
		InputParamterForGetLogicalView.setStepRange(1);
		InputParamterForGetLogicalView.setLanguage("es");
		InputParamterForGetLogicalView.setDistanceToFrozenConcept(0);
		List<String> conceptPath = new ArrayList<String>();
		conceptPath.add(URLEncoder.encode("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair"));
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
		
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		String prop3 = "hasLegislationName";
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		inputParamaterForExecuteSelect.getParameters().add(prop3);
		inputParamaterForExecuteSelect.setLanguage("en");
		
		Parameter parameter1 = new Parameter();
		Parameter parameter2 = new Parameter();
		Parameter parameter3 = new Parameter();
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair");
		t1.setUrlOfProperty(null);

		parameter1.getPath().add(t1);
		parameter2.getPath().add(t1);
		parameter3.getPath().add(t1);
		
		
		
		Tuple t2 = new Tuple();
		t2.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Manufacturer");
		t2.setUrlOfProperty("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#isManufacturedBy");
		
		Tuple t3 = new Tuple();
		t3.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Legislation");
		t3.setUrlOfProperty("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#compliesWithLegislation");
		
		
		parameter3.getPath().add(t2);
		parameter3.getPath().add(t3);
		
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter2);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter3);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(inputParamaterForExecuteSelect));
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
