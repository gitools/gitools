package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.ui.actions.file.CloseAction;
import es.imim.bg.ztools.ui.actions.file.ExitAction;
import es.imim.bg.ztools.ui.actions.file.ExportColumnDataAction;
import es.imim.bg.ztools.ui.actions.file.ExportNames;
import es.imim.bg.ztools.ui.actions.file.ExportParameterDataAction;
import es.imim.bg.ztools.ui.actions.file.OpenAnalysisAction;

public class FileActionSet extends ActionSet {

	private static final long serialVersionUID = 5912417630655786267L;
	
	public static final BaseAction exitAction = new ExitAction();

	public static final BaseAction openAnalysisAction = new OpenAnalysisAction();

	public static final BaseAction closeAction = new CloseAction();

	// export
	
	public static final BaseAction exportParameterDataAction = new ExportParameterDataAction();
	public static final BaseAction exportColumnDataAction = new ExportColumnDataAction();
	public static final BaseAction exportNamesAction = new ExportNames();

	public static final ActionSet exportActionSet = new ActionSet("export", new BaseAction[] {
			exportParameterDataAction,
			exportColumnDataAction,
			exportNamesAction
	});

	public FileActionSet() {
		super("File", new BaseAction[] {
			openAnalysisAction,
			closeAction,
			BaseAction.separator,
			exportActionSet,
			BaseAction.separator,
			exitAction
		});
	}
}
