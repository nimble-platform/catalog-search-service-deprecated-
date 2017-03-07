package eu.nimble.service.catalog.search.impl;

import org.junit.Test;

import com.google.gson.Gson;

import eu.nimble.service.catalog.search.impl.dao.InputParameter;

public class TestINputParameter {

	@Test
	public void doJson() {
		InputParameter parameter = new InputParameter();
		parameter.setUserData(new String[] { "k1", "k2" });
		parameter.setTypeOfQuery("keywordSearch");
		parameter.setTypeOfOutput("xml");

		Gson gson = new Gson();
		System.out.println(gson.toJson(parameter));
	}
}
