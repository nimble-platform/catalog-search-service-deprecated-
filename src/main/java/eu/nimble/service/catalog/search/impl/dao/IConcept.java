package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.Map;

public interface IConcept {
	String ID_FIELD = "id";
	String CODE_FIELD = "code";
	/**
	 * Collection of languages
	 */
	String LANGUAGES_FIELD = "languages";
	/**
	 * The language based label, e.g. <code><i>en</i>_label</code> for english label
	 */
	String LABEL_FIELD = "*_label";
	String ALL_LABEL_FIELD = "all_label";
	/**
	 * The language based label, e.g. <code><i>en</i>_alternate</code> for english alternate label
	 */
	String ALTERNATE_LABEL_FIELD = "*_alternate";
	/**
	 * The language based label, e.g. <code><i>en</i>_label</code> for english label
	 */
	String HIDDEN_LABEL_FIELD = "*_hidden";
	/**
	 * Copy Field, language based. The language based label and description are stored in this field
	 * Final used index name is <code><i>en</i>_txt</code> for english text (label, description)
	 */
	String LANGUAGE_TXT_FIELD = "*_txt";
	/**
	 * Copy Field. All labels, descriptions are stored in this field
	 */
	String TEXT_FIELD = "_text_";
	/**
	 * The language based comment field, e.g.  <code><i>en</i>_comment</code> for english comments
	 */
	String COMMENT_FIELD = "*_comment";
	String NAME_SPACE_FIELD = "nameSpace";
	String LOCAL_NAME_FIELD = "localName"; 
	String DESCRIPTION_FIELD = "*_desc";

	public Collection<String> getLanguages();
	
	public Map<String, String> getLabel();
	public Map<String, String> getComment();
	public Map<String, String> getDescription();
	public String getUri();
	public String getCode();
	public String getNameSpace();
	public String getLocalName();
}