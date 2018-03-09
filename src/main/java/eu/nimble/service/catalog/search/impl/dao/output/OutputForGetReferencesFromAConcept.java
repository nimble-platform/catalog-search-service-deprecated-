package eu.nimble.service.catalog.search.impl.dao.output;

import java.util.ArrayList;
import java.util.List;

public class OutputForGetReferencesFromAConcept {
	
	private List<Reference> allAvailableReferences = new ArrayList<Reference>();

	public List<Reference> getAllAvailableReferences() {
		return allAvailableReferences;
	}

	@Override
	public String toString() {
		return "OutputForGetReferencesFromAConcept [allAvailableReferences=" + allAvailableReferences + "]";
	}
	
	/**
	 * Returen the index, otherswise -1
	 * @param objectPropertyURL
	 * @return
	 */
	public int isReferenceAlreadyIncluded (String objectPropertyURL){
		int index = 0;
		for (Reference  reference : allAvailableReferences){
			if (reference.getObjectPropertyURL().equals(objectPropertyURL)){
				return index;
			}
				index++;
		}
		return  -1;
	}

}
