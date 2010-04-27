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

package org.gitools.ui.actions.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import java.util.List;
import org.gitools.analysis.clustering.ClusteringAnalysis;
import org.gitools.analysis.clustering.ClusteringProcessor;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.matrix.model.element.IElementAttribute;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.analysis.clustering.dialog.ClusteringDialog;
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
		dlg.setAttributes(matrixView.getContents().getCellAttributes());
		dlg.setVisible(true);

		if (dlg.getReturnStatus() != ClusteringDialog.RET_OK) {
			AppFrame.instance().setStatusText("Filter cancelled.");
			return;
		}

		final ClusteringAnalysis analysis = dlg.getAnalysis();

		analysis.setData(matrixView);

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {

				try {

					monitor.begin("Clustering  ...", 1);

					new ClusteringProcessor(analysis).run(monitor);
					
					monitor.end();
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});


		AppFrame.instance().setStatusText("New heatmap created.");
	}
}
