package org.gitools.ui.analysis.htest.wizard;

import java.io.File;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;

import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class EnrichmentAnalysisWizard extends AbstractWizard {
	
	private SaveFilePage saveFilePage;
	private DataPage dataPage;
	private ModulesPage modulesPage;
	private StatisticalTestPage statisticalTestPage;
	private AnalysisDetailsPage analysisDetailsPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("New enrichment analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ENRICHMENT, 96));
	}
	
	@Override
	public void addPages() {
		
		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
		saveFilePage.setFormats(new FileFormat[] {
			new FileFormat("Enrichment analysis (*." 
					+ FileSuffixes.ENRICHMENT + ")",
					FileSuffixes.ENRICHMENT) });
		saveFilePage.setFormatsVisible(false);
		addPage(saveFilePage);

		// Data
		dataPage = new DataPage();
		addPage(dataPage);
		
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

	public String getWorkdir() {
		return saveFilePage.getFolder();
	}

	public String getFileName() {
		return saveFilePage.getFilePath();
	}

	public String getDataFileMime() {
		return dataPage.getFileFormat().getMime();
	}
	
	public File getDataFile() {
		return dataPage.getDataFile();
	}

	public File getPopulationFile() {
		return dataPage.getPopulationFile();
	}
	
	public String getModulesFileMime() {
		return modulesPage.getFileMime();
	}

	public File getModulesFile() {
		return modulesPage.getSelectedFile();
	}

	public EnrichmentAnalysis getAnalysis() {
		EnrichmentAnalysis analysis = new EnrichmentAnalysis();

		analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
		analysis.setDescription(analysisDetailsPage.getAnalysisNotes());
		analysis.setAttributes(analysisDetailsPage.getAnalysisAttributes());
		analysis.setBinaryCutoffEnabled(dataPage.isComplete());
		analysis.setBinaryCutoffCmp(dataPage.getBinaryCutoffCmp());
		analysis.setBinaryCutoffValue(dataPage.getBinaryCutoffValue());
		analysis.setDiscardNonMappedRows(dataPage.isDiscardNonMappedRows());
		analysis.setMinModuleSize(modulesPage.getMinSize());
		analysis.setMaxModuleSize(modulesPage.getMaxSize());
		analysis.setTestConfig(statisticalTestPage.getTestConfig());
		analysis.setMtc(statisticalTestPage.getMtc());

		return analysis;
	}
}
