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
package org.gitools.ui.app.actions.analysis;

import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.Clusters;
import org.gitools.analysis.clustering.HierarchicalClusteringResults;
import org.gitools.analysis.clustering.hierarchical.Cluster;
import org.gitools.analysis.clustering.method.value.ClusterUtils;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.analysis.clustering.ClusteringWizard;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.app.commands.CommandAddHeaderColoredLabels;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClusteringAction extends HeatmapAction {

    public ClusteringAction() {
        super("Clustering...");
        setDesc("Cluster by values");
        setMnemonic(KeyEvent.VK_L);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Heatmap heatmap = getHeatmap();

        final ClusteringWizard wiz = new ClusteringWizard(heatmap);
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
                    Clusters results = method.cluster(wiz.getClusterData(), monitor);

                    // Target dimension
                    HeatmapDimension clusteringDimension = wiz.isApplyToRows() ? heatmap.getRows() : heatmap.getColumns();

                    // Save results as an annotation
                    String annotationLabel = wiz.getMethodName() + " " + heatmap.getLayers().get(wiz.getDataAttribute()).getId();

                    AnnotationMatrix annotationMatrix = clusteringDimension.getAnnotations();
                    if (results instanceof Cluster) {

                        Cluster hresults = (Cluster) results;

                        Application.get().getEditorsPanel().addEditor(new DendrogramEditor(hresults));

                        int l=0;
                        List<Cluster> children = hresults.getChildren();
                        while (!children.isEmpty()) {
                            l++;

                            List<Cluster> nextLevel = new ArrayList<>();
                            for (Cluster cluster : children) {
                                annotationMatrix.setAnnotation(cluster.getName(), annotationLabel + " level " + l, cluster.getName());
                                nextLevel.addAll(cluster.getChildren());
                            }

                            children = nextLevel;
                        }
                    } else {
                        Collection<String> clusters = results.getClusters();
                        for (String cluster : clusters) {
                            for (String item : results.getItems(cluster)) {
                                annotationMatrix.setAnnotation(item, annotationLabel, cluster);
                            }
                        }
                    }

                    // Add header
                    if (wiz.isHeaderSelected()) {

                        if (results instanceof Cluster) {
                            Cluster hresults = (Cluster) results;

                            //int depth = hresults.getTree().getDepth();
                            //depth = (depth > 10 ? 10 : depth);
                            int depth = 3;
                            for (int l = 0; l < depth; l++) {
                                //hresults.setLevel(l);
                                HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(clusteringDimension);
                                CommandAddHeaderColoredLabels.updateFromClusterResults(header, hresults);
                                header.setTitle(annotationLabel + " level " + l);
                                header.setSize(4);
                                header.setAnnotationPattern("${" + annotationLabel + " level " + l + "}");
                                clusteringDimension.addHeader(header);
                            }
                        } else {
                            HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(clusteringDimension);
                            CommandAddHeaderColoredLabels.updateFromClusterResults(header, results);
                            header.setTitle(annotationLabel);
                            header.setAnnotationPattern("${" + annotationLabel + "}");
                            clusteringDimension.addHeader(header);
                        }

                    }

                    // Sort the matrix
                    if (wiz.isSortDataSelected()) {
                        ClusterUtils.updateVisibility(clusteringDimension, results.getClustersMap());
                    }

                    // Export newick file
                    if (wiz.isNewickExportSelected()) {
                        File path = wiz.getSaveFilePage().getPathAsFile();
                        BufferedWriter out = new BufferedWriter(new FileWriter(path));
                        out.write(((HierarchicalClusteringResults) results).getTree().toString());
                        out.flush();
                        out.close();
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
