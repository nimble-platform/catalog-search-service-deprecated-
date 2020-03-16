package de.biba.triple.store.access;

import java.util.List;

import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;

import de.biba.triple.store.access.enums.Language;

public interface IAbstractQueryExecutor {

	String getUrlForQueries();

	void setModeToRemote();

	String getDataSetURL();

	List<String> createResultList(Object result, String propertyName);

	void setModeToLocal();

	boolean loadOntologyModel(String ontoFile);

	Object query(String sparqlStr);

	Object queryRemote(String sparqlStr);

	ResultSet queryLocal(String sparqlStr);

	void removeTypeDescriptionInValues(List<String> allValuesForProperty);

	String removeTypeDescriptionInValues(String value);

	Model getBaseModel();

	List<String> getSupportedLangauges();

	List<Language> getNativeSupportedLangauges();

	void setLanguageLabel(String languageLabel);

}