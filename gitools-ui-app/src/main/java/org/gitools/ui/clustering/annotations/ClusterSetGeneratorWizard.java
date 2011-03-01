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

package org.gitools.ui.clustering.annotations;

import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapColoredClustersHeader;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.ui.platform.wizard.AbstractWizard;

public class ClusterSetGeneratorWizard extends AbstractWizard {
	private HeatmapColoredClustersHeader clusterSet;
	private boolean rowMode;
	private Heatmap heatmap;

	public ClusterSetGeneratorWizard(Heatmap heatmap, boolean rowMode) {
		super();

		HeatmapDim dim = rowMode ? heatmap.getColumnDim() : heatmap.getRowDim();

		this.clusterSet = new HeatmapColoredClustersHeader(dim);
		this.rowMode = rowMode;
		this.heatmap = heatmap;
	}

	@Override
	public void addPages() {
		ClusterSetGeneratePage clusterSetGenerator = new ClusterSetGeneratePage(heatmap, clusterSet, rowMode);
		clusterSetGenerator.setTitle("Generate Cluster Set");
		addPage(clusterSetGenerator);

		ColoredClustersPage clusterSetEditorPage = new ColoredClustersPage(clusterSet);
		clusterSetEditorPage.setTitle("Generate Cluster Set");
		addPage(clusterSetEditorPage);

	}
	

	@Override
	public void performFinish() {
		// TODO Auto-generated method stub
	}

	public HeatmapColoredClustersHeader getClusterSet() {
		return this.clusterSet;
	}

}
