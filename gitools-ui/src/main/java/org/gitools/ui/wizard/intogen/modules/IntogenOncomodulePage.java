package org.gitools.ui.wizard.intogen.modules;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

@Deprecated
public class IntogenOncomodulePage extends AbstractWizardPage {

	private IntogenOncomodulePanel panel;

	public IntogenOncomodulePage() {
		setTitle("Download modules from IntOGen");
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenOncomodulePanel();
		
		panel.sourceCbox.addItemListener(new ItemListener() {
			@Override public void itemStateChanged(ItemEvent e) {
				boolean isExp = panel.sourceCbox.getSelectedItem()
								.toString().equals("Experiment");
				panel.experimentLabel.setVisible(isExp);
				panel.experimentCbox.setVisible(isExp);
			}
			
		});
		
		return panel;
	}

}
