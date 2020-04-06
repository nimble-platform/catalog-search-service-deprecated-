package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;

public class PurchaseTerms {
	 ArrayList < Object > tradingTerms = new ArrayList < Object > ();
	 ArrayList < Object > deliveryTerms = new ArrayList < Object > ();
	 ArrayList < Object > paymentMeans = new ArrayList < Object > ();
	 private float hjid;


	 // Getter Methods 

	 public float getHjid() {
	  return hjid;
	 }

	 // Setter Methods 

	 public void setHjid(float hjid) {
	  this.hjid = hjid;
	 }
}
