package eu.nimble.service.catalog.search.mediator.datatypes;

import javax.swing.table.DefaultTableModel;

import de.biba.mediator.IQuerySolution;
import de.biba.mediator.ISolutionIterator;

public class QuerySolutionModel extends DefaultTableModel {

	private static final long serialVersionUID = 7230652541592370017L;
	private IQuerySolution solution;
	private ISolutionIterator iter;
	
	
	public QuerySolutionModel(IQuerySolution pSolution) {
		super();
		if(pSolution==null)
			return;
		solution = pSolution;
		iter = solution.getIterator();
		
	}

	@Override
	public int getColumnCount() {
		if(iter==null)
			return 0;
//		System.out.println(solution.getColumnNames().size());
		return solution.getColumnNames().size();
	}

	@Override
	public int getRowCount() {
		if(iter==null)
			return 0;
		return iter.getSize();
	}
	
	@Override
	public Object getValueAt(int pRow, int pColumn) {
		if(solution== null || pColumn>=solution.getColumnNames().size())
			return null;
		return iter.get(pColumn,pRow);
	}
	
	@Override
	public String getColumnName(int pColumn) {
		if(solution==null)
			return "";
		return solution.getColumnNames().get(pColumn);
	}
	
	@Override
	public boolean isCellEditable(int pRow, int pColumn) {
		return false;
	}	
}
