package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.gitools.ui.AppFrame;
import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.filter.FilterRowsByNameDialog;

import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.IMatrixView;

public class FilterColumnsByNamesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterColumnsByNamesAction() {
		super("Filter columns by name...");	
		setDesc("Filter columns by name");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof MatrixFigure
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = getMatrixView();
		if (matrixView == null)
			return;
		
		// Show dialog
		FilterRowsByNameDialog d = 
			new FilterRowsByNameDialog(AppFrame.instance());
		
		List<String> names = d.getNameList();
		boolean regexChecked = d.isRegexChecked();
		
		if(names == null)
			return;
		
		final IMatrix contents = matrixView.getContents();
		
		// List of columns that will be shown
		List<Integer> columns = new ArrayList<Integer>();
		if (regexChecked)
			filterByRegex(names, contents, columns);
		else
			filterByNames(names, contents, columns);
		
		// Change visibility of columns in the table
		int[] visibleColumns = new int[columns.size()];
		for (int i = 0; i < columns.size(); i++)
			visibleColumns[i] = columns.get(i);
		matrixView.setVisibleColumns(visibleColumns);
		
		AppFrame.instance()
			.setStatusText("Total visible columns = " + columns.size());
	}

	private void filterByNames(List<String> names, IMatrix contents, List<Integer> columns) {
		
		int columnCount = contents.getColumnCount();
		
		Map<String, Integer> nameToColumnMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < columnCount; i++) {
			final String columnName = contents.getColumn(i).toString();
			nameToColumnMap.put(columnName, i);
		}
		
		for (int i = 0; i < names.size(); i++) {
			final String name = names.get(i);
			Integer column = nameToColumnMap.get(name);
			if (column != null)
				columns.add(column);
		}
	}

	private void filterByRegex(List<String> names, IMatrix contents, List<Integer> columns) {

		// Compile patterns
		List<Pattern> patterns = new ArrayList<Pattern>(names.size());
		for (String name : names)
			patterns.add(Pattern.compile(name));
				
		int columnCount = contents.getColumnCount();
		
		// Check patterns
		for (int i = 0; i < columnCount; i++) {
			final String columnName = contents.getColumn(i).toString();
			for (int j = 0; j < patterns.size(); j++)
				if (patterns.get(j).matcher(columnName).matches()) {
					columns.add(i);
					break;
				}
		}
	}
}
