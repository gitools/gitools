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
		private String[] clusterTitles;
		private String[] dataLabels;
		private Map<String, int[]> clusterDataIndices;
		private Map<String, Integer> dataClusterIndices;

		public AnnotationsClusterResults(String[] dataLabels, Map<String, int[]> clusterDataIndices) {
			this.dataLabels = dataLabels;
			this.clusterDataIndices = clusterDataIndices;

			generateDataClusterIndices(dataLabels, clusterDataIndices);
		}

		private void generateDataClusterIndices(String[] dataLabels, Map<String, int[]> clusters) {
			clusterTitles = new String[clusters.size()];
			dataClusterIndices = new HashMap<String, Integer>();
			int clusterIndex = 0;
			for (Map.Entry<String, int[]> e : clusters.entrySet()) {
				clusterTitles[clusterIndex] = e.getKey();
				for (int i : e.getValue())
					dataClusterIndices.put(dataLabels[i], clusterIndex);
				clusterIndex++;
			}
		}

		@Override
		public int getNumClusters() {
			return clusterTitles.length;
		}

		@Override
		public String[] getClusterTitles() {
			return clusterTitles;
		}

		@Override
		public String[] getDataLabels() {
			return dataLabels;
		}

		@Override
		public String[] getDataLabels(String clusterTitle) {
			int[] dataIndices = clusterDataIndices.get(clusterTitle);
			if (dataIndices == null)
				return new String[0];

			String[] labels = new String[dataIndices.length];
			for (int i = 0; i < dataIndices.length; i++)
				labels[i] = dataLabels[dataIndices[i]];

			return labels;
		}

		@Override
		public int getClusterIndex(String id) {
			Integer index = dataClusterIndices.get(id);
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

		String[] dataLabels = new String[data.getSize()];
		Map<String, List<Integer>> clusters = new HashMap<String, List<Integer>>();
		TextPattern pat = new TextPattern(pattern);
		for (int i = 0; i < data.getSize() && !monitor.isCancelled(); i++) {
			String label = data.getLabel(i);
			dataLabels[i] = label;
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

		Map<String, int[]> clusters2 = new HashMap<String, int[]>();
		for (Map.Entry<String, List<Integer>> e : clusters.entrySet()) {
			List<Integer> indices1 = e.getValue();
			int[] indices2 = new int[indices1.size()];
			for (int i = 0; i < indices1.size(); i++)
				indices2[i] = indices1.get(i);
			clusters2.put(e.getKey(), indices2);
		}

		monitor.end();

		return new AnnotationsClusterResults(dataLabels, clusters2);
	}
}
