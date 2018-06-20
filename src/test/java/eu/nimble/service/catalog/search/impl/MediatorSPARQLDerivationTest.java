package eu.nimble.service.catalog.search.impl;

import static org.junit.Assert.assertTrue;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.google.gson.Gson;

import static org.mockito.Mockito.*;

import eu.nimble.service.catalog.search.impl.dao.Entity;
import de.biba.triple.store.access.enums.Language;
import de.biba.triple.store.access.marmotta.MarmottaReader;
import eu.nimble.service.catalog.search.impl.dao.Filter;
import eu.nimble.service.catalog.search.impl.dao.Group;
import eu.nimble.service.catalog.search.impl.dao.LocalOntologyView;
import eu.nimble.service.catalog.search.impl.dao.enums.PropertySource;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteOptionalSelect;
import eu.nimble.service.catalog.search.impl.dao.input.InputParamaterForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.input.Parameter;
import eu.nimble.service.catalog.search.impl.dao.input.Tuple;
import eu.nimble.service.catalog.search.impl.dao.output.OutputForExecuteSelect;
import eu.nimble.service.catalog.search.impl.dao.output.TranslationResult;
import eu.nimble.service.catalog.search.mediator.MediatorSPARQLDerivationAndExecution;
import eu.nimble.service.catalog.search.mediator.NimbleSpecificSPARQLFactory;

public class MediatorSPARQLDerivationTest extends MediatorSPARQLDerivationAndExecution {

	 @Mock
	    MarmottaReader readerMock;
	
	private static final String C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL = "C:/ontology/FurnitureOntology-v1.5-biba_editedV2.owl";

	@Test
	@Ignore
	public void testGroupingOfPropertyValues() {
		MediatorSPARQLDerivationAndExecution mediatorSPARQLDerivation = new MediatorSPARQLDerivationAndExecution();

		String concept = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Bed";
		String property = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#price";

		Map<String, List<Group>> results = mediatorSPARQLDerivation.generateGroup(3, concept, property, PropertySource.DOMAIN_SPECIFIC_PROPERTY);
		System.out.println(results);

	}

	@Test
	@Ignore
	public void testgetViewForOneStepRange() {
		MediatorSPARQLDerivationAndExecution mediatorSPARQLDerivation = new MediatorSPARQLDerivationAndExecution(
				C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		LocalOntologyView helper = new LocalOntologyView();
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		this.setLanguagelabel(translationLabel);
		
		Entity concept = new Entity();
		concept.setUrl(getURIOfConcept("HighChair"));
		String label = translateConcept(concept.getUrl(), Language.SPANISH, this.getLanguagelabel()).getTranslation();
		concept.setTranslatedURL(label);
		
		helper.setConcept(concept);
		LocalOntologyView result = mediatorSPARQLDerivation.getViewForOneStepRange(helper.getConcept().getUrl(), helper, null, Language.SPANISH);
		assertTrue(result.getDataproperties().size() > 0);
		System.out.println(result.getDataproperties());
		System.out.println(result.getObjectproperties());
	}
	
	@Test
	@Ignore
	public void testgetViewForHighChair() {
		
		MediatorSPARQLDerivationAndExecution mediatorSPARQLDerivation = new MediatorSPARQLDerivationAndExecution(
				C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		LocalOntologyView helper = new LocalOntologyView();
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		this.setLanguagelabel(translationLabel);
		
		Entity concept = new Entity();
		concept.setUrl(getURIOfConcept("HighChair"));
		String label = translateConcept(concept.getUrl(), Language.ENGLISH, this.getLanguagelabel()).getTranslation();
		concept.setTranslatedURL(label);
		
		helper.setConcept(concept);
		LocalOntologyView result = mediatorSPARQLDerivation.getViewForOneStepRange(helper.getConcept().getUrl(), helper, null, Language.SPANISH);
		assertTrue(result.getDataproperties().size() > 0);
		System.out.println(result.getDataproperties());
		System.out.println(result.getObjectproperties());
	}

	
	@Test
	@Ignore
	public void testCreateSPARQLMockedMarmotta() {
		
		final List<String> cocnepts = new ArrayList<String>();
		readerMock = mock(MarmottaReader.class);
		cocnepts.add("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair");
		cocnepts.add("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair2");
		when(readerMock.getAllTransitiveSubConcepts(anyString())).thenReturn(cocnepts);
		when(readerMock.getAllConcepts(anyString())).thenReturn(cocnepts);
		//List<String> allPossibleProperties = reader.getAllProperties(property);
		List<String> properties1 = new ArrayList<String>();
		properties1.add("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#hasHeight");
		when(readerMock.getAllProperties("hasHeight")).thenReturn(properties1);
		
		List<String> properties2 = new ArrayList<String>();
		properties2.add("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#hasWidth");
		when(readerMock.getAllProperties("hasWidth")).thenReturn(properties2);
		
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		
		Filter f1 = new Filter();
		f1.setProperty("hasHeight");
		f1.setMax(5.2f);
		f1.setMin(3.0f);
		
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.getFilters().add(f1);
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(new Tuple());
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		String sparql = createSparql(inputParamaterForExecuteSelect, readerMock);
		assertTrue(sparql.length() > 10);
		System.out.println(sparql);
		
	}
	
	@Test
	@Ignore
	public void testCreateSPARQL() {
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		
		Filter f1 = new Filter();
		f1.setProperty("hasHeight");
		f1.setMax(5.2f);
		f1.setMin(3.0f);
		
		
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.getFilters().add(f1);
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		Parameter parameter1 = new Parameter();
		parameter1.getPath().add(new Tuple());
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		String sparql = createSparql(inputParamaterForExecuteSelect);
		
		assertTrue(sparql.length() > 10);
		System.out.println(sparql);
		
	}
	
	@Test
	@Ignore
	public void testcreateSPARQLAndExecuteIT(){
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		this.setLanguagelabel(translationLabel);
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		inputParamaterForExecuteSelect.setLanguage("es");
		OutputForExecuteSelect result = createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		assertTrue(result.getColumns().size() > 0);
		System.out.println(result);
		
	}

	@Test
	@Ignore
	public void testcreateSPARQLAndExecuteITII(){
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String concept = "HighChair";
		String prop1 = "hasHeight";
		String prop2 = "hasWidth";
		String prop3 = "hasLegislationName";
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		this.setLanguagelabel(translationLabel);
		InputParamaterForExecuteSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteSelect();
		inputParamaterForExecuteSelect.setConcept(concept);
		inputParamaterForExecuteSelect.getParameters().add(prop1);
		inputParamaterForExecuteSelect.getParameters().add(prop2);
		inputParamaterForExecuteSelect.getParameters().add(prop3);
		inputParamaterForExecuteSelect.setLanguage("en");
		
		Parameter parameter1 = new Parameter();
		Parameter parameter2 = new Parameter();
		Parameter parameter3 = new Parameter();
		Tuple t1 = new Tuple();
		t1.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#HighChair");
		t1.setUrlOfProperty(null);

		parameter1.getPath().add(t1);
		parameter2.getPath().add(t1);
		parameter3.getPath().add(t1);
		
		
		
		Tuple t2 = new Tuple();
		t2.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Manufacturer");
		t2.setUrlOfProperty("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#isManufacturedBy");
		
		Tuple t3 = new Tuple();
		t3.setConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#Legislation");
		t3.setUrlOfProperty("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#compliesWithLegislation");
		
		
		parameter3.getPath().add(t2);
		parameter3.getPath().add(t3);
		
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter1);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter2);
		inputParamaterForExecuteSelect.getParametersIncludingPath().add(parameter3);
		
		OutputForExecuteSelect result = createSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		assertTrue(result.getColumns().size() > 0);
		System.out.println(result);
		
	}
	
	
	
	
	
	//http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural
	@Test
	@Ignore
	public void testcreateOptionalSPARQLAndExecuteIT(){
		initForSpecificOntology(C_ONTOLOGY_FURNITURE_TAXONOMY_V1_4_BIBA_OWL);
		String indi = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural";
		
		InputParamaterForExecuteOptionalSelect inputParamaterForExecuteSelect = new InputParamaterForExecuteOptionalSelect();
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		this.setLanguagelabel(translationLabel);
		inputParamaterForExecuteSelect.setLanguage("es");
		inputParamaterForExecuteSelect.setUuid(indi);
		OutputForExecuteSelect result = createOPtionalSPARQLAndExecuteIT(inputParamaterForExecuteSelect);
		assertTrue(result.getColumns().size() > 0);
		System.out.println(result);
		
	}
	
	//<http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#ContactPerson>
	@Test
	@Ignore
	public void testgetLabelToConcept(){
		initForSpecificOntology("C:/ontology/FurnitureOntology-v1.5-biba_edited.owl");
		String indi = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#T950_Plus_Natural";
		String translationLabel = "http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#translation";
		
		TranslationResult result = translateConcept("http://www.semanticweb.org/ontologies/2013/4/Ontology1367568797694.owl#ContactPerson", Language.SPANISH,translationLabel );
		System.out.println("Result: " + result);
		
	}
	
	@Test
	@Ignore
	public void testSPARQLFactory(){
		InputParamaterForExecuteSelect  paramaterForExecuteSelect = new InputParamaterForExecuteSelect();
		paramaterForExecuteSelect.setConcept("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		List<String> parameters = new ArrayList<String>();
		parameters.add("name");
		parameters.add("description");
		parameters.add("vanish");
		
		List<String> parameterURLS = new ArrayList<String>();
		parameterURLS.add("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Name");
		parameterURLS.add("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2#Description");
		parameterURLS.add("http://www.aidimme.es/FurnitureSectorOntology.owl#Varnish");
		paramaterForExecuteSelect.setParameters(parameters);
		paramaterForExecuteSelect.setParametersURL(parameterURLS);
		
		Gson gson = new Gson();
		System.out.println(URLEncoder.encode(gson.toJson(paramaterForExecuteSelect)));
		
		MarmottaReader marmottaReader = new MarmottaReader("https://nimble-platform.salzburgresearch.at/marmotta");
		
		NimbleSpecificSPARQLFactory factory = new NimbleSpecificSPARQLFactory(null,null);
		List<String>  sparqls = factory.createSparql(paramaterForExecuteSelect, marmottaReader);
		for (String str: sparqls){
			System.out.println(str);
		}
		
	}
}
