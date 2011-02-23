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

package org.gitools.ui.analysis.overlap.wizard;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class OverlapAnalysisFromEditorWizard extends AbstractWizard {

	protected String[] attributeNames;

	protected OverlapFromEditorPage corrPage;
	protected AnalysisDetailsPage analysisDetailsPage;

	public OverlapAnalysisFromEditorWizard(String[] attributeNames) {
		super();

		setTitle("Correlation analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CORRELATION, 96));

		this.attributeNames = attributeNames;
	}

	@Override
	public void addPages() {
		// Correlation method
		corrPage = new OverlapFromEditorPage(attributeNames);
		addPage(corrPage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page.isComplete() && (page == corrPage);

		return canFinish;
	}

	public CorrelationAnalysis getAnalysis() {
		CorrelationAnalysis a = new CorrelationAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());

		a.setAttributeIndex(corrPage.getAttributeIndex());
		a.setReplaceNanValue(corrPage.isReplaceNanValuesEnabled() ?
				corrPage.getReplaceNanValue() : null);
		a.setTransposeData(corrPage.isTransposeEnabled());
		
		return a;
	}
}
