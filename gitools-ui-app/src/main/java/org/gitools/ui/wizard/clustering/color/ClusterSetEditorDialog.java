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

package org.gitools.ui.wizard.clustering.color;

import java.awt.Window;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapClusterSet;
import org.gitools.ui.platform.wizard.PageDialog;

/**
 *
 * @author mschroeder
 */
public class ClusterSetEditorDialog {
	
	Heatmap heatmap;
	boolean rowMode;
	HeatmapClusterSet clusterSet;
	HeatmapClusterSet[] clusterSets;

	public ClusterSetEditorDialog(Window owner, Heatmap heatmap, boolean rowMode, int clusterSetIndex) {

		this.heatmap = heatmap;
		this.rowMode = rowMode;
		this.clusterSets = rowMode ?
			this.heatmap.getRowClusterSets().clone() :
			this.heatmap.getColumnClusterSets().clone() ;
		this.clusterSet = this.clusterSets[clusterSetIndex];

		ClusterSetEditorPage clusterSetEditorPage = new ClusterSetEditorPage(clusterSet);
		clusterSetEditorPage.setTitle("Generate Cluster Set");
		clusterSetEditorPage.setMessage("Edit the generated cluster set");

		PageDialog pd = new PageDialog(owner, clusterSetEditorPage);
		pd.setVisible(true);

		if (pd.isCancelled())
			return;
	}

	public ClusterSetEditorDialog(Window owner, Heatmap heatmap, boolean rowMode) {
		this(owner, heatmap, rowMode, 0);
	}

	public HeatmapClusterSet[] getClusterSets() {
		return this.clusterSets;
	}

}
