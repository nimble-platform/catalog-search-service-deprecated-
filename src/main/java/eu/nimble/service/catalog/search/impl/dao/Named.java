package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



public abstract class Named  {
	/**
	 * The uri of the property including namespace
	 */
	
	
	protected String uri;

	
	protected String localName;
	
	
	protected String nameSpace;	

	
	protected Collection<String> languages;
	
	protected Map<String, String> label;
	
	protected Map<String, Collection<String>> alternateLabel;
	
	protected Map<String, Collection<String>> hiddenLabel;
	
	protected Map<String, String> description;
	
	protected Map<String, String> comment;

	public Named() {
		super();
	}

	public Collection<String> getLanguages() {
		return languages;
	}

	public void setLanguages(Collection<String> languages) {
		this.languages = languages;
	}

	public Map<String, String> getLabel() {
		return label;
	}
	public void addLabel(String language, String label) {
		if ( this.label == null) {
			this.label = new HashMap<>();
		}
		this.label.put(language, label);
		// 
		addLanguage(language);
	}
	private void addLanguage(String language) {
		if ( this.languages == null) {
			this.languages = new HashSet<String>();
		}
		this.languages.add(language);
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

	public Map<String, String> getComment() {
		return comment;
	}
	public void addComment(String language, String comment) {
		if ( this.comment == null) {
			this.comment = new HashMap<>();
		}
		this.comment.put(language, comment);
		// be sure to have all stored languages in the language list
		addLanguage(language);
	}

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
	public void addDescription(String language, String desc) {
		if ( this.description == null) {
			this.description = new HashMap<>();
		}
		this.description.put(language, desc);
		// be sure to have all stored languages in the language list
		addLanguage(language);
	}
	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> descMap) {
		if ( descMap != null ) {
			for ( String key : descMap.keySet() ) {
				addComment(key, descMap.get(key));
			}
		}
		else {
			this.comment = null;
		}
	}
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	
	

}
