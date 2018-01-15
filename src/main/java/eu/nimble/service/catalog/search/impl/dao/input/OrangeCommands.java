package eu.nimble.service.catalog.search.impl.dao.input;

import java.util.ArrayList;
import java.util.List;

public class OrangeCommands {

	List<String> names = new ArrayList<String>();

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	@Override
	public String toString() {
		return "OrangeCommands [names=" + names + "]";
	}
	
	
	
}
