package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.table.element.IElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialog.filter.FilterRowsByValueDialog;
import es.imim.bg.ztools.ui.dialog.filter.FilterRowsByValueDialog.ValueCondition;
import es.imim.bg.ztools.ui.dialog.filter.FilterRowsByValueDialog.ValueCriteria;

public class FilterRowsByValuesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterRowsByValuesAction() {
		super("Filter rows by values...");	
		setDesc("Filter rows by values");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;

		List<IElementProperty> cellPropsList = table.getContents().getCellAdapter().getProperties();
		
		Object[] params = new Object[cellPropsList.size()];
		for (int i = 0; i < cellPropsList.size(); i++)
			params[i] = cellPropsList.get(i).getName();

		
		FilterRowsByValueDialog d = new FilterRowsByValueDialog(AppFrame.instance(), params, "row");
		List<ValueCriteria> valueList = d.getValues();
		boolean includeHidden = d.hiddenIncluded();
		boolean allCells = d.allCells();
		boolean sameCell = d.sameCell();
		if(valueList != null) {
			
			List<Integer> rowsToShow = new ArrayList<Integer>();
			int rows;
			int cols;
			final ITableContents contents = table.getContents();
			if(includeHidden) {
				rows = contents.getRowCount();
				cols = contents.getColumnCount();
			} 
			else {
				rows = table.getVisibleRows().length;
				cols = table.getVisibleColumns().length;
			}
			int cellProps = cellPropsList.size();
			for(int i = 0; i < rows; i++) {
				List<ValueCriteria> tempValueList = valueList;
				int[][] eval = new int[tempValueList.size()][cols];

				
				for (int j = 0; j < cols; j++) {
					
					int vcCounter = 0;
					Iterator<ValueCriteria> valueListIt = tempValueList.iterator();
					while (tempValueList.size() > 0 && valueListIt.hasNext()) {
						ValueCriteria vc = valueListIt.next();
						String vcParam = vc.getParam().toString();
						for (int k = 0; k < cellProps; k++) {
							
							String property = cellPropsList.get(k).getName();
							if(!vcParam.equals(property))
								continue;
		
							Object valueObject;
							if(includeHidden) 
								valueObject = contents.getCellValue(i, j, k);
							else 
								valueObject = table.getCellValue(i, j, k);
							
							double value =  Double.parseDouble(valueObject.toString());
							
							eval[vcCounter][j] = evaluateCriteria(vc, value);
							
						}
						vcCounter++;
					}

				}
				boolean addRow = evaluateRow(eval, allCells, sameCell, tempValueList.size());
				if (addRow) {
					if (!includeHidden) {
						int[] visRows = table.getVisibleRows();
						rowsToShow.add(visRows[i]);
					}
					else
						rowsToShow.add(i);
				}
			}
			// Change visibility of rows in the table
			int[] visibleRows = new int[rowsToShow.size()];
			for (int i = 0; i < rowsToShow.size(); i++)
				visibleRows[i] = rowsToShow.get(i);
			table.setVisibleRows(visibleRows);
			
			AppFrame.instance()
			.setStatusText("Total visible rows = " + rowsToShow.size());
		}
	}

	private boolean evaluateRow(int[][] eval, boolean allCells, boolean sameCell, int criteriaNb) {
		int colNb = eval[0].length;
		int[] newEval = new int[colNb];

		for (int i = 0; i < colNb; i++){
			int trues = 0;
			for (int j = 0; j < criteriaNb; j++) {
				trues += eval[j][i];
			}
			
			if (sameCell && trues == criteriaNb)
				newEval[i] = 1;
			else if (sameCell && trues != criteriaNb)
				newEval[i] = 0;
			else if (!sameCell && trues > 0)
				newEval[i] = 1;
			else
				newEval[i] = 0;
		}
		
		int trueCells = 0;
		for (int i = 0; i < colNb; i++)
			trueCells += newEval[i];
		
		boolean addRow = false;
		if (allCells && trueCells == colNb)
			addRow = true;
		else if (!allCells && trueCells > 0)
			addRow = true;
		
		return addRow;
	}

	private int evaluateCriteria(ValueCriteria vc, double value) {
		ValueCondition vcond = vc.getCondition();
		double conditionValue = Double.parseDouble(vc.getValue());
		boolean evaluation = false;
		switch (vcond) {
			case EQ:
				evaluation = (value == conditionValue);
				break;
			case GE:
				evaluation = (value >= conditionValue);
				break;
			case GT:
				evaluation = (value > conditionValue);
				break;
			case LE:
				evaluation = (value <= conditionValue);
				break;
			case LT:
				evaluation = (value < conditionValue);
				break;
			case NE:
				evaluation = (value != conditionValue);
				break;
		}
		return (evaluation) ? 1 : 0;
	}
}
