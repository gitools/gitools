package org.gitools.ui.wizard.common;


import javax.swing.JComponent;
import javax.swing.tree.TreeModel;

import org.gitools.ui.wizard.AbstractWizardPage;

public abstract class FilteredTreePage extends AbstractWizardPage {

	protected FilteredTreePanel panel;
	
	@Override
	public JComponent createControls() {
		panel = new FilteredTreePanel() {
			@Override protected TreeModel createModel(String filterText) {
				return pageCreateModel(filterText);
			}
		};
		
		return panel;
	}
	
	protected abstract TreeModel createModel(String filterText);

	protected TreeModel pageCreateModel(String filterText) {
		return createModel(filterText);
	}

	public FilteredTreePanel getPanel() {
		return panel;
	}
}
