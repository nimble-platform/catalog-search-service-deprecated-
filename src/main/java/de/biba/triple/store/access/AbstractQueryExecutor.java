package de.biba.triple.store.access;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.JenaException;
import org.apache.jena.util.FileManager;

import de.biba.triple.store.access.enums.ExecutionMode;
import de.biba.triple.store.access.enums.Language;

public abstract class AbstractQueryExecutor implements IAbstractQueryExecutor {

	protected String dataSetURL = "";
	protected String urlForQueries = dataSetURL + "/query";
	private Model baseModel = null;
	protected ExecutionMode executionMode = ExecutionMode.REMOTE;
	private List<Language> supportedLangauges = new ArrayList<Language>();
	private String languageLabel = null; // inlcuding the URL

	public AbstractQueryExecutor(String bseUri, String languageLabel) {
		this.languageLabel = languageLabel;
		dataSetURL = bseUri;
		urlForQueries = dataSetURL + "/query";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.triple.store.access.IAbstractQueryExecutor#getUrlForQueries()
	 */
	@Override
	public String getUrlForQueries() {
		return urlForQueries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#setModeToRemote()
	 */
	@Override
	public void setModeToRemote() {
		executionMode = ExecutionMode.REMOTE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#getDataSetURL()
	 */
	@Override
	public String getDataSetURL() {
		return dataSetURL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.triple.store.access.IAbstractQueryExecutor#createResultList(java.
	 * lang.Object, java.lang.String)
	 */
	@Override
	public List<String> createResultList(Object result, String propertyName) {
		ResultSet result2 = (ResultSet) result;
		QuerySolution solution;
		List<String> allProperties = new ArrayList<String>();
		if (result2 != null) {
			while (result2.hasNext()) {
				solution = result2.nextSolution();
				if (solution.get(propertyName) != null) {
					if (!allProperties.contains(solution.get(propertyName).toString())){
					allProperties.add(solution.get(propertyName).toString());
					}
				}
			}
		}
		return allProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#setModeToLocal()
	 */
	@Override
	public void setModeToLocal() {
		executionMode = ExecutionMode.LOCAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.triple.store.access.IAbstractQueryExecutor#loadOntologyModel(java
	 * .lang.String)
	 */
	@Override
	public boolean loadOntologyModel(String ontoFile) {
		baseModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
		try {
			InputStream in = FileManager.get().open(ontoFile);
			try {
				baseModel.read(in, null);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			Logger.getAnonymousLogger().log(Level.INFO, "Ontology " + ontoFile + " loaded.");
		} catch (JenaException je) {
			System.err.println("ERROR" + je.getMessage());
			Logger.getAnonymousLogger().log(Level.SEVERE, je.getMessage());
			je.printStackTrace();
			return false;

		}
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#query(java.lang.
	 * String)
	 */
	@Override
	public Object query(String sparqlStr) {

		switch (executionMode) {

		case LOCAL:
			return queryLocal(sparqlStr);

		case REMOTE:
			return queryRemote(sparqlStr);

		default:
			return null;

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.triple.store.access.IAbstractQueryExecutor#queryRemote(java.lang.
	 * String)
	 */
	@Override
	public Object queryRemote(String sparqlStr) {

		if (isNotInit()) {
			init(ExecutionMode.REMOTE);
		}

		return queryRemoteHelper(sparqlStr);
	}

	public Object queryRemoteHelper(String sparqlStr) {
		Query query = QueryFactory.create(sparqlStr);

		try (QueryExecution qexec = QueryExecutionFactory.sparqlService(urlForQueries, query)) {
			ResultSet results = qexec.execSelect();

			results = ResultSetFactory.copyResults(results);
			// System.out.println(results.getResultVars());
			return results; // Passes the result set out of the try-resources
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getLocalizedMessage());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.biba.triple.store.access.IAbstractQueryExecutor#queryLocal(java.lang.
	 * String)
	 */
	@Override
	public ResultSet queryLocal(String sparqlStr) {

		if (isNotInit()) {
			init(ExecutionMode.LOCAL);
		}

		return queryLocalHelper(sparqlStr);

	}

	public ResultSet queryLocalHelper(String sparqlStr) {
		Query query = QueryFactory.create(sparqlStr);
		try (QueryExecution qexec = QueryExecutionFactory.create(query, baseModel)) {
			ResultSet results = qexec.execSelect();
			return ResultSetFactory.copyResults(results);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			Logger.getAnonymousLogger().log(Level.SEVERE, e.getLocalizedMessage());
			return null;
		}
	}

	public void init(ExecutionMode executionMode) {
		supportedLangauges = new ArrayList<Language>();
		if (languageLabel != null && languageLabel.length() > 0) {
			// NO I have to check if there a labels available
			String sparqlQuery = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX owl: <http://www.w3.org/2002/07/owl#> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>SELECT   ?subject ?object WHERE { ?subject <"
					+ languageLabel + "> ?object.} limit 100";
			Object result = null;
			if (executionMode == ExecutionMode.LOCAL) {
				result = queryLocalHelper(sparqlQuery);
			}
			if (executionMode == ExecutionMode.REMOTE) {
				result = queryRemoteHelper(sparqlQuery);
			}
			
			List<String> allLabels = createResultList(result, "object");
			for (String str : allLabels) {
				int index = str.indexOf("@");
				if (index > -1) {
					String language = str.substring(index + 1, index + 3);
					Language languageToBeAdded = Language.fromString(language);
					if (!supportedLangauges.contains(languageToBeAdded)) {
						supportedLangauges.add(languageToBeAdded);
					}
				}
			}

		}
	}

	private boolean isNotInit() {

		if (supportedLangauges == null || supportedLangauges.size() == 0) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#
	 * removeTypeDescriptionInValues(java.util.List)
	 */
	@Override
	public void removeTypeDescriptionInValues(List<String> allValuesForProperty) {

		for (int i = 0; i < allValuesForProperty.size(); i++) {
			String value = allValuesForProperty.get(i);
			int index = value.indexOf("^^");
			if (index > -1) {
				value = value.substring(0, index);
				allValuesForProperty.set(i, value);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#
	 * removeTypeDescriptionInValues(java.lang.String)
	 */
	@Override
	public String removeTypeDescriptionInValues(String value) {
		int index = value.indexOf("^^");
		if (index > -1) {
			value = value.substring(0, index);
		}

		return value;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.biba.triple.store.access.IAbstractQueryExecutor#getBaseModel()
	 */
	@Override
	public Model getBaseModel() {
		return baseModel;
	}

	@Override
	public List<String> getSupportedLangauges() {
		List<String> languages = new ArrayList<String>();
		
		if (supportedLangauges == null || supportedLangauges.size() ==0){
			init(this.executionMode);
		}
		
		for (Language language : supportedLangauges) {
			languages.add(language.toString());
		}
		return languages;
	}

	@Override
	public List<Language> getNativeSupportedLangauges() {
		
		if (supportedLangauges == null || supportedLangauges.size() ==0){
			init(this.executionMode);
		}
		
		return supportedLangauges;
	}
	
	public String getLanguageLabel() {
		return languageLabel;
	}

	@Override
	public void setLanguageLabel(String languageLabel) {
		this.languageLabel = languageLabel;
	}

}
