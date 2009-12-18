package org.gitools.ui.wizard.analysis;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class DataSourcePage extends AbstractWizardPage {

	private DataSourcePanel panel;

	public DataSourcePage() {
		setTitle("Select data source");
	}
	
	@Override
	public JComponent createControls() {
		ItemListener itemListener = new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				updateComplete();
			}
		};
		
		panel = new DataSourcePanel();
		panel.fileRbt.addItemListener(itemListener);
		panel.intogenRbt.addItemListener(itemListener);
		panel.biomartRbt.addItemListener(itemListener);
		
		return panel;
	}
	
	private void updateComplete() {
		setComplete(panel.fileRbt.isSelected() 
				|| panel.intogenRbt.isSelected()
				|| panel.biomartRbt.isSelected());
	}

	public boolean isFileSelected() {
		return panel.fileRbt.isSelected();
	}
	
	public boolean isIntogenSelected() {
		return panel.intogenRbt.isSelected();
	}
	
	public boolean isBiomartSelected() {
		return panel.biomartRbt.isSelected(); 
	}
}
