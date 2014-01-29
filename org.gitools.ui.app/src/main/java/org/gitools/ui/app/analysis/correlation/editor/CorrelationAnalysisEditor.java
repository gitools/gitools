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
package org.gitools.ui.app.analysis.correlation.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.analysis._DEPRECATED.heatmap.Heatmap;
import org.gitools.analysis._DEPRECATED.heatmap.HeatmapLayer;
import org.gitools.analysis._DEPRECATED.model.decorator.impl.CorrelationDecorator;
import org.gitools.analysis.correlation.format.CorrelationAnalysisFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.Map;

public class CorrelationAnalysisEditor extends AnalysisDetailsEditor<CorrelationAnalysis> {

    public CorrelationAnalysisEditor(CorrelationAnalysis analysis) {
        super(analysis, "/vm/analysis/correlation/analysis_details.vm", null);
    }

    @Override
    protected void prepareContext(VelocityContext context) {

        IResourceLocator dataLocator = analysis.getData().getLocator();
        context.put("dataFile", dataLocator != null ? dataLocator.getName() : "Not defined");

        String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
        context.put("appliedTo", appliedTo);

        if (analysis.getMethod().equals("pearson")) {
            context.put("method", "Pearson's correlation");
        }

        IResourceLocator resultsLocator = analysis.getResults().getLocator();
        context.put("resultsFile", resultsLocator != null ? resultsLocator.getName() : "Not defined");

        IResourceLocator analysisLocator = analysis.getLocator();

        if (analysisLocator != null) {
            context.put("analysisLocation", analysisLocator.getURL());

            if (analysisLocator.isWritable()) {
                setSaveAllowed(true);
            }
        }
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        xmlPersistance = new CorrelationAnalysisFormat();
        fileformat = CorrelationAnalysisFormat.FILE_FORMAT;
        super.doSave(progressMonitor);
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
        if (analysis.getData() == null) {
            Application.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getData().get());
                heatmap.setTitle(analysis.getTitle() + " (data)");

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(getName(), CorrelationAnalysisFormat.EXTENSION, "-data", ""));

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
        if (analysis.getResults() == null) {
            Application.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from results ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getResults().get());
                heatmap.setTitle(analysis.getTitle() + " (results)");

                final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));
                editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                editor.setName(editorPanel.deriveName(getName(), CorrelationAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("Heatmap for correlation results created.");
                    }
                });
            }
        });
    }


    private static Heatmap createHeatmap(CorrelationAnalysis analysis) {

        Heatmap heatmap = new Heatmap(analysis.getResults().get(), true);

        heatmap.setTitle(analysis.getTitle() + " (results)");

        for (HeatmapLayer layer : heatmap.getLayers()) {
            layer.setDecorator(new CorrelationDecorator());
        }

        heatmap.getLayers().setTopLayer(heatmap.getLayers().get("score"));
        heatmap.setTitle(analysis.getTitle());

        return heatmap;
    }
}
