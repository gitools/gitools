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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.GenericClusteringResults;
import weka.clusterers.Cobweb;
import weka.core.Instance;
import weka.core.Instances;

public class WekaCobWebMethod extends AbstractClusteringValueMethod {
	
	private float acuity;

	private float cutoff;
	
	private int seed;
	
	public WekaCobWebMethod() {
		classIndex = 0; // especial initialization value for weka's cobweb
	}

	@Override
	public ClusteringResults cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {
		try {
			
			Instances structure = ClusterUtils.buildInstanceStructure(clusterData, transpose);

			List<String> labels = ClusterUtils.getLabels(clusterData, transpose);

			MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, classIndex);

			if (preprocess)
				ClusterUtils.dataReductionProcess(clusterWekaData, monitor);			
			
			Cobweb clusterer = new Cobweb();

			configure(clusterer);

			clusterer.buildClusterer(clusterWekaData.getStructure());

			monitor.begin("Creating clustering model ...", clusterWekaData.getMatrixView().getSize() + 1);
			
			ClusteringResults results = null;
			Instance current = null;
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

				monitor.end();
				monitor.begin("Clustering instances ...", clusterWekaData.getMatrixView().getSize());

				Integer maxLength = Integer.toString(clusterer.numberOfClusters()).length();

				Integer cluster;

				HashMap<String, List<Integer>> clusterResults = new HashMap<String, List<Integer>>();

				for (int i = 0; i < clusterWekaData.getMatrixView().getSize() && !monitor.isCancelled(); i++) {
					if ((current = clusterWekaData.get(i)) != null) {

						cluster = clusterer.clusterInstance(current);

						List<Integer> instancesCluster = clusterResults.get(ClusterUtils.valueToString(cluster, maxLength));

						if (instancesCluster == null) 
							instancesCluster = new ArrayList<Integer>();
						
						instancesCluster.add(i);

						clusterResults.put(ClusterUtils.valueToString(cluster, maxLength), instancesCluster);
					} 
					monitor.worked(1);
				}

				results = new GenericClusteringResults(labels.toArray(new String[0]), clusterResults);
			}
			return results;

		} catch (Exception ex) {
			throw new ClusteringException("Error in Cobweb clustering method ");
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
