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

package org.gitools.clustering.method.value;

import java.io.IOException;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringException;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.GenericClusteringResults;
import uk.ac.vamsas.objects.utils.trees.BinaryNode;
import uk.ac.vamsas.objects.utils.trees.NewickFile;
import uk.ac.vamsas.objects.utils.trees.SequenceNode;
import weka.core.Instances;
import weka.core.NormalizableDistance;
import weka.core.SelectedTag;


public class WekaHCLMethod extends AbstractClusteringValueMethod {

	private SelectedTag linkType;

	private boolean distanceIsBranchLength;

	private int numClusters;

	private boolean printNewick;
	
	private NormalizableDistance distanceFunction;

	public WekaHCLMethod() {
		classIndex = -1;
	}

	@Override
	public ClusteringResults cluster(ClusteringData clusterData, IProgressMonitor monitor) throws ClusteringException {

		try {
			Instances structure = ClusterUtils.buildInstanceStructure(clusterData, transpose);

			MatrixViewWeka clusterWekaData = new MatrixViewWeka(structure, clusterData, classIndex);

			List<String> labels = ClusterUtils.getLabels(clusterData, transpose);

			if (preprocess)
				ClusterUtils.dataReductionProcess(clusterWekaData, monitor);

			WekaHierarchicalClusterer clusterer = new WekaHierarchicalClusterer();

			configure(clusterer);

			clusterer.buildClusterer(clusterWekaData);

			ClusteringResults results = null;

			if (!monitor.isCancelled()) {

				monitor.end();

				// Identify cluster by instance
				monitor.begin("Clustering instances ...", clusterWekaData.getMatrixView().getSize());

				String newickTree = clusterer.graph();

				List<Integer> instancesCluster = getTreeLeaves(newickTree);
								

				HashMap<String, List<Integer>> clusterResults = new HashMap<String, List<Integer>>();
				
				clusterResults.put("0", instancesCluster);

				results = new GenericClusteringResults(labels.toArray(new String[0]), clusterResults);
			
			}

			return results;

		} catch (Exception ex) {
			throw new ClusteringException("Error in agglomerative hierarchical clustering method ");
		}
	}

	private List<Integer> getTreeLeaves(String result) throws NumberFormatException, IOException {

		List<Integer> instancesCluster = new ArrayList<Integer>();

		Vector v = new Vector();

		NewickFile newickTree = new NewickFile(result);

		SequenceNode sequenceNodes = newickTree.getTree();

		newickTree.findLeaves(sequenceNodes, v);

		Enumeration lv = v.elements();

		while (lv.hasMoreElements()) {
			BinaryNode leave = (BinaryNode) lv.nextElement();
			if (leave.getName() != null) {
				instancesCluster.add(new Integer(leave.getName().substring(1)));
			}
		}
		return instancesCluster;
	}

	public NormalizableDistance getDistanceFunction() {
		return distanceFunction;
	}

	public void setDistanceFunction(NormalizableDistance distanceFunction) {
		this.distanceFunction = distanceFunction;
	}

	public boolean isDistanceIsBranchLength() {
		return distanceIsBranchLength;
	}

	public void setDistanceIsBranchLength(boolean distanceIsBranchLength) {
		this.distanceIsBranchLength = distanceIsBranchLength;
	}

	public SelectedTag getLinkType() {
		return linkType;
	}

	public void setLinkType(SelectedTag linkType) {
		this.linkType = linkType;
	}

	public int getNumClusters() {
		return numClusters;
	}

	public void setNumClusters(int numClusters) {
		this.numClusters = numClusters;
	}

	public boolean isPrintNewick() {
		return printNewick;
	}

	public void setPrintNewick(boolean printNewick) {
		this.printNewick = printNewick;
	}

	private void configure(WekaHierarchicalClusterer clusterer) {
		clusterer.setDistanceFunction(distanceFunction);
		clusterer.setPrintNewick(printNewick);
		clusterer.setNumClusters(numClusters);
		clusterer.setLinkType(linkType);
		clusterer.setDistanceIsBranchLength(distanceIsBranchLength);
	}

}
