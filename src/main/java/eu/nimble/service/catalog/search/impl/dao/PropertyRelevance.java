package eu.nimble.service.catalog.search.impl.dao;

import java.util.Date;

public class PropertyRelevance {

	private boolean isItRelevant = true;
	private boolean hasItBeenChecked = false;
	private Date lastCheck = null;
	
	public PropertyRelevance(){
		lastCheck =  new Date();
	}

	public boolean isItRelevant() {
		return isItRelevant;
	}

	public void setItRelevant(boolean isItRelevant) {
		this.isItRelevant = isItRelevant;
	}

	public boolean isHasItBeenChecked() {
		return hasItBeenChecked;
	}

	public void setHasItBeenChecked(boolean hasItBeenChecked) {
		this.hasItBeenChecked = hasItBeenChecked;
	}

	public Date getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(Date lastCheck) {
		this.lastCheck = lastCheck;
	}

	@Override
	public String toString() {
		return "PropertyRelevance [isItRelevant=" + isItRelevant + ", hasItBeenChecked=" + hasItBeenChecked
				+ ", lastCheck=" + lastCheck + "]";
	}
	
	
	
	
}
