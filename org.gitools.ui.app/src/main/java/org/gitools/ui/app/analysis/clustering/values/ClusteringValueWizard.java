/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.app.analysis.clustering.values;

import org.gitools.analysis.clustering.ClusteringData;
import org.gitools.analysis.clustering.ClusteringMethod;
import org.gitools.analysis.clustering.ClusteringMethodDescriptor;
import org.gitools.analysis.clustering.method.value.AbstractClusteringValueMethod;
import org.gitools.analysis.clustering.method.value.HierarchicalMethod;
import org.gitools.analysis.clustering.method.value.MatrixClusteringData;
import org.gitools.analysis.clustering.method.value.WekaCobWebMethod;
import org.gitools.analysis.clustering.method.value.WekaKmeansMethod;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.wizard.common.SaveFilePage;

public class ClusteringValueWizard extends AbstractWizard {

    private final Heatmap heatmap;

    private AbstractClusteringValueMethod method;

    private ClusteringMethodsPage methodPage;
    private CobwebParamsPage cobwebPage;
    private HCLParamsPage hclPage;
    private KmeansParamsPage kmeansPage;
    private ClusteringOptionsPage optionsPage;
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

        IMatrixView mv = heatmap;
        optionsPage = new ClusteringOptionsPage(mv.getContents().getLayers(), mv.getLayers().getTopLayer());
        addPage(optionsPage);

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
        return currentPage == cobwebPage || currentPage == hclPage || currentPage == kmeansPage;
    }

    @Override
    public boolean isLastPage(IWizardPage page) {
        return currentPage == cobwebPage || currentPage == hclPage || currentPage == kmeansPage;
    }


    @Override
    public IWizardPage getNextPage(IWizardPage currentPage) {

        IWizardPage nextPage = null;

        if (currentPage == optionsPage) {
            if (optionsPage.isNewickExportSelected()) {
                nextPage = newickPage;
            } else {
                nextPage = getMethodConfigPage();
            }
        } else if (currentPage == cobwebPage || currentPage == hclPage || currentPage == kmeansPage) {
            nextPage = null;
        } else {
            nextPage = super.getNextPage(currentPage);
        }

        return nextPage;
    }

    @Override
    public void pageLeft(IWizardPage currentPage) {
        if (currentPage == methodPage) {
            ClusteringMethodDescriptor methodDescriptor = methodPage.getMethodDescriptor();
            Class<? extends ClusteringMethod> methodClass = methodDescriptor.getMethodClass();
            optionsPage.setNewickExportVisible(HierarchicalMethod.class.equals(methodClass));
        } else if (currentPage == cobwebPage || currentPage == hclPage || currentPage == kmeansPage) {
            method = ((ClusteringValueMethodPage) currentPage).getMethod();
            method.setPreprocess(optionsPage.isPreprocessing());
            method.setTranspose(optionsPage.isApplyToRows());
        }
    }


    private IWizardPage getMethodConfigPage() {
        ClusteringMethodDescriptor methodDescriptor = methodPage.getMethodDescriptor();
        Class<? extends ClusteringMethod> methodClass = methodDescriptor.getMethodClass();

        if (WekaCobWebMethod.class.equals(methodClass)) {
            return cobwebPage;
        } else if (HierarchicalMethod.class.equals(methodClass)) {
            return hclPage;
        } else if (WekaKmeansMethod.class.equals(methodClass)) {
            return kmeansPage;
        }
        return null;
    }


    public ClusteringData getClusterData() {
        int attr = optionsPage.getDataAttribute();
        IMatrixView mv = heatmap;
        IMatrixLayer layer = heatmap.getLayers().get(attr);
        return optionsPage.isApplyToRows() ? new MatrixClusteringData(mv, mv.getRows(), mv.getColumns(), layer) : new MatrixClusteringData(mv, mv.getColumns(), mv.getRows(), layer);
    }

    public int getDataAttribute() {
        return optionsPage.getDataAttribute();
    }

    public boolean isHeaderSelected() {
        return optionsPage.isHeaderSelected();
    }

    public boolean isSortDataSelected() {
        return optionsPage.isSort();
    }

    public boolean isNewickExportSelected() {
        return optionsPage.isNewickExportSelected();
    }

    public boolean isApplyToRows() {
        return optionsPage.isApplyToRows();
    }

    public SaveFilePage getSaveFilePage() {
        return newickPage;
    }

    public String getMethodName() {
        return methodPage.getMethodDescriptor().getTitle();
    }

    public AbstractClusteringValueMethod getMethod() {
        return method;
    }
}
