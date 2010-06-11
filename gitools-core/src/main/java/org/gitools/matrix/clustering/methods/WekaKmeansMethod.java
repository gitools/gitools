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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.matrix.clustering.ClusteringMethod;
import org.gitools.matrix.model.IMatrixView;
import weka.clusterers.SimpleKMeans;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.ManhattanDistance;

public class WekaKmeansMethod extends AbstractMethod implements ClusteringMethod{

	public static final String ID = "k-means";

	public WekaKmeansMethod(Properties properties) {
		super(ID,
				"K-Means clustering",
				"K-Means Weka clustering",
				null, properties);
	}


	@Override
	public void buildAndCluster(IMatrixView matrixView, IProgressMonitor monitor) throws Exception, IOException, NumberFormatException {

		Instance instancia;
		int cluster;

		if ((Integer.valueOf(properties.getProperty("k"))) < 2) return;

		Integer valueIndex = Integer.valueOf(properties.getProperty("index", "0"));
		MatrixViewWekaLoader loader =
				new MatrixViewWekaLoader(matrixView, valueIndex);

		SimpleKMeans clusterer = new SimpleKMeans();

		clusterer.setMaxIterations(Integer.valueOf(properties.getProperty("iterations","500")));
		clusterer.setNumClusters(Integer.valueOf(properties.getProperty("k","2")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seedKmeans","10")));

		if (properties.getProperty("distance","euclidean").toLowerCase().equals("euclidean"))
			clusterer.setDistanceFunction(new EuclideanDistance());
		else
			clusterer.setDistanceFunction(new ManhattanDistance());

		Instances dataset = loader.getDataSet();

		monitor.begin("Creating clustering model ...", 1);
		clusterer.buildClusterer(dataset);

		if (!monitor.isCancelled())
		{

			monitor.end();
			
			monitor.begin("Clustering instances ...", dataset.numInstances());

			//Cluster -> List instances
			HashMap<Integer,List<Integer>> clusterResults = new HashMap<Integer,List<Integer>>();

			List<Integer> instancesCluster = null;

			for (int i=0; i < dataset.numInstances(); i++)  {

				instancia = dataset.instance(i);
				cluster = clusterer.clusterInstance(instancia);

				if (clusterResults.get(cluster) == null)
					instancesCluster = new ArrayList<Integer>();
				else
					instancesCluster = clusterResults.get(cluster);

				instancesCluster.add(i);
				clusterResults.put(cluster, instancesCluster);
				monitor.worked(1);

			}

			updateVisibility (matrixView, dataset.numInstances(), clusterResults);
		}

		monitor.end();

	}

	private void updateVisibility (IMatrixView matrixView, Integer numInstances, HashMap<Integer,List<Integer>> clusterResults){

		int index = 0;
		int[] visibleData = null;
		final int[] sortedVisibleData = new int[numInstances];

		visibleData = matrixView.getVisibleColumns();

		for (Integer i : clusterResults.keySet())
			for( Integer val : clusterResults.get(i)){

				sortedVisibleData[index] = visibleData[val];
				index++;
			}

		matrixView.setVisibleColumns(sortedVisibleData);

	}

	@Override
	public String getId() {
		return ID;
	}





}