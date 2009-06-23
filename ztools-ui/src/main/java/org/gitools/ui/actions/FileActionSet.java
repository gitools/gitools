package org.gitools.ui.actions;

import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportAction;
import org.gitools.ui.actions.file.ExportMatrixFigureHtmlAction;
import org.gitools.ui.actions.file.ExportMatrixFigurePictureAction;
import org.gitools.ui.actions.file.ExportRowColumnNames;
import org.gitools.ui.actions.file.ExportTableAllParametersAction;
import org.gitools.ui.actions.file.ExportTableOneParameterAction;
import org.gitools.ui.actions.file.NewEnrichmentAnalysisAction;
import org.gitools.ui.actions.file.OpenAnalysisAction;
import org.gitools.ui.actions.file.OpenMatrixAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	public static final BaseAction exitAction = new ExitAction();

	public static final BaseAction newEnrichmentAnalysisAction = new NewEnrichmentAnalysisAction();
	
	public static final ActionSet newActionSet = new ActionSet("New", new BaseAction[] {
			newEnrichmentAnalysisAction
	});
	
	public static final BaseAction openAnalysisAction = new OpenAnalysisAction();
	public static final BaseAction openMatrixAction = new OpenMatrixAction();
	
	public static final ActionSet openActionSet = new ActionSet("Open", new BaseAction[] {
			openAnalysisAction,
			openMatrixAction
	});
	
	public static final BaseAction closeAction = new CloseAction();

	// export
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
			closeAction,
			BaseAction.separator,
			//exportWizardAction,
			exportActionSet,
			BaseAction.separator,
			exitAction
		});
	}
}
