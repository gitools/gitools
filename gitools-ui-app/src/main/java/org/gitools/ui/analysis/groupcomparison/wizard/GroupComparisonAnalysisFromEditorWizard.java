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

import edu.upf.bg.cutoffcmp.CutoffCmp;
import java.util.ArrayList;
import java.util.List;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.heatmap.Heatmap;
import org.gitools.model.Attribute;
import org.gitools.model.ToolConfig;
import org.gitools.stats.test.Test;
import org.gitools.stats.test.factory.TestFactory;
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
				updateAnalysisDetails();
				return super.getPage(analysisDetailsPage.getId());
			}
		}
		//Group by value page
		else if(page == groupByValuePage) {
			updateAnalysisDetails();
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

		canFinish |= page.isComplete() && (page == groupByLabelPage || page == groupByValuePage);

		return canFinish;
	}

	public GroupComparisonAnalysis getAnalysis() {
		GroupComparisonAnalysis a = new GroupComparisonAnalysis();

		a.setTitle(analysisDetailsPage.getAnalysisTitle());
		a.setDescription(analysisDetailsPage.getAnalysisNotes());
		a.setAttributes(analysisDetailsPage.getAnalysisAttributes());
		a.setTransposeData(false);

        ToolConfig toolConfig = TestFactory.createToolConfig("group comparison", attrSelectPage.getTest().getName());
		
		a.setAttributeIndex(attrSelectPage.getAttributeIndex());
		a.setColumnGrouping(attrSelectPage.getColumnGrouping());
		a.setToolConfig(toolConfig);
		a.setMtc(attrSelectPage.getMtc().getShortName());
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

	private void updateAnalysisDetails() {
		List<Attribute> analysisAttributes = new ArrayList<Attribute>();
		if (attrSelectPage.getColumnGrouping().equals(
				GroupComparisonAnalysis.COLUMN_GROUPING_BY_LABEL)) {
			analysisAttributes.add(new Attribute("Group 1", "user defined group"));
			analysisAttributes.add(new Attribute("Group 2", "user defined group"));
		} else {
			CutoffCmp[] groupCutoffCmps = groupByValuePage.getGroupCutoffCmps();
			double[] groupCutoffValues = groupByValuePage.getGroupCutoffValues();
			String cutoffAttributeString = groupByValuePage.getCutoffAttributeString();

			for (int i = 0; i < groupCutoffCmps.length; i++) {
				String group = "Group " + (i+1);
				String name = cutoffAttributeString + " " +
								groupCutoffCmps[i].getLongName() + " " +
								String.valueOf(groupCutoffValues[i]);
				analysisAttributes.add(new Attribute(group, name));
			}
		}
		analysisDetailsPage.setAnalysisAttributes(analysisAttributes);
	}
}
