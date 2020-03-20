package eu.nimble.service.catalog.search.impl.dao;

public class SearchConfig {

	private boolean IndexingActive = false;
	private String urlOfIOndexingService = "";
	private boolean useSOLRIndex = false;
	private String hybridConfiguration = "";
	private String marmottaUri ="";
	
	
	
	
	
	public String getMarmottaUri() {
		return marmottaUri;
	}
	public void setMarmottaUri(String marmottaUri) {
		this.marmottaUri = marmottaUri;
	}
	public String getHybridConfiguration() {
		return hybridConfiguration;
	}
	public void setHybridConfiguration(String hybridConfiguration) {
		this.hybridConfiguration = hybridConfiguration;
	}
	public boolean isIndexingActive() {
		return IndexingActive;
	}
	public void setIndexingActive(boolean indexingActive) {
		IndexingActive = indexingActive;
	}
	public String getUrlOfIOndexingService() {
		return urlOfIOndexingService;
	}
	public void setUrlOfIOndexingService(String urlOfIOndexingService) {
		this.urlOfIOndexingService = urlOfIOndexingService;
	}
	public boolean isUseSOLRIndex() {
		return useSOLRIndex;
	}
	public void setUseSOLRIndex(boolean useSOLRIndex) {
		this.useSOLRIndex = useSOLRIndex;
	}
	
	
	
}
