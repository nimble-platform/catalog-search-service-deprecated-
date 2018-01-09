package eu.nimble.service.catalog.search.services;

import java.util.ArrayList;
import java.util.List;

import de.biba.triple.store.access.dmo.Entity;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivation;

/**
 * This class enhance the results or the user defined input to the NIMBLE
 * platform specifics
 * 
 * @author fma
 *
 */
public class NimbleAdaptionServiceOfSearchResults {

	private MediatorSPARQLDerivation sparqlDerivation;
	private String languageLabel;
	public final static String propertyURLForName = "urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name";

	public NimbleAdaptionServiceOfSearchResults(MediatorSPARQLDerivation sparqlDerivation, String languageLabel) {
		super();
		this.sparqlDerivation = sparqlDerivation;
		this.languageLabel = languageLabel;
	}

	public List<Entity> getAdditionalPropertiesForAConcept(List<Entity> propertiesURL) {

		List<Entity> additionalProperties = new ArrayList<Entity>();

		checkWhetherNameAsMandatoryPropertyMustBeAdded(additionalProperties, propertiesURL);

		return additionalProperties;

	}

	private void checkWhetherNameAsMandatoryPropertyMustBeAdded(List<Entity> additionalProperties,
			List<Entity> propertiesURL) {
		boolean contained = false;
		Language language = null;
		for (int i = 0; i < propertiesURL.size(); i++) {
			Entity toBeChecked = propertiesURL.get(i);
			if (language == null) {
				if (toBeChecked.getLanguage() != null) {
					language = toBeChecked.getLanguage();
				}
			}
			if (toBeChecked.getUrl().contains("name")) {
				toBeChecked.setUrl(propertyURLForName);
				contained = true;
			}
		}

		if (!contained) {
			Entity entity = new Entity();
			entity.setHidden(false);
			entity.setLanguage(language);
			entity.setUrl(propertyURLForName);
			entity.setTranslatedURL(
					sparqlDerivation.translateProperty(propertyURLForName, language, languageLabel).getTranslation());
			additionalProperties.add(entity);
		}

	}

}
