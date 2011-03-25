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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gitools.newick.NewickNode;
import org.gitools.newick.NewickTree;

public class HierarchicalClusteringResults extends GenericClusteringResults {

	private NewickTree tree;
	private int level;

	public HierarchicalClusteringResults(String[] labels, NewickTree tree, int level) {
		super(labels, new HashMap<String, List<Integer>>());

		this.tree = tree;
		this.level = level;

		updateClusters();
	}

	public NewickTree getTree() {
		return tree;
	}

	public void setNewickTree(NewickTree tree) {
		this.tree = tree;
		this.level = 0;
		updateClusters();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		updateClusters();
	}

	private void updateClusters() {
		NewickNode root = tree.getRoot();

		List<NewickNode> clusterLeaves = root.getLeaves(level);

		StringBuilder sb = new StringBuilder();
		int len = (int) (Math.floor(Math.log10(clusterLeaves.size())) + 1);
		for (int i = 0; i < len; i++)
			sb.append('0');

		String fmtPat = sb.toString();
		DecimalFormat fmt = new DecimalFormat(fmtPat);

		int index = 0;
		Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
		for (NewickNode cluster : clusterLeaves) {
			List<NewickNode> nodes = cluster.getLeaves();
			List<Integer> indices = new ArrayList<Integer>(nodes.size());
			for (NewickNode node : nodes)
				indices.add(Integer.parseInt(node.getName()));

			String name = fmt.format(index++);
			clusters.put(name, indices);
		}

		init(getDataLabels(), clusters);
	}
}
