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
package org.gitools.ui.app.analysis.overlapping;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.analysis.overlapping.format.OverlappingAnalysisFormat;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.LinearDecorator;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.editor.AnalysisEditor;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class OverlappingAnalysisEditor extends AnalysisEditor<OverlappingAnalysis> {

    public OverlappingAnalysisEditor(OverlappingAnalysis analysis) {
        super(analysis, "/vm/analysis/overlapping/analysis_details.vm", OverlappingAnalysisFormat.EXTENSION);
        Application.track("overlapping", "open");
    }

    @Override
    protected void prepareContext(VelocityContext context) {

        OverlappingAnalysis analysis = getModel();

        ResourceReference<IMatrix> resourceRef = analysis.getFilteredData();
        context.put("filteredDataFile", ((resourceRef != null && resourceRef.getLocator() != null) ? resourceRef.getLocator().getURL() : "Not defined"));

        String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
        context.put("appliedTo", appliedTo);

        CutoffCmp cmp = analysis.getBinaryCutoffCmp();
        String filterDesc = cmp == null ? "Not filtered" : "Binary cutoff filter for values " + cmp.getLongName() + " " + analysis.getBinaryCutoffValue();
        context.put("filterDesc", filterDesc);

        resourceRef = analysis.getCellResults();
        context.put("resultsFile", (resourceRef != null && resourceRef.getLocator() != null) ? resourceRef.getLocator().getURL() : "Not defined");

        IResourceLocator analysisLocator = analysis.getLocator();

        if (analysisLocator != null) {
            context.put("analysisLocation", analysisLocator.getURL());

            if (analysisLocator.isWritable()) {
                setSaveAllowed(true);
            }
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
        final OverlappingAnalysis analysis = getModel();
        if (analysis.getSourceData() == null) {
            Application.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from data ...", 1);
                final HeatmapEditor editor = new HeatmapEditor(createDataHeatmap(analysis));

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
        final OverlappingAnalysis analysis = getModel();
        if (analysis.getCellResults() == null) {
            Application.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = Application.get().getEditorsPanel();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Creating new heatmap from results ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getCellResults().get());
                heatmap.setTitle(analysis.getTitle() + " (results)");

                final HeatmapEditor editor = new HeatmapEditor(createResultsHeatmap(analysis));
                editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                editor.setName(editorPanel.deriveName(getName(), OverlappingAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        editorPanel.addEditor(editor);
                        Application.get().setStatusText("Heatmap for results created.");
                    }
                });
            }
        });

    }

    @Deprecated
    private Heatmap createDataHeatmap(OverlappingAnalysis analysis) {

        IMatrix data = analysis.getSourceData().get();
        if (Heatmap.class.isAssignableFrom(data.getClass())) {
            return (Heatmap) data;
        }

        Heatmap heatmap = new Heatmap(data);
        heatmap.setTitle(analysis.getTitle() + " (data)");

        return heatmap;

    }

    @Deprecated
    private Heatmap createResultsHeatmap(OverlappingAnalysis analysis) {

        IMatrix results = analysis.getCellResults().get();
        if (Heatmap.class.isAssignableFrom(results.getClass())) {
            return (Heatmap) results;
        }

        Heatmap heatmap = new Heatmap(results, true);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        for (HeatmapLayer layer : heatmap.getLayers()) {

            LinearDecorator dec = new LinearDecorator();
            Color minColor = new Color(0x63, 0xdc, 0xfe);
            Color maxColor = new Color(0xff, 0x00, 0x5f);
            dec.setMinValue(0.0);
            dec.setMinColor(minColor);
            dec.setMidValue(1.0);
            dec.setMidColor(maxColor);
            dec.setMaxValue(1.0);
            dec.setMaxColor(maxColor);
            dec.setEmptyColor(Color.WHITE);
            layer.setDecorator(dec);
        }

        heatmap.getLayers().setTopLayerIndex(heatmap.getLayers().indexOf("jaccard-index"));
        heatmap.setTitle(analysis.getTitle());

        return heatmap;
    }
}
