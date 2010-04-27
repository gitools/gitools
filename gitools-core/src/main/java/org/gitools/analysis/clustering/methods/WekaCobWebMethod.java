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

import edu.upf.bg.progressmonitor.IProgressMonitor;
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

	//FIXME type shouldn't be an string
	@Override
	public void buildAndCluster(IMatrixView matrixView, String type, IProgressMonitor monitor) throws Exception, IOException, NumberFormatException {

		// FIXME valueIndex should be an integer !!!
		String valueIndex = properties.getProperty("index", "0");
		MatrixViewWekaLoader loader =
				new MatrixViewWekaLoader(matrixView, valueIndex, type);

		Instances structure = loader.getStructure();

		Cobweb clusterer = new Cobweb();

		// FIXME consider empty values using getProperty(key, defaultValue)

		clusterer.setAcuity(Float.valueOf(properties.getProperty("acuity")));
		clusterer.setCutoff(Float.valueOf(properties.getProperty("cutoff")));
		clusterer.setSeed(Integer.valueOf(properties.getProperty("seed")));

		clusterer.buildClusterer(structure);

		monitor.begin("Creating clustering model ...", structure.numInstances() + 1);

		Instance current;
		
		while ((current = loader.getNextInstance(structure)) != null
				&& !monitor.isCancelled()) {
			
			clusterer.updateClusterer(current);
			monitor.worked(1);
		}

		clusterer.updateFinished();

		monitor.end();

		// Identificar el cluster de cada instancia
		Instances dataset = loader.getDataSet();

		monitor.begin("Classifying instances ...", dataset.numInstances());

		int cluster;

		//One cluster differnt instances

		HashMap<Integer, List<Integer>> clusterResults = new HashMap<Integer, List<Integer>>();

		for (int i=0; i < dataset.numInstances() && !monitor.isCancelled(); i++)  {

			cluster = clusterer.clusterInstance(dataset.instance(i));

			List<Integer> instancesCluster = clusterResults.get(cluster);
			if (instancesCluster == null) {
				instancesCluster = new ArrayList<Integer>();
				clusterResults.put(cluster, instancesCluster);
			}

			instancesCluster.add(i);

			monitor.worked(1);
		}

		updateVisibility(type, matrixView, dataset.numInstances(), clusterResults);

		monitor.end();
	}

	private void updateVisibility(String type, IMatrixView matrixView, Integer numInstances, HashMap<Integer, List<Integer>> clusterResults) {

		int[] visibleData = null;

		if (type.equals("rows"))
			visibleData = matrixView.getVisibleRows();
		else 
			visibleData = matrixView.getVisibleColumns();

		final int[] sortedVisibleData = new int[numInstances];

		int index = 0;
		
		//Integer[] clustersSorted = new Integer[clusterResults.keySet().size()];
		//clustersSorted = (Integer[]) clusterResults.keySet().toArray();
		Integer[] clustersSorted = (Integer[]) clusterResults.keySet().toArray(
				new Integer[clusterResults.keySet().size()]);

		Arrays.sort(clustersSorted);

		for (Integer i : clustersSorted)
			for( Integer val : clusterResults.get(i))
				sortedVisibleData[index++] = visibleData[val];

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
	public void build(IMatrixView matrixView, String type, IProgressMonitor monitor) throws MethodException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public ClusteringResult cluster() throws MethodException {
		throw new UnsupportedOperationException("Not supported yet.");
	}




}
