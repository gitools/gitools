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
package org.gitools.ui.app.analysis.groupcomparison.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.format.GroupComparisonAnalysisFormat;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.editor.AnalysisEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.List;
import java.util.Map;


public class GroupComparisonAnalysisEditor extends AnalysisEditor<GroupComparisonAnalysis> {

    public GroupComparisonAnalysisEditor(GroupComparisonAnalysis analysis) {
        super(analysis, "/vm/analysis/groupcomparison/analysis_details.vm", GroupComparisonAnalysisFormat.EXTENSION);
    }

    @Override
    protected void prepareContext(VelocityContext context) {

        GroupComparisonAnalysis analysis = getModel();

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        if (analysis.getMtc().equals("bh")) {
            context.put("mtc", "Benjamini Hochberg FDR");
        } else if (analysis.getMtc().equals("bonferroni")) {
            context.put("mtc", "Bonferroni");
        }

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

        final GroupComparisonAnalysis analysis = getModel();

        if (analysis.getData() == null) {
            Application.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);

                final HeatmapEditor editor = new HeatmapEditor(createDataHeatmap(analysis));
                editor.setName(editorPanel.deriveName(getName(), GroupComparisonAnalysisFormat.EXTENSION, "-data", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("New heatmap created.");
                    }
                });
            }
        });
    }

    private void newResultsHeatmap() {

        final GroupComparisonAnalysis analysis = getModel();

        if (analysis.getResults() == null) {
            Application.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Obtaining heatmap from results ...", 1);

                final HeatmapEditor editor = new HeatmapEditor(createResultsHeatmap(analysis));
                editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                editor.setName(editorPanel.deriveName(getName(), GroupComparisonAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("Heatmap for group comparison results created.");
                    }
                });
            }
        });
    }

    @Deprecated
    private Heatmap createDataHeatmap(GroupComparisonAnalysis analysis) {

        IMatrix data = analysis.getData().get();
        if (Heatmap.class.isAssignableFrom(data.getClass())) {
            return (Heatmap) data;
        }

        Heatmap heatmap = new Heatmap(data);
        /*
        heatmap.setTitle(analysis.getTitle() + " (data)");

        if (analysis.getRowAnnotations() != null) {
            heatmap.getRows().addAnnotations(analysis.getRowAnnotations());
        }
        if (analysis.getRowHeaders() != null)
            copyHeaders(heatmap.getRows(), analysis.getRowHeaders());

        if (analysis.getColumnAnnotations() != null) {
            heatmap.getColumns().addAnnotations(analysis.getColumnAnnotations());
        }
        if (analysis.getColumnHeaders() != null)
            copyHeaders(heatmap.getColumns(), analysis.getColumnHeaders());*/

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
