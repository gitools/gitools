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
package org.gitools.ui.analysis.htest.editor;

import org.apache.commons.lang.WordUtils;
import org.apache.velocity.VelocityContext;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.model.ToolConfig;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.decorator.impl.BinaryElementDecorator;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.formats.analysis.OncodriveAnalysisFormat;
import org.gitools.stats.test.factory.TestFactory;
import org.gitools.ui.analysis.editor.AnalysisDetailsEditor;
import org.gitools.ui.analysis.htest.editor.actions.ViewRelatedDataFromColumnAction;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OncodriveAnalysisEditor extends AnalysisDetailsEditor<OncodriveAnalysis>
{

    public OncodriveAnalysisEditor(OncodriveAnalysis analysis)
    {
        super(analysis, "/vm/analysis/oncodrive/analysis_details.vm", null);
    }

    @Override
    protected void prepareContext(@NotNull VelocityContext context)
    {

        IResourceLocator fileRef = analysis.getData().getLocator();

        context.put("dataFile", fileRef != null ? fileRef.getName() : "Not defined");

        ToolConfig testConfig = analysis.getTestConfig();
        if (!testConfig.get(TestFactory.TEST_NAME_PROPERTY).equals(""))
        {
            context.put("test", WordUtils.capitalize(testConfig.get(TestFactory.TEST_NAME_PROPERTY)));
            HashMap<String, Object> testAttributes = new HashMap<String, Object>();
            for (String key : testConfig.getConfiguration().keySet())
            {
                if (!key.equals(TestFactory.TEST_NAME_PROPERTY))
                {
                    testAttributes.put(WordUtils.capitalize(key), WordUtils.capitalize(testConfig.get(key)));
                }
            }
            if (testAttributes.size() > 0)
            {
                context.put("testAttributes", testAttributes);
            }

        }

        CutoffCmp cmp = analysis.getBinaryCutoffCmp();
        String filterDesc = cmp == null ? "Not filtered" : "Binary cutoff filter for values " + cmp.getLongName() + " " + analysis.getBinaryCutoffValue();
        context.put("filterDesc", filterDesc);

        fileRef = analysis.getModuleMap().getLocator();

        context.put("modulesFile", fileRef != null ? fileRef.getName() : "Unknown");

        context.put("moduleMinSize", analysis.getMinModuleSize());
        int maxSize = analysis.getMaxModuleSize();
        context.put("moduleMaxSize", maxSize != Integer.MAX_VALUE ? maxSize : "No limit");

        if (analysis.getMtc().equals("bh"))
        {
            context.put("mtc", "Benjamini Hochberg FDR");
        }
        else if (analysis.getMtc().equals("bonferroni"))
        {
            context.put("mtc", "Bonferroni");
        }

        fileRef = analysis.getResults().getLocator();
        context.put("resultsFile", fileRef != null ? fileRef.getName() : "Not defined");

        fileRef = analysis.getLocator();
        if (fileRef != null)
        {
            context.put("analysisLocation", fileRef.getURL());
        }
        else
        {
            setSaveAllowed(true);
        }

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
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Creating new heatmap from data ...", 1);

                IMatrixView dataTable = new MatrixView(analysis.getData().get());

                Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
                String testName = analysis.getTestConfig().getConfiguration().get(TestFactory.TEST_NAME_PROPERTY);
                if (!testName.equals(TestFactory.ZSCORE_TEST))
                {
                    //entry data is binary
                    ElementDecorator[] decorators = new ElementDecorator[1];
                    decorators[0] = new BinaryElementDecorator(heatmap.getActiveCellDecorator().getAdapter());
                    heatmap.setCellDecorators(decorators);
                }
                heatmap.setTitle(analysis.getTitle() + " (data)");

                final HeatmapEditor editor = new HeatmapEditor(heatmap);

                editor.setName(editorPanel.deriveName(getName(), OncodriveAnalysisFormat.EXTENSION, "-data", ""));

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

                final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));

                editor.setName(editorPanel.deriveName(getName(), OncodriveAnalysisFormat.EXTENSION, "-results", ""));

                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        editorPanel.addEditor(editor);
                        AppFrame.get().setStatusText("Heatmap for oncodrive results created.");
                    }
                });
            }
        });
    }

    @NotNull
    private static Heatmap createHeatmap(@NotNull OncodriveAnalysis analysis)
    {
        IMatrixView dataTable = new MatrixView(analysis.getResults().get());
        Heatmap heatmap = HeatmapUtil.createFromMatrixView(dataTable);
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }

    //TODO
    @NotNull
    protected static List<BaseAction> createToolBar(@NotNull OncodriveAnalysis analysis)
    {
        ViewRelatedDataFromColumnAction action = new ViewRelatedDataFromColumnAction(analysis.getTitle(), analysis.getData().get(), analysis.getModuleMap().get());
        List<BaseAction> tb = new ArrayList<BaseAction>();
        tb.add(action);
        return tb;
    }
}
