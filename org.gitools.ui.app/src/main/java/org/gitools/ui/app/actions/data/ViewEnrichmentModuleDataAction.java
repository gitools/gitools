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
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.ui.app.analysis.htest.editor.AbstractHtestAnalysisEditor;
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


public class ViewEnrichmentModuleDataAction extends HeatmapAction implements IHeatmapDimensionAction {

    private String annotationValue;
    private HeatmapColoredLabelsHeader coloredHeader;
    private EnrichmentAnalysis analysis;
    private String moduleId;

    public ViewEnrichmentModuleDataAction() {
        super("<html><i>ViewEnrichmentModuleDataAction</i></html>");
        setSmallIconFromResource(IconNames.view16);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (moduleId == null) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) throws Exception {
                IMatrix data = analysis.getData().get();
                if (data instanceof Heatmap) {
                    data = ((Heatmap) data).getContents();
                }
                EditorsPanel epanel = Application.get().getEditorsPanel();

                ArrayList moduleItems = new ArrayList();
                moduleItems.addAll(analysis.getModuleMap().get().getMappingItems(moduleId));

                HeatmapEditor heatmapEditor = getOpenInEditor(data);
                if (heatmapEditor != null) {
                    epanel.setSelectedEditor(heatmapEditor);
                } else {
                    monitor.begin("Creating new heatmap from data ...", 1);
                    heatmapEditor = new HeatmapEditor(AbstractHtestAnalysisEditor.newDataHeatmap(analysis));
                    epanel.addEditor(heatmapEditor);
                    heatmapEditor.setName(analysis.getTitle() + "-data");
                }
                Heatmap dataHeatmap = heatmapEditor.getModel();
                dataHeatmap.getRows().show(moduleItems);
                Application.get().setStatusTextPermanent("Module '" + moduleId + "' shown.");
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
        boolean enable = getHeatmap().getMetadata(EnrichmentAnalysis.CACHE_KEY_ENHRICHMENT) != null &&
                dimension.getId().equals(MatrixDimensionKey.ROWS);
        setEnabled(enable);
        if (enable) {
            this.analysis = getHeatmap().getMetadata(EnrichmentAnalysis.CACHE_KEY_ENHRICHMENT);
            this.moduleId = dimension.getLabel(position.getRow());
        }
        this.setName("<html><i>View</i> data for '" + StringUtils.abbreviate(moduleId, 25) + "' module</html>");


    }
}
