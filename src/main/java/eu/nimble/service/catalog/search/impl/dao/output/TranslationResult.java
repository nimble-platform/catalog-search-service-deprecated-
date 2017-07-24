package eu.nimble.service.catalog.search.impl.dao.output;

public class TranslationResult {

	private String original;
	private boolean success;
	private String translation;
	public String getOriginal() {
		return original;
	}
	public void setOriginal(String original) {
		this.original = original;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getTranslation() {
		return translation;
	}
	public void setTranslation(String translation) {
		this.translation = translation;
	}
	@Override
	public String toString() {
		return "TranslationResult [original=" + original + ", success=" + success + ", translation=" + translation
				+ "]";
	}
	
	
	
	
}
