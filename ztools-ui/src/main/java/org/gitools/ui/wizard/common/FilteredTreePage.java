package org.gitools.ui.wizard.common;

import javax.swing.JComponent;

import org.gitools.ui.wizard.AbstractWizardPage;

public class FilteredTreePage extends AbstractWizardPage {

	protected FilteredTreePanel panel;

	@Override
	public JComponent createControls() {
		panel = new FilteredTreePanel();
		return panel;
	}

}
