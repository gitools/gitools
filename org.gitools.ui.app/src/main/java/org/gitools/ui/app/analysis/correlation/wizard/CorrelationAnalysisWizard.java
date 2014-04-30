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
package org.gitools.ui.app.analysis.correlation.wizard;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.resource.ResourceReference;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;

public class CorrelationAnalysisWizard extends AnalysisWizard<CorrelationAnalysis> {

    private final IMatrixView data;

    private CorrelationFromEditorPage corrPage;
    private AnalysisDetailsPage analysisDetailsPage;

    public CorrelationAnalysisWizard(IMatrixView data) {
        super();

        setTitle("Correlation analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CORRELATION, 96));

        this.data = data;
    }

    @Override
    public void addPages() {
        // Correlation method
        corrPage = new CorrelationFromEditorPage(data.getLayers());
        addPage(corrPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        analysisDetailsPage.setAnalysisTitle("Correlation");
        addPage(analysisDetailsPage);
    }

    @Override
    public CorrelationAnalysis createAnalysis() {

        CorrelationAnalysis analysis = new CorrelationAnalysis();
        analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
        analysis.setDescription(analysisDetailsPage.getAnalysisDescription());
        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());
        analysis.setAttributeIndex(corrPage.getAttributeIndex());
        analysis.setReplaceNanValue(corrPage.isReplaceNanValuesEnabled() ? corrPage.getReplaceNanValue() : null);
        analysis.setTransposeData(corrPage.isTransposeEnabled());
        analysis.setData(new ResourceReference<IMatrix>("data", data));

        return analysis;
    }
}
