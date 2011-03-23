/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.analysis.correlation.wizard;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.Properties;
import javax.swing.SwingUtilities;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.xml.AbstractXmlPersistence;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.DataFilePage;
import org.gitools.ui.analysis.wizard.ExamplePage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class CorrelationAnalysisFromFileWizard extends AbstractWizard {

	private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + FileSuffixes.CORRELATIONS;
	private static final String EXAMPLE_DATA_FILE = "8_kidney_6_brain_downreg_annot.cdm.gz";

	private ExamplePage examplePage;
	private DataFilePage dataPage;
	private DataFilterPage dataFilterPage;
	protected CorrelationFromFilePage corrPage;
	private SaveFilePage saveFilePage;
	protected AnalysisDetailsPage analysisDetailsPage;

	public CorrelationAnalysisFromFileWizard() {
		super();

		setTitle("Correlation analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CORRELATION, 96));
		setHelpContext("analysis_correlation");
	}

	@Override
	public void addPages() {
		// Example
		if (Settings.getDefault().isShowCombinationExamplePage()) {
			examplePage = new ExamplePage("a correlation analysis");
			examplePage.setTitle("Correlation analysis");
			addPage(examplePage);
		}

		// Data
		dataPage = new DataFilePage();
		addPage(dataPage);

		// Data filters
		dataFilterPage = new DataFilterPage();
		dataFilterPage.setRowsFilterFileVisible(false);
		addPage(dataFilterPage);

		// Correlation method
		corrPage = new CorrelationFromFilePage();
		addPage(corrPage);

		// Destination
		saveFilePage = new SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
		saveFilePage.setFormats(new FileFormat[] {
			FileFormats.CORRELATIONS });
		saveFilePage.setFormatsVisible(false);
		addPage(saveFilePage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public void pageLeft(IWizardPage currentPage) {
		if (currentPage == examplePage) {
			Settings.getDefault().setShowCombinationExamplePage(
					examplePage.isShowAgain());

			if (examplePage.isExampleEnabled()) {
				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override public void run(IProgressMonitor monitor) {

						final File basePath = ExamplesManager.getDefault().resolvePath("correlations", monitor);

						if (basePath == null)
							throw new RuntimeException("Unexpected error: There are no examples available");

						File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);
						Properties props = new Properties();
						props.setProperty(AbstractXmlPersistence.LOAD_REFERENCES_PROP, "false");
						try {
							monitor.begin("Loading example parameters ...", 1);

							final CorrelationAnalysis a = (CorrelationAnalysis) PersistenceManager.getDefault()
									.load(analysisFile, props, monitor);

							SwingUtilities.invokeLater(new Runnable() {
								@Override public void run() {
									setAnalysis(a);

									dataPage.setFile(new File(basePath, EXAMPLE_DATA_FILE));
									saveFilePage.setFileNameWithoutExtension("example");
								}
							});

							monitor.end();
						}
						catch (Exception ex) {
							monitor.exception(ex);
						}
					}
				});
			}
		}
	}

	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page.isComplete() && (page == saveFilePage);

		return canFinish;
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastWorkPath(saveFilePage.getFolder());
	}

	public String getWorkdir() {
		return saveFilePage.getFolder();
	}

	public String getFileName() {
		return saveFilePage.getFileName();
	}

	public String getDataFileMime() {
		return dataPage.getFileFormat().getMime();
	}

	public File getDataFile() {
		return dataPage.getFile();
	}

	public File getPopulationFile() {
		return dataFilterPage.getRowsFilterFile();
	}

	public CorrelationAnalysis getAnalysis() {
		CorrelationAnalysis a = new CorrelationAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());

		//a.setAttributeIndex(corrPage.getAttributeIndex());
		a.setReplaceNanValue(corrPage.isReplaceNanValuesEnabled() ?
				corrPage.getReplaceNanValue() : null);
		a.setTransposeData(corrPage.isTransposeEnabled());
		
		return a;
	}

	private void setAnalysis(CorrelationAnalysis a) {
		analysisDetailsPage.setAnalysisTitle(a.getTitle());
		analysisDetailsPage.setAnalysisNotes(a.getDescription());
		analysisDetailsPage.setAnalysisAttributes(a.getAttributes());
		corrPage.setReplaceNanValuesEnabled(a.getReplaceNanValue() != null);
		if (a.getReplaceNanValue() != null)
			corrPage.setReplaceNanValue(a.getReplaceNanValue());
		corrPage.setTransposeEnabled(a.isTransposeData());
	}
}
