/*
 *  Copyright 2010 chris.
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

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.util.Properties;
import org.gitools.matrix.clustering.MatrixViewClusterer;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.clustering.ClusteringDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

public class ClusteringAction extends BaseAction { 

	public ClusteringAction() {
		super("Clustering");
		setDesc("Clustering"); 
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final IMatrixView matrixView = ActionUtils.getMatrixView();

		if (matrixView == null)
			return;

		ClusteringDialog dlg = new ClusteringDialog(AppFrame.instance());
		dlg.setAttributes(matrixView.getContents().getCellAttributes(),matrixView.getSelectedPropertyIndex());
		dlg.setVisible(true);

		if (dlg.getReturnStatus() != ClusteringDialog.RET_OK) {
			AppFrame.instance().setStatusText("Clustering cancelled.");
			return;
		}

		final Properties clusterParameters = dlg.getClusterParameters();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {

				try {

					monitor.begin("Clustering  ...", 1);
					MatrixViewClusterer.cluster(matrixView, clusterParameters, monitor);
					monitor.end();
					
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});


		AppFrame.instance().setStatusText("Done.");
	}
}
