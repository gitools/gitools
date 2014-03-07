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
package org.gitools.analysis.clustering.kmeans;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import org.apache.commons.math3.ml.clustering.*;
import org.gitools.analysis.clustering.*;
import org.gitools.analysis.clustering.distance.DistanceMeasure;
import org.gitools.analysis.clustering.hierarchical.HierarchicalCluster;
import org.gitools.analysis.clustering.hierarchical.HierarchicalClusterer;
import org.gitools.api.analysis.IAggregator;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixDimension;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixPosition;

import java.util.*;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.size;

public class KmeansPlusPlusMethod extends AbstractClusteringMethod {

    private int iterations;
    private int seed;
    private int numClusters;
    private DistanceMeasure distanceFunction;

    public KmeansPlusPlusMethod() {
        super("K-means++");
    }

    @Override
    public Clusters cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {


        if (!(clusterData instanceof MatrixClusteringData)) {
            return null;
        }

        MatrixClusteringData data = (MatrixClusteringData) clusterData;

        KMeansPlusPlusClusterer clusterer = new KMeansPlusPlusClusterer(getNumClusters(), getIterations(), getDistanceFunction());

        Set<String> noData = new HashSet<>();

        List<Slide> points = new ArrayList<>(data.getClusteringDimension().size());
        int aggregationSize = data.getAggregationDimension().size();
        for (String identifier : data.getClusteringDimension()) {

            Iterable<Double> point = data.getMatrix().newPosition().set(data.getClusteringDimension(), identifier).iterate(data.getLayer(), data.getAggregationDimension());

            // Skip all null rows/columns
            if (size(filter(point, notNull())) == 0) {
                noData.add(identifier);
                continue;
            }

            points.add(new Slide(identifier, point, aggregationSize));
        }

        // Cluster data
        List<CentroidCluster<Slide>> clusters = clusterer.cluster(points, monitor);

        // Calculate cluster weight
        IAggregator aggregator = data.getLayer().getAggregator();
        for (Cluster<Slide> cluster : clusters) {
            List<Double> values = new ArrayList<>();
            for (Slide slide : cluster.getPoints()) {
                values.add(aggregator.aggregate(slide.getPoint()));
            }

            cluster.setWeight(aggregator.aggregate(values) / cluster.getPoints().size());
        }

        // Sort clusters
        Collections.sort(clusters);

        // Name and return clusters
        return new KMeansClusters(clusters, noData);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seedKmeans) {
        this.seed = seedKmeans;
    }

    public DistanceMeasure getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(DistanceMeasure distance) {
        this.distanceFunction = distance;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    public static class Slide implements Clusterable {

        private String identifier;

        private Iterable<Double> point;
        private int size;

        public Slide(String identifier, Iterable<Double> point, int size) {
            this.identifier = identifier;
            this.point = point;
            this.size = size;
        }

        public String getIdentifier() {
            return identifier;
        }

        @Override
        public Iterable<Double> getPoint() {
            return point;
        }

        @Override
        public int size() {
            return size;
        }
    }

    public static class KMeansClusters implements Clusters {

        private Map<String, Set<String>> clusters;

        public KMeansClusters(List<? extends Cluster<Slide>> clusters, Set<String> noData) {

            this.clusters = new HashMap<>();
            int digits = HierarchicalClusterer.calculateDigits(clusters.size());

            for (int i=0; i < clusters.size(); i++) {
                Cluster<Slide> cluster = clusters.get(i);

                Set<String> identifiers = new HashSet<>();
                List<Slide> points = cluster.getPoints();

                for (Slide slide : points) {
                    identifiers.add(slide.getIdentifier());
                }

                this.clusters.put(HierarchicalClusterer.createLabel(i, digits), identifiers);
            }

            this.clusters.put("Empty", noData);
        }

        @Override
        public Collection<String> getClusters() {
            return clusters.keySet();
        }

        @Override
        public Set<String> getItems(String cluster) {
            return clusters.get(cluster);
        }

        @Override
        public Map<String, Set<String>> getClustersMap() {
            return clusters;
        }
    }

}
