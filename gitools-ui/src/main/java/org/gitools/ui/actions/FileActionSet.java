package org.gitools.ui.actions;

import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportAction;
import org.gitools.ui.actions.file.ExportHeatmapHtmlAction;
import org.gitools.ui.actions.file.ExportHeatmapImageAction;
import org.gitools.ui.actions.file.ExportLabelNamesAction;
import org.gitools.ui.actions.file.ExportTable;
import org.gitools.ui.actions.file.ExportMatrix;
import org.gitools.ui.actions.file.ImportBioMartTableAction;
import org.gitools.ui.actions.file.ImportBiomartModulesAction;
import org.gitools.ui.actions.file.ImportIntogenHeatmapAction;
import org.gitools.ui.actions.file.ImportIntogenOncomodulesAction;
import org.gitools.ui.actions.file.ImportIntogenTableAction;
import org.gitools.ui.actions.file.NewCombinationAnalysisAction;
import org.gitools.ui.actions.file.NewCorrelationAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisWizardAction;
import org.gitools.ui.actions.file.NewOncozAnalysisAction;
import org.gitools.ui.actions.file.NewProjectAction;
import org.gitools.ui.actions.file.OpenEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenDataAction;
import org.gitools.ui.actions.file.OpenHeatmapAction;
import org.gitools.ui.actions.file.OpenProjectAction;
import org.gitools.ui.actions.file.SaveAction;
import org.gitools.ui.actions.file.SaveAsAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	// File
	
	public static final BaseAction newProjectAction = new NewProjectAction();
	
	public static final BaseAction newEnrichmentAnalysisAction = new NewEnrichmentAnalysisAction();
	public static final BaseAction newEnrichmentAnalysisWizardAction = new NewEnrichmentAnalysisWizardAction();
	public static final BaseAction newOncozAnalysisAction = new NewOncozAnalysisAction();
	public static final BaseAction newCombinationAnalysisAction = new NewCombinationAnalysisAction();
	public static final BaseAction newCorrelationAnalysisAction = new NewCorrelationAnalysisAction();
	
	public static final ActionSet newActionSet = new ActionSet("New", new BaseAction[] {
			//newProjectAction,
			new ActionSet("Analysis", new BaseAction[] {
				newEnrichmentAnalysisAction,
				newEnrichmentAnalysisWizardAction,
				newOncozAnalysisAction,
				newCombinationAnalysisAction,
				newCorrelationAnalysisAction })
	});
	
	public static final BaseAction openProjectAction = new OpenProjectAction();
	public static final BaseAction openDataAction = new OpenDataAction();
	public static final BaseAction openAnalysisAction = new OpenEnrichmentAnalysisAction();
	public static final BaseAction openMatrixAction = new OpenHeatmapAction();
	
	public static final ActionSet openActionSet = new ActionSet("Open", new BaseAction[] {
			//openProjectAction,
			openDataAction,
			openAnalysisAction,
			openMatrixAction
	});
	
	public static final BaseAction saveAction = new SaveAction();
	public static final BaseAction saveAsAction = new SaveAsAction();
	
	public static final BaseAction closeAction = new CloseAction();

	public static final BaseAction exitAction = new ExitAction();
	
	// Import
	
	public static final BaseAction importIntogenTableAction = new ImportIntogenTableAction();
	public static final BaseAction importIntogenModulesAction = new ImportIntogenOncomodulesAction();
	public static final BaseAction importIntogenFigureAction = new ImportIntogenHeatmapAction();
	
	public static final BaseAction importBioMartModulesAction = new ImportBiomartModulesAction();
	public static final BaseAction importBioMartTableAction = new ImportBioMartTableAction();
	
	public static final ActionSet importActionSet = new ActionSet("Import", new BaseAction[] {
			new ActionSet("IntOGen", new BaseAction[] {
				importIntogenTableAction,
				importIntogenModulesAction,
				importIntogenFigureAction }),
			new ActionSet("BioMart", new BaseAction[] {
				importBioMartTableAction,
				importBioMartModulesAction })
	});
	
	// Export
	
	public static final BaseAction exportWizardAction = new ExportAction();
	
	public static final BaseAction exportRowColumnNamesAction = new ExportLabelNamesAction();
	public static final BaseAction exportTableParameter = new ExportMatrix();
	public static final BaseAction exportTableAllParameters = new ExportTable();
	public static final BaseAction exportMatrixFigurePicture = new ExportHeatmapImageAction();
	public static final BaseAction exportMatrixFigureHtml = new ExportHeatmapHtmlAction();

	public static final ActionSet exportActionSet = new ActionSet("Export", new BaseAction[] {
			exportRowColumnNamesAction,
			exportTableParameter,
			exportTableAllParameters,
			exportMatrixFigurePicture,
			exportMatrixFigureHtml
	});

	public FileActionSet() {
		super("File", new BaseAction[] {
			newActionSet,
			openActionSet,
			BaseAction.separator,
			saveAction,
			saveAsAction,
			BaseAction.separator,
			closeAction,
			BaseAction.separator,
			importActionSet,
			//exportWizardAction,
			exportActionSet,
			BaseAction.separator,
			exitAction
		});
		
		setDefaultEnabled(true);
	}
}
