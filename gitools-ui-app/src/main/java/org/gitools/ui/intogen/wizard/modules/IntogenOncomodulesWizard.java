package org.gitools.ui.intogen.wizard.modules;

import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class IntogenOncomodulesWizard extends AbstractWizard {

	private IntogenOncomoduleSetPage dataSetPage;
	private IntogenOncomodulesListPage tumorTypePage;
	private IntogenOncomodulesListPage conditionPage;
	private IntogenOncomodulesListPage experimentsPage;

	public IntogenOncomodulesWizard() {
		super();
		
		setTitle("Import IntOGen cancer modules");
	}
	
	@Override
	public void addPages() {
		// Oncomodules Set
		dataSetPage = new IntogenOncomoduleSetPage();
		addPage(dataSetPage);
		
		experimentsPage = new IntogenOncomodulesListPage();
		experimentsPage.setTitle("Select experiments");
		experimentsPage.getListModel().addElement("Experiment 1");
		experimentsPage.getListModel().addElement("Experiment 2");
		addPage(experimentsPage);
		
		tumorTypePage = new IntogenOncomodulesListPage();
		tumorTypePage.setTitle("Select tumor types");
		tumorTypePage.getListModel().addElement("breast; any");
		tumorTypePage.getListModel().addElement("breast; adenocarcinoma, nos");
		addPage(tumorTypePage);
		
		conditionPage = new IntogenOncomodulesListPage();
		conditionPage.setTitle("Select conditions");
		conditionPage.getListModel().addElement("gain");
		conditionPage.getListModel().addElement("loss");
		addPage(conditionPage);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == dataSetPage) {
			if (dataSetPage.isExperimentsSelected())
				return experimentsPage;
			else if (dataSetPage.isCombinationsSelected())
				return tumorTypePage;
		}
		return super.getNextPage(page);
	}

}
