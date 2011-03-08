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

package org.gitools.ui.clustering.annotations;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.clustering.method.annotations.AnnPatClusteringMethod;
import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringResults;
import org.gitools.clustering.method.annotations.AnnPatColumnClusteringData;
import org.gitools.clustering.method.annotations.AnnPatRowClusteringData;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class ColoredLabelsHeaderWizard extends AbstractWizard {

	private Heatmap heatmap;
	private HeatmapDim hdim;
	private boolean applyToRows;

	private boolean editionMode;
	
	private String lastPattern;
	private HeatmapColoredLabelsHeader header;

	private AnnPatClusteringMethod clusteringMethod;
	
	private ColoredLabelsAnnotationsPage sourcePage;
	private ColoredLabelsHeaderPage headerPage;
	private ColoredLabelsPage clustersPage;

	public ColoredLabelsHeaderWizard(Heatmap heatmap, HeatmapDim hdim, HeatmapColoredLabelsHeader h, boolean applyToRows) {
		super();

		this.heatmap = heatmap;
		this.hdim = hdim;
		this.applyToRows = applyToRows;

		this.lastPattern = "";
		this.header = h;

		clusteringMethod = new AnnPatClusteringMethod();
	}

	@Override
	public void addPages() {
		if (!editionMode) {
			sourcePage = new ColoredLabelsAnnotationsPage(hdim, clusteringMethod);
			addPage(sourcePage);
		}

		headerPage = new ColoredLabelsHeaderPage(header);
		addPage(headerPage);

		clustersPage = new ColoredLabelsPage(header);
		addPage(clustersPage);
	}

	@Override
	public boolean canFinish() {
		return currentPage != sourcePage;
	}

	@Override
	public void pageLeft(IWizardPage currentPage) {
		super.pageLeft(currentPage);

		if (currentPage != sourcePage || editionMode)
			return;

		String pattern = sourcePage.getPattern();
		if (lastPattern.equals(pattern))
			return;

		IMatrixView mv = heatmap.getMatrixView();
		AnnotationMatrix am = hdim.getAnnotations();

		final ClusteringData data = applyToRows ?
				new AnnPatRowClusteringData(mv, am, pattern)
				: new AnnPatColumnClusteringData(mv, am, pattern);

		header.setTitle("Colors: " + sourcePage.getClusterTitle());

		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					final ClusteringResults results =
							clusteringMethod.cluster(data, monitor);

					header.updateFromClusterResults(results);
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}

	public HeatmapColoredLabelsHeader getHeader() {
		return header;
	}

	public void setEditionMode(boolean editionMode) {
		this.editionMode = editionMode;
	}
}
