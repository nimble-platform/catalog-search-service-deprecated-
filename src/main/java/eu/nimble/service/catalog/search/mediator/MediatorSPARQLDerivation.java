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
import de.biba.triple.store.access.enums.PropertyType;
import de.biba.triple.store.access.jena.PropertyValuesCrawler;
import de.biba.triple.store.access.jena.Reader;
import de.biba.triple.store.access.marmotta.MarmottaPropertyValuesCrawler;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.OutputForExecuteSelect;

public class MediatorSPARQLDerivation {

	public static final String FURNITURE2_OWL = "furniture2.owl";
	private IReader reader = null;
	private IPropertyValuesCrawler propertyValuesCrawler = null;

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

		propertyValuesCrawler = new PropertyValuesCrawler();
		propertyValuesCrawler.setModeToLocal();
		propertyValuesCrawler.loadOntologyModel(pntologyFile);
	}

	public OutputForExecuteSelect createSPARQLAndExecuteIT(
			InputParamaterForExecuteSelect inputParamaterForExecuteSelect) {

		String sparql = createSparql(inputParamaterForExecuteSelect);

		Object ouObject = reader.query(sparql);
		String[] params = new String[inputParamaterForExecuteSelect.getParameters().size()];
		inputParamaterForExecuteSelect.getParameters().toArray(params);
		List<String[]> resultList = reader.createResultListArray(ouObject, params);
		OutputForExecuteSelect outputForExecuteSelect = new OutputForExecuteSelect();
		outputForExecuteSelect.setInput(inputParamaterForExecuteSelect);
		outputForExecuteSelect.getColumns().addAll(inputParamaterForExecuteSelect.getParameters());
		addRowsToOutputForExecuteSelect(resultList, outputForExecuteSelect);

		return outputForExecuteSelect;
	}

	public void addRowsToOutputForExecuteSelect(List<String[]> resultList,
			OutputForExecuteSelect outputForExecuteSelect) {
		for (int i = 0; i < resultList.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			for (int a = 0; a < resultList.get(i).length; a++) {
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

		String sparql = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> select distinct ";
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			sparql += " ?" + param;
		}

		sparql += " where{";

		// add cocnept mapping:
		sparql += "?x rdfs:subClassOf*  <" + concept + ">. ";
		sparql += "?instance a ?x.";
		for (String param : inputParamaterForExecuteSelect.getParameters()) {
			String property = resolvedProperties.get(param);

			sparql += "?instance" + "<" + property + "> " + "?" + param + ".";
		}
		sparql += "}";
		return sparql;
	}

	public MediatorSPARQLDerivation(String uri, boolean remote) {
		if (!remote) {
			initForSpecificOntology(uri);

		} else {
			reader = new MarmottaReader(uri);
			propertyValuesCrawler = new MarmottaPropertyValuesCrawler(uri);

		}

	}

	public List<String> detectPossibleConcepts(String regex) {
		return reader.getAllConcepts(regex);
	}

	public List<String> detectPossibleProperties(String regex) {

		return reader.getAllDirectProperties(regex);
		// return reader.getAllProperties(regex);
	}

	public LocalOntologyView getViewForOneStepRange(String concept, LocalOntologyView instance) {
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
				proeprty = reduceUROJustToName(proeprty);
				localOntologyView.addDataproperties(proeprty);
			} else {
				// It is a object property which means I must return the name of
				// the concept
				List<String> ranges = reader.getRangeOfProperty(proeprty);
				for (int i = 0; i < ranges.size(); i++) {
					String range = ranges.get(i);
					range = reduceUROJustToName(range);
					LocalOntologyView localOntologyView2 = new LocalOntologyView();
					localOntologyView2.setConcept(range);
					localOntologyView.getObjectproperties().put(range, localOntologyView2);
				}
			}
		}

		return localOntologyView;

	}

	private String reduceUROJustToName(String range) {
		range = range.substring(range.indexOf("#") + 1);
		return range;
	}

	private String getURIOfConcept(String concept) {
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
}
