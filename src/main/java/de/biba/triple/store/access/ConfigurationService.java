package de.biba.triple.store.access;

public class ConfigurationService {

	private static ConfigurationService instance = null;
	String dataSetURL = "";

	public static ConfigurationService getInstance() {
		if (instance == null) {
			instance = new ConfigurationService();
		}
		return instance;
	}

	public void setDataSetUrl(String url) {
		int indexOfLastCharcter = url.length() - 2;
		if (url != null && url.length() > 3) {
			if (url.charAt(indexOfLastCharcter) == '/') {
				url = url.substring(0, indexOfLastCharcter);
			}
			dataSetURL = url;
		}
	}
	
	public String getDataSetUrl(){
		return dataSetURL;
	}

}
