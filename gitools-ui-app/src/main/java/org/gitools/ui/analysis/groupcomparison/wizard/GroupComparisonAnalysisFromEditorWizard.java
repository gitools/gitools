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
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class GroupComparisonAnalysisFromEditorWizard extends AbstractWizard {

	protected Heatmap heatmap;

	protected GroupComparisonGroupingByValuePage groupByValuePage;
	protected GroupComparisonGroupingByLabelPage groupByLabelPage;
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
		attrSelectPage = new GroupComparisonSelectAttributePage();
		attrSelectPage.setAttributes(heatmap.getMatrixView().getCellAttributes());
		addPage(attrSelectPage);

		groupByLabelPage = new GroupComparisonGroupingByLabelPage(heatmap);
		addPage(groupByLabelPage);

		groupByValuePage = new GroupComparisonGroupingByValuePage();
		groupByValuePage.setAttributes(heatmap.getMatrixView().getCellAttributes());
		addPage(groupByValuePage);

		// Analysis details
		analysisDetailsPage = new AnalysisDetailsPage();
		addPage(analysisDetailsPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		page = getCurrentPage();
		//attribute Selection Page
		if (page == attrSelectPage) {
			if (attrSelectPage.getColumnGrouping().equals(
					GroupComparisonAnalysis.COLUMN_GROUPING_BY_LABEL)) {
						return super.getPage(groupByLabelPage.getId());
			}
			else if(attrSelectPage.getColumnGrouping().equals(
					GroupComparisonAnalysis.COLUMN_GROUPING_BY_VALUE)) {
						return super.getPage(groupByValuePage.getId());
			}
		}
		//Group by label page
		else if(page == groupByLabelPage) {
			if (groupByLabelPage.getGroup1().length == 0) {
				groupByLabelPage.setMessage(MessageStatus.ERROR,
						"No columns match values in Group 1");
				return page;
			} else if (groupByLabelPage.getGroup2().length == 0) {
				groupByLabelPage.setMessage(MessageStatus.ERROR,
						"No columns match values in Group 2");
				return page;
			}
			else {
				return super.getPage(analysisDetailsPage.getId());
			}
		}
		//Group by value page
		else if(page == groupByValuePage) {
			return super.getPage(analysisDetailsPage.getId());
		}
		return super.getNextPage(page);
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		page = getCurrentPage();
		if (page == groupByLabelPage || page == groupByValuePage) {
			return super.getPage(attrSelectPage.getId());
		} else if (page == analysisDetailsPage) {
			if (attrSelectPage.getColumnGrouping().equals(
					GroupComparisonAnalysis.COLUMN_GROUPING_BY_LABEL)) {
						return super.getPage(groupByLabelPage.getId());
			}
			else if(attrSelectPage.getColumnGrouping().equals(
					GroupComparisonAnalysis.COLUMN_GROUPING_BY_VALUE)) {
						return super.getPage(groupByValuePage.getId());
			}
		}
		return super.getPreviousPage(page);
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
		a.setColumnGrouping(attrSelectPage.getColumnGrouping());
		a.setTest(attrSelectPage.getTest());
		a.setMtc(attrSelectPage.getMtc());
		a.setRowAnnotations(heatmap.getRowDim().getAnnotations());
		a.setRowHeaders(heatmap.getRowDim().getHeaders());
		a.setColumnAnnotations(heatmap.getColumnDim().getAnnotations());
		a.setColumnHeaders(heatmap.getColumnDim().getHeaders());

		if (a.getColumnGrouping().equals(GroupComparisonAnalysis.COLUMN_GROUPING_BY_LABEL)) {
			a.setGroup1(groupByLabelPage.getGroup1());
			a.setGroup2(groupByLabelPage.getGroup2());
		}
		else if (a.getColumnGrouping().equals(GroupComparisonAnalysis.COLUMN_GROUPING_BY_VALUE)) {
			a.setGroup1(new BinaryCutoff(
							groupByValuePage.getGroupCutoffCmps()[0],
							groupByValuePage.getGroupCutoffValues()[0]
							),
						groupByValuePage.getCutoffAttributeIndex());
			a.setGroup2(new BinaryCutoff(
						groupByValuePage.getGroupCutoffCmps()[1],
						groupByValuePage.getGroupCutoffValues()[1]
						),
						groupByValuePage.getCutoffAttributeIndex());
		}
		return a;
	}
}
