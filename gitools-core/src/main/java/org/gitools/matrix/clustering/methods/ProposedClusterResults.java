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

package org.gitools.matrix.clustering.methods;

public interface ProposedClusterResults {

	/** Returns the number of clusters */
	int getNumClusters();

	/** Return the cluster titles */
	String[] getClusterTitles();

	/** Get data labels for which there are cluster results */
	String[] getDataLabels();

	/** Returns the data labels for a given cluster */
	String[] getDataLabels(String clusterTitle);

	/** Returns the cluster index for a given row/column label in the matrix.
	 * If there is not cluster associated then return -1. */
	int getClusterIndex(String label);
}
