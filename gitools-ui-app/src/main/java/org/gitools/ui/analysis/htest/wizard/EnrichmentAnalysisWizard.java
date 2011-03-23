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

package org.gitools.ui.analysis.htest.wizard;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.ModulesPage;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import java.io.File;
import java.util.Properties;
import javax.swing.SwingUtilities;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.ui.examples.ExamplesManager;

import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileSuffixes;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.xml.AbstractXmlPersistence;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.DataFilePage;
import org.gitools.ui.analysis.wizard.ExamplePage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.wizard.common.SaveFilePage;

public class EnrichmentAnalysisWizard extends AbstractWizard {

	private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + FileSuffixes.ENRICHMENT;
	private static final String EXAMPLE_DATA_FILE = "20_tumor_types_upreg_annot.cdm.gz";
	private static final String EXAMPLE_MODULES_FILE = "KEGG_pathways_descr__ensembl_gene.tcm";

	private ExamplePage examplePage;
	private DataFilePage dataPage;
	private DataFilterPage dataFilterPage;
	private ModulesPage modulesPage;
	private StatisticalTestPage statisticalTestPage;
	private SaveFilePage saveFilePage;
	private AnalysisDetailsPage analysisDetailsPage;
	
	public EnrichmentAnalysisWizard() {
		super();
		
		setTitle("Enrichment analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ENRICHMENT, 96));
		setHelpContext("analysis_enrichment");
	}
	
	@Override
	public void addPages() {
		// Example
		if (Settings.getDefault().isShowCombinationExamplePage()) {
			examplePage = new ExamplePage("an enrichment analysis");
			examplePage.setTitle("Enrichment analysis");
			addPage(examplePage);
		}

		// Data
		dataPage = new DataFilePage();
		addPage(dataPage);

		// Data filtering
		dataFilterPage = new DataFilterPage();
		dataFilterPage.setDiscardNonMappedRowsVisible(true);
		addPage(dataFilterPage);
		
		// Modules
		modulesPage = new ModulesPage();
		addPage(modulesPage);
		
		// Statistical test
		statisticalTestPage = new StatisticalTestPage();
		addPage(statisticalTestPage);

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

						final File basePath = ExamplesManager.getDefault().resolvePath("enrichment", monitor);

						if (basePath == null)
							throw new RuntimeException("Unexpected error: There are no examples available");

						File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);
						Properties props = new Properties();
						props.setProperty(AbstractXmlPersistence.LOAD_REFERENCES_PROP, "false");
						try {
							monitor.begin("Loading example parameters ...", 1);

							final EnrichmentAnalysis a = (EnrichmentAnalysis) PersistenceManager.getDefault()
									.load(analysisFile, props, monitor);

							SwingUtilities.invokeLater(new Runnable() {
								@Override public void run() {
									setAnalysis(a);

									dataPage.setFile(new File(basePath, EXAMPLE_DATA_FILE));
									modulesPage.setSelectedFile(new File(basePath, EXAMPLE_MODULES_FILE));
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
		IWizardPage page = getCurrentPage();

		boolean canFinish = super.canFinish();
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

	public Double getPopulationDefaultValue() {
		return dataFilterPage.getPopulationDefaultValue();
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
		
		analysis.setBinaryCutoffEnabled(dataFilterPage.isBinaryCutoffEnabled());
		analysis.setBinaryCutoffCmp(dataFilterPage.getBinaryCutoffCmp());
		analysis.setBinaryCutoffValue(dataFilterPage.getBinaryCutoffValue());
		analysis.setDiscardNonMappedRows(dataFilterPage.isDiscardNonMappedRowsEnabled());
		analysis.setMinModuleSize(modulesPage.getMinSize());
		analysis.setMaxModuleSize(modulesPage.getMaxSize());
		analysis.setTestConfig(statisticalTestPage.getTestConfig());
		analysis.setMtc(statisticalTestPage.getMtc());

		return analysis;
	}

	private void setAnalysis(EnrichmentAnalysis a) {
		analysisDetailsPage.setAnalysisTitle(a.getTitle());
		analysisDetailsPage.setAnalysisNotes(a.getDescription());
		analysisDetailsPage.setAnalysisAttributes(a.getAttributes());
		dataFilterPage.setBinaryCutoffEnabled(a.isBinaryCutoffEnabled());
		dataFilterPage.setBinaryCutoffCmp(a.getBinaryCutoffCmp());
		dataFilterPage.setBinaryCutoffValue(a.getBinaryCutoffValue());
		modulesPage.setMinSize(a.getMinModuleSize());
		modulesPage.setMaxSize(a.getMaxModuleSize());
		statisticalTestPage.setTestConfig(a.getTestConfig());
		statisticalTestPage.setMtc(a.getMtc());
	}
}
