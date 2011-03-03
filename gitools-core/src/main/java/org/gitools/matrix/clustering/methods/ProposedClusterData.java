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

/** Generic data interface for clustering methods */
public interface ProposedClusterData {

	/** Return the number of elements in the data */
	int getSize();

	/** Returns the label associated with the element at index <index> */
	String getLabel(int index);

	/** Returns the values associated with the element at index <index> for the attribute <attrIndex> */
	Object getValue(int index, int attrIndex);
}
