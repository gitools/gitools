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

package org.gitools.ui.analysis.groupcomparison.wizard;

import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class GroupComparisonAnalysisFromEditorWizard extends AbstractWizard {

	protected Heatmap heatmap;


	protected GroupComparisonFromEditorPage compPage;
	protected GroupComparisonSelectAttributePage attrSelectPage;
	protected AnalysisDetailsPage analysisDetailsPage;

	public GroupComparisonAnalysisFromEditorWizard(Heatmap heatmap) {
		super();

		setTitle("Correlation analysis");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_GROUP_COMPARISON, 96));

		this.heatmap = heatmap;
	}

	@Override
	public void addPages() {
		// Column Selection
		compPage = new GroupComparisonFromEditorPage(heatmap);
		addPage(compPage);

		attrSelectPage = new GroupComparisonSelectAttributePage();
		attrSelectPage.setAttributes(heatmap.getMatrixView().getCellAttributes());
		addPage(attrSelectPage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		page = getCurrentPage();
		if (page == compPage) {
			if (compPage.getGroup1().length == 0) {
				compPage.setMessage(MessageStatus.ERROR,
						"No columns match values in Group 1");
				return page;
			} else if (compPage.getGroup2().length == 0) {
				compPage.setMessage(MessageStatus.ERROR,
						"No columns match values in Group 2");
				return page;
			}
		}
		return super.getNextPage(page);
	}



	@Override
	public boolean canFinish() {
		boolean canFinish = super.canFinish();

		IWizardPage page = getCurrentPage();

		canFinish |= page.isComplete() && (page == attrSelectPage);

		return canFinish;
	}

	public GroupComparisonAnalysis getAnalysis() {
		GroupComparisonAnalysis a = new GroupComparisonAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());
		a.setTransposeData(false);

		
		a.setAttributeIndex(attrSelectPage.getAttributeIndex());
		a.setTest(attrSelectPage.getTest());
		a.setMtc(attrSelectPage.getMtc());
		a.setGroup1(compPage.getGroup1());
		a.setGroup2(compPage.getGroup2());
		a.setRowAnnotations(heatmap.getRowDim().getAnnotations());
		a.setRowHeaders(heatmap.getRowDim().getHeaders());
		a.setColumnAnnotations(heatmap.getColumnDim().getAnnotations());
		a.setColumnHeaders(heatmap.getColumnDim().getHeaders());


		return a;
	}
}
