package org.gitools.ui.wizard.analysis;

import java.io.File;

import javax.swing.JFileChooser;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.model.ToolConfig;
import org.gitools.ui.utils.Options;
import org.gitools.ui.wizard.AbstractWizard;
import org.gitools.ui.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;

public class EnrichmentAnalysisWizard extends AbstractWizard {
	
	private FileChooserPage selectDestDirPage;
	private FileChooserPage dataFilePage;
	private DataFilteringPage dataFilteringPage;
	private FileChooserPage moduleFilePage;
	private ModuleFilteringPage moduleFilteringPage;
	private StatisticalTestPage statisticalTestPage;
	private AnalysisDetailsPage analysisDetailsPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("New enrichment analysis");
	}
	
	@Override
	public void addPages() {
		// Destination directory
		
		selectDestDirPage = new FileChooserPage();
		selectDestDirPage.setTitle("Select destination folder");
		selectDestDirPage.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		selectDestDirPage.setSelectedFile(
				new File(Options.instance().getLastWorkPath()));
		addPage(selectDestDirPage);

		// Data file
		
		dataFilePage = new FileChooserPage();
		dataFilePage.setTitle("Select data file");
		dataFilePage.setCurrentPath(
				new File(Options.instance().getLastDataPath()));
		addPage(dataFilePage);
		
		// Data filtering
		
		dataFilteringPage = new DataFilteringPage();
		addPage(dataFilteringPage);
		
		// Module file
		
		moduleFilePage = new FileChooserPage();
		moduleFilePage.setTitle("Select modules file");
		moduleFilePage.setCurrentPath(
				new File(Options.instance().getLastMapPath()));
		addPage(moduleFilePage);
		
		// Module filtering
		
		moduleFilteringPage = new ModuleFilteringPage();
		addPage(moduleFilteringPage);
		
		// Statistical test
		
		statisticalTestPage = new StatisticalTestPage();
		addPage(statisticalTestPage);
		
		// Analysis details
		
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}
	
	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();
		
		IWizardPage page = getCurrentPage();
		
		canFinish |= page == statisticalTestPage && page.isComplete();
		
		return canFinish;
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		/*if (page == dataSourcePage) {
			if (dataSourcePage.isFileSelected())
				return dataFilePage;
			else if (dataSourcePage.isIntogenSelected())
				return intogenDataSetPage;
			else if (dataSourcePage.isBiomartSelected())
				return null;
		}
		else if (page == dataFilePage 
				|| page == intogenDataResultsPage)
			return dataFilteringPage;
		
		else if (page == moduleSourcePage) {
			if (moduleSourcePage.isFileSelected())
				return moduleFilePage;
			else if (moduleSourcePage.isIntogenSelected())
				return intogenOncomodulePage;
			else if (moduleSourcePage.isBiomartSelected())
				return null;
		}
		else if (page == moduleFilePage
				|| page == intogenOncomodulePage)
			return moduleFilteringPage;*/
		
		return super.getNextPage(page);
	}

	public String getAnalysisTitle() {
		return analysisDetailsPage.getAnalysisTitle();
	}

	public String getAnalysisNotes() {
		return analysisDetailsPage.getAnalysisNotes();
	}

	public ToolConfig getTestConfig() {
		return statisticalTestPage.getTestConfig();
	}

	public File getDataFile() {
		return dataFilePage.getSelectedFile();
	}

	public boolean getDataBinaryCutoffEnabled() {
		return dataFilteringPage.getBinaryCutoffEnabled();
	}

	public BinaryCutoff getDataBinaryCutoff() {
		return dataFilteringPage.getBinaryCutoff();
	}
}
