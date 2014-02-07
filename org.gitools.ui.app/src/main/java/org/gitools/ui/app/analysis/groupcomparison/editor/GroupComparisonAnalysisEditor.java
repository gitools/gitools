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
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.PValueDecorator;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.util.List;
import java.util.Map;


public class GroupComparisonAnalysisEditor extends AnalysisDetailsEditor<GroupComparisonAnalysis> {

    public GroupComparisonAnalysisEditor(GroupComparisonAnalysis analysis) {
        super(analysis, "/vm/analysis/groupcomparison/analysis_details.vm", null);
    }


    @Override
    protected void prepareContext(VelocityContext context) {

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        context.put("mtc", analysis.mtc());

        fileRef = analysis.getResults().getLocator();
        context.put("resultsFile", fileRef != null ? fileRef.getName() : "Not defined");

        fileRef = analysis.getLocator();
        context.put("analysisLocation", fileRef != null ? fileRef.getURL() : "Not defined");

        if (fileRef == null || fileRef.isWritable()) {
            setSaveAllowed(true);
        }

    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        xmlPersistance = new GroupComparisonAnalysisFormat();
        fileformat = GroupComparisonAnalysisFormat.FILE_FORMAT;
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

                if (analysis.getRowAnnotations() != null) {
                    heatmap.getRows().addAnnotations(analysis.getRowAnnotations());
                }
                if (analysis.getRowHeaders() != null)
                    copyHeaders(heatmap.getRows(), analysis.getRowHeaders());

                if (analysis.getColumnAnnotations() != null) {
                    heatmap.getColumns().addAnnotations(analysis.getColumnAnnotations());
                }
                if (analysis.getColumnHeaders() != null)
                    copyHeaders(heatmap.getColumns(), analysis.getColumnHeaders());

                final HeatmapEditor editor = new HeatmapEditor(heatmap);


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


    private void copyHeaders(HeatmapDimension dim, List<HeatmapHeader> headers) {
        for (HeatmapHeader hh : headers) {
            dim.addHeader(hh);
        }
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

                for (HeatmapLayer layer : heatmap.getLayers()) {
                    if (layer.getId().contains("p-value")) {
                        layer.setDecorator(new PValueDecorator());
                    }
                }

                heatmap.getLayers().setTopLayerById("two-tail-p-value");

                heatmap.setTitle(analysis.getTitle() + " (results)");

                if (analysis.getRowHeaders() != null) {
                    if (analysis.getRowAnnotations() != null) {
                        heatmap.getRows().addAnnotations(analysis.getRowAnnotations());
                    }

                    for (HeatmapHeader hh : analysis.getRowHeaders()) {
                        heatmap.getRows().addHeader(hh);
                        hh.init(heatmap.getRows());
                    }
                }

                final HeatmapEditor editor = new HeatmapEditor(heatmap);
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
}
