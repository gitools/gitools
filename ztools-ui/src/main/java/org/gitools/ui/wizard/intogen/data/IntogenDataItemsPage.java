package org.gitools.ui.wizard.intogen.data;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class IntogenDataItemsPage extends AbstractWizardPage {

	private IntogenDataItemsPanel panel;

	public IntogenDataItemsPage() {
		setTitle("Select items type");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new IntogenDataItemsPanel();
		
		panel.itemsCbox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.annList.removeAll();
			}
		});
		
		panel.annChk.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				boolean isSel = panel.annChk.isSelected();
				panel.annList.setEnabled(isSel);
				panel.annAddBtn.setEnabled(isSel);
				panel.annRemoveBtn.setEnabled(isSel);
			}
		});
		
		return panel;
	}

}
