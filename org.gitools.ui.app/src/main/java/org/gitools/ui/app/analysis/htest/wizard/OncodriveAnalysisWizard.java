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
package org.gitools.ui.app.analysis.htest.wizard;

import org.gitools.analysis.htest.oncodrive.OncodriveAnalysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.geneset.GeneSet;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.app.analysis.wizard.DataFilterPage;
import org.gitools.ui.app.analysis.wizard.ModulesPage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;

import java.io.File;

public class OncodriveAnalysisWizard extends AnalysisWizard<OncodriveAnalysis> {

    private DataFilterPage dataFilterPage;
    private ModulesPage modulesPage;
    private StatisticalTestPage statisticalTestPage;
    private AnalysisDetailsPage analysisDetailsPage;
    private IMatrixView sourceData;

    public OncodriveAnalysisWizard(IMatrixView sourceData) {
        super();

        this.sourceData = sourceData;

        setTitle("Oncodrive analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_ONCODRIVE, 96));
        setHelpContext("analysis_oncodrive");
    }

    @Override
    public void addPages() {

        // Data filtering
        dataFilterPage = new DataFilterPage();
        addPage(dataFilterPage);

        // Modules
        modulesPage = new ModulesPage();
        modulesPage.setMinSize(0);
        modulesPage.setEmptyFileAllowed(true);
        modulesPage.setTitle("Select sets of columns to be analysed independently");
        modulesPage.setMessage(MessageStatus.INFO, "If no file is selected then all data columns will be analysed as one set");
        addPage(modulesPage);

        // Statistical test
        statisticalTestPage = new StatisticalTestPage();
        addPage(statisticalTestPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        analysisDetailsPage.setAnalysisTitle("Oncodrive");
        addPage(analysisDetailsPage);
    }

    @Override
    public OncodriveAnalysis createAnalysis() {
        OncodriveAnalysis analysis = new OncodriveAnalysis();

        // Details
        analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
        analysis.setDescription(analysisDetailsPage.getAnalysisDescription());
        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());

        // Source data
        analysis.setLayer(sourceData.getLayers().getTopLayer().getId());
        analysis.setData(new ResourceReference<IMatrix>("data", sourceData));

        // Module map data
        File file = modulesPage.getSelectedFile();
        IResourceFormat<IModuleMap> format = modulesPage.getFileResourceFormat();
        analysis.setModuleMap(new ResourceReference<>(new UrlResourceLocator(file), format));

        // Test parameters
        analysis.setTestConfig(statisticalTestPage.getTestConfig());
        analysis.setBinaryCutoffEnabled(dataFilterPage.isBinaryCutoffEnabled());
        analysis.setBinaryCutoffCmp(dataFilterPage.getBinaryCutoffCmp());
        analysis.setBinaryCutoffValue(dataFilterPage.getBinaryCutoffValue());
        analysis.setMinModuleSize(modulesPage.getMinSize());
        analysis.setMaxModuleSize(modulesPage.getMaxSize());

        // Multiple test correction
        analysis.setMtc(statisticalTestPage.getMtc());

        // Population
        File populationFile = dataFilterPage.getPopulationFile();
        if (populationFile != null) {
            analysis.setPopulation(new ResourceReference<>(new UrlResourceLocator(populationFile), GeneSet.class));
            analysis.setPopulationDefaultValue(dataFilterPage.getPopulationDefaultValue());
        }

        return analysis;
    }

}
