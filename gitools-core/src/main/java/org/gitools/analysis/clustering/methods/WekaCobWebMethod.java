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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.analysis.AbstractMethod;
import org.gitools.analysis.MethodException;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.ClusteringResult;
import org.gitools.matrix.model.IMatrixView;
import weka.clusterers.Cobweb;
import weka.core.Instance;
import weka.core.Instances;

public class WekaCobWebMethod extends AbstractMethod implements ClusteringMethod{

	public static final String ID = "cobweb";

	public WekaCobWebMethod(Properties properties) {
		super(ID,
				"CobWeb's clustering",
				"CobWeb's Weka clustering",
				ClusteringResult.class, properties);
	}

	@Override
	public void buildAndCluster(IMatrixView matrixView, String type) throws Exception, IOException, NumberFormatException {

		MatrixViewWekaLoader loader = new MatrixViewWekaLoader(matrixView, properties.getProperty("index"),type);

		Instances structure = loader.getStructure();

		System.out.println("Crear algoritmo de clustering ...");

		Cobweb clusterer = new Cobweb();

		clusterer.setAcuity(Float.valueOf(properties.getProperty("acuity")));
		clusterer.setCutoff(Float.valueOf(properties.getProperty("cutoff")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seed")));

		clusterer.buildClusterer(structure);

		Instance current;

		System.out.println("Entrenar algoritmo de clustering ...");

		while ((current = loader.getNextInstance(structure)) != null)

			clusterer.updateClusterer(current);


		clusterer.updateFinished();


		// Identificar el cluster de cada instancia
		System.out.println("Asignación de instancias a clusters ...");

		Instance instancia;

		int cluster;

		Instances dataset = loader.getDataSet();

		//One cluster differnt instances

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

		repaintHeatMap(type,matrixView, dataset.numInstances(),clusterResults);		

	}

	private void repaintHeatMap (String type, IMatrixView matrixView, Integer numInstances, HashMap<Integer,List<Integer>> clusterResults){

		int[] visibleData = null;

		if (type.equals("rows"))
			visibleData = matrixView.getVisibleRows();
		else 
			visibleData = matrixView.getVisibleColumns();

		final int[] sortedVisibleData = new int[numInstances];

		int index = 0;
		
		//Integer[] clustersSorted = new Integer[clusterResults.keySet().size()];
		//clustersSorted = (Integer[]) clusterResults.keySet().toArray();
		Integer[] clustersSorted = (Integer[])clusterResults.keySet().toArray(new Integer[clusterResults.keySet().size()]);

		Arrays.sort(clustersSorted);

		for (Integer i : clustersSorted){

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
