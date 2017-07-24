package eu.nimble.service.catalog.search.mediator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.biba.triple.store.access.IPropertyValuesCrawler;
import de.biba.triple.store.access.IReader;
import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.enums.PropertyType;
import de.biba.triple.store.access.jena.PropertyValuesCrawler;
import de.biba.triple.store.access.jena.Reader;
import de.biba.triple.store.access.jena.Search;
import de.biba.triple.store.access.marmotta.MarmottaPropertyValuesCrawler;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParameterdetectMeaningLanguageSpecific;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.OutputdetectPossibleConcepts;
import eu.nimble.service.catalog.search.impl.dao.output.TranslationResult;

public class MediatorSPARQLDerivation {

	public static final String FURNITURE2_OWL = "furniture2.owl";
	private IReader reader = null;
	private IPropertyValuesCrawler propertyValuesCrawler = null;
	private String languagelabel = null;

	public MediatorSPARQLDerivation() {
		File f = new File(FURNITURE2_OWL);
		if (f.exists()) {
			initForSpecificOntology(FURNITURE2_OWL);
		} else {
			Logger.getAnonymousLogger().log(Level.WARNING, "Cannot load default ontology: " + FURNITURE2_OWL);
		}
	}

	public MediatorSPARQLDerivation(String pntologyFile) {
		initForSpecificOntology(pntologyFile);

	}

	public void initForSpecificOntology(String pntologyFile) {
		reader = new Reader();
		reader.setModeToLocal();
		reader.loadOntologyModel(pntologyFile);
		reader.setLanguageLabel(languagelabel);

		propertyValuesCrawler = new PropertyValuesCrawler();
		propertyValuesCrawler.setModeToLocal();
		propertyValuesCrawler.loadOntologyModel(pntologyFile);
	}

	public OutputForExecuteSelect createSPARQLAndExecuteIT(
			InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {

		String sparql = createSparql(inputParamaterForExecuteSelect);

		Object ouObject = reader.query(sparql);
		// This is necessary to get the uuid for each instance
		inputParamaterForExecuteSelect.getParameters().add(0, "instance");
		String[] params = new String[inputParamaterForExecuteSelect.getParameters().size()];

		inputParamaterForExecuteSelect.getParameters().toArray(params);
		List<String[]> resultList = reader.createResultListArray(ouObject, params);
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		outputForExecuteSelect.setInput(inputParamaterForExecuteSelect);

		inputParamaterForExecuteSelect.getParameters().remove(0); // Must be
																	// removed
																	// the uuid
																	// is not
																	// part of
																	// the
																	// result
		outputForExecuteSelect.getColumns().addAll(inputParamaterForExecuteSelect.getParameters());

		// add uuid to result data structures
		for (String[] row : resultList) {
			outputForExecuteSelect.getUuids().add(row[0]);
		}

		addRowsToOutputForExecuteSelect(resultList, outputForExecuteSelect, 1); // NO
																				// uuid
																				// should
																				// be
																				// inserted

		return outputForExecuteSelect;
	}

	public OutputForExecuteSelect createOPtionalSPARQLAndExecuteIT(
			InputParamaterForExecuteOptionalSelect inputParamaterForExecuteOptionalSelect) {

		// This is/ necessary to get the uuid for each instance
		Map<String, String> result = reader
				.getPropertyValuesOfAIndividium(inputParamaterForExecuteOptionalSelect.getUuid());

		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();

		ArrayList<String> row = new ArrayList<String>();
		for (String key : result.keySet()) {
			String column = key.substring(key.indexOf("#") + 1);
			outputForExecuteSelect.getColumns().add(column);

			String value = result.get(key);
			if (value != null && value.length() > 0) {
				int index = -1;
				index = value.indexOf("^^");
				if (index > -1) {
					value = value.substring(0, index);
				}

				row.add(value);
			}

		}

		outputForExecuteSelect.getRows().add(row);
		return outputForExecuteSelect;
	}

	public void addRowsToOutputForExecuteSelect(List<String[]> resultList,
			OutputForExecuteSelect outputForExecuteSelect, int startIndex) {
		for (int i = 0; i < resultList.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			for (int a = startIndex; a < resultList.get(i).length; a++) {
				String value = resultList.get(i)[a];
				if (value != null && value.length() > 0) {
					int index = -1;
					index = value.indexOf("^^");
					if (index > -1) {
						value = value.substring(0, index);
					}
				}
				row.add(value);
			}
			outputForExecuteSelect.getRows().add(row);
		}
	}

	protected String createSparql(InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {
		// TODO Auto-generated method stub
		String concept = getURIOfConcept(inputParamaterForExecuteSelect.getConcept());

		Map<String, String> resolvedProperties = new HashMap<String, String>();
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			String parameter = getURIOfProperty(param);
			resolvedProperties.put(param, parameter);
		}

		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ?instance ";
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			sparql += " ?" + param;
		}

		sparql += " where{";

		// add cocnept mapping:
		sparql += "?x rdfs:subClassOf*  <" + concept + ">. ";
		sparql += "?instance a ?x.";
		sparql = addProperties(inputParamaterForExecuteSelect, resolvedProperties, sparql);
		sparql = addFilters(inputParamaterForExecuteSelect, resolvedProperties, sparql);
		sparql += "}";
		return sparql;
	}

	private String addFilters(InputParamaterForExecuteSelect inputParamaterForExecuteSelect,
			Map<String, String> resolvedProperties, String sparql) {
		String filter = "";
		for (Filter fil : inputParamaterForExecuteSelect.getFilters()) {
			String filterText = "";
			String shortName = fil.getProperty();
			filterText += "FILTER ( xsd:decimal(?" + shortName + ") <  xsd:decimal(" + fil.getMax() + ")).";
			filterText += "FILTER ( xsd:decimal(?" + shortName + ") >=  xsd:decimal(" + fil.getMin() + ")).";
			filter += filterText;
		}

		return sparql + filter;
	}

	public String addProperties(InputParamaterForExecuteSelect inputParamaterForExecuteSelect,
			Map<String, String> resolvedProperties, String sparql) {
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			String property = resolvedProperties.get(param);

			sparql += "?instance" + "<" + property + "> " + "?" + param + ".";
		}
		return sparql;
	}

	public MediatorSPARQLDerivation(String uri, boolean remote) {
		if (!remote) {
			initForSpecificOntology(uri);

		} else {
			reader = new MarmottaReader(uri);
			propertyValuesCrawler = new MarmottaPropertyValuesCrawler(uri);
			reader.setLanguageLabel(languagelabel);

		}

	}

	// InputParameterdetectMeaningLanguageSpecific
	// inputParameterdetectMeaningLanguageSpecific
	public List<String> detectPossibleConcepts(
			InputParameterdetectMeaningLanguageSpecific inputParameterdetectMeaningLanguageSpecific) {
		Logger.getAnonymousLogger().log(Level.INFO, "Apply reader: " + reader.getClass().toString());
		return reader.getAllConcepts(inputParameterdetectMeaningLanguageSpecific.getKeyword());
	}

	/**
	 * Translation Concept: Returns the translation if available as String.
	 * Otherwise it delivers NA
	 * 
	 * @param uri
	 * @param language
	 * @param languagelabel
	 *            The label in which the translations are codified. if no label
	 *            property available, please set to null
	 * @return Returns the translation if available as String. Otherwise it
	 *         delivers uri
	 */
	public TranslationResult translateConcept(String uri, Language language, String languageLabel) {
		return translateEntity(uri, language, languageLabel);

	}

	/**
	 * Translation Property: Returns the translation if available as String.
	 * Otherwise it delivers uri
	 * 
	 * @param uri
	 * @param language
	 * @param languagelabel
	 *            The label in which the translations are codified. if no label
	 *            property available, please set to null
	 * @return Returns the translation if available as String. Otherwise it
	 *         delivers NA
	 */
	public TranslationResult translateProperty(String uri, Language language, String languageLabel) {
		return translateEntity(uri, language, languageLabel);
	}

	private TranslationResult translateEntity(String uri, Language language, String languageLabel) {
		TranslationResult translationResult = new TranslationResult();
		translationResult.setSuccess(false);
		translationResult.setOriginal(uri);
		String namespace = uri.substring(0, uri.indexOf("#"));
		String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>SELECT   ?subject ?object WHERE { <"
				+ uri + "> <" + languageLabel + "> ?object.}";
		Object result = reader.query(query);
		List<String> translations = reader.createResultList(result, "object");
		if (translations.size() > 0) {
			String postfix = Language.toOntologyPostfix(language);
			if (postfix != null) {
				for (String lang : translations) {
					if (lang.contains(postfix)) {

						translationResult.setTranslation(lang.substring(0, lang.indexOf(postfix)));
						translationResult.setSuccess(true);
						return translationResult;
					}
				}
			}
		}
		translationResult.setTranslation(uri.substring(uri.indexOf("#") + 1));
		return translationResult;
	}

	public List<String> detectPossibleConcepts(String regex) {
		Logger.getAnonymousLogger().log(Level.INFO, "Apply reader: " + reader.getClass().toString());
		return reader.getAllConcepts(regex);
	}

	public List<Entity> detectPossibleConceptsLanguageSpecific(String regex, Language language) {
		Logger.getAnonymousLogger().log(Level.INFO, "Apply reader: " + reader.getClass().toString());
		Logger.getAnonymousLogger().log(Level.INFO, "Language specific serach for:  " + language.toString());
		List<de.biba.triple.store.access.enums.Language> langaues = reader.getNativeSupportedLangauges();
		
		List<Entity> concepts = null;
		if (langaues.contains(language)) {
			Logger.getAnonymousLogger().log(Level.INFO, "Apply language specific serach: " + language);
			concepts = reader.getAllConceptsLanguageSpecific(regex, language);
		} else {
			Logger.getAnonymousLogger().log(Level.INFO, "Apply language UNspecific serach: " + language.UNKNOWN);
			concepts = reader.getAllConceptsFocusOnlyOnURI(regex);
		}

		return concepts;
	}

	public List<String> detectPossibleProperties(String regex) {

		return reader.getAllDirectProperties(regex);
		// return reader.getAllProperties(regex);
	}

	public LocalOntologyView getViewForOneStepRange(String concept, LocalOntologyView instance, Language language) {
		LocalOntologyView localOntologyView = null;

		if (instance == null) {
			localOntologyView = new LocalOntologyView();
		} else {
			localOntologyView = instance;
		}

		String conceptAsUri = getURIOfConcept(concept);
		Logger.getAnonymousLogger().log(Level.INFO, "Request properties from: " + conceptAsUri);
		List<String> properties = reader.getAllPropertiesIncludingEverything(conceptAsUri);
		for (String proeprty : properties) {
			PropertyType pType = reader.getPropertyType(proeprty);
			if (pType == PropertyType.DATATYPEPROPERTY) {
				String translatedName = reduceURIJustToName(proeprty,language);
				Entity entity = new Entity();
				entity.setUrl(proeprty);
				entity.setTranslatedURL(translatedName);
				
				localOntologyView.addDataproperties(entity);
			} else {
				// It is a object property which means I must return the name of
				// the concept
				List<String> ranges = reader.getRangeOfProperty(proeprty);
				for (int i = 0; i < ranges.size(); i++) {
					String range = ranges.get(i);
					range = reduceURIJustToName(range, language);
					LocalOntologyView localOntologyView2 = new LocalOntologyView();
					localOntologyView2.setConcept(range);
					localOntologyView.getObjectproperties().put(range, localOntologyView2);
				}
			}
		}

		return localOntologyView;

	}

	private String reduceURIJustToName(String uri, Language language) {
		TranslationResult range = translateConcept(uri, language, languagelabel);
		//range = range.substring(range.indexOf("#") + 1);
		return range.getTranslation();
	}

	private String getURIOfConcept(String concept) {
		
		if (concept.contains("#")){
			return concept;
		}
		
		List<String> allPossibleConcepts = reader.getAllConcepts(concept);
		for (String conceptURI : allPossibleConcepts) {
			String conceptURIShortened = conceptURI.substring(conceptURI.indexOf("#") + 1);
			if (conceptURIShortened.equals(concept)) {
				return conceptURI;
			}
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Couldn't find right concept in ontology: " + concept);
		return concept;
	}

	private String getURIOfProperty(String property) {
		List<String> allPossibleProperties = reader.getAllProperties(property);
		for (String propertyURI : allPossibleProperties) {
			String propertyURIShortened = propertyURI.substring(propertyURI.indexOf("#") + 1);
			if (propertyURIShortened.equals(property)) {
				return propertyURI;
			}
		}
		Logger.getAnonymousLogger().log(Level.WARNING, "Couldn't find right concept in ontology: " + property);
		return property;
	}

	/**
	 * Die Methode nimmt den REader/Search um das zu machen
	 * 
	 * @param amountOfGroups
	 * @param concept
	 * @param property
	 * @return
	 */
	public Map<String, List<Group>> generateGroup(int amountOfGroups, String concept, String property) {
		concept = getURIOfConcept(concept);
		String shortPropertyName = property;
		property = getURIOfProperty(property);
		List<String> values = propertyValuesCrawler.getAllDifferentValuesForAProperty(concept, property);
		for (int i = 0; i < values.size(); i++) {
			String str = values.get(i);
			int index = str.lastIndexOf("^");
			if (index > -1) {
				str = str.substring(0, index - 1);
			}
			str = str.replace(",", ".");
			values.set(i, str);
		}
		if (values != null && values.size() > 0) {
			try {
				Map<String, List<Group>> result = new HashMap<String, List<Group>>();
				float min = getMinOfData(values);
				float max = getMaxOfData(values);
				float stepRate = (max - min) / (float) amountOfGroups;
				List<Group> discreditedGroups = new ArrayList<Group>();
				for (int i = 0; i < amountOfGroups; i++) {
					Group group = new Group();
					float newMin = min + (stepRate * i);
					float newMax = min + (stepRate * (i + 1));
					group.setDescription("From: " + newMin + " to " + newMax);
					group.setMin(newMin);
					group.setMax(newMax);
					group.setProperty(shortPropertyName);
					discreditedGroups.add(group);
				}
				result.put(shortPropertyName, discreditedGroups);
				return result;
			} catch (Exception e) {
				Logger.getAnonymousLogger().log(Level.WARNING,
						"Cannot transform data from " + property + " into floats");
			}
		} else {
			return new HashMap<String, List<Group>>();
		}
		return new HashMap<String, List<Group>>();
	}

	private float getMinOfData(List<String> values) {
		float min = 999999;
		for (String value : values) {
			float number = Float.valueOf(value);
			if (number < min) {
				min = number;
			}
		}

		return min;
	}

	private float getMaxOfData(List<String> values) {
		float max = -999999;
		for (String value : values) {
			float number = Float.valueOf(value);
			if (number > max) {
				max = number;
			}
		}

		return max;
	}

	public String getLanguagelabel() {
		return languagelabel;
	}

	public void setLanguagelabel(String languagelabel) {
		this.languagelabel = languagelabel;
		if (reader != null) {
			reader.setLanguageLabel(languagelabel);
		}
	}

}
