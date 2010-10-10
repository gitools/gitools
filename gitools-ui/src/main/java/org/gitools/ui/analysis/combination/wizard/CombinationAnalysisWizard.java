/*
 *  Copyright 2010 chris.
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
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.persistence.IEntityPersistence;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.text.ObjectMatrixTextPersistence;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.SelectFilePage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class CombinationAnalysisWizard extends AbstractWizard {

	private static final FileFormat[] formats = new FileFormat[] {
			FileFormats.RESULTS_MATRIX,
			FileFormats.GENE_MATRIX,
			FileFormats.GENE_MATRIX_TRANSPOSED,
			FileFormats.DOUBLE_MATRIX,
			FileFormats.DOUBLE_BINARY_MATRIX,
			FileFormats.MODULES_2C_MAP,
			FileFormats.MODULES_INDEXED_MAP
	};
	
	private SelectFilePage dataPage;
	private DataFilterPage dataFilterPage;
	private CombinationAnalysisParamsPage combParamsPage;
	private SaveFilePage saveFilePage;
	private AnalysisDetailsPage analysisDetailsPage;

	private File dataFile;
	private List<IElementAttribute> attrs;

	public CombinationAnalysisWizard() {
		super();

		setTitle("Combination analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_COMBINATION, 96));
	}

	@Override
	public void addPages() {
		// Data
		dataPage = new SelectFilePage(formats);
		addPage(dataPage);

		// Data filters
		/*dataFilterPage = new DataFilterPage();
		dataFilterPage.setPopulationFileVisible(false);
		addPage(dataFilterPage);*/

		// Combination parameters
		combParamsPage = new CombinationAnalysisParamsPage();
		addPage(combParamsPage);

		// Destination
		saveFilePage = new org.gitools.ui.wizard.common.SaveFilePage();
		saveFilePage.setTitle("Select destination file");
		saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
		saveFilePage.setFormats(new FileFormat[] {
			FileFormats.COMBINATION });
		saveFilePage.setFormatsVisible(false);
		addPage(saveFilePage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public void pageEntered(IWizardPage page) {
		if (combParamsPage.equals(page)) {
			if (dataFile == null || !dataPage.getFile().equals(dataFile)) {
				JobThread.execute(AppFrame.instance(), new JobRunnable() {
					@Override
					public void run(IProgressMonitor monitor) {
						monitor.begin("Reading data header ...", 1);

						String mimeType = dataPage.getFileFormat().getMime();
						IEntityPersistence<Object> ep = PersistenceManager.getDefault()
								.createEntityPersistence(mimeType, new Properties());
						Map<String, Object> meta = null;
						try {
							meta = ep.readMetadata(dataFile, new String[] {
								ObjectMatrixTextPersistence.META_ATTRIBUTES }, monitor);

							if (meta != null) {
								combParamsPage.setAttributes((List<IElementAttribute>)
										meta.get(ObjectMatrixTextPersistence.META_ATTRIBUTES));
								
								dataFile = dataPage.getFile();
							}
						}
						catch (Exception ex) {
							monitor.exception(ex);
						}

						monitor.end();
					}
				});
			}
		}
	}
}
