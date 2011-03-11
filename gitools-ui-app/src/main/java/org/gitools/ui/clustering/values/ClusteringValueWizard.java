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

package org.gitools.ui.clustering.values;

import org.gitools.clustering.ClusteringData;
import org.gitools.clustering.ClusteringMethodDescriptor;
import org.gitools.clustering.method.value.AbstractClusteringValueMethod;
import org.gitools.clustering.method.value.MatrixColumnClusteringData;
import org.gitools.clustering.method.value.MatrixRowClusteringData;
import org.gitools.clustering.method.value.WekaCobWebMethod;
import org.gitools.clustering.method.value.WekaHCLMethod;
import org.gitools.clustering.method.value.WekaKmeansMethod;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.ui.heatmap.header.coloredlabels.ColoredLabelsConfigPage;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class ClusteringValueWizard extends AbstractWizard {

	private Heatmap heatmap;
	
	private AbstractClusteringValueMethod method;
	private HeatmapColoredLabelsHeader header;

	private ClusteringMethodsPage sourcePage;
	private CobwebParamsPage cobwebPage;
	private HCLParamsPage hclPage;
	private KmeansParamsPage kmeansPage;
	private ClusteringSummaryPage clusterSummPage;
	private ColoredLabelsConfigPage headerPage;
	private SaveFilePage saveFilePage;



	public ClusteringValueWizard(Heatmap heatmap) {
		super();

		this.heatmap = heatmap;
	}

	@Override
	public void addPages() {

		sourcePage = new ClusteringMethodsPage();
		addPage(sourcePage);

		hclPage = new HCLParamsPage();
		addPage(hclPage);

		kmeansPage = new KmeansParamsPage();
		addPage(kmeansPage);

		cobwebPage = new CobwebParamsPage();
		addPage(cobwebPage);

		// Destination
		saveFilePage = new org.gitools.ui.wizard.common.SaveFilePage();
		addPage(saveFilePage);
		
		clusterSummPage = new ClusteringSummaryPage(heatmap.getMatrixView().getContents().getCellAttributes(),heatmap.getMatrixView().getSelectedPropertyIndex());
		addPage(clusterSummPage);

		/* Header page
		header = new HeatmapColoredLabelsHeader(heatmap.getColumnDim());
		headerPage = new ColoredLabelsConfigPage(header);
		addPage(headerPage);
		*/
	}

	@Override
	public boolean canFinish() {
		return (currentPage == clusterSummPage || currentPage == headerPage);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (currentPage == sourcePage) {

			ClusteringMethodDescriptor methodDescriptor = sourcePage.getMethodDescriptor();

			if (methodDescriptor == null)
				return null;

			if (methodDescriptor.getMethodClass().equals(WekaCobWebMethod.class)) 				
				page = cobwebPage;			

			if (methodDescriptor.getMethodClass().equals(WekaHCLMethod.class)) 			
				page = hclPage;			

			if (methodDescriptor.getMethodClass().equals(WekaKmeansMethod.class))
				page = kmeansPage;			
		}

		if (currentPage == hclPage) {
			saveFilePage.setTitle("Select Newick's tree destination file");
			saveFilePage.setFolder(Settings.getDefault().getLastWorkPath());
			saveFilePage.setFormatsVisible(false);
			saveFilePage.setComplete(true);
			page = saveFilePage;
		}

		if (currentPage instanceof ClusteringValueMethodPage && currentPage != hclPage) {
			method = ((ClusteringValueMethodPage) currentPage).getMethod();
			//clusterSummPage.enableHeader(currentPage != cobwebPage);
			page = clusterSummPage;
		}

		if (currentPage == saveFilePage) {
			method = hclPage.getMethod();
			page = clusterSummPage;
		}
		/*
		if (currentPage == clusterSummPage) {
			HeatmapDim hdim = isTranspose() ?
				heatmap.getRowDim() : heatmap.getColumnDim();

			header.setHeatmapDim(hdim);
			header.setTitle("Cluster_header_" + hdim.getHeaders().size());
			headerPage.setHeader(header);
			page = headerPage;
			}
		*/
		return page;
	}

	@Override
	public void pageLeft(IWizardPage page) {

		if (currentPage == sourcePage)
			return;

		if (currentPage == clusterSummPage) {

			ClusteringMethodDescriptor methodDescriptor = sourcePage.getMethodDescriptor();

			if (methodDescriptor == null) 
				return;	

			if (methodDescriptor.getMethodClass().equals(WekaCobWebMethod.class))
				page = cobwebPage;

			if (methodDescriptor.getMethodClass().equals(WekaHCLMethod.class)) 
				page = hclPage;

			if (methodDescriptor.getMethodClass().equals(WekaHCLMethod.class)) 
				page = kmeansPage;
			
		}
		if (currentPage instanceof ClusteringValueMethodPage)
			page = sourcePage;

		if (currentPage == saveFilePage)
			page = hclPage;
		/*
		if (currentPage == headerPage)
			page = clusterSummPage;
		*/
		super.pageLeft(page);
	}

	public ClusteringData getClusterData() {
		return clusterSummPage.isValuesFromRows() ?
		new MatrixRowClusteringData(heatmap.getMatrixView(), clusterSummPage.getDataAttribute())
		: new MatrixColumnClusteringData(heatmap.getMatrixView(), clusterSummPage.getDataAttribute());
	}

	public AbstractClusteringValueMethod getMethod() {

		method.setPreprocess(clusterSummPage.isPreprocessing());
		method.setTranspose(clusterSummPage.isValuesFromRows());

		return method;
	}

	public Boolean isAddHeader() {
		return clusterSummPage.isHeader();
	}

	public Boolean isSortData() {
		return clusterSummPage.isSort();
	}

	public Boolean isTranspose() {
		return clusterSummPage.isValuesFromRows();
	}

	public HeatmapColoredLabelsHeader getHeader() {
		return header;
	}
}
