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

import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

public class HierarchicalClusterer {

    private LinkageStrategy linkageStrategy;
    private DistanceMeasure measure;

    public HierarchicalClusterer(LinkageStrategy linkageStrategy, DistanceMeasure measure) {
        this.linkageStrategy = linkageStrategy;
        this.measure = measure;
    }

    public Cluster cluster(IMatrix matrix, IMatrixLayer<Double> layer, IMatrixDimension clusterDimension, IMatrixDimension aggregationDimension) {

        Map<String, Cluster> clusters = new HashMap<>(clusterDimension.size());
        List<ClusterPair> linkages = new ArrayList<>(clusterDimension.size());

        IMatrixPosition position1 = matrix.newPosition();
        IMatrixPosition position2 = matrix.newPosition();
        for (String id1 : position1.iterate(clusterDimension)) {
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
        HierarchyBuilder builder = new HierarchyBuilder(newArrayList(clusters.values()), linkages);
        while (!builder.isTreeComplete()) {
            builder.agglomerate(linkageStrategy);
        }

        return builder.getRootCluster();
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
