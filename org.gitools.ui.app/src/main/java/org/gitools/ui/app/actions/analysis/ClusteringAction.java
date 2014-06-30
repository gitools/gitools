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
import org.gitools.analysis.clustering.hierarchical.HierarchicalMethod;
import org.gitools.analysis.clustering.kmeans.KMeansPlusPlusMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.sort.SortByLabelComparator;
import org.gitools.ui.app.analysis.clustering.ClusteringWizard;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.app.commands.CommandAddHeaderColoredLabels;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

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
        wdlg.open();
        if (wdlg.isCancelled()) {
            return;
        }

        final ClusteringMethod method = wiz.getMethod();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {

                // Cluster data
                Clusters results = method.cluster(wiz.getClusterData(), monitor);

                // Target dimension
                HeatmapDimension clusteringDimension = heatmap.getDimension(wiz.getClusteringDimension());

                // Annotations
                AnnotationMatrix annotationMatrix = clusteringDimension.getAnnotations();

                if (method instanceof HierarchicalMethod) {
                    processHierarchical(results, clusteringDimension, annotationMatrix, wiz.getClusteringLayer(), method, heatmap);

                } else {
                    processKMeans(results, clusteringDimension, annotationMatrix, wiz.getClusteringLayer(), method, heatmap);
                }
            }
        });

        Application.get().showNotificationPermanent("Clusters and bookmarks created and applied");
    }

    private static void processKMeans(Clusters results, HeatmapDimension clusteringDimension, AnnotationMatrix annotationMatrix, String layerId, ClusteringMethod method, Heatmap heatmap) {

        String annotationLabel = "Cluster " + layerId;

        if (method instanceof KMeansPlusPlusMethod) {
            annotationLabel = annotationLabel + " #" + ((KMeansPlusPlusMethod) method).getNumClusters();
        }

        // Save annotations
        Collection<String> clusters = results.getClusters();
        for (String cluster : clusters) {
            for (String item : results.getItems(cluster)) {
                annotationMatrix.setAnnotation(item, annotationLabel, cluster);
            }
        }

        String sortLabel = "${" + annotationLabel + "}";
        HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(clusteringDimension);
        CommandAddHeaderColoredLabels.updateFromClusterResults(header, results.getClusters());
        header.setTitle(annotationLabel);
        header.setAnnotationPattern(sortLabel);
        clusteringDimension.addHeader(header);

        // Sort the header
        clusteringDimension.sort(new SortByLabelComparator(clusteringDimension, SortDirection.ASCENDING, new PatternFunction(sortLabel, clusteringDimension.getAnnotations()), -1, false));

        // Bookmark current sort
        addBookmarkKMeans(clusteringDimension, layerId, heatmap, (KMeansPlusPlusMethod) method);
    }

    private static void processHierarchical(Clusters results, HeatmapDimension clusteringDimension, AnnotationMatrix annotationMatrix, String layerId, ClusteringMethod method, Heatmap heatmap) {
        String annotationPrefix = "Cluster " + layerId + " L";

        HierarchicalCluster rootCluster = (HierarchicalCluster) results;

        List<HierarchicalCluster> children = rootCluster.getChildren();
        rootCluster.setName("");

        Map<Integer, List<HierarchicalCluster>> clustersMapPerLevel = new HashMap<>();
        int maxLevel = 0;
        while (maxLevel < 15) {
            maxLevel++;

            List<HierarchicalCluster> nextLevel = new ArrayList<>();
            for (HierarchicalCluster cluster : children) {
                if (!cluster.getChildren().isEmpty()) {
                    for (String identifier : cluster.getIdentifiers()) {
                        annotationMatrix.setAnnotation(identifier, annotationPrefix + maxLevel, cluster.getName());
                    }
                }
                nextLevel.addAll(cluster.getChildren());
            }

            clustersMapPerLevel.put(maxLevel, children);
            children = nextLevel;

            if (children.isEmpty()) {
                maxLevel--;
                break;
            }

        }

        // Hierarchical clustering
        int depth = FastMath.min(10, maxLevel);
        HierarchicalClusterHeatmapHeader hierarchicalHeader = new HierarchicalClusterHeatmapHeader(clusteringDimension);
        for (int l = depth; l >= 1; l--) {
            HeatmapColoredLabelsHeader levelHeader = new HeatmapColoredLabelsHeader(clusteringDimension);

            List<HierarchicalCluster> clusters = clustersMapPerLevel.get(l);
            List<ColoredLabel> coloredLabels = new ArrayList<>(clusters.size());
            for (HierarchicalCluster cluster : clusters) {
                coloredLabels.add(new ColoredLabel(cluster.getName(), new Color(cluster.getColor())));
            }

            levelHeader.setClusters(coloredLabels);


            levelHeader.setTitle(annotationPrefix + l);
            levelHeader.setSize(7);
            levelHeader.setAnnotationPattern("${" + annotationPrefix + l + "}");
            clusteringDimension.sort(new SortByLabelComparator(clusteringDimension, SortDirection.ASCENDING, new HierarchicalSortFunction(l, annotationPrefix, clusteringDimension.getAnnotations()), -1, false));
            hierarchicalHeader.addLevel(levelHeader);
        }
        hierarchicalHeader.setTitle("Cluster " + layerId);
        hierarchicalHeader.setHierarchicalCluster(rootCluster);
        clusteringDimension.addHeader(hierarchicalHeader);


        // Bookmark current sort
        addBookmarkHierarchical(clusteringDimension, layerId, heatmap, (HierarchicalMethod) method);

        // Open a tree editor
        rootCluster.setName(clusteringDimension.getId() + "-" + layerId);
        Application.get().getEditorsPanel().addEditor(new DendrogramEditor(rootCluster));
    }

    private static void addBookmarkHierarchical(HeatmapDimension clusteringDimension, String layerId, Heatmap heatmap, HierarchicalMethod method) {
        boolean rowsUsed = clusteringDimension.getId().equals(MatrixDimensionKey.ROWS);
        String bookmarkName = method.getName() + "-" + clusteringDimension.getId().toString().substring(0, 3) + "-" + layerId;
        int[] include = new int[]{rowsUsed ? Bookmarks.ROWS : Bookmarks.COLUMNS,
                Bookmarks.LAYER};
        String description = "Automatically generated bookmark for hierarchical clustering of " +
                clusteringDimension.getId().toString() + " using the data values \"" + layerId + "\"." +
                " Distance used: " + method.getDistanceMeasure().toString() + ", Link type used: " + method.getLinkageStrategy().toString() + ".";
        heatmap.getBookmarks().createNew(heatmap, bookmarkName, description, include);
    }

    private static void addBookmarkKMeans(HeatmapDimension clusteringDimension, String layerId, Heatmap heatmap, KMeansPlusPlusMethod method) {
        boolean rowsUsed = clusteringDimension.getId().equals(MatrixDimensionKey.ROWS);
        String bookmarkName = method.getName() + "-" + clusteringDimension.getId().toString().substring(0, 3) + "-" + layerId;
        int[] include = new int[]{rowsUsed ? Bookmarks.ROWS : Bookmarks.COLUMNS,
                Bookmarks.LAYER};
        String description = "Automatically generated bookmark for K-Means clustering of " +
                clusteringDimension.getId().toString() + " using the data values \"" + layerId + "\"." +
                " Clusters: " + method.getNumClusters().toString() + ", Distance used: " + method.getDistance().toString() + ", Iterations made: " + method.getIterations().toString() + ".";
        heatmap.getBookmarks().createNew(heatmap, bookmarkName, description, include);
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

            for (int l = level - 1; l >= 1 && clusterName == null; l--) {
                clusterName = annotations.getAnnotation(identifier, prefix + l);
            }

            return clusterName;
        }
    }
}
