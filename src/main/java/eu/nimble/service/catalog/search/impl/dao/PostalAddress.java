package eu.nimble.service.catalog.search.impl.dao;

public class PostalAddress {
	 private String streetName;
	 private String buildingNumber;
	 private String cityName;
	 private String postalZone;
	 Country CountryObject;
	 private float hjid;


	 // Getter Methods 

	 public String getStreetName() {
	  return streetName;
	 }

	 public String getBuildingNumber() {
	  return buildingNumber;
	 }

	 public String getCityName() {
	  return cityName;
	 }

	 public String getPostalZone() {
	  return postalZone;
	 }

	 public Country getCountry() {
	  return CountryObject;
	 }

	 public float getHjid() {
	  return hjid;
	 }

	 // Setter Methods 

	 public void setStreetName(String streetName) {
	  this.streetName = streetName;
	 }

	 public void setBuildingNumber(String buildingNumber) {
	  this.buildingNumber = buildingNumber;
	 }

	 public void setCityName(String cityName) {
	  this.cityName = cityName;
	 }

	 public void setPostalZone(String postalZone) {
	  this.postalZone = postalZone;
	 }

	 public void setCountry(Country countryObject) {
	  this.CountryObject = countryObject;
	 }

	 public void setHjid(float hjid) {
	  this.hjid = hjid;
	 }
}
