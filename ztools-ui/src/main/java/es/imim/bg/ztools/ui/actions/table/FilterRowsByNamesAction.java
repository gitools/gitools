package es.imim.bg.ztools.ui.actions.table;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.ITableContents;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.dialogs.FilterRowsByNameDialog;

public class FilterRowsByNamesAction extends BaseAction {

	private static final long serialVersionUID = -1582437709508438222L;

	public FilterRowsByNamesAction() {
		super("Filter rows by name...");	
		setDesc("Filter rows by name");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		final ITable table = getTable();
		if (table == null)
			return;
		
		// Show dialog
		FilterRowsByNameDialog d = 
			new FilterRowsByNameDialog(AppFrame.instance());
		
		List<String> names = d.getNameList();
		boolean regexChecked = d.isRegexChecked();
		
		if(names == null)
			return;
		
		final ITableContents contents = table.getContents();

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
		table.setVisibleRows(visibleRows);
		
		AppFrame.instance()
			.setStatusText("Total visible rows = " + rows.size());
	}

	private void filterByNames(List<String> names, ITableContents contents, List<Integer> rows) {
		
		int rowCount = contents.getRowCount();
		
		Map<String, Integer> nameToRowMap = new HashMap<String, Integer>();
		
		for (int i = 0; i < rowCount; i++) {
			final String rowName = contents.getRow(i).toString();
			nameToRowMap.put(rowName, i);
		}
		
		for (int i = 0; i < names.size(); i++) {
			final String name = names.get(i);
			Integer row = nameToRowMap.get(name);
			if (row != null)
				rows.add(row);
		}
	}

	private void filterByRegex(List<String> names, ITableContents contents, List<Integer> rows) {

		// Compile patterns
		List<Pattern> patterns = new ArrayList<Pattern>(names.size());
		for (String name : names)
			patterns.add(Pattern.compile(name));
				
		int rowCount = contents.getRowCount();
		
		// Check patterns
		for (int i = 0; i < rowCount; i++) {
			final String rowName = contents.getRow(i).toString();
			for (int j = 0; j < patterns.size(); j++)
				if (patterns.get(j).matcher(rowName).matches()) {
					rows.add(i);
					break;
				}
		}
	}
}
