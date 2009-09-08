package org.gitools.ui.actions;

import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportAction;
import org.gitools.ui.actions.file.ExportMatrixFigureHtmlAction;
import org.gitools.ui.actions.file.ExportMatrixFigurePictureAction;
import org.gitools.ui.actions.file.ExportRowColumnNames;
import org.gitools.ui.actions.file.ExportTableAllParametersAction;
import org.gitools.ui.actions.file.ExportTableOneParameterAction;
import org.gitools.ui.actions.file.ImportEnsemblModulesAction;
import org.gitools.ui.actions.file.ImportEnsemblTableAction;
import org.gitools.ui.actions.file.ImportIntogenFigureAction;
import org.gitools.ui.actions.file.ImportIntogenModulesAction;
import org.gitools.ui.actions.file.ImportIntogenTableAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisWizardAction;
import org.gitools.ui.actions.file.NewOncozAnalysisAction;
import org.gitools.ui.actions.file.OpenAnalysisAction;
import org.gitools.ui.actions.file.OpenMatrixAction;
import org.gitools.ui.actions.file.SaveMatrixFigureAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	// File
	
	public static final BaseAction newEnrichmentAnalysisAction = new NewEnrichmentAnalysisAction();
	public static final BaseAction newEnrichmentAnalysisWizardAction = new NewEnrichmentAnalysisWizardAction();
	public static final BaseAction newOncozAnalysisAction = new NewOncozAnalysisAction();
	
	public static final ActionSet newActionSet = new ActionSet("New", new BaseAction[] {
			newEnrichmentAnalysisAction,
			newEnrichmentAnalysisWizardAction,
			newOncozAnalysisAction
	});
	
	public static final BaseAction openAnalysisAction = new OpenAnalysisAction();
	public static final BaseAction openMatrixAction = new OpenMatrixAction();
	
	public static final ActionSet openActionSet = new ActionSet("Open", new BaseAction[] {
			openAnalysisAction,
			openMatrixAction
	});
	
	public static final BaseAction saveMatrixFigureAction = new SaveMatrixFigureAction();
	
	public static final ActionSet saveActionSet = new ActionSet("Save", new BaseAction[] {
			saveMatrixFigureAction
	});
	
	public static final BaseAction closeAction = new CloseAction();

	public static final BaseAction exitAction = new ExitAction();
	
	// Import
	
	public static final BaseAction importIntogenTable = new ImportIntogenTableAction();
	public static final BaseAction importIntogenModules = new ImportIntogenModulesAction();
	public static final BaseAction importIntogenFigure = new ImportIntogenFigureAction();
	
	public static final BaseAction importEnsemblTable = new ImportEnsemblTableAction();
	public static final BaseAction importEnsemblModules = new ImportEnsemblModulesAction();
	
	public static final ActionSet importActionSet = new ActionSet("Import", new BaseAction[] {
		importIntogenTable,
		importIntogenModules,
		importIntogenFigure,
		importEnsemblTable,
		importEnsemblModules
	});
	
	// Export
	
	public static final BaseAction exportWizardAction = new ExportAction();
	
	public static final BaseAction exportRowColumnNamesAction = new ExportRowColumnNames();
	public static final BaseAction exportTableParameter = new ExportTableOneParameterAction();
	public static final BaseAction exportTableAllParameters = new ExportTableAllParametersAction();
	public static final BaseAction exportMatrixFigurePicture = new ExportMatrixFigurePictureAction();
	public static final BaseAction exportMatrixFigureHtml = new ExportMatrixFigureHtmlAction();

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
			saveActionSet,
			closeAction,
			BaseAction.separator,
			importActionSet,
			//exportWizardAction,
			exportActionSet,
			BaseAction.separator,
			exitAction
		});
	}
}
