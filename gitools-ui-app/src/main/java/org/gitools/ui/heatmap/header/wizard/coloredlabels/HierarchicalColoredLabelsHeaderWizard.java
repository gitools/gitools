/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.ui.heatmap.header.wizard.coloredlabels;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapHierarchicalColoredLabelsHeader;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class HierarchicalColoredLabelsHeaderWizard extends AbstractWizard {

	private Heatmap heatmap;
	private HeatmapDim hdim;

	private HeatmapHierarchicalColoredLabelsHeader header;
	private int previousLevel;
	
	private ColoredLabelsConfigPage headerPage;
	private ColoredLabelsGroupsPage clustersPage;
	private HclLevelPage hclPage;

	public HierarchicalColoredLabelsHeaderWizard(
			Heatmap heatmap, HeatmapDim hdim,
			HeatmapHierarchicalColoredLabelsHeader header) {

		super();

		this.heatmap = heatmap;
		this.hdim = hdim;

		this.header = header;
		previousLevel = header.getTreeLevel();
	}

	@Override
	public void addPages() {
		headerPage = new ColoredLabelsConfigPage(header);
		addPage(headerPage);

		hclPage = new HclLevelPage(header);
		addPage(hclPage);

		clustersPage = new ColoredLabelsGroupsPage(header.getClusters());
		addPage(clustersPage);
	}

	@Override
	public boolean canFinish() {
		return true;
	}

	@Override
	public void performCancel() {
		JobThread.execute(AppFrame.instance(), new JobRunnable() {
			@Override public void run(IProgressMonitor monitor) {
				try {
					header.setTreeLevel(previousLevel);
				}
				catch (Throwable ex) {
					monitor.exception(ex);
				}
			}
		});
	}

	@Override
	public void pageLeft(IWizardPage currentPage) {	
		super.pageLeft(currentPage);

		if (currentPage == hclPage) {
			JobThread.execute(AppFrame.instance(), new JobRunnable() {
				@Override public void run(IProgressMonitor monitor) {
					try {
						header.setTreeLevel(hclPage.getLevel());
					}
					catch (Throwable ex) {
						monitor.exception(ex);
					}
				}
			});
		}
	}

	public HeatmapColoredLabelsHeader getHeader() {
		return header;
	}
}
