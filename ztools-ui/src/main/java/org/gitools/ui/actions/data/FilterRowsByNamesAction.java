package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.gitools.ui.actions.BaseAction;
import org.gitools.ui.dialog.filter.FilterRowsByNameDialog;
import org.gitools.ui.platform.AppFrame;

import org.gitools.model.figure.heatmap.Heatmap;
import org.gitools.model.matrix.IMatrix;
import org.gitools.model.matrix.IMatrixView;

public class FilterRowsByNamesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterRowsByNamesAction() {
		super("Filter rows by name...");	
		setDesc("Filter rows by name");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
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

		// List of rows that will be shown
		List<Integer> rows = new ArrayList<Integer>();
		if (regexChecked)
			filterByRegex(names, contents, rows);
		else
			filterByNames(names, contents, rows);
		
		// Change visibility of rows in the table
		int[] visibleRows = new int[rows.size()];
		for (int i = 0; i < rows.size(); i++)
			visibleRows[i] = rows.get(i);
		matrixView.setVisibleRows(visibleRows);
		
		AppFrame.instance()
			.setStatusText("Total visible rows = " + rows.size());
	}

	private void filterByNames(List<String> names, IMatrix contents, List<Integer> rows) {
		
		int rowCount = contents.getRowCount();
		
		Map<String, Integer> nameToRowMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < rowCount; i++) {
			final String rowName = contents.getRowLabel(i);
			nameToRowMap.put(rowName, i);
		}
		
		for (int i = 0; i < names.size(); i++) {
			final String name = names.get(i);
			Integer row = nameToRowMap.get(name);
			if (row != null)
				rows.add(row);
		}
	}

	private void filterByRegex(List<String> names, IMatrix contents, List<Integer> rows) {

		// Compile patterns
		List<Pattern> patterns = new ArrayList<Pattern>(names.size());
		for (String name : names)
			patterns.add(Pattern.compile(name));
				
		int rowCount = contents.getRowCount();
		
		// Check patterns
		for (int i = 0; i < rowCount; i++) {
			final String rowName = contents.getRowLabel(i);
			for (int j = 0; j < patterns.size(); j++)
				if (patterns.get(j).matcher(rowName).matches()) {
					rows.add(i);
					break;
				}
		}
	}
}
