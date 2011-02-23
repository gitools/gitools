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

package org.gitools.ui.analysis.overlapping.wizard;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class OverlappingAnalysisFromEditorWizard extends AbstractWizard {

	protected String[] attributeNames;

	protected OverlappingAnalysisWizard ovPage;
	protected AnalysisDetailsPage analysisDetailsPage;

	public OverlappingAnalysisFromEditorWizard(String[] attributeNames) {
		super();

		setTitle("Overlapping analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_OVERLAPPING, 96));

		this.attributeNames = attributeNames;
	}

	public OverlappingAnalysisFromEditorWizard() {
		super();

		setTitle("Overlapping analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_OVERLAPPING, 96));

	}
	
	@Override
	public void addPages() {
		// Overlapping method
		ovPage = new OverlappingAnalysisWizard(attributeNames);
		addPage(ovPage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page.isComplete() && (page == ovPage);

		return canFinish;
	}

	public OverlappingAnalysis getAnalysis() {
		OverlappingAnalysis a = new OverlappingAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());

		//FIXME overlapping: verify
		//a.setAttributeIndex(ovPage.getAttributeIndex());
		a.setReplaceNanValue(ovPage.isReplaceNanValuesEnabled() ?
				ovPage.getReplaceNanValue() : null);
		a.setTransposeData(ovPage.isTransposeEnabled());
		
		return a;
	}
}
