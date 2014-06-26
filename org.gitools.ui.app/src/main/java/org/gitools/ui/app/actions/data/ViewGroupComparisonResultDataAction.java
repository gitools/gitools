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
package org.gitools.ui.app.actions.data;

import org.apache.commons.lang.StringUtils;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.groupcomparison.dimensiongroups.DimensionGroup;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.app.commands.CommandAddHeaderColoredLabels;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.core.components.editor.AbstractEditor;
import org.gitools.ui.core.components.editor.EditorsPanel;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class ViewGroupComparisonResultDataAction extends HeatmapAction implements IHeatmapDimensionAction {

    public static final String GROUP_GC_ANALYSIS_ANNOTATION = "Group (GC-analysis)";
    private String annotationValue;
    private HeatmapColoredLabelsHeader coloredHeader;
    private GroupComparisonAnalysis analysis;
    private String rowId;

    public ViewGroupComparisonResultDataAction() {
        super("<html><i>ViewEnrichmentModuleDataAction</i></html>");
        setSmallIconFromResource(IconNames.view16);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (rowId == null) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                IMatrix data = analysis.getData().get();
                Heatmap dataHeatmap = null;
                if (data instanceof Heatmap) {
                    dataHeatmap = (Heatmap) data;
                    data = ((Heatmap) data).getContents();
                }
                EditorsPanel epanel = Application.get().getEditorsPanel();


                HeatmapEditor heatmapEditor = getOpenInEditor(data);
                if (heatmapEditor != null) {
                    epanel.setSelectedEditor(heatmapEditor);
                } else {

                    monitor.begin("Creating new heatmap from data ...", 1);
                    heatmapEditor = new HeatmapEditor(GroupComparisonAnalysisEditor.createDataHeatmap(analysis));
                    epanel.addEditor(heatmapEditor);
                    heatmapEditor.setName(analysis.getTitle() + "-data");
                    dataHeatmap = heatmapEditor.getModel();
                }

                // Show row in question
                ArrayList<String> rowIds = new ArrayList<>();
                rowIds.add(rowId);
                dataHeatmap.getRows().show(rowIds);
                if (dataHeatmap.getRows().getCellSize() < 30) {
                    dataHeatmap.getRows().setCellSize(30);
                }

                // Layer
                dataHeatmap.getLayers().setTopLayer(dataHeatmap.getLayers().get(analysis.getLayerName()));

                // Column groups
                ArrayList<String> colOrder = new ArrayList<>();
                IMatrixLayer<Double> layer = dataHeatmap.getLayers().get(analysis.getLayerName());
                for (DimensionGroup group : analysis.getGroups()) {

                    IMatrixPosition pos = dataHeatmap.getContents().newPosition().set(dataHeatmap.getRows(), rowId);
                    IMatrixIterable<Double> iterable = pos.iterate(layer, dataHeatmap.getColumns()).filter(group.getPredicate());
                    for (Double value : iterable) {
                        String columnName = iterable.getPosition().get(dataHeatmap.getColumns());
                        colOrder.add(columnName);
                        dataHeatmap.getColumns().getAnnotations().setAnnotation(columnName, GROUP_GC_ANALYSIS_ANNOTATION, group.getName());
                    }

                }
                dataHeatmap.getColumns().show(colOrder);

                // add header if not already added
                boolean hasHeader = false;
                for (HeatmapHeader h : dataHeatmap.getColumns().getHeaders()) {
                    if (h.getTitle().equals(GROUP_GC_ANALYSIS_ANNOTATION)) {
                        hasHeader = true;
                    }
                }
                if (!hasHeader) {
                    new CommandAddHeaderColoredLabels(heatmapEditor.getName(), "COLUMNS", "${" + GROUP_GC_ANALYSIS_ANNOTATION + "}",
                            null, null, true, true, "").execute(monitor);
                }

                Application.get().showNotificationPermanent("Data for '" + rowId + "' grouped and shown.");
            }
        });

    }

    public HeatmapEditor getOpenInEditor(IMatrix data) {
        EditorsPanel epanel = Application.get().getEditorsPanel();

        for (AbstractEditor e : epanel.getEditors()) {
            if (e instanceof HeatmapEditor) {
                if (((Heatmap) e.getModel()).getContents() == data) {
                    return (HeatmapEditor) e;
                }
            }
        }
        return null;
    }


    @Override
    public void onConfigure(HeatmapDimension dimension, HeatmapPosition position) {
        boolean enable = getHeatmap().getMetadata(GroupComparisonAnalysis.CACHE_KEY_GC_ANALYSIS) != null &&
                dimension.getId().equals(MatrixDimensionKey.ROWS);
        setEnabled(enable);
        if (enable) {
            this.analysis = getHeatmap().getMetadata(GroupComparisonAnalysis.CACHE_KEY_GC_ANALYSIS);
            this.rowId = dimension.getLabel(position.getRow());
        }
        this.setName("<html><i>View</i> data for '" + StringUtils.abbreviate(rowId, 25) + "' results</html>");


    }
}
