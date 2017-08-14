package eu.nimble.service.catalog.search.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameter;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterForgetPropertyValuesDiscretised;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamterForGetLogicalView;
import eu.nimble.service.catalog.search.impl.dao.output.MeaningResult;
import eu.nimble.service.catalog.search.impl.dao.output.OutoutForGetSupportedLanguages;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputdetectPossibleConcepts;
import eu.nimble.service.catalog.search.mediator.MediatorEntryPoint;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

@Controller
public class SearchController {

	private static final String NULL_ASSIGNED_VALUE = "null";

	@Value("${nimble.shared.property.config.d:C:/Resources/NIMBLE/config.xml}")
	private String configPath;

	@Value("${nimble.shared.property.ontologyfile:null}")
	private String ontologyFile;

	@Value("${nimble.shared.property.marmottauri:null}")
	private String marmottaUri;

	@Value("${nimble.shared.property.languagelabel:http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation}")
	private String languageLabel;

	private MediatorSPARQLDerivation sparqlDerivation = null;

	@PostConstruct
	public void init() {
		if (ontologyFile.equals(NULL_ASSIGNED_VALUE) && (marmottaUri.equals(NULL_ASSIGNED_VALUE))) {
			sparqlDerivation = new MediatorSPARQLDerivation();
		} else {

			if (!ontologyFile.equals(NULL_ASSIGNED_VALUE)) {

				File f = new File(ontologyFile);
				if (f.exists()) {
					Logger.getAnonymousLogger().log(Level.INFO, "Load defined ontology file: " + ontologyFile);
					sparqlDerivation = new MediatorSPARQLDerivation(ontologyFile);
				} else {
					Logger.getAnonymousLogger().log(Level.INFO,
							"Load STANDARD ontology file: " + MediatorSPARQLDerivation.FURNITURE2_OWL);
					sparqlDerivation = new MediatorSPARQLDerivation();
				}
			} else {
				sparqlDerivation = new MediatorSPARQLDerivation(marmottaUri, true);
			}
		}
		sparqlDerivation.setLanguagelabel(languageLabel);
	}

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
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	HttpEntity<Object> query() {
		{
			return new ResponseEntity<Object>("hallo, Back and Front", HttpStatus.OK);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/detectMeaning", method = RequestMethod.GET)
	HttpEntity<Object> detectMeaning(@RequestParam("keyword") String keyword) {
		try {
			List<String> concepts = sparqlDerivation.detectPossibleConcepts(keyword);
			MeaningResult meaningResult = new MeaningResult();
			List<Entity> data = new ArrayList<Entity>();

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

				Entity entity = new Entity();
				entity.setLanguage(Language.UNKNOWN);
				entity.setUrl(concept);
				entity.setTranslatedURL(concept2);
				data.add(entity);

			}

			Gson gson = new Gson();
			String result = "";
			result = gson.toJson(meaningResult);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@CrossOrigin
	@RequestMapping(value = "/detectMeaningLanguageSpecific", method = RequestMethod.GET)
	HttpEntity<Object> detectMeaningLanguageSpecific(@RequestParam("inputAsJson") String inputAsJson) {
		try {
			Logger.getAnonymousLogger().log(Level.INFO, "Invoke: detectMeaningLanguageSpecific: " + inputAsJson);
			Gson gson = new Gson();
			InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific = gson
					.fromJson(inputAsJson, InputParameterdetectMeaningLanguageSpecific.class);
			List<Entity> concepts = sparqlDerivation.detectPossibleConceptsLanguageSpecific(
					inputParameterdetectMeaningLanguageSpecific.getKeyword(),
					inputParameterdetectMeaningLanguageSpecific.getLanguage());
			MeaningResult meaningResult = new MeaningResult();

			meaningResult.setConceptOverview(concepts);
			meaningResult.setSearchTyp("ExplorativeSearch");

			Gson output = new Gson();
			String result = "";
			result = output.toJson(meaningResult);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Returns from a given concept (must be the unique url) the data properties
	 * and obejctproperties and to each objecproperty a concept in the case the
	 * step range is greater 1
	 * 
	 * @param concept
	 * @param step
	 *            range
	 * @param
	 * @return
	 *
	@CrossOrigin
	@RequestMapping(value = "/getLogicalView", method = RequestMethod.GET)
	HttpEntity<Object> getLogicalView(@RequestParam("inputAsJson") String inputAsJson) {
		try {
			Logger.getAnonymousLogger().log(Level.INFO, "Invoke: getLogicalView: " + inputAsJson);
			Gson gson = new Gson();
			InputParamterForGetLogicalView paramterForGetLogicalView = gson.fromJson(inputAsJson,
					InputParamterForGetLogicalView.class);
			LocalOntologyView ontologyView = new LocalOntologyView();

			Entity concept = new Entity();
			concept.setUrl(paramterForGetLogicalView.getConcept());
			String label = sparqlDerivation.translateConcept(paramterForGetLogicalView.getConcept(),
					paramterForGetLogicalView.getLanguageAsLanguage(), languageLabel).getTranslation();
			concept.setTranslatedURL(label);

			ontologyView.setConcept(concept);
			List<LocalOntologyView> allAdressedConcepts = new ArrayList<LocalOntologyView>();
			List<LocalOntologyView> allAdressedConceptsHelper = new ArrayList<LocalOntologyView>();
			LocalOntologyView referenceLocalViewRoot = null;
			LocalOntologyView helper = new LocalOntologyView();
			helper.setConcept(concept);
			allAdressedConcepts.add(helper);
			for (int i = 0; i < paramterForGetLogicalView.getStepRange(); i++) {

				for (LocalOntologyView concept2 : allAdressedConcepts) {
					LocalOntologyView view = sparqlDerivation.getViewForOneStepRange(concept2.getConcept().getUrl(),
							concept2, Language.fromString(paramterForGetLogicalView.getLanguage()));
					if (i == 0) {
						referenceLocalViewRoot = view;

					} else {

					}

					for (String key : view.getObjectproperties().keySet()) {
						allAdressedConceptsHelper.add(view.getObjectproperties().get(key));
					}

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
	*/

	@CrossOrigin
	@RequestMapping(value = "/getLogicalView", method = RequestMethod.POST)
	HttpEntity<Object> getLogicalView(@RequestBody InputParamterForGetLogicalView paramterForGetLogicalView) {
		try {
			Logger.getAnonymousLogger().log(Level.INFO, "Invoke: getLogicalView:.");
			LocalOntologyView referenceLocalViewRoot = null;

			Gson gson = new Gson();

			LocalOntologyView ontologyView = new LocalOntologyView();

			Entity concept = new Entity();
			concept.setUrl(paramterForGetLogicalView.getConcept());
			String label = sparqlDerivation.translateConcept(paramterForGetLogicalView.getConcept(),
					paramterForGetLogicalView.getLanguageAsLanguage(), languageLabel).getTranslation();
			concept.setTranslatedURL(label);
			concept.setLanguage(paramterForGetLogicalView.getLanguageAsLanguage());
			
			String forzenConcept = paramterForGetLogicalView.getFrozenConcept();
	
			referenceLocalViewRoot = paramterForGetLogicalView.getOldJsonLogicalView();
			List<String> conceptURIPath = paramterForGetLogicalView.getConceptURIPath();

			ontologyView.setConcept(concept);
			// Key is a local ontology view, value is the parent ontology view of the local ontology view.
			HashMap<LocalOntologyView, LocalOntologyView> allAdressedConcepts = new HashMap<LocalOntologyView, LocalOntologyView>();
			HashMap<LocalOntologyView, LocalOntologyView> allAdressedConceptsHelper = new HashMap<LocalOntologyView, LocalOntologyView>();

			
			LocalOntologyView helper = null;
			LocalOntologyView parentOfHelper = null;
			// If the given old logical view json is not null, then the new search concept must be in the old logical view.
			// Because, the search concept is from the old logical view, and search is only for extension of old logical view.
			if(referenceLocalViewRoot != null)
			{
				if(!referenceLocalViewRoot.getConcept().getUrl().equals(conceptURIPath.get(0)))
				{
					throw new IllegalArgumentException( "The old logical view should start with "
							+ "the same root as in the parent concept URI path!!" );
				}
				
				helper = referenceLocalViewRoot.findLocalOntologyViewForConceptFromRoot(concept.getUrl(), conceptURIPath, referenceLocalViewRoot);
				
				if(helper == null)
				{
					throw new IllegalArgumentException( "The to be searched concept is not in the old logical view!!" );
				}
				
				if(helper.hasParentConcept())
				{
					List<String> parentConeptURIPath = conceptURIPath.subList(0, conceptURIPath.size()-1);
					String parentConceptURI = parentConeptURIPath.get(parentConeptURIPath.size()-1);
					parentOfHelper = referenceLocalViewRoot.findLocalOntologyViewForConceptFromRoot(parentConceptURI, parentConeptURIPath, referenceLocalViewRoot);
				}
			}
			else
			{
				helper = new LocalOntologyView();
				helper.setConcept(concept);
				helper.setFrozenConcept(forzenConcept);
				helper.setDistanceToFrozenConcept(0);
				
				List<String> uriPath = new ArrayList<String>();
				uriPath.add(concept.getUrl());
				helper.setConceptURIPath(uriPath);
			}
			
			allAdressedConcepts.put(helper, parentOfHelper);
			for (int i = 0; i < paramterForGetLogicalView.getStepRange(); i++) {
				for (LocalOntologyView concept2 : allAdressedConcepts.keySet()) {
					LocalOntologyView view = sparqlDerivation.getViewForOneStepRange(concept2.getConcept().getUrl(),
							concept2, allAdressedConcepts.get(concept2), Language.fromString(paramterForGetLogicalView.getLanguage()));
					if (i == 0) {
						if(referenceLocalViewRoot == null)
						{
							referenceLocalViewRoot = view;
						}

					} else {

					}

					for (String key : view.getObjectproperties().keySet()) {
						allAdressedConceptsHelper.put(view.getObjectproperties().get(key), view);
					}

				}
				allAdressedConcepts.clear();
				allAdressedConcepts.putAll(allAdressedConceptsHelper);
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
			Logger.getAnonymousLogger().log(Level.INFO, "Invoke: getPropertyValuesDiscretised: " + inputAsJson);
			Gson gson = new Gson();
			InputParameterForgetPropertyValuesDiscretised paramterForGetLogicalView = gson.fromJson(inputAsJson,
					InputParameterForgetPropertyValuesDiscretised.class);
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
	@RequestMapping(value = "/executeSPARQLSelect", method = RequestMethod.GET)
	HttpEntity<Object> executeSPARQLSelect(@RequestParam("inputAsJson") String inputAsJson) {
		Logger.getAnonymousLogger().log(Level.INFO, "Invoke: executeSPARQLSelect: " + inputAsJson);
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		try {
			Gson gson = new Gson();
			InputParamaterForExecuteSelect inputParamaterForExecuteSelect = gson.fromJson(inputAsJson,
					InputParamaterForExecuteSelect.class);

			outputForExecuteSelect = sparqlDerivation.createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);

			String result = "";
			result = gson.toJson(outputForExecuteSelect);

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
	@RequestMapping(value = "/executeSPARQLOptionalSelect", method = RequestMethod.GET)
	HttpEntity<Object> executeSPARQLWithOptionalSelect(@RequestParam("inputAsJson") String inputAsJson) {
		Logger.getAnonymousLogger().log(Level.INFO, "Invoke: executeSPARQLWithOptionalSelect: " + inputAsJson);
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		if (inputAsJson == null) {
			String example = "inputAsJson: {\"uuid\":\"http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural\"}";
			return new ResponseEntity<Object>(example, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			try {
				Gson gson = new Gson();
				InputParamaterForExecuteOptionalSelect inputParamaterForExecuteSelect = gson.fromJson(inputAsJson,
						InputParamaterForExecuteOptionalSelect.class);

				outputForExecuteSelect = sparqlDerivation
						.createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteSelect);

				String result = "";
				result = gson.toJson(outputForExecuteSelect);

				return new ResponseEntity<Object>(result, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/getSupportedLanguages", method = RequestMethod.GET)
	public HttpEntity<Object> getSupportedLanguages() {
		try {
			List<String> languages = sparqlDerivation.getSupportedLanguages();

			Gson output = new Gson();
			String result = "";
			OutoutForGetSupportedLanguages supportedLanguages = new OutoutForGetSupportedLanguages();
			supportedLanguages.getLanguages().addAll(languages);
			result = output.toJson(supportedLanguages);

			return new ResponseEntity<Object>(result, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
