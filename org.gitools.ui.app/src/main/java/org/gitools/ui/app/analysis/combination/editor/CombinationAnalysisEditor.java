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
package org.gitools.ui.app.analysis.combination.editor;

import org.apache.velocity.VelocityContext;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.model.decorator.impl.PValueDecorator;
import org.gitools.persistence.formats.analysis.CombinationAnalysisFormat;
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

public class CombinationAnalysisEditor extends AnalysisDetailsEditor<CombinationAnalysis> {

    public CombinationAnalysisEditor(CombinationAnalysis analysis) {
        super(analysis, "/vm/analysis/combination/analysis_details.vm", null);
    }

    @Override
    protected void prepareContext(VelocityContext context) {
        String combOf = "columns";
        if (analysis.isTransposeData()) {
            combOf = "rows";
        }
        context.put("combinationOf", combOf);

        IResourceLocator resourceLocator = analysis.getData().getLocator();
        context.put("dataFile", resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getGroupsMap().getLocator();
        String groupsFile = resourceLocator != null ? resourceLocator.getName() : "Not specified. All " + combOf + " are combined";
        context.put("groupsFile", groupsFile);

        String sizeAttr = analysis.getSizeLayer();
        if (sizeAttr == null || sizeAttr.isEmpty()) {
            sizeAttr = "Constant value of 1";
        }
        context.put("sizeAttr", sizeAttr);

        resourceLocator = analysis.getResults().getLocator();
        context.put("resultsFile", resourceLocator != null ? resourceLocator.getName() : "Not defined");

        resourceLocator = analysis.getLocator();
        if (resourceLocator != null) {
            context.put("analysisLocation", resourceLocator.getURL());
        } else {
            setSaveAllowed(true);
        }
    }

    @Override
    public void doSave(IProgressMonitor progressMonitor) {
        xmlPersistance = new CombinationAnalysisFormat();
        fileformat = CombinationAnalysisFormat.FILE_FORMAT;
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
                try {
                    monitor.begin("Creating new heatmap from data ...", 1);

                    Heatmap heatmap = new Heatmap(analysis.getData().get());
                    heatmap.setTitle(analysis.getTitle() + " (data)");

                    final HeatmapEditor editor = new HeatmapEditor(heatmap);

                    editor.setName(editorPanel.deriveName(getName(), CombinationAnalysisFormat.EXTENSION, "-data", ""));

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            editorPanel.addEditor(editor);
                            Application.get().setStatusText("New heatmap created.");
                        }
                    });

                    monitor.end();
                } catch (Exception e) {
                    monitor.exception(e);
                }
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

                try {
                    Heatmap heatmap = new Heatmap(analysis.getResults().get());
                    heatmap.getLayers().setTopLayerById("right-p-value");
                    heatmap.getLayers().getTopLayer().setDecorator(new PValueDecorator());
                    heatmap.setTitle(analysis.getTitle() + " (results)");

                    final HeatmapEditor editor = new HeatmapEditor(createHeatmap(analysis));
                    editor.setIcon(IconUtils.getIconResource(IconNames.analysisHeatmap16));

                    editor.setName(editorPanel.deriveName(getName(), CombinationAnalysisFormat.EXTENSION, "-results", ""));

                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            editorPanel.addEditor(editor);
                            Application.get().setStatusText("Heatmap for combination results created.");
                        }
                    });
                } catch (Exception e) {
                    monitor.exception(e);
                }
            }
        });
    }


    private static Heatmap createHeatmap(CombinationAnalysis analysis) {
        Heatmap heatmap = new Heatmap(analysis.getResults().get());
        heatmap.setTitle(analysis.getTitle() + " (results)");
        return heatmap;
    }
}
