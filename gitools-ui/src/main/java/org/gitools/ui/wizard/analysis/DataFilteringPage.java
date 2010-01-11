package org.gitools.ui.wizard.analysis;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.ui.platform.wizard.AbstractWizardPage;

public class DataFilteringPage extends AbstractWizardPage {

	private DataFilteringPanel panel;

	public DataFilteringPage() {
		setTitle("Select data filtering options");
		setComplete(true);
	}
	
	@Override
	public JComponent createControls() {
		panel = new DataFilteringPanel();
		panel.cutoffCondCbox.setModel(new DefaultComboBoxModel(CutoffCmp.comparators));
		
		panel.cutoffValueField.getDocument().addDocumentListener(new DocumentListener() {
			@Override public void removeUpdate(DocumentEvent e) {
				validateCutoffValue(); }
			@Override public void insertUpdate(DocumentEvent e) {
				validateCutoffValue(); }
			@Override public void changedUpdate(DocumentEvent e) {
				validateCutoffValue(); }
		});
		
		return panel;
	}

	private void validateCutoffValue() {
		boolean fail = false;
		try {
			Double.valueOf(panel.cutoffValueField.getText());}
		catch (NumberFormatException e) {
			fail = true;
		}
		
		if (fail) {
			setMessage("Cutoff value should be a real number.");
		}
	}

	public boolean getBinaryCutoffEnabled() {
		return panel.cutoffChk.isSelected();
	}
	
	public BinaryCutoff getBinaryCutoff() {
		CutoffCmp cmp = (CutoffCmp) panel.cutoffCondCbox.getSelectedItem();
		
		double cutoff = 0.0;
		
		try {
			cutoff = Double.valueOf(panel.cutoffValueField.getText()); }
		catch (Exception e) { }
		
		return new BinaryCutoff(cmp, cutoff);
	}

}
