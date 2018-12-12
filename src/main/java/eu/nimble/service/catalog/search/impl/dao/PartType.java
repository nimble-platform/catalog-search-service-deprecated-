package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;

public class PartType {
	private String id;
	 ArrayList < Object > nace = new ArrayList < Object > ();
	 private String name;
	 private String websiteURI;
	 IndustryClassificationCode IndustryClassificationCodeObject;
	 private float ppapCompatibilityLevel;
	 ArrayList < Object > preferredItemClassificationCode = new ArrayList < Object > ();
	 ArrayList < Object > mostRecentItemsClassificationCode = new ArrayList < Object > ();
	 PostalAddress PostalAddressObject;
	 ArrayList < Object > partyTaxScheme = new ArrayList < Object > ();
	 Contact ContactObject;
	 ArrayList < Object > person = new ArrayList < Object > ();
	 ArrayList < Object > certificate = new ArrayList < Object > ();
	 ArrayList < Object > qualityIndicator = new ArrayList < Object > ();
	 ArrayList < Object > ppapDocumentReference = new ArrayList < Object > ();
	 ArrayList < Object > documentReference = new ArrayList < Object > ();
	 ArrayList < Object > industrySector = new ArrayList < Object > ();
	 PurchaseTerms PurchaseTermsObject;
	 private float hjid;


	 // Getter Methods 

	 public String getId() {
	  return id;
	 }

	 public String getName() {
	  return name;
	 }

	 public String getWebsiteURI() {
	  return websiteURI;
	 }

	 public IndustryClassificationCode getIndustryClassificationCode() {
	  return IndustryClassificationCodeObject;
	 }

	 public float getPpapCompatibilityLevel() {
	  return ppapCompatibilityLevel;
	 }

	 public PostalAddress getPostalAddress() {
	  return PostalAddressObject;
	 }

	 public Contact getContact() {
	  return ContactObject;
	 }

	 public PurchaseTerms getPurchaseTerms() {
	  return PurchaseTermsObject;
	 }

	 public float getHjid() {
	  return hjid;
	 }

	 // Setter Methods 

	 public void setId(String id) {
	  this.id = id;
	 }

	 public void setName(String name) {
	  this.name = name;
	 }

	 public void setWebsiteURI(String websiteURI) {
	  this.websiteURI = websiteURI;
	 }

	 public void setIndustryClassificationCode(IndustryClassificationCode industryClassificationCodeObject) {
	  this.IndustryClassificationCodeObject = industryClassificationCodeObject;
	 }

	 public void setPpapCompatibilityLevel(float ppapCompatibilityLevel) {
	  this.ppapCompatibilityLevel = ppapCompatibilityLevel;
	 }

	 public void setPostalAddress(PostalAddress postalAddressObject) {
	  this.PostalAddressObject = postalAddressObject;
	 }

	 public void setContact(Contact contactObject) {
	  this.ContactObject = contactObject;
	 }

	 public void setPurchaseTerms(PurchaseTerms purchaseTermsObject) {
	  this.PurchaseTermsObject = purchaseTermsObject;
	 }

	 public void setHjid(float hjid) {
	  this.hjid = hjid;
	 }
	}
	
	
	
	
	

