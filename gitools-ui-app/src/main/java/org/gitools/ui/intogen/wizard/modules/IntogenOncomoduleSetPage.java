package org.gitools.ui.intogen.wizard.modules;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class IntogenOncomoduleSetPage extends AbstractWizardPage {

	private IntogenOncomoduleSetPanel panel;

	public IntogenOncomoduleSetPage() {
		super();
		
		setTitle("Select oncomodules set");
	}
	
	@Override
	public JComponent createControls() {
		ActionListener listener = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				updateComplete();
			}
		};
		
		panel = new IntogenOncomoduleSetPanel();
		panel.combinationsRbt.addActionListener(listener);
		panel.experimentsRbt.addActionListener(listener);
		return panel;
	}

	private void updateComplete() {
		boolean isCompleted = panel.experimentsRbt.isSelected()
						|| panel.combinationsRbt.isSelected();
		setComplete(isCompleted);
	}

	public boolean isExperimentsSelected() {
		return panel.experimentsRbt.isSelected();
	}
	
	public boolean isCombinationsSelected() {
		return panel.combinationsRbt.isSelected();
	}
}
