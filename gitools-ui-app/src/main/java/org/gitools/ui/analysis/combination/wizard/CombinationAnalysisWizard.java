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

package org.gitools.ui.analysis.combination.wizard;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.*;
import org.gitools.persistence._DEPRECATED.FileFormat;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.formats.matrix.MultiValueMatrixFormat;
import org.gitools.persistence.formats.analysis.AbstractXmlFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.DataFilePage;
import org.gitools.ui.analysis.wizard.ExamplePage;
import org.gitools.ui.analysis.wizard.SelectFilePage;
import org.gitools.ui.examples.ExamplesManager;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

public class CombinationAnalysisWizard extends AbstractWizard {

	private static final String EXAMPLE_ANALYSIS_FILE = "analysis." + FileSuffixes.COMBINATION;
	private static final String EXAMPLE_DATA_FILE = "19_lung_10_breast_upreg_annot.cdm.gz";
	private static final String EXAMPLE_COLUM_SETS_FILE = "lung_breast_experiments_annotated.tcm";

	private static final FileFormat[] dataFormats = new FileFormat[] {
			FileFormats.MULTIVALUE_DATA_MATRIX,
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.MODULES_2C_MAP,
			FileFormats.MODULES_INDEXED_MAP
	};

	private static final FileFormat[] columnSetsFormats = new FileFormat[] {
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.MODULES_2C_MAP,
			FileFormats.MODULES_INDEXED_MAP
	};

	private ExamplePage examplePage;
	private DataFilePage dataPage;
	private CombinationAnalysisParamsPage combinationParamsPage;
	private SelectFilePage columnSetsPage;
	private SaveFilePage saveFilePage;
	private AnalysisDetailsPage analysisDetailsPage;

	private boolean examplePageEnabled;
	private boolean dataFromMemory;
	private List<IElementAttribute> attributes;
	private boolean saveFilePageEnabled;

	private File dataFile;

	public CombinationAnalysisWizard() {
		super();

		examplePageEnabled = true;
		dataFromMemory = false;
		saveFilePageEnabled = true;

		setTitle("Combination analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_COMBINATION, 96));
		setHelpContext("analysis_combination");
	}

	@Override
	public void addPages() {
		// Example
		if (examplePageEnabled && Settings.getDefault().isShowCombinationExamplePage()) {
			examplePage = new ExamplePage("a combination analysis");
			examplePage.setTitle("Combination analysis");
			addPage(examplePage);
		}
		
		// Data
		if (!dataFromMemory) {
			dataPage = new DataFilePage(dataFormats);
			addPage(dataPage);
		}

		// Combination parameters
		combinationParamsPage = new CombinationAnalysisParamsPage();
		combinationParamsPage.setAttributes(attributes);
		addPage(combinationParamsPage);

		// Set of columns
		columnSetsPage = new SelectFilePage(columnSetsFormats) {
			@Override protected String getLastPath() {
				return Settings.getDefault().getLastMapPath(); }

			@Override protected void setLastPath(String path) {
				Settings.getDefault().setLastMapPath(path); }
		};
		columnSetsPage.setTitle("Select sets of columns/rows to combine");
		columnSetsPage.setMessage(MessageStatus.INFO, "Leave blank to combine all the columns");
		columnSetsPage.setBlankFileAllowed(true);
		addPage(columnSetsPage);

		// Destination
		if (saveFilePageEnabled) {
			saveFilePage = new org.gitools.ui.wizard.common.SaveFilePage();
			saveFilePage.setTitle("Select destination file");
			saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
			saveFilePage.setFormats(new FileFormat[] {
				FileFormats.COMBINATION });
			saveFilePage.setFormatsVisible(false);
			addPage(saveFilePage);
		}

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public void pageEntered(IWizardPage page) {
		if (combinationParamsPage.equals(page)) {
			if (!dataFromMemory && (dataFile == null || !dataPage.getFile().equals(dataFile))) {
				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override
					public void run(IProgressMonitor monitor) {
						monitor.begin("Reading data header ...", 1);

                        try {
                            dataFile = dataPage.getFile();

                            String mime = PersistenceManager.get().getMimeFromFile(dataFile.getName());
                            if (MimeTypes.OBJECT_MATRIX.equals(mime)) {
                                attributes = MultiValueMatrixFormat.readMetaAttributes(dataFile, monitor);
                            } else {
                                attributes = null;
                            }

							combinationParamsPage.setAttributes(attributes);
						} catch (Exception ex) {
							monitor.exception(ex);
						}
						monitor.end();
					}
				});
			}
		}
	}

	@Override
	public void pageLeft(IWizardPage currentPage) {
		if (currentPage == examplePage) {
			Settings.getDefault().setShowCombinationExamplePage(
					examplePage.isShowAgain());

			if (examplePage.isExampleEnabled()) {
				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override public void run(IProgressMonitor monitor) {

						final File basePath = ExamplesManager.getDefault().resolvePath("combination", monitor);

						if (basePath == null)
							throw new RuntimeException("Unexpected error: There are no examples available");

						File analysisFile = new File(basePath, EXAMPLE_ANALYSIS_FILE);
						Properties props = new Properties();
						props.setProperty(AbstractXmlFormat.LOAD_REFERENCES_PROP, "false");
						try {
							final CombinationAnalysis a = PersistenceManager.get()
									.load(analysisFile, CombinationAnalysis.class, props, monitor);
						
							SwingUtilities.invokeLater(new Runnable() {
								@Override public void run() {
									setAnalysis(a);

									dataPage.setFile(new File(basePath, EXAMPLE_DATA_FILE));
									columnSetsPage.setFile(new File(basePath, EXAMPLE_COLUM_SETS_FILE));
									saveFilePage.setFileNameWithoutExtension("example");
								}
							});
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

	public boolean isExamplePageEnabled() {
		return examplePageEnabled;
	}

	public void setExamplePageEnabled(boolean examplePageEnabled) {
		this.examplePageEnabled = examplePageEnabled;
	}

	public boolean isDataFromMemory() {
		return dataFromMemory;
	}

	public void setDataFromMemory(boolean dataFromMemory) {
		this.dataFromMemory = dataFromMemory;
	}

	public List<IElementAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<IElementAttribute> attributes) {
		this.attributes = attributes;
	}

	public boolean isSaveFilePageEnabled() {
		return saveFilePageEnabled;
	}

	public void setSaveFilePageEnabled(boolean saveFilePageEnabled) {
		this.saveFilePageEnabled = saveFilePageEnabled;
	}

	public CombinationAnalysis getAnalysis() {
		CombinationAnalysis a = new CombinationAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());

		IElementAttribute attr = combinationParamsPage.getSizeAttribute();
		String sizeAttrName = attr != null ? attr.getId() : null;
		a.setSizeAttrName(sizeAttrName);

		attr = combinationParamsPage.getPvalueAttribute();
		String pvalueAttrName = attr != null ? attr.getId() : null;
		a.setPvalueAttrName(pvalueAttrName);

		a.setTransposeData(combinationParamsPage.isTransposeEnabled());

		return a;
	}

	private void setAnalysis(CombinationAnalysis a) {
		analysisDetailsPage.setAnalysisTitle(a.getTitle());
		analysisDetailsPage.setAnalysisNotes(a.getDescription());
		analysisDetailsPage.setAnalysisAttributes(a.getAttributes());
		combinationParamsPage.setPreferredSizeAttr(a.getSizeAttrName());
		combinationParamsPage.setPreferredPvalueAttr(a.getPvalueAttrName());
		combinationParamsPage.setTransposeEnabled(a.isTransposeData());
	}

	public DataFilePage getDataFilePage() {
		return dataPage;
	}

	public CombinationAnalysisParamsPage getCombinationParamsPage() {
		return combinationParamsPage;
	}

	public SelectFilePage getColumnSetsPage() {
		return columnSetsPage;
	}

	public SaveFilePage getSaveFilePage() {
		return saveFilePage;
	}
}
