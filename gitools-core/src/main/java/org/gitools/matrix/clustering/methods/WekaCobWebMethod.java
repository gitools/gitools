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
import weka.clusterers.Cobweb;
import weka.core.Instance;

public class WekaCobWebMethod extends AbstractMethod implements ProposedClusterMethod {
	public static final String ID = "cobweb";

	public WekaCobWebMethod(Properties properties) {
		super(ID,
				"CobWeb's clustering",
				"CobWeb's Weka clustering",null, properties);
	} 

	/**
	 * Build the model and cluster the dataset
	 * Hashmap with all Cluster and the instances wich belong to
	 * @param data
	 * @param monitor
	 * @return
	 * @throws Exception
	 */
	@Override
	public ProposedClusterResults cluster(ProposedClusterData dataToCluster, IProgressMonitor monitor) throws ClusterException {
		try {
			MatrixViewWeka data = (MatrixViewWeka) dataToCluster;

			HashMap<Integer, List<Integer>> clusterResults = null;
			Cobweb clusterer = new Cobweb();

			configure(clusterer);
			clusterer.buildClusterer(data.getStructure());

			monitor.begin("Creating clustering model ...", data.getMatrixView().getVisibleColumns().length + 1);
			Instance current = null;
			int j = 0;
			while ((j < data.getMatrixView().getVisibleColumns().length) && !monitor.isCancelled()) {
				if ((current = data.get(j)) != null) {
					clusterer.updateClusterer(current);
				}
				monitor.worked(1);
				j++;
			}
			if (!monitor.isCancelled()) {
				clusterer.updateFinished();
				monitor.end();
				monitor.begin("Clustering instances ...", data.getMatrixView().getVisibleColumns().length);
				int cluster;
				clusterResults = new HashMap<Integer, List<Integer>>();
				for (int i = 0; i < data.getMatrixView().getVisibleColumns().length && !monitor.isCancelled(); i++) {
					if ((current = data.get(i)) != null) {
						cluster = clusterer.clusterInstance(current);
						List<Integer> instancesCluster = clusterResults.get(cluster);
						if (instancesCluster == null) {
							instancesCluster = new ArrayList<Integer>();
							clusterResults.put(cluster, instancesCluster);
						}
						instancesCluster.add(i);
					} 
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

	private void configure(Cobweb clusterer) throws NumberFormatException {
		
		clusterer.setAcuity(Float.valueOf(properties.getProperty("acuity", "0.5")));
		clusterer.setCutoff(Float.valueOf(properties.getProperty("cutoff", "0.0028")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seedCobweb", "42")));
	}

	@Override
	public String getId() {
		return ID;
	}
}
