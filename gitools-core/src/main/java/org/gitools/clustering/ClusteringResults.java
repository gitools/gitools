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

import java.util.Map;

public interface ClusteringResults {

	/** Returns the number of clusters */
	int getNumClusters();

	/** Returns the cluster titles */
	String[] getClusterTitles();

	/** Returns the cluster index by its title */
	int getClusterTitleIndex(String clusterTitle);

	/** Returns the number of data labels */
	int getNumDataLabels();

	/** Returns data labels for which there are cluster results */
	String[] getDataLabels();

	/** Returns the data label index */
	int getDataLabelIndex(String dataLabel);

	/** Returns the data indicess for a given cluster */
	int[] getDataIndices(int clusterIndex);
	
	int[] getDataIndices(String clusterTitle);
	
	/** Returns the data labels for a given cluster */
	String[] getDataLabels(int clusterIndex);

	String[] getDataLabels(String clusterTitle);

	/** Returns the cluster the data belongs to */
	int getClusterIndex(int dataIndex);

	int getClusterIndex(String dataLabel);

	/** Returns a map from cluster title to an array of data indices included in the cluster */
	Map<String, int[]> getDataIndicesByClusterTitle();

	/** Returns a map from the data label to the cluster index the data belongs to */
	Map<String, Integer> getClusterIndexByDataLabel();
}
