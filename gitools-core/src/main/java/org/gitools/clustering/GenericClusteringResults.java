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

package org.gitools.clustering;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericClusteringResults implements ClusteringResults {

	private String[] clusterTitles;
	private String[] dataLabels;

	private int[][] clusterDataIndices;

	private int[] dataClusterIndex;
	
	private Map<String, Integer> clusterTitlesIndex;
	private Map<String, Integer> dataLabelsIndex;

	public GenericClusteringResults(String[] dataLabels, Map<String, List<Integer>> clusters) {
		init(dataLabels, clusters);
	}

	protected final void init(String[] dataLabels, Map<String, List<Integer>> clusters) {
		this.dataClusterIndex = null;
		this.clusterTitlesIndex = null;
		this.dataLabelsIndex = null;

		this.dataLabels = dataLabels;
		int numClusters = clusters.size();
		clusterDataIndices = new int[numClusters][];
		clusterTitles = clusters.keySet().toArray(new String[numClusters]);
		Arrays.sort(clusterTitles);
		for (int ci = 0; ci < numClusters; ci++) {
			List<Integer> indices = clusters.get(clusterTitles[ci]);
			int[] indicesArray = new int[indices.size()];
			for (int i = 0; i < indicesArray.length; i++)
				indicesArray[i] = indices.get(i);
			clusterDataIndices[ci] = indicesArray;
		}
	}

	private void createClusterDataIndices() {
		if (dataClusterIndex == null)
			throw new RuntimeException("Data cluster indices required");

		int numClusters = clusterTitles.length;

		int[] len = new int[numClusters];
		Arrays.fill(len, 0);

		for (int ci : dataClusterIndex)
			len[ci]++;

		clusterDataIndices = new int[numClusters][];
		for (int ci = 0; ci < numClusters; ci++)
			clusterDataIndices[ci] = new int[len[ci]];

		Arrays.fill(len, 0);
		for (int i = 0; i< dataClusterIndex.length; i++) {
			int ci = dataClusterIndex[i];
			int[] dataIndices = clusterDataIndices[ci];
			dataIndices[len[ci]++] = i;
		}
	}

	private void createDataClusterIndices() {
		if (clusterDataIndices == null)
			throw new RuntimeException("Cluster data indices required");

		dataClusterIndex = new int[dataLabels.length];

		for (int ci = 0; ci < clusterDataIndices.length; ci++) {
			int[] dataIndices = clusterDataIndices[ci];
			if (dataIndices == null)
				throw new RuntimeException("Cluster data indices required: " + ci);

			for (int i = 0; i < dataIndices.length; i++)
				dataClusterIndex[dataIndices[i]] = ci;
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
	public int getClusterTitleIndex(String clusterTitle) {
		if (clusterTitlesIndex == null) {
			clusterTitlesIndex = new HashMap<String, Integer>();
			for (int i = 0; i < clusterTitles.length; i++)
				clusterTitlesIndex.put(clusterTitles[i], i);
		}
		Integer index = clusterTitlesIndex.get(clusterTitle);
		return index != null ? index : -1;
	}

	@Override
	public int getNumDataLabels() {
		return dataLabels.length;
	}

	@Override
	public String[] getDataLabels() {
		return dataLabels;
	}

	@Override
	public int getDataLabelIndex(String dataLabel) {
		if (dataLabelsIndex == null) {
			dataLabelsIndex = new HashMap<String, Integer>();
			for (int i = 0; i < dataLabels.length; i++)
				dataLabelsIndex.put(dataLabels[i], i);
		}
		Integer index = dataLabelsIndex.get(dataLabel);
		return index != null ? index : -1;
	}

	@Override
	public int[] getDataIndices(int clusterIndex) {
		if (clusterDataIndices == null)
			createClusterDataIndices();

		return clusterDataIndices[clusterIndex];
	}

	@Override
	public int[] getDataIndices(String clusterTitle) {
		return getDataIndices(getClusterIndex(clusterTitle));
	}

	@Override
	public String[] getDataLabels(int clusterIndex) {
		int[] indices = getDataIndices(clusterIndex);
		String[] labels = new String[indices.length];
		for (int i = 0; i < indices.length; i++)
			labels[i] = dataLabels[indices[i]];
		return labels;
	}

	@Override
	public String[] getDataLabels(String clusterTitle) {
		return getDataLabels(getClusterIndex(clusterTitle));
	}

	@Override
	public int getClusterIndex(int dataIndex) {
		if (dataClusterIndex == null)
			createDataClusterIndices();

		return dataClusterIndex[dataIndex];
	}

	@Override
	public int getClusterIndex(String dataLabel) {
		return getClusterIndex(getDataLabelIndex(dataLabel));
	}

	@Override
	public Map<String, int[]> getDataIndicesByClusterTitle() {
		if (clusterDataIndices == null)
			createClusterDataIndices();

		Map<String, int[]> map = new HashMap<String, int[]>();
		for (int ci = 0; ci < clusterDataIndices.length; ci++)
			map.put(clusterTitles[ci], clusterDataIndices[ci]);

		return map;
	}

	@Override
	public Map<String, Integer> getClusterIndexByDataLabel() {
		if (dataClusterIndex == null)
			createDataClusterIndices();

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < dataClusterIndex.length; i++)
			map.put(dataLabels[i], dataClusterIndex[i]);
		
		return map;
	}
}
