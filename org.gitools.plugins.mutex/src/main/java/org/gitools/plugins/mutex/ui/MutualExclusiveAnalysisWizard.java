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
package org.gitools.plugins.mutex.ui;


import org.gitools.heatmap.Heatmap;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysis;
import org.gitools.resource.Property;
import org.gitools.ui.core.components.wizard.AnalysisDetailsPage;
import org.gitools.ui.core.components.wizard.AnalysisWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.utils.textpattern.TextPattern;

import java.util.List;

public class MutualExclusiveAnalysisWizard extends AnalysisWizard<MutualExclusiveAnalysis> {

    private final Heatmap heatmap;

    private MutualExclusiveAnalysisPage mutexPage;
    private AnalysisDetailsPage analysisDetailsPage;
    private MutualExclusiveAnalysis analysis;

    public MutualExclusiveAnalysisWizard(Heatmap heatmap) {
        super();

        setTitle("Mutual exclusive & Co-occurrence analysis");

        this.heatmap = heatmap;
    }

    @Override
    public void addPages() {
        // Column Selection

        mutexPage = new MutualExclusiveAnalysisPage(heatmap);
        addPage(mutexPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);


        String title = heatmap.getTitle() + " mutex-cooc";
        analysisDetailsPage.setAnalysisTitle(title);
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

        canFinish |= page.isComplete() && (page == mutexPage);

        return canFinish;
    }


    public MutualExclusiveAnalysis createAnalysis() {
        analysis = new MutualExclusiveAnalysis(heatmap);

        analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
        analysis.setDescription(analysisDetailsPage.getAnalysisDescription());
        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());


        //TODO a.setMtc(mutexPage.getMtc());
        //TODO a.setCopyAnnotation(groupingPage.isCopyHeatmapHeaders());/

        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());
        List<Property> props = analysis.getProperties();
        if (mutexPage.getColumnGroupsPattern() != null)
        { props.add(new Property("Column grouping",
                new TextPattern(mutexPage.getColumnGroupsPattern()).getVariableTokens().get(0).getVariableName()));
        }
        props.add(new Property("Rows grouping",
                new TextPattern(mutexPage.getRowsGroupsPattern()).getVariableTokens().get(0).getVariableName()));


        analysis.setTestDimension(heatmap.getRows());
        analysis.setLayer(heatmap.getLayers().getTopLayer().getId());
        analysis.setEventFunction(heatmap.getLayers().get(analysis.getLayer()).getEventFunction());
        analysis.setDiscardEmpty(mutexPage.getDiscardEmpty());
        analysis.setIterations(mutexPage.getPermutations());
        analysis.setRowModuleMap(MutualExclusiveAnalysis.createModules(mutexPage.getRowsGroupsPattern(), false, heatmap.getRows()));
        analysis.setColumnsModuleMap(MutualExclusiveAnalysis.createModules(mutexPage.getColumnGroupsPattern(), mutexPage.isAllColumnsGroup(), heatmap.getColumns()));

        return analysis;
    }

}
