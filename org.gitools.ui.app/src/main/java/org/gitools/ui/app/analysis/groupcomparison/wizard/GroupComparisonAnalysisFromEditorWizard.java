/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.analysis.groupcomparison.wizard;

import org.gitools.analysis.ToolConfig;
import org.gitools.analysis.groupcomparison.DimensionGroups.DimensionGroupEnum;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.resource.Property;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.CloneUtils;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.datafilters.BinaryCutoff;

import java.util.ArrayList;
import java.util.List;

public class GroupComparisonAnalysisFromEditorWizard extends AbstractWizard {

    private final Heatmap heatmap;

    private GroupComparisonGroupingByValuePage groupByValuePage;
    private GroupComparisonGroupingByLabelPage groupByLabelPage;
    private GroupComparisonStatisticsPage attrSelectPage;
    private AnalysisDetailsPage analysisDetailsPage;
    private GroupComparisonGroupsPage groupsPage;
    private GroupComparisonGroupingPage groupingPage;

    public GroupComparisonAnalysisFromEditorWizard(Heatmap heatmap) {
        super();

        setTitle("Correlation analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_GROUP_COMPARISON, 96));

        this.heatmap = heatmap;
    }

    @Override
    public void addPages() {
        // Column Selection

        groupingPage = new GroupComparisonGroupingPage(heatmap, DimensionGroupEnum.Free);
        addPage(groupingPage);

        attrSelectPage = new GroupComparisonStatisticsPage();
        attrSelectPage.setAttributes(heatmap.getLayers());
        addPage(attrSelectPage);

        groupByLabelPage = new GroupComparisonGroupingByLabelPage(heatmap.getColumns());
        addPage(groupByLabelPage);

        groupsPage = new GroupComparisonGroupsPage();
        addPage(groupsPage);

        groupByValuePage = new GroupComparisonGroupingByValuePage();
        groupByValuePage.setAttributes(heatmap.getLayers());
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
            if (attrSelectPage.getColumnGrouping().equals(DimensionGroupEnum.Annotation)) {
                return super.getPage(groupByLabelPage.getId());
            } else if (attrSelectPage.getColumnGrouping().equals(DimensionGroupEnum.Value)) {
                return super.getPage(groupByValuePage.getId());
            }
        }
        //Group by label page
        else if (page == groupByLabelPage) {
            if (groupByLabelPage.getGroup1().size() == 0) {
                groupByLabelPage.setMessage(MessageStatus.ERROR, "No columns match values in Group 1");
                return page;
            } else if (groupByLabelPage.getGroup2().size() == 0) {
                groupByLabelPage.setMessage(MessageStatus.ERROR, "No columns match values in Group 2");
                return page;
            } else {
                updateAnalysisDetails();

                //groupsPage.addGroups(
                //        new DimensionGroupValue("g1", groupByLabelPage.getGroup1()),
                //        new DimensionGroupValue("g2", groupByLabelPage.getGroup2())
                //       );
                groupsPage.setColumnGroupType(getAnalysis().getColumnGroupType());
                return super.getPage(groupsPage.getId());
                //return super.getPage(analysisDetailsPage.getId());
            }
        }
        //Group by value page
        else if (page == groupByValuePage) {
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
            if (attrSelectPage.getColumnGrouping().equals(DimensionGroupEnum.Annotation)) {
                return super.getPage(groupByLabelPage.getId());
            } else if (attrSelectPage.getColumnGrouping().equals(DimensionGroupEnum.Value)) {
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
        a.setProperties(analysisDetailsPage.getAnalysisAttributes());
        a.setTransposeData(false);

        ToolConfig toolConfig = TestFactory.createToolConfig("group comparison", attrSelectPage.getTest().getName());

        a.setAttributeIndex(attrSelectPage.getAttributeIndex());
        a.setColumnGrouping(attrSelectPage.getColumnGrouping());
        a.setToolConfig(toolConfig);
        a.setMtc(attrSelectPage.getMtc().getShortName());
        a.setRowAnnotations(heatmap.getRows().getAnnotations());

        List<HeatmapHeader> rowHeaders = new ArrayList<>();
        for (HeatmapHeader header : heatmap.getRows().getHeaders()) {
            try {
                HeatmapHeader headerClone = CloneUtils.clone(header);
                rowHeaders.add(headerClone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        a.setRowHeaders(rowHeaders);
        a.setColumnAnnotations(heatmap.getColumns().getAnnotations());

        List<HeatmapHeader> columnHeaders = new ArrayList<>();
        for (HeatmapHeader header : heatmap.getColumns().getHeaders()) {
            try {
                HeatmapHeader headerClone = CloneUtils.clone(header);
                columnHeaders.add(headerClone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        a.setColumnHeaders(columnHeaders);

        if (a.getColumnGrouping().equals(DimensionGroupEnum.Annotation)) {
            a.setGroup(groupByLabelPage.getGroup1(), 0);
            a.setGroup(groupByLabelPage.getGroup2(), 1);
        } else if (a.getColumnGrouping().equals(DimensionGroupEnum.Value)) {
            a.setGroup(new BinaryCutoff(groupByValuePage.getGroupCutoffCmps()[0], groupByValuePage.getGroupCutoffValues()[0]), groupByValuePage.getCutoffAttributeIndex(), 0);
            a.setGroup(new BinaryCutoff(groupByValuePage.getGroupCutoffCmps()[1], groupByValuePage.getGroupCutoffValues()[1]), groupByValuePage.getCutoffAttributeIndex(), 1);
            a.setNoneConversion(groupByValuePage.getNoneConversion());
        }
        return a;
    }

    private void updateAnalysisDetails() {
        List<Property> analysisAttributes = new ArrayList<>();
        if (attrSelectPage.getColumnGrouping().equals(DimensionGroupEnum.Annotation)) {
            analysisAttributes.add(new Property("Group 1", "user defined group"));
            analysisAttributes.add(new Property("Group 2", "user defined group"));
        } else {
            CutoffCmp[] groupCutoffCmps = groupByValuePage.getGroupCutoffCmps();
            double[] groupCutoffValues = groupByValuePage.getGroupCutoffValues();
            String cutoffAttributeString = groupByValuePage.getCutoffAttributeString();

            for (int i = 0; i < groupCutoffCmps.length; i++) {
                String group = "Group " + (i + 1);
                String name = cutoffAttributeString + " " +
                        groupCutoffCmps[i].getLongName() + " " +
                        String.valueOf(groupCutoffValues[i]);
                analysisAttributes.add(new Property(group, name));
            }
            if (groupByValuePage.isIncludeNone()) {
                analysisAttributes.add(new Property("NoneConvertedTo",
                        String.valueOf(groupByValuePage.getNoneConversion())))
                ;
            }
        }
        analysisDetailsPage.setAnalysisAttributes(analysisAttributes);
    }
}
