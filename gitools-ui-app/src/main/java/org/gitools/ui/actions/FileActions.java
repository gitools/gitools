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
package org.gitools.ui.actions;

import org.gitools.ui.actions.file.*;
import org.gitools.ui.platform.actions.BaseAction;

public class FileActions
{

    // New
    public static final BaseAction newEnrichmentAnalysisAction = new NewEnrichmentAnalysisAction();
    public static final BaseAction newOncozAnalysisAction = new NewOncodriveAnalysisAction();
    public static final BaseAction newCombinationAnalysisAction = new NewCombinationAnalysisAction();
    public static final BaseAction newCorrelationAnalysisAction = new NewCorrelationAnalysisAction();
    public static final BaseAction newOverlapAnalysisAction = new NewOverlappingAnalysisAction();

    // Open
    public static final BaseAction openAction = new OpenAction();
    public static final BaseAction openGenomeSpaceAction = new OpenGenomeSpaceAction();

    // Save
    public static final BaseAction saveAction = new SaveAction();
    public static final BaseAction saveAsAction = new SaveAsAction();

    // Close
    public static final BaseAction closeAction = new CloseAction();

    // Exit
    public static final BaseAction exitAction = new ExitAction();

    // Import
    public static final BaseAction importIntogenTableAction = new ImportIntogenMatrixAction();
    public static final BaseAction importExcelMatrixAction = new ImportExcelMatrixAction();
    public static final BaseAction importIntogenOncomodulesAction = new ImportIntogenOncomodulesAction();
    public static final BaseAction importBioMartModulesAction = new ImportBiomartModulesAction();
    public static final BaseAction importBioMartTableAction = new ImportBiomartTableAction();
    public static final BaseAction importKeggModulesAction = new ImportKeggModulesAction();
    public static final BaseAction importGoModulesAction = new ImportGoModulesAction();

    // Export
    public static final BaseAction exportLabelNamesAction = new ExportHeatmapLabelsAction();
    public static final BaseAction exportMatrixAction = new ExportMatrixAction();
    public static final BaseAction exportTableAction = new ExportTableAction();
    public static final BaseAction exportHeatmapImageAction = new ExportHeatmapImageAction();
    public static final BaseAction exportHeatmapHtmlAction = new ExportHeatmapHtmlAction();
    public static final BaseAction exportScaleImageAction = new ExportScaleImageAction();
    public static final BaseAction exportPdfReportAction = new ExportPdfReportAction();

    // Other
    public static final BaseAction openIntegrativeGenomicViewerAction = new OpenIntegrativeGenomicViewerAction();
}
