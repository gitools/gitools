package org.gitools.ui.common.wizard;


import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class FilteredListPage extends AbstractWizardPage {

	private FilteredListPanel panel;

	public FilteredListPage() {
	}
	
	public FilteredListPage(Object[] listData) {
		panel.setListData(listData);
	}

	public FilteredListPage(Object[] listData, int selectionMode) {
		panel.setListData(listData);
		panel.setSelectionMode(selectionMode);
	}

	@Override
	public JComponent createControls() {
		panel = new FilteredListPanel() {
			@Override protected void selectionChanged() {
				FilteredListPage.this.selectionChanged(); } };
		
		return panel;
	}

	private void selectionChanged() {
		Object value = panel.getSelectedValue();
		setComplete(value != null);
	}

	public void setSelectionMode(int mode) {
		panel.setSelectionMode(mode);
	}

	public String getFilterText() {
		return panel.getFilterText();
	}
	
	protected void setListData(Object[] listData) {
		panel.setListData(listData);
	}
	
	public Object getSelection() {
		return panel.getSelectedValue();
	}
}
