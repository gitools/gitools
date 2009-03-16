package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.file.CloseAction;
import es.imim.bg.ztools.ui.actions.file.ExitAction;
import es.imim.bg.ztools.ui.actions.file.ExportColumnDataAction;
import es.imim.bg.ztools.ui.actions.file.ExportNames;
import es.imim.bg.ztools.ui.actions.file.ExportParameterDataAction;
import es.imim.bg.ztools.ui.actions.file.ExportTableDataAction;
import es.imim.bg.ztools.ui.actions.file.OpenAnalysisAction;
import es.imim.bg.ztools.ui.actions.file.NewZCalcAnalysisAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	public static final BaseAction exitAction = new ExitAction();

	public static final BaseAction openAnalysisAction = new OpenAnalysisAction();

	public static final BaseAction newZCalcAnalysisAction = new NewZCalcAnalysisAction();
	
	public static final BaseAction closeAction = new CloseAction();

	// export
	
	public static final BaseAction exportParameterDataAction = new ExportParameterDataAction();
	public static final BaseAction exportColumnDataAction = new ExportColumnDataAction();
	public static final BaseAction exportNamesAction = new ExportNames();
	public static final BaseAction exportTableData = new ExportTableDataAction();

	public static final ActionSet exportActionSet = new ActionSet("Export", new BaseAction[] {
			/*exportParameterDataAction,
			exportColumnDataAction,*/
			exportNamesAction,
			exportTableData
	});

	public FileActionSet() {
		super("File", new BaseAction[] {
			newZCalcAnalysisAction,
			openAnalysisAction,
			closeAction,
			BaseAction.separator,
			exportActionSet,
			BaseAction.separator,
			exitAction
		});
	}
}
