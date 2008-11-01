package es.imim.bg.ztools.ui;

import java.util.HashMap;
import java.util.Map;

import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.actions.ChangeSelectionModeAction;
import es.imim.bg.ztools.ui.actions.ExitAction;
import es.imim.bg.ztools.ui.actions.ExportColumnDataAction;
import es.imim.bg.ztools.ui.actions.ExportParameterDataAction;
import es.imim.bg.ztools.ui.actions.HideSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.HideSelectedRowsAction;
import es.imim.bg.ztools.ui.actions.InvertSelectionAction;
import es.imim.bg.ztools.ui.actions.OpenAnalysisAction;
import es.imim.bg.ztools.ui.actions.SelectAllAction;
import es.imim.bg.ztools.ui.actions.SortSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.UnselectAllAction;
import es.imim.bg.ztools.ui.model.SelectionMode;

public class Actions {

	public static final BaseAction openAnalysisAction = 
		new OpenAnalysisAction();
	
	public static final BaseAction exportParameterDataAction = 
		new ExportParameterDataAction();
	
	public static final BaseAction exportColumnDataAction = 
		new ExportColumnDataAction();
	
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

	public static final BaseAction exit =
		new ExitAction();
	
	protected Map<String, BaseAction> actions;
	
	public Actions() {
		actions = new HashMap<String, BaseAction>();
	}
	
	public BaseAction put(String key, BaseAction action) {
		actions.put(key, action);
		return action;
	}
	
	public BaseAction get(String key) {
		return actions.get(key);
	}
}
