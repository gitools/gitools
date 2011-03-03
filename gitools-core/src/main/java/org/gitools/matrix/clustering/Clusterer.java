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

package org.gitools.matrix.clustering;

import org.gitools.matrix.clustering.methods.ProposedHierarchicalClusterResults;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Properties;
import org.gitools.matrix.clustering.methods.ClusterMethodFactory;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.clustering.methods.ProposedClusterMethod;
import org.gitools.matrix.model.IMatrixView;
import weka.core.Instances;

public class Clusterer {

	public static ProposedHierarchicalClusterResults matrixClustering (IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor) throws Exception {

		ProposedClusterMethod method = ClusterMethodFactory.createMethod(clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("transpose"))) {

			TransposedMatrixView mt = new TransposedMatrixView();
			mt.setMatrix(matrixView);
			matrixView = mt;
		}

		Instances str = ClusterUtils.getInstance().matrix2Instances(matrixView, clusterParameters);

		MatrixViewWeka data = new MatrixViewWeka(str, matrixView, clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("preprocessing")))
			ClusterUtils.getInstance().dataReductionProcess(data, monitor);

		ProposedHierarchicalClusterResults results = (ProposedHierarchicalClusterResults) method.cluster(data, monitor);

		monitor.end();

		return results;
	}
	
	public static ProposedHierarchicalClusterResults matrixSortClustering (IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor) throws Exception {

		ProposedClusterMethod method = ClusterMethodFactory.createMethod(clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("transpose"))) {

			TransposedMatrixView mt = new TransposedMatrixView();
			mt.setMatrix(matrixView);
			matrixView = mt;
		}

		Instances str = ClusterUtils.getInstance().matrix2Instances(matrixView, clusterParameters);

		MatrixViewWeka data = new MatrixViewWeka(str, matrixView, clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("preprocessing")))
			ClusterUtils.getInstance().dataReductionProcess(data, monitor);

		ProposedHierarchicalClusterResults results = (ProposedHierarchicalClusterResults) method.cluster(data, monitor);

		monitor.end();

		if (!monitor.isCancelled())
			ClusterUtils.getInstance().updateVisibility(matrixView, results.getClusteredData());

		return results;
	}
}
