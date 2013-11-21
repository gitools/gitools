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
package org.gitools.ui.analysis.correlation.wizard;

import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.ui.IconNames;
import org.gitools.ui.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.wizard.AbstractWizard;
import org.gitools.ui.platform.wizard.IWizardPage;

public class CorrelationAnalysisFromEditorWizard extends AbstractWizard {

    private final String[] attributeNames;

    private CorrelationFromEditorPage corrPage;
    private AnalysisDetailsPage analysisDetailsPage;

    public CorrelationAnalysisFromEditorWizard(String[] attributeNames) {
        super();

        setTitle("Correlation analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_CORRELATION, 96));

        this.attributeNames = attributeNames;
    }

    @Override
    public void addPages() {
        // Correlation method
        corrPage = new CorrelationFromEditorPage(attributeNames);
        addPage(corrPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }

    @Override
    public boolean canFinish() {
        boolean canFinish = super.canFinish();

        IWizardPage page = getCurrentPage();

        canFinish |= page.isComplete() && (page == corrPage);

        return canFinish;
    }


    public CorrelationAnalysis getAnalysis() {
        CorrelationAnalysis a = new CorrelationAnalysis();

        a.setTitle(analysisDetailsPage.getAnalysisTitle());
        a.setDescription(analysisDetailsPage.getAnalysisNotes());
        a.setProperties(analysisDetailsPage.getAnalysisAttributes());

        a.setAttributeIndex(corrPage.getAttributeIndex());
        a.setReplaceNanValue(corrPage.isReplaceNanValuesEnabled() ? corrPage.getReplaceNanValue() : null);
        a.setTransposeData(corrPage.isTransposeEnabled());

        return a;
    }
}
