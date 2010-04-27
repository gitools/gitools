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

package org.gitools.analysis.clustering;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.AnalysisException;
import org.gitools.analysis.clustering.methods.ClusteringMethodFactory;


public class ClusteringProcessor {

	protected ClusteringAnalysis analysis;

	public ClusteringProcessor(ClusteringAnalysis analysis) {
		this.analysis = analysis;
	}

	public void run(IProgressMonitor monitor) throws AnalysisException, Exception {

			ClusteringMethod method = ClusteringMethodFactory.createMethod(analysis.getParams());

			if (analysis.isApplyToRows()) 				

					method.buildAndCluster(analysis.getData(),"rows");			
			else

			if (analysis.isApplyToColumns())				

					method.buildAndCluster(analysis.getData(),"cols");

	}


}
