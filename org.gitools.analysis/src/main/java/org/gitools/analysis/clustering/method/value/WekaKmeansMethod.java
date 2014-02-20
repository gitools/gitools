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
package org.gitools.analysis.clustering.method.value;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringException;
import org.gitools.analysis.clustering.Clusters;
import org.gitools.analysis.clustering.GenericClusteringResults;
import org.gitools.api.analysis.IProgressMonitor;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WekaKmeansMethod extends AbstractClusteringValueMethod {

    private int iterations;

    private int numClusters;

    private int seed;

    private NormalizableDistance distanceFunction;

    public WekaKmeansMethod() {
        super("K-means");
    }


    @Override
    public Clusters cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {
        try {

            if (iterations < 2) {
                return null;
            }

            Instances structure = ClusterUtils.buildInstanceStructure(clusterData);

            List<String> labels = ClusterUtils.getLabels(clusterData);

            MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, -1);

            SimpleKMeans clusterer = new SimpleKMeans();

            configure(clusterer);

            monitor.begin("Creating clustering model ...", 1);

            clusterer.buildClusterer(clusterWekaData);

            Clusters results = null;

            if (!monitor.isCancelled()) {

                monitor.end();

                monitor.begin("Clustering instances ...", clusterWekaData.numInstances());

                int cluster;
                Instance current = null;

                Integer maxLength = Integer.toString(clusterer.numberOfClusters()).length();

                HashMap<String, Set<String>> clusterResults = new HashMap<>();

                for (int i = 0; i < clusterWekaData.numInstances() && !monitor.isCancelled(); i++) {
                    if ((current = clusterWekaData.get(i)) != null) {
                        cluster = clusterer.clusterInstance(current);

                        Set<String> instancesCluster = clusterResults.get(ClusterUtils.valueToString(cluster, maxLength));
                        if (instancesCluster == null) {
                            instancesCluster = new HashSet<>();
                        }
                        instancesCluster.add(labels.get(i));
                        clusterResults.put(ClusterUtils.valueToString(cluster, maxLength), instancesCluster);
                    }
                    monitor.worked(1);
                }

                results = new GenericClusteringResults(clusterResults);
            }
            return results;

        } catch (Throwable ex) {
            if (ex instanceof OutOfMemoryError) {
                throw new ClusteringException("Insufficient memory for K-means clustering. Increase memory size or try another clustering method", ex);
            } else {
                throw new ClusteringException(ex);
            }
        }
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

    public NormalizableDistance getDistanceFunction() {
        return distanceFunction;
    }

    public void setDistanceFunction(NormalizableDistance distance) {
        this.distanceFunction = distance;
    }

    public int getNumClusters() {
        return numClusters;
    }

    public void setNumClusters(int numClusters) {
        this.numClusters = numClusters;
    }

    private void configure(SimpleKMeans clusterer) throws Exception {

        clusterer.setMaxIterations(iterations);

        clusterer.setNumClusters(numClusters);

        clusterer.setSeed(seed);

        clusterer.setDistanceFunction(distanceFunction);
    }
}
