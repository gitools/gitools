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
package org.gitools.matrix.clustering;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.Properties;
import org.gitools.analysis.AnalysisException;
import org.gitools.matrix.clustering.methods.ClusteringMethodFactory;
import org.gitools.matrix.MatrixViewTransposition;
import org.gitools.matrix.model.IMatrixView;

public class MatrixViewClusterer {

	public static void cluster (IMatrixView matrixView, Properties clusterParameters, IProgressMonitor monitor) throws AnalysisException, Exception {

		ClusteringMethod method = ClusteringMethodFactory.createMethod(clusterParameters);

		if (Boolean.valueOf(clusterParameters.getProperty("transpose", "true"))) {
			
			MatrixViewTransposition mt = new MatrixViewTransposition();
			mt.setMatrix(matrixView);
			matrixView = mt;
		}

		method.buildAndCluster(matrixView, monitor);

	}
}
