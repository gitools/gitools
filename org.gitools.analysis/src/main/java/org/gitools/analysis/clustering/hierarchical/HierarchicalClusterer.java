/*
 * #%L
 * gitools-core
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
package org.gitools.analysis.clustering.hierarchical;

import com.google.common.base.Joiner;
import org.apache.commons.math3.util.FastMath;
import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.google.common.collect.Sets.newHashSet;

public class HierarchicalClusterer {

    private LinkageStrategy linkageStrategy;
    private DistanceMeasure measure;

    public HierarchicalClusterer(LinkageStrategy linkageStrategy, DistanceMeasure measure) {
        this.linkageStrategy = linkageStrategy;
        this.measure = measure;
    }

    public Cluster cluster(IMatrix matrix, IMatrixLayer<Double> layer, IMatrixDimension clusterDimension, IMatrixDimension aggregationDimension, IProgressMonitor monitor) {

        Map<String, Cluster> clusters = new HashMap<>(clusterDimension.size());
        SortedSet<ClusterPair> linkages = new ConcurrentSkipListSet<>();

        IMatrixPosition position1 = matrix.newPosition();
        IMatrixPosition position2 = matrix.newPosition();


        monitor.begin("Calculating distances...", clusterDimension.size());
        for (String id1 : position1.iterate(clusterDimension)) {

            // Check user cancel action
            monitor.worked(1);
            if (monitor.isCancelled()) {
                throw new CancellationException();
            }

            for (String id2 : position2.iterate(clusterDimension).from(id1)) {

                // Skip equal ids
                if (id1.equals(id2)) continue;

                Double distance = measure.compute(
                        position1.iterate(layer, aggregationDimension),
                        position2.iterate(layer, aggregationDimension)
                );

                Cluster cluster1 = newCluster(clusters, id1);
                Cluster cluster2 = newCluster(clusters, id2);

                linkages.add(new ClusterPair(distance, cluster1, cluster2));
            }
        }

        /* Process */
        HierarchyBuilder builder = new HierarchyBuilder(newHashSet(clusters.values()), linkages);
        builder.agglomerate(linkageStrategy, monitor, clusterDimension.size());

        /* Set cluster names */
        Cluster root = builder.getRootCluster();
        root.setName("");
        nameClusters(root.getChildren(), true);
        root.setName("root");

        return root;
    }

    private void nameClusters(List<Cluster> children, boolean sortDirection) {

        if (children.isEmpty()) {
            return;
        }

        int digits = calculateDigits(children.size());
        if (sortDirection) {
            Collections.sort(children);
        } else {
            Collections.sort(children, Collections.reverseOrder());
        }
        boolean childSorting = sortDirection;
        for (int i = 0; i < children.size(); i++) {
            Cluster child = children.get(i);
            if (child.getChildren().isEmpty()) {
                child.setName(JOINER.join(child.getIdentifiers()));
            } else {
                child.setName(child.getParent().getName() + createLabel(i, digits));
            }
            nameClusters(child.getChildren(), childSorting);
            childSorting = !childSorting;
        }

    }

    private static Joiner JOINER = Joiner.on("&");
    private static char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static int calculateDigits(int size) {
        return ((int) FastMath.log(ALPHABET.length, size)) + 1;
    }

    private static String createLabel(int number, int digits) {

        char[] label = new char[digits];
        label[digits - 1] = ALPHABET[number % ALPHABET.length];

        for (int d = 1; d < digits; d++) {
            int quocient = number / (ALPHABET.length * d);
            label[digits - d - 1] = ALPHABET[quocient % ALPHABET.length];
        }

        return String.valueOf(label);

    }

    private static Cluster newCluster(Map<String, Cluster> clusters, String id) {

        if (!clusters.containsKey(id)) {
            Cluster newCluster = new Cluster(id);
            clusters.put(id, newCluster);
            return newCluster;
        }

        return clusters.get(id);
    }

}
