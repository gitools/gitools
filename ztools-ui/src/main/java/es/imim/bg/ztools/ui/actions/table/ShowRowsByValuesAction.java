package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import es.imim.bg.ztools.model.elements.ElementProperty;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog.ValueCondition;
import es.imim.bg.ztools.ui.dialogs.ValueListDialog.ValueCriteria;
import es.imim.bg.ztools.ui.model.table.ITable;
import es.imim.bg.ztools.ui.model.table.ITableContents;

public class ShowRowsByValuesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public ShowRowsByValuesAction() {
		super("Show rows by values...");	
		setDesc("Show rows by values");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		ITable table = getTable();
		if (table == null)
			return;

		List<ElementProperty> cellPropsList = table.getContents().getCellsFacade().getProperties();
		
		//Object[] params = {"p1", "p2", "p3", "p4"};
		Object[] params = new Object[cellPropsList.size()];
		for (int i = 0; i < cellPropsList.size(); i++)
			params[i] = cellPropsList.get(i).getName();

		
		ValueListDialog d = new ValueListDialog(AppFrame.instance(), params);
		List<ValueCriteria> valueList = d.getValues();
		boolean includeHidden = d.hiddenIncluded();
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
				System.out.println("without hidden " + rows + " " + cols);
				System.out.println(table.getRowCount());
			}
			int cellProps = cellPropsList.size();
			for(int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					boolean eval = true;
					cell: for (int k = 0; k < cellProps; k++){
						Object valueObject;
						if(includeHidden) 
							valueObject = contents.getCellValue(i, j, k);
						else
							valueObject = table.getCellValue(i, j, k);
						
			/*			System.out.println("row " + i + "; col" + j);
						System.out.println("contents\ttable");						
						System.out.print(contents.getCellValue(i, j, k).toString() + "\t");						
						System.out.print(contents.getCellValue(i, j, k).toString() + "\n");*/						

							
						float value =  Float.parseFloat(valueObject.toString());
						String property = cellPropsList.get(k).getName();
						Iterator<ValueCriteria> valueListIt = valueList.iterator();
						while (valueListIt.hasNext()){
							ValueCriteria vc = valueListIt.next();
							String vcParam = vc.getParam().toString();
							if (!eval)
								break cell;
							else if(!vcParam.equals(property))
								continue;
							else
								eval = evaluateCriteria(vc, value);
						}
					}
					if (eval){
						rowsToShow.add(i);
						break;
					}
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

	private boolean evaluateCriteria(ValueCriteria vc, float cellValue) {
		ValueCondition vcond = vc.getCondition();
		float conditionValue = Float.parseFloat(vc.getValue());
		boolean evaluation = false;
		switch (vcond) {
			case EQ:
				evaluation = (cellValue == conditionValue);
				break;
			case GE:
				evaluation = (cellValue >= conditionValue);
				break;
			case GT:
				evaluation = (cellValue > conditionValue);
				break;
			case LE:
				evaluation = (cellValue <= conditionValue);
				break;
			case LT:
				evaluation = (cellValue < conditionValue);
				break;
			case NE:
				evaluation = (cellValue != conditionValue);
				break;
		}
		return evaluation;
	}
}
