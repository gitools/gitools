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

package org.gitools.matrix.clustering.methods;

import java.io.IOException;
import org.gitools.matrix.clustering.MatrixViewWeka;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.gitools.analysis.AbstractMethod;
import org.gitools.matrix.clustering.ClusterException;
import uk.ac.vamsas.objects.utils.trees.BinaryNode;
import uk.ac.vamsas.objects.utils.trees.NewickFile;
import uk.ac.vamsas.objects.utils.trees.SequenceNode;
import weka.core.EuclideanDistance;
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;


public class WekaHCLMethod extends AbstractMethod implements ProposedClusterMethod {

	public static final String ID = "hierarchical";


	public WekaHCLMethod(Properties properties) {
		super(ID,
				"Agglomerative hierarchical clustering",
				"Agglomerative hierarchical's Weka clustering",null, properties);
	}

/*
 * Build the model and cluster the dataset
 * Hashmap with all Cluster and the instances wich belong to
 */
	@Override
	public ProposedClusterResults cluster(ProposedClusterData dataToCluster, IProgressMonitor monitor) throws ClusterException {
		try {
			MatrixViewWeka data = (MatrixViewWeka) dataToCluster;

			ProposedHierarchicalClusterResults results = new ProposedHierarchicalClusterResults();
			HashMap<Integer, List<Integer>> clusterResults = new HashMap<Integer, List<Integer>>();
			List<Integer> instancesCluster = new ArrayList<Integer>();

			WekaHierarchicalClusterer clusterer = new WekaHierarchicalClusterer();

			configure(clusterer);

			clusterer.buildClusterer(data);

			if (!monitor.isCancelled()) {

				monitor.end();

				// Identify cluster by instance
				monitor.begin("Clustering instances ...", data.getMatrixView().getVisibleColumns().length);

				String newickTree = clusterer.graph();

				instancesCluster = getTreeLeaves(newickTree);

				clusterResults.put(0, instancesCluster);
			}

			results.setClusteredData(clusterResults);
			return results;

		} catch (Exception ex) {
			throw new ClusterException("Error in " + ID + " clustering method ");
		}
	}


	@Override
	public String getId() {
		return ID;
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

	private void configure(WekaHierarchicalClusterer clusterer) {

		clusterer.setLinkType(
				new SelectedTag(properties.getProperty("link").toUpperCase(), WekaHierarchicalClusterer.TAGS_LINK_TYPE));

		clusterer.setDistanceIsBranchLength(false);
		clusterer.setNumClusters(1);
		clusterer.setPrintNewick(true);

		if (properties.getProperty("distance").equalsIgnoreCase("euclidean"))
			clusterer.setDistanceFunction(new EuclideanDistance());
		else
			clusterer.setDistanceFunction(new ManhattanDistance());

	}
}
