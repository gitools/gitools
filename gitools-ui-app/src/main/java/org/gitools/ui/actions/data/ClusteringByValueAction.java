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
package org.gitools.ui.actions.data;

import org.gitools.core.clustering.ClusteringMethod;
import org.gitools.core.clustering.ClusteringResults;
import org.gitools.core.clustering.HierarchicalClusteringResults;
import org.gitools.core.clustering.method.value.ClusterUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapHierarchicalColoredLabelsHeader;
import org.gitools.core.matrix.TransposedMatrixView;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.clustering.values.ClusteringValueWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ClusteringByValueAction extends BaseAction {

    public ClusteringByValueAction() {
        super("Clustering");
        setDesc("Cluster by values");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap heatmap = ActionUtils.getHeatmap();

        if (heatmap == null) {
            return;
        }

        final ClusteringValueWizard wiz = new ClusteringValueWizard(heatmap);
        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wiz);
        wdlg.setVisible(true);
        if (wdlg.isCancelled()) {
            return;
        }

        final ClusteringMethod method = wiz.getMethod();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    monitor.begin("Clustering  ...", 1);

                    ClusteringResults results = method.cluster(wiz.getClusterData(), monitor);

                    if (wiz.isSortDataSelected()) {
                        if (wiz.isApplyToRows()) {
                            TransposedMatrixView transposedMatrix = new TransposedMatrixView(heatmap);
                            ClusterUtils.updateVisibility(transposedMatrix, results.getDataIndicesByClusterTitle());
                        } else {
                            ClusterUtils.updateVisibility(heatmap, results.getDataIndicesByClusterTitle());
                        }
                    }

                    if (wiz.isNewickExportSelected()) {
                        File path = wiz.getSaveFilePage().getPathAsFile();
                        BufferedWriter out = new BufferedWriter(new FileWriter(path));
                        out.write(((HierarchicalClusteringResults) results).getTree().toString());
                        out.flush();
                        out.close();
                    }

                    if (wiz.isHeaderSelected()) {
                        boolean hcl = results instanceof HierarchicalClusteringResults;

                        HeatmapDimension hdim = wiz.isApplyToRows() ? heatmap.getRows() : heatmap.getColumns();

                        HeatmapColoredLabelsHeader header = hcl ? new HeatmapHierarchicalColoredLabelsHeader(hdim) : new HeatmapColoredLabelsHeader(hdim);

                        header.setTitle(wiz.getMethodName());

                        if (hcl) {
                            HeatmapHierarchicalColoredLabelsHeader hclHeader = (HeatmapHierarchicalColoredLabelsHeader) header;
                            HierarchicalClusteringResults hclResults = (HierarchicalClusteringResults) results;

                            hclHeader.setClusteringResults(hclResults);
                            int level = hclResults.getTree().getDepth() / 2;
                            hclHeader.setTreeLevel(level);
                        }

                        header.updateFromClusterResults(results);

                        if (wiz.isApplyToRows()) {
                            heatmap.getRows().addHeader(header);
                        } else {
                            heatmap.getColumns().addHeader(header);
                        }
                    }

                    monitor.end();
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Clusters created");
    }
}
