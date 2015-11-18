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
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.*;
import org.gitools.api.matrix.view.IMatrixViewDimension;
import org.gitools.heatmap.header.HierarchicalCluster;
import org.gitools.heatmap.header.HierarchicalClusterNamer;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentSkipListSet;

import static com.google.common.collect.Sets.complementOf;
import static com.google.common.collect.Sets.newHashSet;

public class HierarchicalClusterer {

    private LinkageStrategy linkageStrategy;
    private DistanceMeasure measure;
    private IAggregator aggregator;

    public HierarchicalClusterer(LinkageStrategy linkageStrategy, DistanceMeasure measure, IAggregator aggregator) {
        this.linkageStrategy = linkageStrategy;
        this.measure = measure;
        this.aggregator = aggregator;
    }

    public HierarchicalCluster cluster(IMatrix matrix, IMatrixLayer<Double> layer, IMatrixDimension clusterDimension, IMatrixDimension aggregationDimension, IProgressMonitor monitor) {

        Map<String, HierarchicalCluster> clusters = new HashMap<>(clusterDimension.size());
        SortedSet<ClusterPair> linkages = new ConcurrentSkipListSet<>();


        // Aggregate all the values to sort the clusters by weight
        monitor.begin("Aggregating values...", clusterDimension.size());
        final Map<String, Double> aggregation = new HashMap<>(clusterDimension.size());
        Set<String> allNullValues = new HashSet<>();
        IMatrixPosition position = matrix.newPosition();
        for (String id : position.iterate(clusterDimension)) {

            Double value = aggregator.aggregate(position.iterate(layer, aggregationDimension));

            if (value != null) {
                aggregation.put(id, value);
            } else {
                allNullValues.add(id);
            }

        }

        // First sort the clustering dimension to show the clusters ordered by weight at the end
        if (clusterDimension instanceof IMatrixViewDimension) {
            IMatrixViewDimension sortDimension = (IMatrixViewDimension) clusterDimension;
            sortDimension.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return SortDirection.ASCENDING.compare(aggregation.get(o1), aggregation.get(o2));
                }
            });
        }

        // Calculate all the distances
        IMatrixPosition position1 = matrix.newPosition();
        IMatrixPosition position2 = matrix.newPosition();
        monitor.begin("Calculating distances...", clusterDimension.size());
        for (String id1 : position1.iterate(clusterDimension)) {

            // Check user cancel action
            monitor.worked(1);
            if (monitor.isCancelled()) {
                throw new CancellationException();
            }

            // Skip all null values
            if (allNullValues.contains(id1)) {
                continue;
            }

            HierarchicalCluster cluster1 = newCluster(clusters, id1);
            cluster1.setWeight( aggregation.get(id1) );

            for (String id2 : position2.iterate(clusterDimension.from(id1))) {

                // Skip equal ids
                if (id1.equals(id2)) continue;

                // Skip all null columns
                if (allNullValues.contains(id2)) {
                    continue;
                }

                Double distance = measure.compute(
                        position1.iterate(layer, aggregationDimension),
                        position2.iterate(layer, aggregationDimension)
                );

                HierarchicalCluster cluster2 = newCluster(clusters, id2);

                linkages.add(new ClusterPair(distance, cluster1, cluster2));
            }
        }

        // Create the clusters agglomerating nodes by the nearest distances
        HierarchyBuilder builder = new HierarchyBuilder(newHashSet(clusters.values()), linkages);
        builder.agglomerate(linkageStrategy, monitor, clusterDimension.size());

        // Set cluster names ordered by weight
        HierarchicalCluster root = builder.getRootCluster();
        root.setName("");
        root.setSortName("");
        Color color = HierarchicalClusterNamer.nameClusters(root, HierarchicalClusterNamer.DEFAULT_PALETTE);
        root.setColor(color.getRGB());
        root.setName("root");

        return root;
    }



    private static HierarchicalCluster newCluster(Map<String, HierarchicalCluster> clusters, String id) {

        if (!clusters.containsKey(id)) {
            HierarchicalCluster newCluster = new HierarchicalCluster(id);
            clusters.put(id, newCluster);
            return newCluster;
        }

        return clusters.get(id);
    }

}
