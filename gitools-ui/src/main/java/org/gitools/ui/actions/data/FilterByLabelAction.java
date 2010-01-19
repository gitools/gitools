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

import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.LabelFilterDialog;

public class FilterByLabelAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterByLabelAction() {
		super("Filter by label...");
		setDesc("Filter by label");
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = ActionUtils.getMatrixView();
		if (matrixView == null)
			return;

		LabelFilterDialog dlg = new LabelFilterDialog(AppFrame.instance());

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
