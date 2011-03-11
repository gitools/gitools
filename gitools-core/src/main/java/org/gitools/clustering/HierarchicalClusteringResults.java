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

package org.gitools.clustering;

import java.util.List;
import java.util.Map;
import org.gitools.newick.NewickTree;


public class HierarchicalClusteringResults extends GenericClusteringResults {

	private String newickTree;

	private NewickTree tree;

	public HierarchicalClusteringResults(String[] dataLabels, Map<String, List<Integer>> clusters, NewickTree tree, String newickFormat) {
		super(dataLabels, clusters);

		this.newickTree = newickFormat;
	}

	public String getNewickTree() {
		return newickTree;
	}

	public void setNewickTree(String newickTree) {
		this.newickTree = newickTree;
	}

	public NewickTree getTree() {
		return tree;
	}

	public void setTree(NewickTree tree) {
		this.tree = tree;
	}
}
