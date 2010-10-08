/*
 *  Copyright 2010 xrafael.
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
import org.gitools.matrix.clustering.ClusterUtils;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.matrix.clustering.ClusteringMethod;
import org.gitools.matrix.model.IMatrixView;
import weka.clusterers.Cobweb;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class WekaCobWebMethod extends AbstractMethod implements ClusteringMethod{
	public static final String ID = "hierarchical";

	public WekaCobWebMethod(Properties properties) {
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

		Instance current = null;

		HashMap<Integer, List<Integer>> clusterResults = null;

		Cobweb clusterer = new Cobweb();

		clusterer.setAcuity(Float.valueOf(properties.getProperty("acuity","0.5")));
		clusterer.setCutoff(Float.valueOf(properties.getProperty("cutoff","0.0028")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seedCobweb","42")));

		clusterer.buildClusterer(data.getStructure());

		monitor.begin("Creating clustering model ...",  data.getMatrixView().getVisibleColumns().length + 1);

		int j = 0;
		while ((j <  data.getMatrixView().getVisibleColumns().length ) && !monitor.isCancelled()) {

			if ((current = data.get(j)) != null)
				clusterer.updateClusterer(current);

			monitor.worked(1);
			j++;
		}

		if (!monitor.isCancelled())
		{
			clusterer.updateFinished();

			monitor.end();

			monitor.begin("Clustering instances ...",  data.getMatrixView().getVisibleColumns().length);

			int cluster;

			clusterResults = new HashMap<Integer, List<Integer>>();

			for (int i=0; i <  data.getMatrixView().getVisibleColumns().length && !monitor.isCancelled(); i++)  {

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
