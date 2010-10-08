package org.gitools.ui.analysis.htest.wizard;

import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.SelectFilePage;
import org.gitools.ui.analysis.wizard.ModulesPage;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import java.io.File;

import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.FileSuffixes;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;

import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class OncodriverAnalysisWizard extends AbstractWizard {

	private SelectFilePage dataPage;
	private DataFilterPage dataFilterPage;
	private ModulesPage modulesPage;
	private StatisticalTestPage statisticalTestPage;
	private SaveFilePage saveFilePage;
	private AnalysisDetailsPage analysisDetailsPage;

	public OncodriverAnalysisWizard() {
		super();
		
		setTitle("Oncodriver analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ONCODRIVER, 96));
	}
	
	@Override
	public void addPages() {
		// Data
		dataPage = new SelectFilePage();
		addPage(dataPage);

		// Data filtering
		dataFilterPage = new DataFilterPage();
		addPage(dataFilterPage);

		// Modules
		modulesPage = new ModulesPage();
		modulesPage.setMinSize(0);
		modulesPage.setEmptyFileAllowed(true);
		modulesPage.setTitle("Select sets of columns to be analysed independently");
		modulesPage.setMessage(MessageStatus.INFO, "If no file is selected then all data columns will be analysed as one set");
		addPage(modulesPage);

		// Statistical test
		statisticalTestPage = new StatisticalTestPage();
		addPage(statisticalTestPage);

		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
		saveFilePage.setFormats(new FileFormat[] {
			FileFormats.ONCODRIVE });
		saveFilePage.setFormatsVisible(false);
		addPage(saveFilePage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page == saveFilePage && page.isComplete();

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
		return dataPage.getFile();
	}

	public File getPopulationFile() {
		return dataFilterPage.getPopulationFile();
	}

	public String getModulesFileMime() {
		return modulesPage.getFileMime();
	}

	public File getModulesFile() {
		return modulesPage.getSelectedFile();
	}

	public OncodriveAnalysis getAnalysis() {
		OncodriveAnalysis analysis = new OncodriveAnalysis();

		analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
		analysis.setDescription(analysisDetailsPage.getAnalysisNotes());
		analysis.setAttributes(analysisDetailsPage.getAnalysisAttributes());

		analysis.setBinaryCutoffEnabled(dataFilterPage.isBinaryCutoffEnabled());
		analysis.setBinaryCutoffCmp(dataFilterPage.getBinaryCutoffCmp());
		analysis.setBinaryCutoffValue(dataFilterPage.getBinaryCutoffValue());
		analysis.setMinModuleSize(modulesPage.getMinSize());
		analysis.setMaxModuleSize(modulesPage.getMaxSize());
		analysis.setTestConfig(statisticalTestPage.getTestConfig());
		analysis.setMtc(statisticalTestPage.getMtc());

		return analysis;
	}
}
