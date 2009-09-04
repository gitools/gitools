package org.gitools.ui.wizard.analysis;

import javax.swing.JFileChooser;

import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.wizard.intogen.IntogenSamplesPage;
import org.gitools.ui.wizard.intogen.data.IntogenDataItemsPage;
import org.gitools.ui.wizard.intogen.data.IntogenDataResultsPage;
import org.gitools.ui.wizard.intogen.data.IntogenDataSetPage;
import org.gitools.ui.wizard.intogen.modules.IntogenOncomodulePage;

public class EnrichmentAnalysisWizard extends AbstractWizard {

	private static final String PAGE_SELECT_DEST_DIR = "SelectDestDir";
	private static final String PAGE_SELECT_DATA_FILE = "SelectDataFile";
	private static final String PAGE_DATA_FILTERING_OPT = "DataFilteringOptions";
	private static final String PAGE_MODULES_SRC = "ModulesSrc";
	private static final String PAGE_SELECT_MODULES_FILE = "SelectModulesFile";
	private static final String PAGE_MODULE_FILTERING_OPT = "ModuleFilteringOptions";
	private static final String PAGE_STATISTICAL_TEST = "StatisticalTest";
	private static final String PAGE_INTOGEN_ONCOMODULE = "IntogenOncomodule";
	private static final String PAGE_ANALYSIS_DETAILS = "AnalysisDetails";
	private static final String PAGE_DATA_SOURCE = "DataSource";
	private static final String PAGE_INTOGEN_DATA_SET = "IntogenDataSet";
	private static final String PAGE_INTOGEN_SAMPLES = "IntogenSamples";
	private static final String PAGE_INTOGEN_DATA_ITEMS = "IntogenDataItems";
	private static final String PAGE_INTOGEN_DATA_RESULTS = "IntogenDataResults";
	
	private FileChooserPage selectDestDirPage;
	private FileChooserPage selectDataFilePage;
	private DataFilteringPage dataFilteringPage;
	private ModuleSourcePage moduleSourcePage;
	private FileChooserPage selectModuleFilePage;
	private ModuleFilteringPage moduleFilteringPage;
	private StatisticalTestPage statisticalTestPage;
	private IntogenOncomodulePage intogenOncomodulePage;
	private AnalysisDetailsPage analysisDetailsPage;
	private DataSourcePage dataSourcePage;
	private IntogenDataSetPage intogenDataSetPage;
	private IntogenSamplesPage intogenSamplesPage;
	private IntogenDataItemsPage intogenDataItemsPage;
	private IntogenDataResultsPage intogenDataResultsPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("New enrichment analysis");
	}
	
	@Override
	public void addPages() {
		// Destination directory
		
		selectDestDirPage = new FileChooserPage(
				"Select destination folder",
				JFileChooser.DIRECTORIES_ONLY);
		addPage(PAGE_SELECT_DEST_DIR, selectDestDirPage);

		// Data source
		
		dataSourcePage = new DataSourcePage();
		addPage(PAGE_DATA_SOURCE, dataSourcePage);
		
		// Data source > File
		
		selectDataFilePage = new FileChooserPage("Select data file");
		addPage(PAGE_SELECT_DATA_FILE, selectDataFilePage);
		
		// Data source > IntOGen
		
		intogenDataSetPage = new IntogenDataSetPage();
		addPage(PAGE_INTOGEN_DATA_SET, intogenDataSetPage);
		
		// Data source > IntOGen > Samples
		
		intogenSamplesPage = new IntogenSamplesPage();
		addPage(PAGE_INTOGEN_SAMPLES, intogenSamplesPage);
		
		// Data source > IntOGen > Experiments
		// ...
		
		// Data source > IntOGen > Combinations
		// ...
		
		// Data source > IntOGen
		
		intogenDataItemsPage = new IntogenDataItemsPage();
		addPage(PAGE_INTOGEN_DATA_ITEMS, intogenDataItemsPage);
		
		intogenDataResultsPage = new IntogenDataResultsPage();
		addPage(PAGE_INTOGEN_DATA_RESULTS, intogenDataResultsPage);
		
		// Data source > BioMart
		// ...
		
		// Data filtering
		
		dataFilteringPage = new DataFilteringPage();
		addPage(PAGE_DATA_FILTERING_OPT, dataFilteringPage);
		
		// Module source
		
		moduleSourcePage = new ModuleSourcePage();
		addPage(PAGE_MODULES_SRC, moduleSourcePage);
		
		// Module source > File
		
		selectModuleFilePage = new FileChooserPage("Select modules file");
		addPage(PAGE_SELECT_MODULES_FILE, selectModuleFilePage);
		
		// Module source > IntOGen
		
		intogenOncomodulePage = new IntogenOncomodulePage();
		addPage(PAGE_INTOGEN_ONCOMODULE, intogenOncomodulePage);
		
		// Module source > BioMart
		// ...
		
		// Module filtering
		
		moduleFilteringPage = new ModuleFilteringPage();
		addPage(PAGE_MODULE_FILTERING_OPT, moduleFilteringPage);
		
		// Statistical test
		
		statisticalTestPage = new StatisticalTestPage();
		addPage(PAGE_STATISTICAL_TEST, statisticalTestPage);
		
		// Analysis details
		
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(PAGE_ANALYSIS_DETAILS, analysisDetailsPage);
		
	}
	
	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();
		
		IWizardPage page = getCurrentPage();
		
		canFinish |= page.getId().equals(PAGE_STATISTICAL_TEST) && page.isComplete();
		
		return canFinish;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		final String id = page.getId();
		
		if (PAGE_DATA_SOURCE.equals(id)) {
			if (dataSourcePage.isFileSelected())
				return getPage(PAGE_SELECT_DATA_FILE);
			else if (dataSourcePage.isIntogenSelected())
				return getPage(PAGE_INTOGEN_DATA_SET);
			else if (dataSourcePage.isBiomartSelected())
				return null;
		}
		else if (PAGE_SELECT_DATA_FILE.equals(id))
			return getPage(PAGE_DATA_FILTERING_OPT);
		else if (PAGE_INTOGEN_DATA_RESULTS.equals(id))
			return getPage(PAGE_DATA_FILTERING_OPT);
		
		else if (PAGE_MODULES_SRC.equals(id)) {
			if (moduleSourcePage.isFileSelected())
				return getPage(PAGE_SELECT_MODULES_FILE);
			else if (moduleSourcePage.isIntogenSelected())
				return getPage(PAGE_INTOGEN_ONCOMODULE);
			else if (moduleSourcePage.isBiomartSelected())
				return null;
		}
		else if (PAGE_SELECT_MODULES_FILE.equals(id))
			return getPage(PAGE_MODULE_FILTERING_OPT);
		else if (PAGE_INTOGEN_ONCOMODULE.equals(id))
			return getPage(PAGE_MODULE_FILTERING_OPT);
		
		return super.getNextPage(page);
	}

}
