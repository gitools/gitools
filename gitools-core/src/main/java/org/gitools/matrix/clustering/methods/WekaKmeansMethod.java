/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.matrix.clustering.methods;

import org.gitools.matrix.clustering.MatrixViewWeka;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.matrix.clustering.ClusterException;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.ManhattanDistance;

public class WekaKmeansMethod extends AbstractMethod implements ProposedClusterMethod{

	public static final String ID = "k-means";

	public WekaKmeansMethod(Properties properties) {
		super(ID,
				"K-Means clustering",
				"K-Means Weka clustering",
				null, properties);
	}

/*
 * Build the model and cluster the dataset
 * Hashmap with all Cluster and the instances wich belong to
 */
	@Override
	public ProposedClusterResults cluster(ProposedClusterData dataToCluster, IProgressMonitor monitor) throws ClusterException {
		try {
			MatrixViewWeka data = (MatrixViewWeka) dataToCluster;

			Instance instancia;

			int cluster;

			HashMap<Integer, List<Integer>> clusterResults = null;

			if ((Integer.valueOf(properties.getProperty("k"))) < 2) {
				return null;
			}

			SimpleKMeans clusterer = new SimpleKMeans();

			configure(clusterer);

			monitor.begin("Creating clustering model ...", 1);

			clusterer.buildClusterer(data);

			if (!monitor.isCancelled()) {

				monitor.end();

				monitor.begin("Clustering instances ...", data.numInstances());

				//Cluster -> List instances
				clusterResults = new HashMap<Integer, List<Integer>>();

				List<Integer> instancesCluster = null;

				for (int i = 0; i < data.numInstances(); i++) {

					instancia = data.instance(i);
					cluster = clusterer.clusterInstance(instancia);

					if (clusterResults.get(cluster) == null) {
						instancesCluster = new ArrayList<Integer>();
					} else {
						instancesCluster = clusterResults.get(cluster);
					}

					instancesCluster.add(i);
					clusterResults.put(cluster, instancesCluster);
					monitor.worked(1);

				}

			}

			ProposedHierarchicalClusterResults results = new ProposedHierarchicalClusterResults();

			results.setClusteredData(clusterResults);

			return results;

		} catch (Exception ex) {
			throw new ClusterException("Error in " + ID + " clustering method ");
		}
	}

	private void configure(SimpleKMeans clusterer) throws Exception, NumberFormatException {

		clusterer.setMaxIterations(Integer.valueOf(properties.getProperty("iterations", "500")));
		clusterer.setNumClusters(Integer.valueOf(properties.getProperty("k", "2")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seedKmeans", "10")));

		if (properties.getProperty("distance", "euclidean").toLowerCase().equals("euclidean")) {
			clusterer.setDistanceFunction(new EuclideanDistance());
		} else {
			clusterer.setDistanceFunction(new ManhattanDistance());
		}
	}

	@Override
	public String getId() {
		return ID;
	}
}
