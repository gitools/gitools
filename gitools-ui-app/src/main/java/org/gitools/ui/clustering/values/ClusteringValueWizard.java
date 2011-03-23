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
import org.gitools.clustering.ClusteringMethod;
import org.gitools.clustering.ClusteringMethodDescriptor;
import org.gitools.clustering.method.value.AbstractClusteringValueMethod;
import org.gitools.clustering.method.value.MatrixColumnClusteringData;
import org.gitools.clustering.method.value.MatrixRowClusteringData;
import org.gitools.clustering.method.value.WekaCobWebMethod;
import org.gitools.clustering.method.value.WekaHCLMethod;
import org.gitools.clustering.method.value.WekaKmeansMethod;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.heatmap.header.coloredlabels.ColoredLabelsConfigPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.wizard.common.SaveFilePage;

public class ClusteringValueWizard extends AbstractWizard {

	private Heatmap heatmap;
	
	private AbstractClusteringValueMethod method;
	private HeatmapColoredLabelsHeader header;

	private ClusteringMethodsPage methodPage;
	private CobwebParamsPage cobwebPage;
	private HCLParamsPage hclPage;
	private KmeansParamsPage kmeansPage;
	private ClusteringOptionsPage optionsPage;
	private ColoredLabelsConfigPage headerPage;
	private SaveFilePage newickPage;

	public ClusteringValueWizard(Heatmap heatmap) {
		super();

		this.heatmap = heatmap;

		setTitle("Clustering by value");
		setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CLUSTERING, 96));
		setHelpContext("analysis_overlapping");
	}

	@Override
	public void addPages() {
		methodPage = new ClusteringMethodsPage();
		addPage(methodPage);

		IMatrixView mv = heatmap.getMatrixView();
		optionsPage = new ClusteringOptionsPage(
				mv.getContents().getCellAttributes(),
				mv.getSelectedPropertyIndex());
		addPage(optionsPage);

		header = new HeatmapColoredLabelsHeader(heatmap.getColumnDim());
		headerPage = new ColoredLabelsConfigPage(header);
		addPage(headerPage);

		newickPage = new SaveFilePage();
		newickPage.setTitle("Select Newick's tree destination file");
		newickPage.setFolder(Settings.getDefault().getLastExportPath());
		newickPage.setFormatsVisible(false);
		addPage(newickPage);

		hclPage = new HCLParamsPage();
		addPage(hclPage);

		kmeansPage = new KmeansParamsPage();
		addPage(kmeansPage);

		cobwebPage = new CobwebParamsPage();
		addPage(cobwebPage);
	}

	@Override
	public void performFinish() {
		Settings.getDefault().setLastExportPath(newickPage.getFolder());
		Settings.getDefault().save();
	}

	@Override
	public boolean canFinish() {
		return currentPage == cobwebPage
				|| currentPage == hclPage
				|| currentPage == kmeansPage;
	}

	@Override
	public boolean isLastPage(IWizardPage page) {
		return currentPage == cobwebPage
				|| currentPage == hclPage
				|| currentPage == kmeansPage;
	}

	@Override
	public IWizardPage getNextPage(IWizardPage currentPage) {

		IWizardPage nextPage = null;

		if (currentPage == optionsPage) {
			if (optionsPage.isHeaderSelected())
				nextPage = headerPage;
			else if (optionsPage.isNewickExportSelected())
				nextPage = newickPage;
			else
				nextPage = getMethodConfigPage();
		}
		else if ((currentPage == headerPage && !optionsPage.isNewickExportSelected())
					|| currentPage == newickPage)
			nextPage = getMethodConfigPage();
		else if (currentPage == cobwebPage
				|| currentPage == hclPage
				|| currentPage == kmeansPage)
			nextPage = null;
		else
			nextPage = super.getNextPage(currentPage);

		/* TODO
		if (currentPage == clusterSummPage) {
			HeatmapDim hdim = isTranspose() ?
				heatmap.getRowDim() : heatmap.getColumnDim();

			header.setHeatmapDim(hdim);
			header.setTitle("Cluster_header_" + hdim.getHeaders().size());
			headerPage.setHeader(header);
			page = headerPage;
			}
		*/
		return nextPage;
	}

	@Override
	public void pageLeft(IWizardPage currentPage) {
		if (currentPage == methodPage) {
			ClusteringMethodDescriptor methodDescriptor = methodPage.getMethodDescriptor();
			Class<? extends ClusteringMethod> methodClass = methodDescriptor.getMethodClass();
			optionsPage.setNewickExportVisible(WekaHCLMethod.class.equals(methodClass));
		}
		else if (currentPage == cobwebPage
				|| currentPage == hclPage
				|| currentPage == kmeansPage)
			method = ((ClusteringValueMethodPage) currentPage).getMethod();
	}

	private IWizardPage getMethodConfigPage() {
		ClusteringMethodDescriptor methodDescriptor = methodPage.getMethodDescriptor();
		Class<? extends ClusteringMethod> methodClass = methodDescriptor.getMethodClass();

		if (WekaCobWebMethod.class.equals(methodClass))
			return cobwebPage;
		else if(WekaHCLMethod.class.equals(methodClass))
			return hclPage;
		else if(WekaKmeansMethod.class.equals(methodClass))
			return kmeansPage;
		return null;
	}

	public ClusteringData getClusterData() {
		int attr = optionsPage.getDataAttribute();
		IMatrixView mv = heatmap.getMatrixView();
		return optionsPage.isValuesFromRows() ?
			new MatrixRowClusteringData(mv, attr)
			: new MatrixColumnClusteringData(mv, attr);
	}

	//FIXME Esto tiene pinta de que debería ir o en pageLeft o en Page:updateModel
	public AbstractClusteringValueMethod getMethod() {
		method.setPreprocess(optionsPage.isPreprocessing());
		method.setTranspose(optionsPage.isValuesFromRows());

		return method;
	}

	public boolean isHeaderEnabled() {
		return optionsPage.isHeaderSelected();
	}

	public boolean isSortData() {
		return optionsPage.isSort();
	}

	public boolean isTranspose() {
		return optionsPage.isValuesFromRows();
	}

	public HeatmapColoredLabelsHeader getHeader() {
		return header;
	}
	
	public SaveFilePage getSaveFilePage() {
		return newickPage;
	}
}
