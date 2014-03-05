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
package org.gitools.ui.app.analysis.overlapping.wizard;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.resource.ResourceReference;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.app.analysis.wizard.DataFilterPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.utils.cutoffcmp.CutoffCmp;

public class OverlappingAnalysisWizard extends AnalysisWizard<OverlappingAnalysis> {

    private DataFilterPage dataFilterPage;
    private OverlappingAnalysisWizardPage overlappingPage;
    private AnalysisDetailsPage analysisDetailsPage;

    private IMatrixView data;

    public OverlappingAnalysisWizard(IMatrixView data) {
        super();

        setTitle("Overlapping analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_OVERLAPPING, 96));
        setHelpContext("analysis_overlapping");

        this.data = data;
    }

    @Override
    public void addPages() {

        // Data filters
        dataFilterPage = new DataFilterPage();
        dataFilterPage.setRowsFilterFileVisible(false);
        dataFilterPage.setBinaryCutoffCmp(CutoffCmp.LT);
        dataFilterPage.setBinaryCutoffValue(0.05);
        dataFilterPage.setBinaryCutoffEnabled(true);
        addPage(dataFilterPage);

        // Overlapping parameters
        overlappingPage = new OverlappingAnalysisWizardPage();
        overlappingPage.setAttributes(data.getLayers());
        addPage(overlappingPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        analysisDetailsPage.setAnalysisTitle("Overlapping");
        addPage(analysisDetailsPage);
    }

    public OverlappingAnalysis createAnalysis() {

        OverlappingAnalysis analysis = new OverlappingAnalysis();
        analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
        analysis.setDescription(analysisDetailsPage.getAnalysisDescription());
        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());
        analysis.setBinaryCutoffEnabled(dataFilterPage.isBinaryCutoffEnabled());
        analysis.setBinaryCutoffCmp(dataFilterPage.getBinaryCutoffCmp());
        analysis.setBinaryCutoffValue(dataFilterPage.getBinaryCutoffValue());
        analysis.setAttributeName(overlappingPage.getAttributeName());
        analysis.setReplaceNanValue(overlappingPage.isReplaceNanValuesEnabled() ? overlappingPage.getReplaceNanValue() : null);
        analysis.setTransposeData(overlappingPage.isTransposeEnabled());
        analysis.setSourceData(new ResourceReference<IMatrix>("source-data", data));

        return analysis;
    }

}
