package org.gitools.ui.actions;

import org.gitools.ui.actions.file.CloseAction;
import org.gitools.ui.actions.file.ExitAction;
import org.gitools.ui.actions.file.ExportAction;
import org.gitools.ui.actions.file.ExportColumnDataAction;
import org.gitools.ui.actions.file.ExportNames;
import org.gitools.ui.actions.file.ExportParameterDataAction;
import org.gitools.ui.actions.file.ExportTableAllParametersAction;
import org.gitools.ui.actions.file.ExportTableParameterAction;
import org.gitools.ui.actions.file.ExportTableToPdfAction;
import org.gitools.ui.actions.file.NewZCalcAnalysisAction;
import org.gitools.ui.actions.file.OpenAnalysisAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	public static final BaseAction exitAction = new ExitAction();

	public static final BaseAction openAnalysisAction = new OpenAnalysisAction();

	public static final BaseAction newZCalcAnalysisAction = new NewZCalcAnalysisAction();
	
	public static final BaseAction closeAction = new CloseAction();

	// export
	public static final BaseAction export = new ExportAction();
	
	public static final BaseAction exportParameterDataAction = new ExportParameterDataAction();
	public static final BaseAction exportColumnDataAction = new ExportColumnDataAction();
	public static final BaseAction exportNamesAction = new ExportNames();
	public static final BaseAction exportTableParameter = new ExportTableParameterAction();
	public static final BaseAction exportTableAllParameters = new ExportTableToPdfAction();
	public static final BaseAction exportTableToPdf = new ExportTableAllParametersAction();

	public static final ActionSet exportActionSet = new ActionSet("Export", new BaseAction[] {
			exportNamesAction,
			exportTableParameter,
			exportTableAllParameters,
			exportTableToPdf
	});

	public FileActionSet() {
		super("File", new BaseAction[] {
			newZCalcAnalysisAction,
			openAnalysisAction,
			closeAction,
			BaseAction.separator,
			export,
			//exportActionSet,
			BaseAction.separator,
			exitAction
		});
	}
}
