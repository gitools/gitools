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
import weka.core.Instance;
import weka.core.Instances;

public class WekaCobWebMethod extends AbstractMethod implements ClusteringMethod{

	public static final String ID = "hierarchical";

	public WekaCobWebMethod(Properties properties) {
		super(ID,
				"CobWeb's clustering",
				"CobWeb's Weka clustering",null, properties);
	}

	@Override
	public void buildAndCluster(IMatrixView matrixView, IProgressMonitor monitor) throws Exception, IOException, NumberFormatException {

		Integer valueIndex = new Integer(properties.getProperty("index", "0"));
		MatrixViewWekaLoader loader =
				new MatrixViewWekaLoader(matrixView, valueIndex);

		Instances structure = loader.getStructure();

		Cobweb clusterer = new Cobweb();

		clusterer.setAcuity(Float.valueOf(properties.getProperty("acuity","1.0")));
		clusterer.setCutoff(Float.valueOf(properties.getProperty("cutoff","0.0028")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seed","42")));

		clusterer.buildClusterer(structure);

		monitor.begin("Creating clustering model ...", matrixView.getVisibleColumns().length + 1);

		Instance current = null;

		int j = 0;
		while ((j < matrixView.getVisibleColumns().length ) && !monitor.isCancelled()) {

			if ((current = loader.get(j)) != null) 
				clusterer.updateClusterer(current);

			monitor.worked(1);
			j++;
		}

		if (!monitor.isCancelled())
		{
			clusterer.updateFinished();

			monitor.end();

			// Identificar el cluster de cada instancia

			monitor.begin("Clustering instances ...", matrixView.getVisibleColumns().length);

			int cluster;

			//One cluster different instances
			HashMap<Integer, List<Integer>> clusterResults = new HashMap<Integer, List<Integer>>();

			for (int i=0; i < matrixView.getVisibleColumns().length && !monitor.isCancelled(); i++)  {

				if ((current = loader.get(i)) != null) {

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

			if (!monitor.isCancelled()) updateVisibility(matrixView, clusterResults);
		}
		
		monitor.end();
	}

	private void updateVisibility(IMatrixView matrixView, HashMap<Integer, List<Integer>> clusterResults) {

		int[] visibleData = matrixView.getVisibleColumns();

		final int[] sortedVisibleData = new int[matrixView.getVisibleColumns().length];

		int index = 0;
		

		Integer[] clustersSorted = (Integer[]) clusterResults.keySet().toArray(
				new Integer[clusterResults.keySet().size()]);

		Arrays.sort(clustersSorted);

		for (Integer i : clustersSorted)
			for( Integer val : clusterResults.get(i))
				sortedVisibleData[index++] = visibleData[val];

		matrixView.setVisibleColumns(sortedVisibleData);

	}

	@Override
	public String getId() {
		return ID;
	}





}
