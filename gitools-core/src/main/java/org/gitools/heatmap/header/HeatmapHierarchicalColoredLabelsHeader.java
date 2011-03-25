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

package org.gitools.heatmap.header;

import org.gitools.heatmap.HeatmapDim;
import org.gitools.clustering.HierarchicalClusteringResults;

public class HeatmapHierarchicalColoredLabelsHeader extends HeatmapColoredLabelsHeader {

	public static final String CLUSTERING_RESULTS_CHANGED = "clusteringResults";
	public static final String TREE_LEVEL_CHANGED = "treeLevel";

	private HierarchicalClusteringResults clusteringResults;

	public HeatmapHierarchicalColoredLabelsHeader(HeatmapDim hdim) {
		super(hdim);
	}

	public HierarchicalClusteringResults getClusteringResults() {
		return clusteringResults;
	}

	public void setClusteringResults(HierarchicalClusteringResults results) {
		HierarchicalClusteringResults old = this.clusteringResults;
		this.clusteringResults = results;
		firePropertyChange(CLUSTERING_RESULTS_CHANGED, old, results);
	}

	public int getTreeLevel() {
		return clusteringResults.getLevel();
	}

	public void setTreeLevel(int level) {
		int old = clusteringResults.getLevel();
		clusteringResults.setLevel(level);
		updateFromClusterResults(clusteringResults);
		firePropertyChange(TREE_LEVEL_CHANGED, old, level);
	}

}
