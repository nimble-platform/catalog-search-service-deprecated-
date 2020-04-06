package de.biba.triple.store.access;

import java.util.List;

public interface IPropertyValuesCrawler extends IAbstractQueryExecutor{

	List<String> getAllDifferentValuesForAProperty(String propertyURL);

	List<String> getAllDifferentValuesForAProperty(String concept, String propertyURL);

}