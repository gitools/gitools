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
package org.gitools.ui.analysis.combination.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.formats.analysis.CombinationAnalysisXmlFormat;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class CombinationAnalysisEditor extends AnalysisDetailsEditor<CombinationAnalysis>
{

    public CombinationAnalysisEditor(CombinationAnalysis analysis)
    {
        super(analysis, "/vm/analysis/combination/analysis_details.vm", null);
    }

    @Override
    protected void prepareContext(@NotNull VelocityContext context)
    {
        String combOf = "columns";
        if (analysis.isTransposeData())
        {
            combOf = "rows";
        }
        context.put("combinationOf", combOf);

        IResourceLocator resourceLocator = analysis.getData().getLocator();
        context.put("dataFile", resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getGroupsMap().getLocator();
        String groupsFile = resourceLocator != null ? resourceLocator.getName()
                : "Not specified. All " + combOf + " are combined";
        context.put("groupsFile", groupsFile);

        String sizeAttr = analysis.getSizeAttrName();
        if (sizeAttr == null || sizeAttr.isEmpty())
        {
            sizeAttr = "Constant value of 1";
        }
        context.put("sizeAttr", sizeAttr);

        String pvalueAttr = analysis.getPvalueAttrName();
        if (pvalueAttr == null || pvalueAttr.isEmpty())
        {
            List<IElementAttribute> attrs = analysis.getData().get().getCellAttributes();
            if (attrs.size() > 0)
            {
                pvalueAttr = attrs.get(0).getName();
            }
        }
        context.put("pvalueAttr", pvalueAttr);

        resourceLocator = analysis.getResults().getLocator();
        context.put("resultsFile",
                resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getLocator();
        if (resourceLocator != null)
        {
            context.put("analysisLocation", resourceLocator.getURL());
        }
        else
        {
            setSaveAllowed(true);
        }
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor)
    {
        xmlPersistance = new CombinationAnalysisXmlFormat();
        fileformat = CombinationAnalysisXmlFormat.COMBINATION;
        super.doSave(progressMonitor);
    }

    @Override
    protected void performUrlAction(String name, Map<String, String> params)
    {
        if ("NewDataHeatmap".equals(name))
        {
            newDataHeatmap();
        }
        else if ("ViewModuleMap".equals(name))
        {
            viewModuleMap();
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
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Creating new heatmap from data ...", 1);

                    IMatrixView dataTable = new MatrixView(analysis.getData().get());

                    Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
                    heatmap.setTitle(analysis.getTitle() + " (data)");

                    final HeatmapEditor editor = new HeatmapEditor(heatmap);

                    editor.setName(editorPanel.deriveName(
                            getName(), CombinationAnalysisXmlFormat.EXTENSION,
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

                    monitor.end();
                } catch (Exception e)
                {
                    monitor.exception(e);
                }
            }
        });
    }

    private void viewModuleMap()
    {
        UnimplementedDialog.show(AppFrame.get());
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
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from results ...", 1);

                try
                {
                    IMatrixView dataTable = new MatrixView(analysis.getResults().get());

                    Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
                    heatmap.setTitle(analysis.getTitle() + " (results)");

                    final CombinationResultsEditor editor = new CombinationResultsEditor(analysis);

                    editor.setName(editorPanel.deriveName(
                            getName(), CombinationAnalysisXmlFormat.EXTENSION,
                            "-results", ""));

                    SwingUtilities.invokeLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            editorPanel.addEditor(editor);
                            AppFrame.get().setStatusText("Heatmap for combination results created.");
                        }
                    });
                } catch (Exception e)
                {
                    monitor.exception(e);
                }
            }
        });
    }
}
