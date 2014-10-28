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

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.api.ApplicationContext;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysis;
import org.gitools.plugins.mutex.analysis.MutualExclusiveAnalysisFormat;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.components.editor.AnalysisEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.enterprise.context.ApplicationScoped;
import javax.swing.*;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class MutualExclusiveAnalysisEditor extends AnalysisEditor<MutualExclusiveAnalysis> {

    public MutualExclusiveAnalysisEditor(MutualExclusiveAnalysis analysis) {
        super(analysis, "/vm/analysis_details.vm", MutualExclusiveAnalysisFormat.EXTENSION);
        setName(analysis.getTitle());
    }

    @Override
    protected void prepareContext(VelocityContext context) {

        MutualExclusiveAnalysis analysis = getModel();

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        /*
        if (analysis.getMtc().equals("bh")) {
            context.put("mtc", "Benjamini Hochberg FDR");
        } else if (analysis.getMtc().equals("bonferroni")) {
            context.put("mtc", "Bonferroni");
        }
        */

        if (analysis.getProperties().size() > 0) {
            context.put("analysis.attributes", analysis.getProperties());
        }

        fileRef = analysis.getResults().getLocator();
        context.put("resultsFile", fileRef != null ? fileRef.getName() : "Not defined");

        fileRef = analysis.getLocator();
        context.put("analysisLocation", fileRef != null ? fileRef.getURL() : "Not defined");

        if (fileRef == null || fileRef.isWritable()) {
            setSaveAllowed(true);
        }

    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params) {
        if ("NewDataHeatmap".equals(name)) {
            newDataHeatmap();
        } else if ("NewResultsHeatmap".equals(name)) {
            newResultsHeatmap();
        }
    }

    private void newDataHeatmap() {

        final MutualExclusiveAnalysis analysis = getModel();

        if (analysis.getData() == null) {
            Application.get().showNotificationPermanent("Analysis doesn't contain data.");
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ApplicationContext.getEditorManger().addEditor(analysis.getData().get());
                        Application.get().showNotification("Data heatmap created.");
                    }
                });
            }
        });
    }

    private void newResultsHeatmap() {

        final MutualExclusiveAnalysis analysis = getModel();

        if (analysis.getResults() == null) {
            Application.get().showNotificationPermanent("Analysis doesn't contain results.");
            return;
        }

        final Heatmap analysisResult = getModel().getResults().get();


        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Obtaining heatmap from results ...", 1);

                analysisResult.setMetadata(MutualExclusiveAnalysis.CACHE_KEY_MUTEX_ANALYSIS, analysis);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {

                        ApplicationContext.getEditorManger().addEditor(analysisResult);
                        Application.get().showNotification("Heatmap for group comparison results created.");
                    }
                });
            }
        });
    }

    @Deprecated
    public static Heatmap createDataHeatmap(GroupComparisonAnalysis analysis) {

        IMatrix data = analysis.getData().get();
        if (Heatmap.class.isAssignableFrom(data.getClass())) {
            return (Heatmap) data;
        }

        Heatmap heatmap = new Heatmap(data);


        return heatmap;
    }

    @Deprecated
    private Heatmap createResultsHeatmap(GroupComparisonAnalysis analysis) {

        IMatrix results = analysis.getResults().get();
        if (Heatmap.class.isAssignableFrom(results.getClass())) {
            return (Heatmap) results;
        }

        Heatmap heatmap = new Heatmap(results);
        return heatmap;

    }

    private void copyHeaders(HeatmapDimension dim, List<HeatmapHeader> headers) {
        for (HeatmapHeader hh : headers) {
            dim.addHeader(hh);
        }
    }
}
