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

import java.util.HashMap;
import java.util.List;


public class ProposedHierarchicalClusterResults implements ProposedClusterResults {

	HashMap<Integer, List<Integer>> clusteredData;

	boolean header;

	int headerSize;

	String headerBgColor;

	boolean newickTree;

	String newickFormat;

	public HashMap<Integer, List<Integer>> getClusteredData() {
		return clusteredData;
	}

	public void setClusteredData(HashMap<Integer, List<Integer>> clusteredData) {
		this.clusteredData = clusteredData;
	}

	public boolean isHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}

	public String getHeaderBgColor() {
		return headerBgColor;
	}

	public void setHeaderBgColor(String headerBgColor) {
		this.headerBgColor = headerBgColor;
	}

	public int getHeaderSize() {
		return headerSize;
	}

	public void setHeaderSize(int headerSize) {
		this.headerSize = headerSize;
	}

	public String getNewickFormat() {
		return newickFormat;
	}

	public void setNewickFormat(String newickFormat) {
		this.newickFormat = newickFormat;
	}

	public boolean isNewickTree() {
		return newickTree;
	}

	public void setNewickTree(boolean newickTree) {
		this.newickTree = newickTree;
	}

	@Override
	public int getNumClusters() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getClusterIndex(String id) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String[] getClusterTitles() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String[] getDataLabels() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String[] getDataLabels(String clusterTitle) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
