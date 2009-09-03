package org.gitools.ui.wizard.analysis;

import javax.swing.JFileChooser;

import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.common.FileChooserPage;

public class EnrichmentAnalysisWizard extends AbstractWizard {

	private static final String PAGE_SELECT_DEST_DIR = "SelectDestDir";
	private static final String PAGE_SELECT_DATA_FILE = "SelectDataFile";
	private static final String PAGE_DATA_FILTERING_OPT = "DataFilteringOptions";
	private static final String PAGE_MODULES_SRC = "ModulesSrc";
	private static final String PAGE_SELECT_MODULES_FILE = "SelectModulesFile";
	private static final String PAGE_MODULE_FILTERING_OPT = "ModuleFilteringOptions";
	private static final String PAGE_STATISTICAL_TEST = "StatisticalTest";
	
	private FileChooserPage selectDestDirPage;
	private FileChooserPage selectDataFilePage;
	private DataFilteringPage dataFilteringPage;
	private ModuleSourcePage moduleSourcePage;
	private FileChooserPage selectModuleFilePage;
	private ModuleFilteringPage moduleFilteringPage;
	private StatisticalTestPage statisticalTestPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("New enrichment analysis");
	}
	
	@Override
	public void addPages() {
		selectDestDirPage = new FileChooserPage(
				"Select destination folder",
				JFileChooser.DIRECTORIES_ONLY);
		
		addPage(PAGE_SELECT_DEST_DIR, selectDestDirPage);

		selectDataFilePage = new FileChooserPage("Select data file");
		
		addPage(PAGE_SELECT_DATA_FILE, selectDataFilePage);
		
		dataFilteringPage = new DataFilteringPage();
		
		addPage(PAGE_DATA_FILTERING_OPT, dataFilteringPage);
		
		moduleSourcePage = new ModuleSourcePage();
		
		addPage(PAGE_MODULES_SRC, moduleSourcePage);
		
		selectModuleFilePage = new FileChooserPage("Select modules file");
		
		addPage(PAGE_SELECT_MODULES_FILE, selectModuleFilePage);
		
		moduleFilteringPage = new ModuleFilteringPage();
		
		addPage(PAGE_MODULE_FILTERING_OPT, moduleFilteringPage);
		
		statisticalTestPage = new StatisticalTestPage();
		
		addPage(PAGE_STATISTICAL_TEST, statisticalTestPage);
	}

}
