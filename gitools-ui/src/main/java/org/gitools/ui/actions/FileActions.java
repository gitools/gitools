package org.gitools.ui.actions;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportAction;
import org.gitools.ui.actions.file.ExportHeatmapHtmlAction;
import org.gitools.ui.actions.file.ExportHeatmapImageAction;
import org.gitools.ui.actions.file.ExportLabelNamesAction;
import org.gitools.ui.actions.file.ExportTableAction;
import org.gitools.ui.actions.file.ExportMatrixAction;
import org.gitools.ui.actions.file.ExportPdfReportAction;
import org.gitools.ui.actions.file.ExportScaleImageAction;
import org.gitools.ui.actions.file.ImportBiomartMatrixAction;
import org.gitools.ui.actions.file.ImportBiomartTableAction;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportIntogenHeatmapAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.ImportIntogenMatrixAction;
import org.gitools.ui.actions.file.NewCombinationAnalysisAction;
import org.gitools.ui.actions.file.NewCorrelationAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewOncodriveAnalysisAction;
import org.gitools.ui.actions.file.NewProjectAction;
import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenHeatmapAction;
import org.gitools.ui.actions.file.OpenProjectAction;
import org.gitools.ui.actions.file.SaveAction;
import org.gitools.ui.actions.file.SaveAsAction;

public class FileActions {

	// New

	public static final BaseAction newProjectAction = new NewProjectAction();
	
	public static final BaseAction newEnrichmentAnalysisAction = new NewEnrichmentAnalysisAction();
	public static final BaseAction newOncozAnalysisAction = new NewOncodriveAnalysisAction();
	public static final BaseAction newCombinationAnalysisAction = new NewCombinationAnalysisAction();
	public static final BaseAction newCorrelationAnalysisAction = new NewCorrelationAnalysisAction();

	// Open

	public static final BaseAction openProjectAction = new OpenProjectAction();
	public static final BaseAction openEnrichmentAnalysisAction = new OpenEnrichmentAnalysisAction();
	public static final BaseAction openHeatmapFromMatrixAction = new OpenHeatmapAction();

	// Save

	public static final BaseAction saveAction = new SaveAction();
	public static final BaseAction saveAsAction = new SaveAsAction();

	// Close

	public static final BaseAction closeAction = new CloseAction();

	// Exit

	public static final BaseAction exitAction = new ExitAction();
	
	// Import
	
	public static final BaseAction importIntogenTableAction = new ImportIntogenMatrixAction();
	public static final BaseAction importIntogenOncomodulesAction = new ImportIntogenOncomodulesAction();
	public static final BaseAction importIntogenHeatmapAction = new ImportIntogenHeatmapAction();
	
	public static final BaseAction importBioMartModulesAction = new ImportBiomartModulesAction();
	public static final BaseAction importBioMartTableAction = new ImportBiomartTableAction();
	public static final BaseAction importBioMartMatrixAction = new ImportBiomartMatrixAction();
	
	// Export
	
	public static final BaseAction exportWizardAction = new ExportAction();
	
	public static final BaseAction exportLabelNamesAction = new ExportLabelNamesAction();
	public static final BaseAction exportMatrixAction = new ExportMatrixAction();
	public static final BaseAction exportTableAction = new ExportTableAction();
	public static final BaseAction exportHeatmapImageAction = new ExportHeatmapImageAction();
	public static final BaseAction exportHeatmapHtmlAction = new ExportHeatmapHtmlAction();
	public static final BaseAction exportScaleImageAction = new ExportScaleImageAction();
	public static final BaseAction exportPdfReportAction = new ExportPdfReportAction();
}
