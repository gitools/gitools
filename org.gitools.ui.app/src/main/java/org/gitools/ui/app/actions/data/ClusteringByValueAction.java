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

import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.ClusteringResults;
import org.gitools.analysis.clustering.HierarchicalClusteringResults;
import org.gitools.analysis.clustering.hierarchical.Cluster;
import org.gitools.analysis.clustering.method.value.ClusterUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.clustering.values.ClusteringValueWizard;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Set;

public class ClusteringByValueAction extends HeatmapAction {

    public ClusteringByValueAction() {
        super("Clustering...");
        setDesc("Cluster by values");
        setMnemonic(KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Heatmap heatmap = getHeatmap();

        final ClusteringValueWizard wiz = new ClusteringValueWizard(heatmap);
        WizardDialog wdlg = new WizardDialog(Application.get(), wiz);
        wdlg.setVisible(true);
        if (wdlg.isCancelled()) {
            return;
        }

        final ClusteringMethod method = wiz.getMethod();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Clustering  ...", 1);

                    // Cluster data
                    ClusteringResults results = method.cluster(wiz.getClusterData(), monitor);

                    if (results instanceof Cluster) {
                        Application.get().getEditorsPanel().addEditor(new DendrogramEditor((Cluster) results));
                        return;
                    }

                    // Target dimension
                    HeatmapDimension hdim = wiz.isApplyToRows() ? heatmap.getRows() : heatmap.getColumns();
                    boolean hcl = results instanceof HierarchicalClusteringResults;

                    // Save results as an annotation
                    String annotationLabel = wiz.getMethodName() + " " + heatmap.getLayers().get(wiz.getDataAttribute()).getId();

                    AnnotationMatrix annotationMatrix = hdim.getAnnotations();
                    if (hcl) {
                        HierarchicalClusteringResults hresults = (HierarchicalClusteringResults) results;
                        for (int l = 0; l < hresults.getTree().getDepth(); l++) {
                            hresults.setLevel(l);
                            Collection<String> clusters = results.getClusters();
                            for (String cluster : clusters) {
                                Set<String> items = results.getItems(cluster);

                                for (String item : items) {
                                    annotationMatrix.setAnnotation(item, annotationLabel + " level " + l, cluster);
                                }
                            }
                        }
                    } else {
                        Collection<String> clusters = results.getClusters();
                        for (String cluster : clusters) {
                            for (String item : results.getItems(cluster)) {
                                annotationMatrix.setAnnotation(item, annotationLabel, cluster);
                            }
                        }
                    }

                    // Sort the matrix
                    if (wiz.isSortDataSelected()) {
                        ClusterUtils.updateVisibility(hdim, results.getClustersMap());
                    }

                    // Export newick file
                    if (wiz.isNewickExportSelected()) {
                        File path = wiz.getSaveFilePage().getPathAsFile();
                        BufferedWriter out = new BufferedWriter(new FileWriter(path));
                        out.write(((HierarchicalClusteringResults) results).getTree().toString());
                        out.flush();
                        out.close();
                    }

                    // Add header
                    if (wiz.isHeaderSelected()) {

                        if (hcl) {
                            HierarchicalClusteringResults hresults = (HierarchicalClusteringResults) results;
                            int depth = hresults.getTree().getDepth();
                            depth = (depth > 10 ? 10 : depth);
                            for (int l = 0; l < depth; l++) {
                                hresults.setLevel(l);
                                HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(hdim);
                                header.updateFromClusterResults(hresults);
                                header.setTitle(annotationLabel + " level " + l);
                                header.setSize(4);
                                header.setAnnotationPattern("${" + annotationLabel + " level " + l + "}");
                                hdim.addHeader(header);
                            }
                        } else {
                            HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(hdim);
                            header.updateFromClusterResults(results);
                            header.setTitle(annotationLabel);
                            header.setAnnotationPattern("${" + annotationLabel + "}");
                            hdim.addHeader(header);
                        }

                    }

                    monitor.end();
                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().setStatusText("Clusters created");
    }
}
