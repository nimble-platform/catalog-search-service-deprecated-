package de.biba.triple.store.access.dmo;

import java.util.ArrayList;
import java.util.List;

public class DeleteOperationResult {

	boolean success = false;
	List<String> references = new ArrayList<String>();
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<String> getReferences() {
		return references;
	}
	public void setReferences(List<String> references) {
		this.references = references;
	}
	@Override
	public String toString() {
		return "DeleteOperationResult [success=" + success + ", references=" + references + "]";
	}
	
	
	
}
