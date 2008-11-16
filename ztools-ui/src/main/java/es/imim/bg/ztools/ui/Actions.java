package es.imim.bg.ztools.ui;

import java.lang.reflect.Field;

import es.imim.bg.ztools.ui.actions.AboutAction;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.actions.ChangeSelectionModeAction;
import es.imim.bg.ztools.ui.actions.CloseAction;
import es.imim.bg.ztools.ui.actions.ExitAction;
import es.imim.bg.ztools.ui.actions.ExportColumnDataAction;
import es.imim.bg.ztools.ui.actions.ExportParameterDataAction;
import es.imim.bg.ztools.ui.actions.ExportNames;
import es.imim.bg.ztools.ui.actions.HideSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.HideSelectedRowsAction;
import es.imim.bg.ztools.ui.actions.InvertSelectionAction;
import es.imim.bg.ztools.ui.actions.MoveColRowAction;
import es.imim.bg.ztools.ui.actions.MtcBonferroniAction;
import es.imim.bg.ztools.ui.actions.OpenAnalysisAction;
import es.imim.bg.ztools.ui.actions.SelectAllAction;
import es.imim.bg.ztools.ui.actions.SortSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.UnselectAllAction;
import es.imim.bg.ztools.ui.actions.ZcalcAnalysisAction;
import es.imim.bg.ztools.ui.actions.MoveColRowAction.MoveDirection;
import es.imim.bg.ztools.ui.model.SelectionMode;

public class Actions {

	// File
	
	public static final BaseAction openAnalysisAction = 
		new OpenAnalysisAction();
	
	public static final BaseAction closeAction =
		new CloseAction();
	
	public static final BaseAction exportParameterDataAction = 
		new ExportParameterDataAction();
	
	public static final BaseAction exportColumnDataAction = 
		new ExportColumnDataAction();
	
	public static final BaseAction exportNamesAction = 
		new ExportNames();
	
	public static final BaseAction exitAction =
		new ExitAction();
	
	// Edit
	
	public static final BaseAction selectAllAction = 
		new SelectAllAction();
	
	public static final BaseAction invertSelectionAction = 
		new InvertSelectionAction();
	
	public static final BaseAction unselectAllAction = 
		new UnselectAllAction();
	
	public static final BaseAction columnSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.columns);
	
	public static final BaseAction rowSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.rows);
	
	public static final BaseAction cellSelectionModeAction = 
		new ChangeSelectionModeAction(SelectionMode.cells);
	
	public static final BaseAction hideSelectedColumnsAction = 
		new HideSelectedColumnsAction();
	
	public static final BaseAction sortSelectedColumnsAction = 
		new SortSelectedColumnsAction();

	public static final BaseAction hideSelectedRowsAction = 
		new HideSelectedRowsAction();

	public static final BaseAction moveRowsUpAction =
		new MoveColRowAction(MoveDirection.ROW_UP);
	
	public static final BaseAction moveRowsDownAction =
		new MoveColRowAction(MoveDirection.ROW_DOWN);
	
	public static final BaseAction moveColsLeftAction =
		new MoveColRowAction(MoveDirection.COL_LEFT);
	
	public static final BaseAction moveColsRightAction =
		new MoveColRowAction(MoveDirection.COL_RIGHT);
	
	// Analysis
	
	public static final BaseAction zcalcAnalysisAction =
		new ZcalcAnalysisAction();
	
	public static final BaseAction mtcBonferroniAction =
		new MtcBonferroniAction();
	
	// Help
	
	public static final BaseAction aboutAction =
		new AboutAction();
	
	//protected Map<String, BaseAction> actions;
	
	private Actions() {
		//actions = new HashMap<String, BaseAction>();
	}
	
	/*public BaseAction put(String key, BaseAction action) {
		actions.put(key, action);
		return action;
	}
	
	public BaseAction get(String key) {
		return actions.get(key);
	}*/

	public static void disableAll() {
		for (Field field : Actions.class.getDeclaredFields()) {
			if (BaseAction.class.equals(field.getType())) {
				try {
					BaseAction action = (BaseAction) field.get(null);
					if (action != null)
						action.setEnabled(false);
				} catch (Exception e) {
				}				
			}
		}
	}
}
