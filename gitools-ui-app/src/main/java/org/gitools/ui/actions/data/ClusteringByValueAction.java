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

package org.gitools.ui.actions.data;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.awt.event.ActionEvent;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.HierarchicalClusteringResults;
import org.gitools.clustering.method.value.ClusterUtils;
import org.gitools.clustering.method.value.MatrixRowClusteringData;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.TransposedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.clustering.values.ClusteringValueWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;

public class ClusteringByValueAction extends BaseAction {

	public ClusteringByValueAction() {
		super("Cluster by values");
		setDesc("Cluster by values");
	}

	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		final Heatmap heatmap = ActionUtils.getHeatmap();

		if (heatmap == null)
			return;

		final ClusteringValueWizard wiz = new ClusteringValueWizard(heatmap);

		WizardDialog wdlg = new WizardDialog(AppFrame.instance(), wiz);

		wdlg.open();

		if (wdlg.isCancelled())
			return;

		final ClusteringMethod method = wiz.getMethod();

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					monitor.begin("Clustering  ...", 1);

					ClusteringResults results = method.cluster(wiz.getClusterData(), monitor);

					if (wiz.isSortData())
						if (wiz.isTranspose()) {
							TransposedMatrixView transposedMatrix = new TransposedMatrixView(heatmap.getMatrixView());
							ClusterUtils.updateVisibility(transposedMatrix, results.getDataIndicesByClusterTitle());
						} else
							ClusterUtils.updateVisibility(heatmap.getMatrixView(), results.getDataIndicesByClusterTitle());

					if (wiz.isAddHeader()) {

						HeatmapDim hdim = wiz.isTranspose() ?
							heatmap.getRowDim() : heatmap.getColumnDim();

						HeatmapColoredLabelsHeader header = new HeatmapColoredLabelsHeader(hdim);
						header.setTitle("Cluster_header_" + hdim.getHeaders().size());

						if (results instanceof HierarchicalClusteringResults)
							header.setHCLResults((HierarchicalClusteringResults) results);

						header.updateFromClusterResults(results);

						if (wiz.isTranspose())
							heatmap.getRowDim().addHeader(header);
						else
							heatmap.getColumnDim().addHeader(header);
					}

					monitor.end();
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});

		AppFrame.instance().setStatusText("Clusters created");
	}
}
