/*
 *  Copyright 2011 chris.
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

package org.gitools.clustering.method.annotations;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gitools.clustering.GenericClusteringResults;

public class AnnPatClusteringMethod implements ClusteringMethod {

	private String pattern;

	public AnnPatClusteringMethod() {
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/** Execute the clustering and return the results */
	@Override
	public ClusteringResults cluster(ClusteringData data, IProgressMonitor monitor) {
		monitor.begin("Clustering by annotations", data.getSize() + 1);

		String[] dataLabels = new String[data.getSize()];
		Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
		for (int i = 0; i < data.getSize() && !monitor.isCancelled(); i++) {
			String label = data.getLabel(i);
			dataLabels[i] = label;
			String clusterName = data.getInstance(i).getTypedValue(0, String.class);
			List<Integer> indices = clusters.get(clusterName);
			if (indices == null) {
				indices = new ArrayList<Integer>();
				clusters.put(clusterName, indices);
			}
			indices.add(i);

			monitor.worked(1);
		}

		if (monitor.isCancelled())
			return null;

		monitor.end();

		return new GenericClusteringResults(dataLabels, clusters);
	}
}
