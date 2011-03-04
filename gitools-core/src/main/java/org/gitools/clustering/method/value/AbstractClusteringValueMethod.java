/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;


public abstract class AbstractClusteringValueMethod implements ClusteringMethod {

	protected boolean preprocess;

	protected boolean transpose;

	protected int classIndex;
	
	public abstract ClusteringResults cluster(ClusteringData data, IProgressMonitor monitor) throws ClusteringException;

	public boolean isPreprocess() {
		return preprocess;
	}

	public void setPreprocess(boolean preprocess) {
		this.preprocess = preprocess;
	}

	public boolean isTranspose() {
		return transpose;
	}

	public void setTranspose(boolean transposed) {
		this.transpose = transposed;
	}

	public int getClassIndex() {
		return classIndex;
	}

	public void setClassIndex(int classIndex) {
		this.classIndex = classIndex;
	}

}
