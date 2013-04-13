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
package org.gitools.ui.analysis.overlapping;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.DiagonalMatrixView;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.decorator.impl.LinearTwoSidedElementDecorator;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.analysis.OverlappingAnalysisFormat;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class OverlappingAnalysisEditor extends AnalysisDetailsEditor<OverlappingAnalysis>
{

    public OverlappingAnalysisEditor(OverlappingAnalysis analysis)
    {
        super(analysis, "/vm/analysis/overlapping/analysis_details.vm", null);
    }

    @Override
    protected void prepareContext(@NotNull VelocityContext context)
    {


        ResourceReference<IMatrix> resourceRef = analysis.getFilteredData();
        context.put("filteredDataFile", (resourceRef != null ? resourceRef.getLocator().getURL() : "Not defined"));

        String appliedTo = analysis.isTransposeData() ? "rows" : "columns";
        context.put("appliedTo", appliedTo);

        CutoffCmp cmp = analysis.getBinaryCutoffCmp();
        String filterDesc = cmp == null ? "Not filtered" : "Binary cutoff filter for values " + cmp.getLongName() + " " + analysis.getBinaryCutoffValue();
        context.put("filterDesc", filterDesc);

        resourceRef = analysis.getCellResults();
        context.put("resultsFile", resourceRef != null ? resourceRef.getLocator().getURL() : "Not defined");

        IResourceLocator analysisLocator = analysis.getLocator();

        if (analysisLocator != null)
        {
            context.put("analysisLocation", analysisLocator.getURL());

            if (analysisLocator.isWritable())
            {
                setSaveAllowed(true);
            }
        }

    }

    @Override
    public void doSave(IProgressMonitor progressMonitor)
    {

        xmlPersistance = new OverlappingAnalysisFormat();
        fileformat = OverlappingAnalysisFormat.FILE_FORMAT;
        super.doSave(progressMonitor);
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params)
    {
        if ("NewDataHeatmap".equals(name))
        {
            newDataHeatmap();
        }
        else if ("NewResultsHeatmap".equals(name))
        {
            newResultsHeatmap();
        }
    }

    private void newDataHeatmap()
    {
        if (analysis.getSourceData() == null)
        {
            AppFrame.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from data ...", 1);

                Heatmap heatmap =  new Heatmap(analysis.getSourceData().get());
                heatmap.setTitle(analysis.getTitle() + " (data)");

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(getName(), OverlappingAnalysisFormat.EXTENSION, "-data", ""));

                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("New heatmap created.");
                    }
                });
            }
        });
    }

    private void newResultsHeatmap()
    {
        if (analysis.getCellResults() == null)
        {
            AppFrame.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from results ...", 1);

                Heatmap heatmap = new Heatmap(analysis.getCellResults().get());
                heatmap.setTitle(analysis.getTitle() + " (results)");

                final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));

                editor.setName(editorPanel.deriveName(getName(), OverlappingAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("Heatmap for results created.");
                    }
                });
            }
        });


    }

    @Nullable
    private static Heatmap createHeatmap(@NotNull OverlappingAnalysis analysis)
    {
        IMatrixView results = new DiagonalMatrixView(analysis.getCellResults().get());
        Heatmap heatmap = new Heatmap(results);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        int propertiesNb = results.getLayers().size();
        LinearTwoSidedElementDecorator[] dec = new LinearTwoSidedElementDecorator[propertiesNb];
        for (int i = 0; i < propertiesNb; i++)
        {
            dec[i] = new LinearTwoSidedElementDecorator(results.getCellAdapter());
            int valueIndex = results.getLayers().findId("jaccard-index");
            Color minColor = new Color(0x63, 0xdc, 0xfe);
            Color maxColor = new Color(0xff, 0x00, 0x5f);
            dec[i].setValueIndex(valueIndex != -1 ? valueIndex : 0);
            dec[i].setMinValue(0.0);
            dec[i].setMinColor(minColor);
            dec[i].setMidValue(1.0);
            dec[i].setMidColor(maxColor);
            dec[i].setMaxValue(1.0);
            dec[i].setMaxColor(maxColor);
            dec[i].setEmptyColor(Color.WHITE);
        }
        heatmap.setCellDecorators(dec);

        heatmap.setTitle(analysis.getTitle());

        return heatmap;
    }
}
