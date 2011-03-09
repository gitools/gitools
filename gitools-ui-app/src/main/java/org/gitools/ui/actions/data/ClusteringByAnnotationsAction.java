/*
 *  Copyright 2011 chris.
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
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ClusteringByAnnotationsAction extends BaseAction {

	public ClusteringByAnnotationsAction() {
		super("Cluster by annotations");
		setDesc("Cluster by annotations");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		EditorsPanel editorPanel = AppFrame.instance().getEditorsPanel();

		IEditor currentEditor = editorPanel.getSelectedEditor();

		Object model = currentEditor.getModel();

		if (!(model instanceof Heatmap))
			return;

		Heatmap heatmap = (Heatmap) model;
		boolean rowMode = true;
		
		/*Colo wiz = new ClusterSetGeneratorWizard(heatmap, rowMode);
		wiz.setTitle("Create and edit a new Cluster Set");
		WizardDialog dlg = new WizardDialog(AppFrame.instance(), wiz);
		dlg.setVisible(true);

		if (dlg.isCancelled())
			return;


		HeatmapColoredClustersHeader hcs = wiz.getClusterSet();
		if (rowMode)
			heatmap.getRowDim().getHeaders().add(hcs);
		else
			heatmap.getColumnDim().getHeaders().add(hcs);*/

		AppFrame.instance().setStatusText("Clusters created");
	}
}
