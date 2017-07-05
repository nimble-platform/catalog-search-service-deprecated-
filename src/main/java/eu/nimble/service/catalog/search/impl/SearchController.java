package eu.nimble.service.catalog.search.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.InputParameter;
import eu.nimble.service.catalog.search.impl.dao.InputParameterForgetPropertyValuesDiscretised;
import eu.nimble.service.catalog.search.impl.dao.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.MeaningResult;
import eu.nimble.service.catalog.search.mediator.MediatorEntryPoint;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

@Controller
public class SearchController {

	@Value("${nimble.shared.property.config.d:C:/Resources/NIMBLE/config.xml}")
	private String configPath;

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	HttpEntity<Object> query(@RequestParam("query") String query) {
		{
			Gson gson = new Gson();
			InputParameter parameter = gson.fromJson(query, InputParameter.class);
			MediatorEntryPoint service = new MediatorEntryPoint(configPath, parameter);
			eu.nimble.service.catalog.search.mediator.datatypes.MediatorResult result = service.query();

			return new ResponseEntity<Object>(result.toOutput(parameter.getTypeOfOutput()), HttpStatus.OK);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	HttpEntity<Object> query() {
		{
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!: Have called");
			return new ResponseEntity<Object>("hallo, Back and Front", HttpStatus.OK);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/detectMeaning", method = RequestMethod.GET)
	HttpEntity<Object> detectMeaning(@RequestParam("keyword") String keyword) {
		MediatorSPARQLDerivation sparqlDerivation = new MediatorSPARQLDerivation();

		List<String> concepts = sparqlDerivation.detectPossibleConcepts(keyword);
		MeaningResult meaningResult = new MeaningResult();
		List<String> data = new ArrayList<String>();

		meaningResult.setConceptOverview(data);
		meaningResult.setSearchTyp("ExplorativeSearch");

		for (String concept : concepts) {
			int index = -1;
			if (index == -1) {
				index = concept.indexOf("#");
			}
			if (index == -1) {
				index = concept.lastIndexOf("/");
			}
			index++;
			String concept2 = concept.substring(index);
			// List<String> properies =
			// sparqlDerivation.detectPossibleProperties(concept);
			// List<String> properiesCleaned = postprocessProperties(properies);
			data.add(concept2);
		}

		Gson gson = new Gson();
		String result = "";
		result = gson.toJson(meaningResult);

		return new ResponseEntity<Object>(result, HttpStatus.OK);

	}

	/**
	 * Returns from a given concept the data properties and obejctproperties and
	 * to each objecproperty a concept in the case the step range is greater 1
	 * 
	 * @param concept
	 * @param step
	 *            range
	 * @param
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getLogicalView", method = RequestMethod.GET)
	HttpEntity<Object> getLogicalView(@RequestParam("inputAsJson") String inputAsJson) {
		try {
			MediatorSPARQLDerivation sparqlDerivation = new MediatorSPARQLDerivation();
			Gson gson = new Gson();
			InputParamterForGetLogicalView paramterForGetLogicalView = gson.fromJson(inputAsJson,
					InputParamterForGetLogicalView.class);
			LocalOntologyView ontologyView = new LocalOntologyView();
			ontologyView.setConcept(paramterForGetLogicalView.getConcept());
			List<LocalOntologyView> allAdressedConcepts = new ArrayList<LocalOntologyView>();
			List<LocalOntologyView> allAdressedConceptsHelper = new ArrayList<LocalOntologyView>();
			LocalOntologyView referenceLocalViewRoot = null;
			LocalOntologyView helper = new LocalOntologyView();
			helper.setConcept(paramterForGetLogicalView.getConcept());
			allAdressedConcepts.add(helper);
			for (int i = 0; i < paramterForGetLogicalView.getStepRange(); i++) {

				for (LocalOntologyView concept : allAdressedConcepts) {
					LocalOntologyView view = sparqlDerivation.getViewForOneStepRange(concept.getConcept(), concept);
					if (i == 0) {
						referenceLocalViewRoot = view;

					} else {

					}

					for (String key : view.getObjectproperties().keySet()) {
						allAdressedConceptsHelper.add(view.getObjectproperties().get(key));
					}

					// wie muss ich es jetzt verheiraten
				}
				allAdressedConcepts.clear();
				allAdressedConcepts.addAll(allAdressedConceptsHelper);
				allAdressedConceptsHelper.clear();
			}

			String result = "";
			result = gson.toJson(referenceLocalViewRoot);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Returns from a given concept the data properties and obejctproperties and
	 * to each objecproperty a concept in the case the step range is greater 1
	 * 
	 * @param concept
	 * @param step
	 *            range
	 * @param
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/getPropertyValuesDiscretised", method = RequestMethod.GET)
	HttpEntity<Object> getPropertyValuesDiscretised(@RequestParam("inputAsJson") String inputAsJson) {
		try {
			Gson gson = new Gson();
			InputParameterForgetPropertyValuesDiscretised paramterForGetLogicalView = gson.fromJson(inputAsJson,
					InputParameterForgetPropertyValuesDiscretised.class);
			MediatorSPARQLDerivation sparqlDerivation = new MediatorSPARQLDerivation();
			Map<String, List<Group>> mapOfPropertyGroups = sparqlDerivation.generateGroup(
					paramterForGetLogicalView.getAmountOfGroups(), paramterForGetLogicalView.getConcept(),
					paramterForGetLogicalView.getProperty());
			String result = "";
			result = gson.toJson(mapOfPropertyGroups);
			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	private List<String> postprocessProperties(List<String> properies) {

		List<String> result = new ArrayList<String>();
		for (String str : properies) {

			int index = -1;
			if (index == -1) {
				index = str.indexOf("#");
			}
			if (index == -1) {
				index = str.lastIndexOf("/");
			}
			index++;
			String concept2 = str.substring(index);
			result.add(concept2);
		}
		// TODO Auto-generated method stub
		return result;
	}
}
