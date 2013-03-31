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

package org.gitools.clustering.method.value;

import org.gitools.utils.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.GenericClusteringResults;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.NormalizableDistance;

public class WekaKmeansMethod extends AbstractClusteringValueMethod {

	private int iterations;

	private int numClusters;

	private int seed;
	
	private NormalizableDistance distanceFunction;

	public WekaKmeansMethod() {
		classIndex = -1;
	}

	@Override
	public ClusteringResults cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {
		try {

			if (iterations < 2)
				return null;

			Instances structure = ClusterUtils.buildInstanceStructure(clusterData, transpose);

			List<String> labels = ClusterUtils.getLabels(clusterData, transpose);

			MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, classIndex);

			if (preprocess)
				ClusterUtils.dataReductionProcess(clusterWekaData, monitor);

			SimpleKMeans clusterer = new SimpleKMeans();

			configure(clusterer);

			monitor.begin("Creating clustering model ...", 1);

			clusterer.buildClusterer(clusterWekaData);

			ClusteringResults results = null;
			
			if (!monitor.isCancelled()) {

				monitor.end();

				monitor.begin("Clustering instances ...", clusterWekaData.numInstances());

				int cluster;
				Instance current = null;

				Integer maxLength = Integer.toString(clusterer.numberOfClusters()).length();

				HashMap<String, List<Integer>> clusterResults = new HashMap<String, List<Integer>>();

				for (int i = 0; i < clusterWekaData.numInstances() && !monitor.isCancelled(); i++) {
					if ((current = clusterWekaData.get(i)) != null) {
						cluster = clusterer.clusterInstance(current);

						List<Integer> instancesCluster = clusterResults.get(ClusterUtils.valueToString(cluster, maxLength));
						if (instancesCluster == null) {
							instancesCluster = new ArrayList<Integer>();							
						}
						instancesCluster.add(i);
						clusterResults.put(ClusterUtils.valueToString(cluster, maxLength), instancesCluster);
					}
					monitor.worked(1);
				}

				results = new GenericClusteringResults(labels.toArray(new String[0]), clusterResults);
			}
			return results;

		} catch (Throwable ex) {
			if (ex instanceof OutOfMemoryError)
				throw new ClusteringException("Insufficient memory for HCL clustering. Increase memory size or try another clustering method", ex);
			else
				throw new ClusteringException(ex);
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
