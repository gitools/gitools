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

package org.gitools.matrix.clustering.methods;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.textpatt.TextPattern;
import edu.upf.bg.textpatt.TextPattern.VariableValueResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gitools.matrix.model.AnnotationMatrix;

public class AnnotationsClustererMethod implements ProposedClusterMethod {

	public static class AnnotationsClusterResults implements ProposedClusterResults {
		private String[] clusterNames;
		private Map<String, Integer> clusterIndices;

		public AnnotationsClusterResults(String[] clusterNames, Map<String, Integer> clusterIndices) {
			this.clusterNames = clusterNames;
			this.clusterIndices = clusterIndices;
		}

		@Override
		public int getNumClusters() {
			return clusterNames.length;
		}

		@Override
		public String getClusterName(int index) {
			return clusterNames[index];
		}

		@Override
		public int getClusterIndex(String id) {
			Integer index = clusterIndices.get(id);
			if (index == null)
				return -1;
			return index;
		}
	}

	private static class AnnotationResolver implements VariableValueResolver {

		private AnnotationMatrix am;

		private String label;
		private int annRow;

		public AnnotationResolver(AnnotationMatrix am) {
			this.am = am;
		}

		public void setState(String label, int annRow) {
			this.label = label;
			this.annRow = annRow;
		}

		@Override
		public String resolveValue(String variableName) {
			if (variableName.equalsIgnoreCase("id"))
				return label;

			int annCol = am.getColumnIndex(variableName);
			if (annCol == -1)
				return "${" + variableName + "}";

			return am.getCell(annRow, annCol);
		}
	}

	private AnnotationResolver resolver;
	private AnnotationMatrix am;
	private String pattern;
	
	public AnnotationsClustererMethod(AnnotationMatrix am, String pattern) {
		this.resolver = new AnnotationResolver(am);
		this.am = am;
		this.pattern = pattern;
	}

	/** Execute the clustering and return the results */
	@Override
	public ProposedClusterResults cluster(ProposedClusterData data, IProgressMonitor monitor) {
		monitor.begin("Clustering by annotations", data.getSize() + 1);

		Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
		TextPattern pat = new TextPattern(pattern);
		for (int i = 0; i < data.getSize() && !monitor.isCancelled(); i++) {
			String label = data.getLabel(i);
			int annRow = am.getRowIndex(label);
			resolver.setState(label, annRow);
			String clusterName = pat.generate(resolver);
			List<Integer> indices = clusters.get(clusterName);
			if (indices == null) {
				indices = new ArrayList<Integer>();
				clusters.put(label, indices);
			}
			indices.add(i);

			monitor.worked(1);
		}

		if (monitor.isCancelled())
			return null;

		String[] clusterNames = new String[clusters.size()];
		Map<String, Integer> clusterIndices = new HashMap<String, Integer>();
		int clusterIndex = 0;
		for (Map.Entry<String, List<Integer>> e : clusters.entrySet()) {
			String clusterName = e.getKey();
			List<Integer> indices = e.getValue();

			clusterNames[clusterIndex] = clusterName;
			for (Integer i : indices)
				clusterIndices.put(data.getLabel(i), clusterIndex);

			clusterIndex++;
		}

		monitor.end();

		return new AnnotationsClusterResults(clusterNames, clusterIndices);
	}
}
