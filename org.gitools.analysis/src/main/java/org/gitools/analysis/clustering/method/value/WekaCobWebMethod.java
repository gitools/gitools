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
import weka.clusterers.Cobweb;
import weka.core.Instance;
import weka.core.Instances;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WekaCobWebMethod extends AbstractClusteringValueMethod {

    private float acuity;

    private float cutoff;

    private int seed;

    public WekaCobWebMethod() {
        super("CobWeb");
    }


    @Override
    public Clusters cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {
        try {

            Instances structure = ClusterUtils.buildInstanceStructure(clusterData);

            List<String> labels = ClusterUtils.getLabels(clusterData);

            MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, 0);

            Cobweb clusterer = new Cobweb();

            configure(clusterer);

            clusterer.buildClusterer(clusterWekaData.getStructure());

            monitor.begin("Creating clustering model ...", clusterWekaData.getMatrixView().getSize() + 1);

            Clusters results = null;
            Instance current;
            int j = 0;

            while ((j < clusterWekaData.getMatrixView().getSize()) && !monitor.isCancelled()) {
                if ((current = clusterWekaData.get(j)) != null) {
                    clusterer.updateClusterer(current);
                }
                monitor.worked(1);
                j++;
            }

            if (!monitor.isCancelled()) {

                clusterer.updateFinished();

                monitor.begin("Clustering instances ...", clusterWekaData.getMatrixView().getSize());

                Integer maxLength = Integer.toString(clusterer.numberOfClusters()).length();

                Integer cluster;

                HashMap<String, Set<String>> clusterResults = new HashMap<>();

                for (int i = 0; i < clusterWekaData.getMatrixView().getSize() && !monitor.isCancelled(); i++) {
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
                throw new ClusteringException("Insufficient memory for HCL clustering. Increase memory size or try another clustering method", ex);
            } else {
                throw new ClusteringException(ex);
            }
        }
    }

    public float getAcuity() {
        return acuity;
    }

    public void setAcuity(float acuity) {
        this.acuity = acuity;
    }

    public float getCutoff() {
        return cutoff;
    }

    public void setCutoff(float cutoff) {
        this.cutoff = cutoff;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    private void configure(Cobweb clusterer) {

        clusterer.setAcuity(acuity);

        clusterer.setCutoff(cutoff);

        clusterer.setSeed(seed);
    }

}
