package eu.nimble.service.catalog.search.impl.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class PartyType extends Concept implements IParty {

//	@Indexed(name=NAME_FIELD)
//	private String name;
	private String legalName;
	private Map<String, String> brandName;
	private Map<String,String> origin;
	private Map<String,Collection<String>> certificateType;
	private Integer ppapComplianceLevel;
	private Map<String,String> ppapDocumentType;
	private Double trustScore;
	private Double trustRating;
	private Double trustTradingVolume;
	private Double trustSellerCommunication;
	private Double trustFullfillmentOfTerms;
	private Double trustDeliveryPackaging;
	private Double trustNumberOfTransactions;
	public String getId() {
		return getUri();
	}
	public void setId(String id) {
		setUri(id);
	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	/**
	 * Getter for the multilingual origin labels
	 */
	public Map<String, String> getOrigin() {
		return origin;
	}
	public void setOrigin(Map<String, String> originMap) {
		if ( originMap != null ) {
			for ( String key : originMap.keySet() ) {
				addOrigin(key, originMap.get(key));
			}
		}
		else {
			this.origin = null;
		}
	}
	/**
	 * Helper method for adding a multilingual origin to the concept. Only one origin per language is stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label for the origin
	 */
	public void addOrigin(String language, String label) {
		if ( this.origin == null) {
			this.origin = new HashMap<>();
		}
		this.origin.put(language, label);
		// 
		addLanguage(language);
	}
	/**
	 * Getter for the multilingual origin labels
	 */
	public Map<String, String> getBrandName() {
		return brandName;
	}
	public void setBrandName(Map<String, String> brandNameMap) {
		if ( brandNameMap != null ) {
			for ( String key : brandNameMap.keySet() ) {
				addBrandName(key, brandNameMap.get(key));
			}
		}
		else {
			this.brandName = null;
		}
	}
	/**
	 * Helper method for adding a multilingual origin to the concept. Only one origin per language is stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label for the brandName
	 */
	public void addBrandName(String language, String label) {
		if ( this.brandName == null) {
			this.brandName = new HashMap<>();
		}
		this.brandName.put(language, label);
		// 
		addLanguage(language);
	}

	
	public Map<String,Collection<String>> getCertificateType() {
		return certificateType;
	}

	/**
	 * Setter for the certificateType labels
	 * @param certificateTypeMap
	 */
	public void setCertificateType(Map<String, Collection<String>> certificateTypeMap) {
		if ( certificateTypeMap != null ) {
			for ( String lang : certificateTypeMap.keySet() ) {
				for (String label : certificateTypeMap.get(lang)) {
					addCertificateType(lang, label);
					
				}
			}
		}
		else {
			this.certificateType = null;
		}
	}
	/** 
	 * Helper method for adding a (multilingual) label to the list of certificat types 
	 * @param language
	 * @param certificatTypeLabel
	 */
	public void addCertificateType(String language, String certificatTypeLabel) {
		if (this.certificateType ==null) {
			this.certificateType = new HashMap<>();
		}
		if ( !this.certificateType.containsKey(language)) {
			this.certificateType.put(language, new HashSet<>());
		}
		this.certificateType.get(language).add(certificatTypeLabel);
		// 
		addLanguage(language);
	}
	
	public Integer getPpapComplianceLevel() {
		return ppapComplianceLevel;
	}
	public void setPpapComplianceLevel(Integer ppapComplianceLevel) {
		this.ppapComplianceLevel = ppapComplianceLevel;
	}

	public Double getTrustScore() {
		return trustScore;
	}
	public void setTrustScore(Double trustScore) {
		this.trustScore = trustScore;
	}
	public Double getTrustRating() {
		return trustRating;
	}
	public void setTrustRating(Double trustRating) {
		this.trustRating = trustRating;
	}
	public Double getTrustTradingVolume() {
		return trustTradingVolume;
	}
	public void setTrustTradingVolume(Double trustTradingVolume) {
		this.trustTradingVolume = trustTradingVolume;
	}
	public Double getTrustSellerCommunication() {
		return trustSellerCommunication;
	}
	public void setTrustSellerCommunication(Double trustSellerCommunication) {
		this.trustSellerCommunication = trustSellerCommunication;
	}
	public Double getTrustFullfillmentOfTerms() {
		return trustFullfillmentOfTerms;
	}
	public void setTrustFullfillmentOfTerms(Double trustFullfillmentOfTerms) {
		this.trustFullfillmentOfTerms = trustFullfillmentOfTerms;
	}
	public Double getTrustDeliveryPackaging() {
		return trustDeliveryPackaging;
	}
	public void setTrustDeliveryPackaging(Double trustDeliveryPackaging) {
		this.trustDeliveryPackaging = trustDeliveryPackaging;
	}
	public Double getTrustNumberOfTransactions() {
		return trustNumberOfTransactions;
	}
	public void setTrustNumberOfTransactions(Double trustNumberOfTransactions) {
		this.trustNumberOfTransactions = trustNumberOfTransactions;
	}
	public String getLegalName() {
		return legalName;
	}
	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}
	/**
	 * Getter for the multilingual origin labels
	 */
	public Map<String, String> getPpapDocumentType() {
		return ppapDocumentType;
	}
	public void setPpapDocumentType(Map<String, String> originMap) {
		if ( originMap != null ) {
			for ( String key : originMap.keySet() ) {
				addLabel(key, originMap.get(key));
			}
		}
		else {
			this.ppapDocumentType = null;
		}
	}
	/**
	 * Helper method for adding a multilingual PPAP Document Type labels to the concept. Only one label per language is stored.
	 * This method maintains the list of languages in use, see {@link #getLanguages()}
	 * @param language The language code such as <i>en</i>, <i>es</i>
	 * @param label The respective label for the PPAP Document Type
	 */
	public void addPpapDocumentType(String language, String label) {
		if ( this.ppapDocumentType == null) {
			this.ppapDocumentType = new HashMap<>();
		}
		this.ppapDocumentType.put(language, label);
		// 
		addLanguage(language);
	}


}
