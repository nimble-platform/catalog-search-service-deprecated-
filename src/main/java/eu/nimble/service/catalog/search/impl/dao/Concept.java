package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Concept implements IConcept {
	/**
	 * The uri of the property including namespace
	 */
	
	
	protected String uri;
	
	
	protected String code;

	
	protected String localName;
	
	
	protected String nameSpace;	

	
	protected Collection<String> languages;
	
	protected Map<String, String> label;
	
	protected Map<String, Collection<String>> alternateLabel;
	
	protected Map<String, Collection<String>> hiddenLabel;
	
	protected Map<String, String> description;
	
	protected Map<String, String> comment;

	public Concept() {
		super();
	}
	static class SimpleConcept extends Concept {
		
	}
	public static Concept buildNew() {
		SimpleConcept c = new Concept.SimpleConcept();
		return c;
	}
	public static Concept buildFrom(IConcept other) {
		SimpleConcept c = new Concept.SimpleConcept();
		c.setUri(other.getUri());
		c.setCode(other.getCode());
		c.setLabel(other.getLabel());
		c.setDescription(other.getDescription());
		c.setComment(other.getComment());
		c.setLanguages(other.getLanguages());
		c.setNameSpace(other.getNameSpace());
		c.setLocalName(other.getLocalName());
		return c;
	}
	/**
	 * Getter for the languages in use, the list is maintained by the add... methods
	 */
	public Collection<String> getLanguages() {
		return languages;
	}
	
	/**
	 * Setter for the languages in use, <pre>do not use directly</pre>the distinct methods for adding labels
	 * are maintaining this collection.
	 * @param languages
	 */
	public void setLanguages(Collection<String> languages) {
		this.languages = languages;
	}
	/**
	 * Getter for the multilingual labels
	 */
	public Map<String, String> getLabel() {
		return label;
	}
	public void setLabel(Map<String, String> labelMap) {
		if ( labelMap != null ) {
			for ( String key : labelMap.keySet() ) {
				addLabel(key, labelMap.get(key));
			}
		}
		else {
			this.label = null;
		}
	}
	/**
	 * Helper method for adding a multilingual label to the concept. Only one label per language is stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label
	 */
	public void addLabel(String language, String label) {
		if ( this.label == null) {
			this.label = new HashMap<>();
		}
		this.label.put(language, label);
		// 
		addLanguage(language);
	}
	/**
	 * Helper method for adding alternate multilingual label to the concept. Multiple labels per language are stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label
	 */
	public void addAlternateLabel(String language, String alternate) {
		if (this.alternateLabel ==null) {
			this.alternateLabel = new HashMap<>();
		}
		if ( !this.alternateLabel.containsKey(language)) {
			this.alternateLabel.put(language, new HashSet<>());
		}
		this.alternateLabel.get(language).add(alternate);
		// 
		addLanguage(language);
	}
	/**
	 * Helper method for adding hidden multilingual label to the concept. Multiple labels per language are stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label
	 */
	public void addHiddenLabel(String language, String alternate) {
		if (this.hiddenLabel ==null) {
			this.hiddenLabel = new HashMap<>();
		}
		if ( !this.hiddenLabel.containsKey(language)) {
			this.hiddenLabel.put(language, new HashSet<>());
		}
		this.hiddenLabel.get(language).add(alternate);
		// 
		addLanguage(language);
	}
	/**
	 * Helper method maintaining the list of languages in use
	 * @param language
	 */
	protected void addLanguage(String language) {
		if ( this.languages == null) {
			this.languages = new HashSet<String>();
		}
		if ( ! languages.contains(language))
			this.languages.add(language);
	}

	/**
	 * Retrieve all comment entries
	 * @return
	 */
	public Map<String, String> getComment() {
		return comment;
	}
	/**
	 * Method adding a multilingual comment. Only one comment per langauge is stored
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param comment
	 */
	public void addComment(String language, String comment) {
		if ( this.comment == null) {
			this.comment = new HashMap<>();
		}
		this.comment.put(language, comment);
		// be sure to have all stored languages in the language list
		addLanguage(language);
	}
	/**
	 * Setter for the multilingual comments
	 * @param commentMap
	 */
	public void setComment(Map<String, String> commentMap) {
		if ( commentMap != null ) {
			for ( String key : commentMap.keySet() ) {
				addComment(key, commentMap.get(key));
			}
		}
		else {
			this.comment = null;
		}
	}
	/**
	 * Method adding a multilingual description. Only one description per langauge is stored
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param desc The description
	 */
	public void addDescription(String language, String desc) {
		if ( this.description == null) {
			this.description = new HashMap<>();
		}
		this.description.put(language, desc);
		// be sure to have all stored languages in the language list
		addLanguage(language);
	}
	/**
	 * Getter for the multilingual descriptions
	 */
	public Map<String, String> getDescription() {
		return description;
	}
	/**
	 * Setter for the multilingual descriptions
	 * @param descMap
	 */
	public void setDescription(Map<String, String> descMap) {
		if ( descMap != null ) {
			for ( String key : descMap.keySet() ) {
				addDescription(key, descMap.get(key));
			}
		}
		else {
			this.description = null;
		}
	}
	/**
	 * Getter for the identifier (URI)
	 */
	public String getUri() {
		return uri;
	}
	/**
	 * Setter for the identifier (URI)
	 * @param uri
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * Getter for the {@link #localName}
	 */
	public String getLocalName() {
		return localName;
	}
	/**
	 * Setter for the localName
	 * @param localName
	 */
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	/**
	 * Getter for the namespace
	 */
	public String getNameSpace() {
		return nameSpace;
	}
	/**
	 * Getter for the namespace
	 * @param nameSpace
	 */
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	/**
	 * Getter for the alternate labels
	 * @return
	 */
	public Map<String, Collection<String>> getAlternateLabel() {
		return alternateLabel;
	}
	/**
	 * Setter for the alternate labels
	 * @param alternateLabel
	 */
	public void setAlternateLabel(Map<String, Collection<String>> alternateLabel) {
		if ( alternateLabel != null ) {
			for ( String lang : alternateLabel.keySet() ) {
				for (String label : alternateLabel.get(lang)) {
					addAlternateLabel(lang, label);
					
				}
			}
		}
		else {
			this.alternateLabel = null;
		}
	}
	/**
	 * Getter for the hidden labels
	 * @return
	 */
	public Map<String, Collection<String>> getHiddenLabel() {
		return hiddenLabel;
	}
	/**
	 * Setter for the hidden labels
	 * @param alternateLabel
	 */
	public void setHiddenLabel(Map<String, Collection<String>> hiddenLabel) {
		if ( hiddenLabel != null ) {
			for ( String lang : hiddenLabel.keySet() ) {
				for (String label : hiddenLabel.get(lang)) {
					addHiddenLabel(lang, label);
					
				}
			}
		}
		else {
			this.hiddenLabel = null;
		}
	}
	/**
	 * Getter for the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * Setter for the code
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	


}
