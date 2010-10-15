/*
 *  Copyright 2010 mschroeder.
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

package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapClusterSet;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.ui.wizard.clustering.color.ClusterSetGeneratorWizard;

/**
 *
 * @author mschroeder
 */

public class ColorClusterGenerateAction extends BaseAction {

	boolean rowMode;
	Heatmap heatmap;

	public ColorClusterGenerateAction(Heatmap heatmap, boolean rowMode) {
		super("Generate color cluster set");
		setDesc("Generate color cluster set");
		this.rowMode = rowMode;
		this.heatmap = heatmap;
	}


	@Override
	public void actionPerformed(ActionEvent e) {

		/*final HeatmapHeader header = rowMode ?
			ActionUtils.getHeatmap().getRowHeader() :
			ActionUtils.getHeatmap().getColumnHeader();
		ColorClusterDialog dlg = new ColorClusterDialog(null, rowMode, contents, header);*/

		ClusterSetGeneratorWizard wiz = new ClusterSetGeneratorWizard(heatmap, rowMode);
		wiz.setTitle("Create and edit a new Cluster Set");
		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;


		HeatmapClusterSet hcs = wiz.getClusterSet();
		//FIXME: allow multiple HeatmapClusterSets to be stored
		HeatmapClusterSet[] hcss = new HeatmapClusterSet[1];
		hcss[0] = hcs;
		if (rowMode) {
			heatmap.setRowClusterSets(hcss);
			heatmap.getRowHeader().setColorAnnEnabled(true);
		} else {
			heatmap.setColumnClusterSets(hcss);
			heatmap.getColumnHeader().setColorAnnEnabled(true);
		}


		AppFrame.instance().setStatusText("Cluster Set \"" +
				hcs.getTitle() +
				"\" stored");
	}


	
}
