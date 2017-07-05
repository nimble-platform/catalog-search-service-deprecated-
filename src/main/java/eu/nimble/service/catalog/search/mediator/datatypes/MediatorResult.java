package eu.nimble.service.catalog.search.mediator.datatypes;

import java.io.Serializable;

import org.springframework.util.MultiValueMap;


public class MediatorResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  String[][] data;
	private  String[] columnNames;

	public String[][] getData() {
		return data;
	}

	public void setData(String[][] data) {
		this.data = data;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder(10000);
		for (String col: columnNames){
			sb.append(col + "|");
		}
		sb.append("\n");
		for (String[] row : data){
			for (String item: row){
				sb.append(item);
				sb.append( "|");
			}
			sb.append( "\n");
		}
		return sb.toString();
	}

	public String toOutput(String typeOfOutput) {
		// TODO Auto-generated method stub
		return toString();
	}
}
