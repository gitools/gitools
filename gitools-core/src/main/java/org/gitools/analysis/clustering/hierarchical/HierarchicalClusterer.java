package org.gitools.analysis.clustering.hierarchical;

import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.hierarchical.strategy.LinkageStrategy;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.position.IMatrixPosition;

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
