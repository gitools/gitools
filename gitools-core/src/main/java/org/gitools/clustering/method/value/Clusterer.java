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
import java.util.List;
import java.util.Properties;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringMethodDescriptor;
import org.gitools.clustering.ClusteringMethodFactory;
import org.gitools.clustering.ClusteringResults;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;

public class Clusterer {
	
	public static ClusteringResults matrixClustering (IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor, boolean sortMatrix) throws Exception {

		ClusteringMethodDescriptor descriptor = null;
		List<ClusteringMethodDescriptor> descriptors = ClusteringMethodFactory.getDefault().getDescriptors();

		for (ClusteringMethodDescriptor desc : descriptors) {
			if(desc.getDescription().contains(clusterParameters.getProperty("method")))
				descriptor = desc;
		}
		ClusteringData data = null;
		ClusteringMethod method = null;

		if (descriptor.getMethodClass().equals(WekaCobWebMethod.class)) {

			method = (WekaCobWebMethod) ClusteringMethodFactory.getDefault().create(descriptor);

			int dimMatrix = new Integer (clusterParameters.getProperty("index"));

			if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
				data = new MatrixRowClusteringData(matrixView, dimMatrix);
			else
				data = new MatrixColumnClusteringData(matrixView, dimMatrix);

			((WekaCobWebMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
			((WekaCobWebMethod) method).setAcuity(Float.valueOf(clusterParameters.getProperty("acuity")));
			((WekaCobWebMethod) method).setCutoff(Float.valueOf(clusterParameters.getProperty("cutoff")));
			((WekaCobWebMethod) method).setSeed(Integer.valueOf(clusterParameters.getProperty("seedCobweb")));
			((WekaCobWebMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
		}

		if (descriptor.getMethodClass().equals(WekaKmeansMethod.class)) {

			method = (WekaKmeansMethod) ClusteringMethodFactory.getDefault().create(descriptor);

			int dimMatrix = new Integer (clusterParameters.getProperty("index"));

			if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
				data = new MatrixRowClusteringData(matrixView, dimMatrix);
			else
				data = new MatrixColumnClusteringData(matrixView, dimMatrix);

			((WekaKmeansMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
			((WekaKmeansMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
			((WekaKmeansMethod) method).setIterations(Integer.valueOf(clusterParameters.getProperty("iterations", "500")));
			((WekaKmeansMethod) method).setNumClusters(Integer.valueOf(clusterParameters.getProperty("k", "2")));
			((WekaKmeansMethod) method).setSeed(Integer.valueOf(clusterParameters.getProperty("seedKmeans", "10")));
			if (clusterParameters.getProperty("distance", "euclidean").toLowerCase().equals("euclidean")) {
				((WekaKmeansMethod) method).setDistanceFunction(new EuclideanDistance());
			} else {
				((WekaKmeansMethod) method).setDistanceFunction(new ManhattanDistance());
			}			
		}

		if (descriptor.getMethodClass().equals(WekaHCLMethod.class)) {

			method = (WekaHCLMethod) ClusteringMethodFactory.getDefault().create(descriptor);

			int dimMatrix = new Integer (clusterParameters.getProperty("index"));

			if (Boolean.valueOf(clusterParameters.getProperty("transpose")))
				data = new MatrixRowClusteringData(matrixView, dimMatrix);
			else
				data = new MatrixColumnClusteringData(matrixView, dimMatrix);

			((WekaHCLMethod) method).setTranspose(Boolean.valueOf(clusterParameters.getProperty("transpose")));
			((WekaHCLMethod) method).setPreprocess(Boolean.valueOf(clusterParameters.getProperty("preprocessing")));
			((WekaHCLMethod) method).setLinkType(
					new SelectedTag(clusterParameters.getProperty("link").toUpperCase(), WekaHierarchicalClusterer.TAGS_LINK_TYPE));

			((WekaHCLMethod) method).setDistanceIsBranchLength(false);
			((WekaHCLMethod) method).setNumClusters(1);
			((WekaHCLMethod) method).setPrintNewick(true);

			if (clusterParameters.getProperty("distance").equalsIgnoreCase("euclidean"))
				((WekaHCLMethod) method).setDistanceFunction(new EuclideanDistance());
			else
				((WekaHCLMethod) method).setDistanceFunction(new ManhattanDistance());

		}

		ClusteringResults results = method.cluster(data, monitor);

		monitor.end();
 
		if (!monitor.isCancelled() && sortMatrix)
			if (Boolean.valueOf(clusterParameters.getProperty("transpose"))) {
				TransposedMatrixView transposedMatrix = new TransposedMatrixView(matrixView);
				ClusterUtils.updateVisibility(transposedMatrix, results.getDataIndicesByClusterTitle());
			} else
				ClusterUtils.updateVisibility(matrixView, results.getDataIndicesByClusterTitle());

		return results;

	}
}