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
package org.gitools.ui.analysis.groupcomparison.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence._DEPRECATED.FileFormats;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.formats.analysis.GroupComparisonAnalysisXmlFormat;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.SerialClone;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.swing.*;
import java.util.List;
import java.util.Map;


public class GroupComparisonAnalysisEditor extends AnalysisDetailsEditor<GroupComparisonAnalysis>
{

    public GroupComparisonAnalysisEditor(GroupComparisonAnalysis analysis)
    {
        super(analysis, "/vm/analysis/groupcomparison/analysis_details.vm", null);
    }


    @Override
    protected void prepareContext(VelocityContext context)
    {

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile",
                fileRef != null ? fileRef.getName() : "Not defined");

        context.put("mtc", analysis.getMtc().getName());

        fileRef = analysis.getResults().getLocator();
        context.put("resultsFile",
                fileRef != null ? fileRef.getName() : "Not defined");

        fileRef = analysis.getLocator();
        context.put("analysisLocation", fileRef.getURL());

        if (fileRef == null || fileRef.isWritable())
        {
            setSaveAllowed(true);
        }

    }

    @Override
    public void doSave(IProgressMonitor progressMonitor)
    {
        xmlPersistance = new GroupComparisonAnalysisXmlFormat();
        fileformat = FileFormats.GROUP_COMPARISON;
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
        if (analysis.getData() == null)
        {
            AppFrame.get().setStatusText("Analysis doesn't contain data.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from data ...", 1);

                IMatrixView dataTable = new MatrixView(analysis.getData());

                Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
                heatmap.setTitle(analysis.getTitle() + " (data)");

                if (analysis.getRowHeaders() != null)
                {
                    heatmap.getRowDim().setAnnotations(analysis.getRowAnnotations());
                    copyHeaders(heatmap.getRowDim(), analysis.getRowHeaders());
                }
                if (analysis.getColumnHeaders() != null)
                {
                    heatmap.getColumnDim().setAnnotations(analysis.getColumnAnnotations());
                    copyHeaders(heatmap.getColumnDim(), analysis.getColumnHeaders());
                }

                final HeatmapEditor editor = new HeatmapEditor(heatmap);


                editor.setName(editorPanel.deriveName(
                        getName(), FileSuffixes.GROUP_COMPARISON,
                        "-data", ""));

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


    private void copyHeaders(HeatmapDim dim, List<HeatmapHeader> headers)
    {

        dim.removeHeader(0);

        for (HeatmapHeader hh : headers)
        {
            HeatmapHeader headerCopy = null;
            if (hh instanceof HeatmapTextLabelsHeader)
            {
                HeatmapTextLabelsHeader oldHeader = (HeatmapTextLabelsHeader) hh;
                HeatmapTextLabelsHeader textHeaderCopy = new HeatmapTextLabelsHeader(dim);
                textHeaderCopy.setFont(oldHeader.getFont());
                textHeaderCopy.setLabelColor(oldHeader.getLabelColor());
                textHeaderCopy.setLabelAnnotation(oldHeader.getLabelAnnotation());
                textHeaderCopy.setLabelPattern(oldHeader.getLabelPattern());
                textHeaderCopy.setLabelSource(oldHeader.getLabelSource());
                headerCopy = textHeaderCopy;
            }
            else if (hh instanceof HeatmapColoredLabelsHeader)
            {
                //headerCopy = hh;
                // Not working yet!

                HeatmapColoredLabelsHeader oldHeader = (HeatmapColoredLabelsHeader) hh;
                HeatmapColoredLabelsHeader colorHeaderCopy = new HeatmapColoredLabelsHeader(dim);
                colorHeaderCopy.setLabelColor(oldHeader.getLabelColor());
                colorHeaderCopy.setForceLabelColor(oldHeader.isForceLabelColor());
                colorHeaderCopy.setLabelFont(oldHeader.getLabelFont());
                colorHeaderCopy.setLabelRotated(oldHeader.isLabelRotated());
                colorHeaderCopy.setLabelVisible(oldHeader.isLabelVisible());
                colorHeaderCopy.setMargin(oldHeader.getMargin());
                colorHeaderCopy.setSeparationGrid(oldHeader.isSeparationGrid());
                colorHeaderCopy.setThickness(oldHeader.getThickness());

                ColoredLabel[] clusters = oldHeader.getClusters();
                ColoredLabel[] newClusters = new ColoredLabel[clusters.length];
                int index = 0;

                for (ColoredLabel cl : clusters)
                {
                    //newClusters[index] ;
                    ColoredLabel newcl =
                            new ColoredLabel(cl.getDisplayedLabel(), cl.getColor());
                    newClusters[index] = newcl;
                    index++;
                }
                colorHeaderCopy.setClusters(newClusters);
                colorHeaderCopy.setAssignedColoredLabels(
                        oldHeader.getAssignedColoredLabels());

                dim.getAnnotations().getRows();

                headerCopy = colorHeaderCopy;

            }
            headerCopy.setBackgroundColor(hh.getBackgroundColor());
            //newHh.setHeatmapDim(dim);
            headerCopy.setSize(hh.getSize());
            headerCopy.setTitle(hh.getTitle());
            headerCopy.setVisible(hh.isVisible());
            dim.addHeader(headerCopy);
        }
    }

    private void newResultsHeatmap()
    {
        if (analysis.getResults() == null)
        {
            AppFrame.get().setStatusText("Analysis doesn't contain results.");
            return;
        }

        final EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from results ...", 1);

                IMatrixView dataTable = new MatrixView(analysis.getResults());

                Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
                heatmap.setTitle(analysis.getTitle() + " (results)");

                if (analysis.getRowHeaders() != null)
                {
                    heatmap.getRowDim().setAnnotations(SerialClone.xclone(analysis.getRowAnnotations()));
                    heatmap.getRowDim().removeHeader(0);
                    for (HeatmapHeader hh : analysis.getRowHeaders())
                    {
                        heatmap.getRowDim().addHeader(SerialClone.xclone(hh));
                    }
                }

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(
                        getName(), FileSuffixes.GROUP_COMPARISON,
                        "-results", ""));

                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("Heatmap for group comparison results created.");
                    }
                });
            }
        });
    }
}
