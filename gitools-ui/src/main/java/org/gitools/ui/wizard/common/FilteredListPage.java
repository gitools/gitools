package org.gitools.ui.wizard.common;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.ListModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.gitools.ui.wizard.AbstractWizardPage;

public class FilteredListPage extends AbstractWizardPage {

	private FilteredListPanel panel;
	
	private Object[] listData;
	
	private String lastFilterText = "";
	
	@Override
	public JComponent createControls() {
		panel = new FilteredListPanel();
		
		panel.filterField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void changedUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
			@Override public void insertUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
			@Override public void removeUpdate(DocumentEvent e) {
				updateFilterActionPerformed(); }
		});
		
		panel.clearBtn.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				panel.filterField.setText("");
				panel.filterField.requestFocusInWindow();
			}
		});
		
		panel.list.addListSelectionListener(new ListSelectionListener() {
			@Override public void valueChanged(ListSelectionEvent e) {
				selectionChangeActionPerformed();
			}
		});
		
		updateFilterActionPerformed();
		
		return panel;
	}
	
	private String getFilterText() {
		return panel.filterField.getText();
	}
	
	private void updateFilterActionPerformed() {
		final String filterText = getFilterText();
		
		panel.clearBtn.setEnabled(!filterText.isEmpty());
		
		if (!filterText.equalsIgnoreCase(lastFilterText)) {
			lastFilterText = filterText;
			panel.list.setModel(
					createListModel(listData, getFilterText()));
		}
	}
	
	private void selectionChangeActionPerformed() {
		Object value = panel.list.getSelectedValue();
		setComplete(value != null);
	}
	
	private ListModel createListModel(Object[] listData, String filterText) {
		final DefaultListModel model = new DefaultListModel();
		
		if (filterText != null && !filterText.isEmpty()) {
			final String filter = filterText.toLowerCase();
			for (Object dataObject : listData) {
				String dataText = dataObject.toString().toLowerCase();
				if (dataText.contains(filter))
					model.addElement(dataObject);
			}
		}
		else {
			for (Object dataObject : listData)
				model.addElement(dataObject);
		}
		
		return model;
	}
	
	protected void setListData(Object[] listData) {
		this.listData = listData;
		ListModel model = createListModel(listData, getFilterText());
		panel.list.setModel(model);
		selectionChangeActionPerformed();
	}
	
	public Object getSelection() {
		return panel.list.getSelectedValue();
	}
}
