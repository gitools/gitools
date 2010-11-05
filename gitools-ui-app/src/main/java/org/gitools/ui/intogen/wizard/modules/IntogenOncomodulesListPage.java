package org.gitools.ui.intogen.wizard.modules;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenOncomodulesListPage extends AbstractWizardPage {
	
	private IntogenOncomodulesListPanel panel;
	private DefaultListModel listModel;

	public IntogenOncomodulesListPage() {
		listModel = new DefaultListModel();
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenOncomodulesListPanel();

		panel.list.setModel(listModel);
		updateComplete();
		
		panel.list.getModel().addListDataListener(new ListDataListener() {
			@Override public void contentsChanged(ListDataEvent e) {
				updateComplete(); }
			@Override public void intervalAdded(ListDataEvent e) {
				updateComplete(); }
			@Override public void intervalRemoved(ListDataEvent e) {
				updateComplete(); }
		});
		
		return panel;
	}
	
	private void updateComplete() {
		setComplete(panel.list.getModel().getSize() != 0);
	}
	
	public DefaultListModel getListModel() {
		return listModel;
	}

}
