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
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroup;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroupEnum;
import org.gitools.analysis.stats.test.factory.TestFactory;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.resource.Property;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.CloneUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupComparisonAnalysisWizard extends AnalysisWizard<GroupComparisonAnalysis> {

    private final Heatmap heatmap;

    private GroupComparisonStatisticsPage statisticsPage;
    private AnalysisDetailsPage analysisDetailsPage;
    private GroupComparisonGroupingPage groupingPage;

    public GroupComparisonAnalysisWizard(Heatmap heatmap) {
        super();

        setTitle("Group Comparison analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_GROUP_COMPARISON, 96));

        this.heatmap = heatmap;
    }

    @Override
    public void addPages() {
        // Column Selection

        groupingPage = new GroupComparisonGroupingPage(heatmap, DimensionGroupEnum.Free);
        addPage(groupingPage);

        statisticsPage = new GroupComparisonStatisticsPage();
        addPage(statisticsPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }

    @Override
    public IWizardPage getNextPage(IWizardPage page) {
        page = getCurrentPage();

        if (super.getNextPage(page) == statisticsPage) {
            statisticsPage.setGroupNumber(groupingPage.getGroups().size());
        } else if (super.getNextPage(page) == analysisDetailsPage) {
            updateAnalysisDetails();
        }

        return super.getNextPage(page);
    }

    @Override
    public IWizardPage getPreviousPage(IWizardPage page) {
        page = getCurrentPage();
        return super.getPreviousPage(page);
    }


    @Override
    public boolean canFinish() {
        boolean canFinish = super.canFinish();

        IWizardPage page = getCurrentPage();

        canFinish |= page.isComplete() && (page == statisticsPage);

        return canFinish;
    }


    public GroupComparisonAnalysis createAnalysis() {
        GroupComparisonAnalysis a = new GroupComparisonAnalysis();

        a.setData(new ResourceReference<IMatrix>("data", heatmap));

        a.setTitle(analysisDetailsPage.getAnalysisTitle());
        a.setDescription(analysisDetailsPage.getAnalysisDescription());
        a.setProperties(analysisDetailsPage.getAnalysisProperties());
        a.setTransposeData(false);

        ToolConfig toolConfig = TestFactory.createToolConfig("group comparison", statisticsPage.getTest().getName());

        a.setLayer(groupingPage.getLayer());
        a.setNullConversion(groupingPage.getNullConversion());
        a.setToolConfig(toolConfig);
        a.setMtc(statisticsPage.getMtc());
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

        a.addGroups(groupingPage.getGroups());
        a.setProperties(analysisDetailsPage.getAnalysisProperties());
        return a;
    }

    /**
     * Fill the form with the analysis details
     */
    private void updateAnalysisDetails() {
        List<Property> analysisAttributes = new ArrayList<>();
        analysisAttributes.add(new Property("Grouping type", groupingPage.getGroupingType().toString()));

        String title = "Group Comparison";
        if (groupingPage.getSelectedGroupingType().equals(DimensionGroupEnum.Annotation)) {
            String nicepattern = groupingPage.getGroupingPattern().replaceAll("[\\$\\{\\}]", "");
            analysisAttributes.add(new Property("Grouping pattern", nicepattern));
            analysisDetailsPage.setAnalysisTitle(nicepattern + " " + title);
        } else {
            analysisDetailsPage.setAnalysisTitle(title);
        }

        int counter = 0;
        for (DimensionGroup g : groupingPage.getGroups()) {
            counter++;
            analysisAttributes.add(new Property("Group " + String.valueOf(counter) + ": " + g.getName(), g.getProperty()));
        }

        Double nullConversion = groupingPage.getNullConversion();
        analysisAttributes.add(new Property("NoneConvertedTo",
                nullConversion != null ? Double.toString(nullConversion) : "null"));

        analysisDetailsPage.setAnalysisAttributes(analysisAttributes);
    }
}
