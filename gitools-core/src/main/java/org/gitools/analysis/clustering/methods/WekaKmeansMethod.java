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

package org.gitools.analysis.clustering.methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.ClusteringResult;
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
				ClusteringResult.class, properties);
	}

	@Override
	public void buildAndCluster(IMatrixView matrixView, String type) throws Exception, IOException, NumberFormatException {

		Instance instancia;

		int cluster;

		if ((Integer.valueOf(properties.getProperty("k"))) < 2) return;

		MatrixViewWekaLoader loader = new MatrixViewWekaLoader(matrixView, properties.getProperty("index"),type);

		Instances structure = loader.getStructure();

		System.out.println("Loading clustering algorithm ...");

		SimpleKMeans clusterer = new SimpleKMeans();

		clusterer.setMaxIterations(Integer.valueOf(properties.getProperty("iterations")));

		clusterer.setNumClusters(Integer.valueOf(properties.getProperty("k")));

		clusterer.setSeed(Integer.valueOf(properties.getProperty("seed")));

		if (properties.getProperty("distance").toLowerCase().equals("euclidean"))
			clusterer.setDistanceFunction(new EuclideanDistance());
		else
		if (properties.getProperty("distance").toLowerCase().equals("manhattan"))
			clusterer.setDistanceFunction(new ManhattanDistance());


		System.out.println("Training clustering model...");

		clusterer.buildClusterer(loader.getDataSet());

		System.out.println("Setting instances into clusters ...");

		Instances dataset = loader.getDataSet();

		//Cluster -> List instances 
		HashMap<Integer,List<Integer>> clusterResults = new HashMap<Integer,List<Integer>>();

		List<Integer> instancesCluster = null;
				
		for (int i=0; i < dataset.numInstances(); i++)  {

			instancia = dataset.instance(i);

			cluster = clusterer.clusterInstance(instancia);

			if (clusterResults.get(cluster) == null) instancesCluster = new ArrayList<Integer>();

			else instancesCluster = clusterResults.get(cluster);
			
			instancesCluster.add(i);

			clusterResults.put(cluster, instancesCluster);

		}

		rePaintHeatMap (type, matrixView, dataset.numInstances(), clusterResults);


	}

	private void rePaintHeatMap (String type, IMatrixView matrixView, Integer numInstances, HashMap<Integer,List<Integer>> clusterResults){

		int[] visibleData = null;

		if (type.equals("rows"))
			visibleData = matrixView.getVisibleRows();
		else
			visibleData = matrixView.getVisibleColumns();

		final int[] sortedVisibleData = new int[numInstances];

		int index = 0;

		for (Integer i : clusterResults.keySet()){

			for( Integer val : clusterResults.get(i)){

				sortedVisibleData[index] = visibleData[val];

				index++;
			}

		}

		if (type.equals("rows"))
			matrixView.setVisibleRows(sortedVisibleData);
		else
			matrixView.setVisibleColumns(sortedVisibleData);

	}

	@Override
	public String getId() {
		return ID;
	}


	@Override
	public void build(IMatrixView matrixView, String type) throws MethodException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ClusteringResult cluster() throws MethodException {
		throw new UnsupportedOperationException("Not supported yet.");
	}




}
