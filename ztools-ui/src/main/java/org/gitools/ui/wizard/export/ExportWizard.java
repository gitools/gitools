package org.gitools.ui.wizard.export;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.List;

import org.gitools.ui.dialog.DialogHeaderPanel.Status;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.WizardDialog;

public class ExportWizard extends WizardDialog {

	private static final long serialVersionUID = -6058042494975580570L;
	
	public ExportWizard(Window owner) {
		super(owner,
				"Export", 
				"Select", 
				"Choose export destination", 
				Status.normal, 
				null);
		
		setMinimumSize(new Dimension(600, 400));
		setPreferredSize(new Dimension(600, 400));
	}

	@Override
	public List<IWizardPage> createPages() {
		List<IWizardPage> pages = new ArrayList<IWizardPage>();
		pages.add(new ExportChooserPage());
		return pages;
	}
	
	@Override
	protected boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}
}
