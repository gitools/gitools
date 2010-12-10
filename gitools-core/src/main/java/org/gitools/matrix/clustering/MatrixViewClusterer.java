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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import org.gitools.matrix.clustering.methods.ClusteringMethodFactory;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import weka.core.Instances;

public class MatrixViewClusterer {

	public static void cluster (IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor) throws Exception {

		ClusteringMethod method = ClusteringMethodFactory.createMethod(clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("transpose"))) {
			
			TransposedMatrixView mt = new TransposedMatrixView();
			mt.setMatrix(matrixView);
			matrixView = mt;
		}

		Instances str = ClusterUtils.getInstance().matrix2Instances(matrixView, clusterParameters);

		MatrixViewWeka data = new MatrixViewWeka(str);		

		data.initialize(matrixView, clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("preprocessing")))
			ClusterUtils.getInstance().dataReductionProcess(data, monitor);
		
		HashMap<Integer, List<Integer>> clusterResults = method.buildAndCluster(data, monitor);

		if (!monitor.isCancelled())
			ClusterUtils.getInstance().updateVisibility(data.getMatrixView(), clusterResults);

		monitor.end();

	}



}
