package eu.nimble.service.catalog.search.impl.dao;

import java.util.ArrayList;
import java.util.List;

public class OutputForExecuteSelect {

	private InputParamaterForExecuteSelect input;
	private List<String> columns = new ArrayList<String>();
	private List<String> uuids = new ArrayList<String>();
	private List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();
	public InputParamaterForExecuteSelect getInput() {
		return input;
	}
	public void setInput(InputParamaterForExecuteSelect input) {
		this.input = input;
	}
	public List<String> getColumns() {
		return columns;
	}
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	public List<ArrayList<String>> getRows() {
		return rows;
	}
	public void setRows(List<ArrayList<String>> rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "OutputForExecuteSelect [input=" + input + ", columns=" + columns + ", uuids=" + uuids + ", rows=" + rows
				+ "]";
	}
	public List<String> getUuids() {
		return uuids;
	}

	
	
}
