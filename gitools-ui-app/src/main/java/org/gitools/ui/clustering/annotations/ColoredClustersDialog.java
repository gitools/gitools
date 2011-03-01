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

import java.awt.Window;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapColoredClustersHeader;
import org.gitools.ui.platform.wizard.PageDialog;

public class ColoredClustersDialog {
	
	protected Heatmap heatmap;
	protected boolean rowMode;
	protected HeatmapColoredClustersHeader clustersHeader;

	public ColoredClustersDialog(Window owner, Heatmap heatmap, boolean rowMode, HeatmapColoredClustersHeader clustersHeader) {

		this.heatmap = heatmap;
		this.rowMode = rowMode;
		this.clustersHeader = clustersHeader;

		ColoredClustersPage page = new ColoredClustersPage(clustersHeader);
		page.setTitle("Generate clusters from annotations");
		page.setMessage("Edit the generated clusters");

		PageDialog pd = new PageDialog(owner, page);
		pd.setVisible(true);

		if (pd.isCancelled())
			return;
	}
}
