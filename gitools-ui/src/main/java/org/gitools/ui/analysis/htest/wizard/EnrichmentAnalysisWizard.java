package org.gitools.ui.analysis.htest.wizard;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.io.File;

import org.gitools.datafilters.BinaryCutoff;
import org.gitools.fileutils.FileFormat;
import org.gitools.model.ToolConfig;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.FileChooserPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class EnrichmentAnalysisWizard extends AbstractWizard {
	
	private SaveFilePage saveFilePage;
	private DataPage dataSourcePage;
	private FileChooserPage moduleFilePage;
	private ModulesPage modulesPage;
	private StatisticalTestPage statisticalTestPage;
	private AnalysisDetailsPage analysisDetailsPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("New enrichment analysis");
	}
	
	@Override
	public void addPages() {
		
		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
		saveFilePage.setFormats(new FileFormat[] {
			new FileFormat("Enrichment analysis (*." 
					+ FileSuffixes.ENRICHMENT_ANALYSIS + ")",
					FileSuffixes.ENRICHMENT_ANALYSIS) });
		saveFilePage.setFormatsVisible(false);
		addPage(saveFilePage);

		// Data
		dataSourcePage = new DataPage();
		addPage(dataSourcePage);
		
		// Modules
		modulesPage = new ModulesPage();
		addPage(modulesPage);
		
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
	public void performCancel() {
		super.performCancel();
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastWorkPath(saveFilePage.getFolder());
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
		return dataSourcePage.getSelectedFile();
	}

	public boolean isBinaryCutoffEnabled() {
		return dataSourcePage.isBinaryCutoffEnabled();
	}

	public CutoffCmp getDataBinaryCutoffCmp() {
		return dataSourcePage.getBinaryCutoffCmp();
	}
}
