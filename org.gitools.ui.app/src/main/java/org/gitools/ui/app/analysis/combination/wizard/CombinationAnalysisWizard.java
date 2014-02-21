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
package org.gitools.ui.app.analysis.combination.wizard;


import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.ResourceReference;
import org.gitools.matrix.FileFormats;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.analysis.htest.wizard.AnalysisWizard;
import org.gitools.ui.app.analysis.wizard.AnalysisDetailsPage;
import org.gitools.ui.app.analysis.wizard.SelectFilePage;
import org.gitools.ui.platform.IconUtils;
import org.gitools.ui.platform.dialog.MessageStatus;

import java.io.File;

public class CombinationAnalysisWizard extends AnalysisWizard<CombinationAnalysis> {

    private static final FileFormat[] columnSetsFormats = new FileFormat[]{FileFormats.GENE_MATRIX, FileFormats.GENE_MATRIX_TRANSPOSED, FileFormats.DOUBLE_MATRIX, FileFormats.DOUBLE_BINARY_MATRIX, FileFormats.MODULES_2C_MAP, FileFormats.MODULES_INDEXED_MAP};

    private CombinationAnalysisParamsPage combinationParamsPage;
    private SelectFilePage columnSetsPage;
    private AnalysisDetailsPage analysisDetailsPage;

    private IMatrixView data;

    public CombinationAnalysisWizard(IMatrixView data) {
        super();

        setTitle("Combination analysis");
        setLogo(IconUtils.getImageIconResourceScaledByHeight(IconNames.LOGO_COMBINATION, 96));
        setHelpContext("analysis_combination");

        this.data = data;
    }

    @Override
    public void addPages() {

        // Combination parameters
        combinationParamsPage = new CombinationAnalysisParamsPage(data.getLayers());
        addPage(combinationParamsPage);

        // Set of columns
        columnSetsPage = new SelectFilePage(columnSetsFormats);
        columnSetsPage.setTitle("Select sets of columns/rows to combine");
        columnSetsPage.setMessage(MessageStatus.INFO, "Leave blank to combine all the columns");
        columnSetsPage.setBlankFileAllowed(true);
        addPage(columnSetsPage);

        // Analysis details
        analysisDetailsPage = new AnalysisDetailsPage();
        addPage(analysisDetailsPage);
    }

    @Override
    public CombinationAnalysis createAnalysis() {

        CombinationAnalysis analysis = new CombinationAnalysis();
        analysis.setData(new ResourceReference<IMatrix>("data", data));
        analysis.setTitle(analysisDetailsPage.getAnalysisTitle());
        analysis.setDescription(analysisDetailsPage.getAnalysisDescription());
        analysis.setProperties(analysisDetailsPage.getAnalysisProperties());
        analysis.setSizeLayer(combinationParamsPage.getSizeLayerId());
        analysis.setValueLayer(combinationParamsPage.getPvalueLayerId());
        analysis.setTransposeData(combinationParamsPage.isTransposeEnabled());
        File columnSetsFile = columnSetsPage.getFile();
        String columnsPath = columnSetsFile != null ? columnSetsFile.getAbsolutePath() : null;
        IResourceFormat<IModuleMap> columnsFormat = columnSetsFile != null ? columnSetsPage.getFileFormat().getFormat(IModuleMap.class) : null;
        if (columnsPath != null) {
            ResourceReference<IModuleMap> columnsMap = new ResourceReference<>(new UrlResourceLocator(new File(columnsPath)), columnsFormat);
            analysis.setGroupsMap(columnsMap);
        }
        return analysis;

    }

}
