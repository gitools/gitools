package org.gitools.ui.wizard.export;

import org.gitools.ui.platform.wizard.AbstractWizard;

public class ExportWizard extends AbstractWizard {

	private static final long serialVersionUID = -6058042494975580570L;
	
	private static final String PAGE_EXPORT_CHOOSER = "ExportChooser";
	
	public ExportWizard() {
		super();
		
		setTitle("Export"); 
	}

	@Override
	public void addPages() {
		addPage(PAGE_EXPORT_CHOOSER, new ExportChooserPage());
	}
	
	@Override
	public boolean canFinish() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void performFinish() {
		// TODO Auto-generated method stub
		
	}
}
