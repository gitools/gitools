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

import org.gitools.persistence.FileFormat;
import org.gitools.persistence.FileFormats;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.DataFilterPage;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.analysis.wizard.SelectFilePage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class CombinationAnalysisWizard extends AbstractWizard {

	private SelectFilePage dataPage;
	private DataFilterPage dataFilterPage;
	private SaveFilePage saveFilePage;
	private AnalysisDetailsPage analysisDetailsPage;

	public CombinationAnalysisWizard() {
		super();

		setTitle("Combination analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_COMBINATION, 96));
	}

	@Override
	public void addPages() {
		// Data
		dataPage = new SelectFilePage();
		addPage(dataPage);

		// Data filters
		dataFilterPage = new DataFilterPage();
		dataFilterPage.setPopulationFileVisible(false);
		addPage(dataFilterPage);

		//TODO analysis parameters

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

}
