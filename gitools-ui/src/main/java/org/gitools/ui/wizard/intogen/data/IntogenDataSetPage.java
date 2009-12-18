package org.gitools.ui.wizard.intogen.data;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class IntogenDataSetPage extends AbstractWizardPage {

	private IntogenDataSetPanel panel;

	public IntogenDataSetPage() {
		setTitle("Select data set");
	}
	
	@Override
	public JComponent createControls() {
		ItemListener itemListener = new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				updateComplete();
			}
		};
		
		panel = new IntogenDataSetPanel();
		panel.samplesRbt.addItemListener(itemListener);
		panel.experimentsRbt.addItemListener(itemListener);
		panel.combinationsRbt.addItemListener(itemListener);
		
		return panel;
	}

	private void updateComplete() {
		setComplete(panel.samplesRbt.isSelected() 
				|| panel.experimentsRbt.isSelected()
				|| panel.combinationsRbt.isSelected());
	}

	public boolean isSamplesSelected() {
		return panel.samplesRbt.isSelected();
	}
	
	public boolean isExperimentsSelected() {
		return panel.experimentsRbt.isSelected();
	}
	
	public boolean isCombinationsSelected() {
		return panel.combinationsRbt.isSelected();
	}
}
