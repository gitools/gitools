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
import org.gitools.matrix.clustering.ClusteringMethod;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;


public class WekaHCLMethod extends AbstractMethod implements ClusteringMethod{

	public static final String ID = "hierarchical";

	public WekaHCLMethod(Properties properties) {
		super(ID,
				"CobWeb's clustering",
				"CobWeb's Weka clustering",null, properties);
	}

/*
 * Build the model and cluster the dataset
 * Hashmap with all Cluster and the instances wich belong to
 */
	@Override
	public HashMap<Integer, List<Integer>> buildAndCluster(MatrixViewWeka data, IProgressMonitor monitor) throws Exception {

		HashMap<Integer, List<Integer>> clusterResults = new HashMap<Integer, List<Integer>>();

		HierarchicalClusterer clusterer = new HierarchicalClusterer();
		
		clusterer.buildClusterer(data);

		if (!monitor.isCancelled())
		{
			//clusterer.updateFinished();
			monitor.end();

			// Identificar el cluster de cada instancia
			monitor.begin("Clustering instances ...", data.getMatrixView().getVisibleColumns().length);

			int cluster;
			Instance current = null;

			for (int i=0; i < data.getMatrixView().getVisibleColumns().length && !monitor.isCancelled(); i++)  {

				if ((current = data.get(i)) != null) {

					cluster = clusterer.clusterInstance(current);

					List<Integer> instancesCluster = clusterResults.get(cluster);
					if (instancesCluster == null) {
						instancesCluster = new ArrayList<Integer>();
						clusterResults.put(cluster, instancesCluster);
					}

					instancesCluster.add(i);
				}
				else
					System.out.println("ERROR Loading instance: "+i);
				monitor.worked(1);
			}			

		}

		return clusterResults;
		
	}


	@Override
	public String getId() {
		return ID;
	}



}
