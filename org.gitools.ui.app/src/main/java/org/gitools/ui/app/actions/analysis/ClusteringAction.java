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
import org.gitools.analysis.clustering.hierarchical.HierarchicalMethod;
import org.gitools.analysis.clustering.kmeans.KMeansPlusPlusMethod;
import org.gitools.api.analysis.Clusters;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.SortDirection;
import org.gitools.heatmap.Bookmark;
import org.gitools.heatmap.Bookmarks;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.sort.SortByLabelComparator;
import org.gitools.ui.app.actions.data.analysis.SortByHierarchicalClusteringCommand;
import org.gitools.ui.app.analysis.clustering.ClusteringWizard;
import org.gitools.ui.app.analysis.clustering.visualization.DendrogramEditor;
import org.gitools.ui.app.commands.AddHeaderColoredLabelsCommand;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;

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

                boolean createBookmark = wiz.doCreateBookmark();

                // Annotations
                AnnotationMatrix annotationMatrix = clusteringDimension.getAnnotations();

                if (method instanceof HierarchicalMethod) {
                    processHierarchical(results, clusteringDimension, annotationMatrix, wiz.getClusteringLayer(), (HierarchicalMethod) method, heatmap, monitor);

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
            if (method.getUserGivenName().trim().length() > 0) {
                annotationLabel = method.getUserGivenName();
            } else {
                annotationLabel = annotationLabel + " #" + ((KMeansPlusPlusMethod) method).getNumClusters();
            }
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
        AddHeaderColoredLabelsCommand.updateFromClusterResults(header, results.getClusters());
        header.setTitle(annotationLabel);
        header.setAnnotationPattern(sortLabel);
        clusteringDimension.addHeader(header);

        // Sort the header
        clusteringDimension.sort(new SortByLabelComparator(clusteringDimension, SortDirection.ASCENDING, new PatternFunction(sortLabel, clusteringDimension.getAnnotations()), -1, false));

        // Bookmark current sort
        addBookmarkKMeans(clusteringDimension, layerId, heatmap, (KMeansPlusPlusMethod) method);
    }

    private static void processHierarchical(Clusters results, HeatmapDimension clusteringDimension, AnnotationMatrix annotationMatrix, String layerId, HierarchicalMethod method, Heatmap heatmap, IProgressMonitor monitor) {

        HierarchicalCluster rootCluster = (HierarchicalCluster) results;
        rootCluster.setName("");

        // Sort
        new SortByHierarchicalClusteringCommand(clusteringDimension, rootCluster).execute(monitor);

        // Bookmark current sort
        String clusterDesc = "hierarchical clustering of " +
                clusteringDimension.getId().getLabel() + "s using the data values \"" + layerId + "\"." +
                " Distance used: " + method.getDistanceMeasure().toString() +
                ", Link type used: " + method.getLinkageStrategy().toString() + ".";
        Bookmark b = addBookmarkHierarchical(clusteringDimension, layerId, heatmap, method, clusterDesc);


        // Hierarchical cluster header
        int maxLevels = 20;
        rootCluster.setName(b.getName());
        HierarchicalClusterHeatmapHeader hierarchicalHeader = new HierarchicalClusterHeatmapHeader(clusteringDimension);
        hierarchicalHeader.setDescription(clusterDesc);
        hierarchicalHeader.setTitle(rootCluster.getName());
        hierarchicalHeader.setHierarchicalCluster(rootCluster);

        // Add to annotations
        String annotationPrefix = b.getName() + " L";

        HierarchicalClusterHeatmapHeader.createHierarchicalLevelsHeaders(hierarchicalHeader, maxLevels, annotationPrefix);

        clusteringDimension.addHeader(hierarchicalHeader);

        // Open a tree editor
        Application.get().getEditorsPanel().addEditor(new DendrogramEditor(rootCluster));
    }

    private static Bookmark addBookmarkHierarchical(HeatmapDimension clusteringDimension, String layerId, Heatmap heatmap, HierarchicalMethod method, String clusterDesc) {
        boolean rowsUsed = clusteringDimension.getId().equals(MatrixDimensionKey.ROWS);
        String bookmarkName = method.getUserGivenName().trim().length() > 0 ? method.getUserGivenName().trim() : method.getName() + "-" + clusteringDimension.getId().toString().substring(0, 3).toLowerCase() + "s-" + layerId;
        int[] include = new int[]{rowsUsed ? Bookmarks.ROWS : Bookmarks.COLUMNS,
                Bookmarks.LAYER};
        String description = "Automatically generated bookmark for " + clusterDesc;
        return heatmap.getBookmarks().createNew(heatmap, bookmarkName, description, include);
    }

    private static Bookmark addBookmarkKMeans(HeatmapDimension clusteringDimension, String layerId, Heatmap heatmap, KMeansPlusPlusMethod method) {
        boolean rowsUsed = clusteringDimension.getId().equals(MatrixDimensionKey.ROWS);
        String bookmarkName = method.getUserGivenName().trim().length() > 0 ? method.getUserGivenName().trim() : method.getName() + "-" + clusteringDimension.getId().toString().substring(0, 3).toLowerCase() + "s-" + layerId;
        int[] include = new int[]{rowsUsed ? Bookmarks.ROWS : Bookmarks.COLUMNS,
                Bookmarks.LAYER};
        String description = "Automatically generated bookmark for K-Means clustering of " +
                clusteringDimension.getId().toString() + " using the data values \"" + layerId + "\"." +
                " Clusters: " + method.getNumClusters().toString() + ", Distance used: " + method.getDistance().toString() + ", Iterations made: " + method.getIterations().toString() + ".";
        return heatmap.getBookmarks().createNew(heatmap, bookmarkName, description, include);
    }

}
