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

import com.google.common.base.Function;
import org.apache.commons.math3.util.FastMath;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.Clusters;
import org.gitools.analysis.clustering.hierarchical.Cluster;
import org.gitools.analysis.clustering.method.value.ClusterUtils;
import org.gitools.analysis.clustering.method.value.HierarchicalMethod;
import org.gitools.analysis.clustering.method.value.WekaCobWebMethod;
import org.gitools.analysis.clustering.method.value.WekaKmeansMethod;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.sort.SortByLabelComparator;
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
import java.util.*;

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

                    // Cluster data
                    monitor.start();
                    Clusters results = method.cluster(wiz.getClusterData(), monitor);
                    monitor.end();

                    // Target dimension
                    HeatmapDimension clusteringDimension = heatmap.getDimension(wiz.getClusteringDimension());

                    // Save results as an annotation
                    String annotationLabel = "Clusters " + wiz.getClusteringLayer();

                    AnnotationMatrix annotationMatrix = clusteringDimension.getAnnotations();
                    int maxLevel=0;
                    Map<Integer, List<Cluster>> clustersMapPerLevel = new HashMap<>();
                    if (results instanceof Cluster) {

                        Cluster rootCluster = (Cluster) results;

                        Application.get().getEditorsPanel().addEditor(new DendrogramEditor(rootCluster));

                        List<Cluster> children = rootCluster.getChildren();
                        rootCluster.setName("");

                        while (!children.isEmpty() && maxLevel < 15) {
                            maxLevel++;

                            List<Cluster> nextLevel = new ArrayList<>();
                            for (Cluster cluster : children) {
                                if (!cluster.getChildren().isEmpty()) {
                                    for (String identifier : cluster.getIdentifiers()) {
                                        annotationMatrix.setAnnotation(identifier, annotationLabel + " " + maxLevel, cluster.getName());
                                    }
                                }
                                nextLevel.addAll(cluster.getChildren());
                            }

                            clustersMapPerLevel.put(maxLevel, children);
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
                    String sortLabel;
                    if (results instanceof Cluster) {

                        // Hierarchical clustering

                        int depth = FastMath.min(10, maxLevel);
                        for (int l = depth; l >= 1; l--) {
                            HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(clusteringDimension);
                            ClusterUtils.updateHierarchicalColors(header, clustersMapPerLevel.get(l));
                            header.setTitle(annotationLabel + " " + l);
                            header.setSize(7);
                            sortLabel = "${" + annotationLabel + " " + l + "}";
                            header.setAnnotationPattern(sortLabel);
                            clusteringDimension.addHeader(header);
                            clusteringDimension.sort(new SortByLabelComparator(SortDirection.ASCENDING, new HierarchicalSortFunction(l, annotationLabel + " ", clusteringDimension.getAnnotations()), false));
                        }

                    } else {
                        sortLabel = "${" + annotationLabel + "}";
                        HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(clusteringDimension);
                        CommandAddHeaderColoredLabels.updateFromClusterResults(header, results.getClusters());
                        header.setTitle(annotationLabel);
                        header.setAnnotationPattern(sortLabel);
                        clusteringDimension.addHeader(header);

                        // Sort the header
                        clusteringDimension.sort(new SortByLabelComparator(SortDirection.ASCENDING, new PatternFunction(sortLabel, clusteringDimension.getAnnotations()), false));
                    }



                } catch (Throwable ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().setStatusText("Clusters created");
    }

    private static class HierarchicalSortFunction implements Function<String, String> {

        private int level;
        private String prefix;
        private IAnnotations annotations;

        private HierarchicalSortFunction(int level, String prefix, IAnnotations annotations) {
            this.level = level;
            this.prefix = prefix;
            this.annotations = annotations;
        }

        @Override
        public String apply(String identifier) {

            String clusterName = annotations.getAnnotation(identifier, prefix + level);

            for (int l = level - 1; l>=1 && clusterName==null; l--) {
                clusterName = annotations.getAnnotation(identifier, prefix + l);
            }

            return clusterName;
        }
    }
}
