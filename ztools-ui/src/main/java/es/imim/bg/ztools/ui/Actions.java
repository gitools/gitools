package es.imim.bg.ztools.ui;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.actions.HideSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.HideSelectedRowsAction;
import es.imim.bg.ztools.ui.actions.InvertSelectionAction;
import es.imim.bg.ztools.ui.actions.OpenAnalysisAction;
import es.imim.bg.ztools.ui.actions.SelectAllAction;
import es.imim.bg.ztools.ui.actions.SortSelectedColumnsAction;
import es.imim.bg.ztools.ui.actions.UnselectAllAction;

public class Actions {

	public static final BaseAction openAnalysisAction = 
		new OpenAnalysisAction();
	
	public static final BaseAction selectAllAction = 
		new SelectAllAction();
	
	public static final BaseAction invertSelectionAction = 
		new InvertSelectionAction();
	
	public static final BaseAction unselectAllAction = 
		new UnselectAllAction();
	
	public static final BaseAction hideSelectedColumnsAction = 
		new HideSelectedColumnsAction();
	
	public static final BaseAction sortSelectedColumnsAction = 
		new SortSelectedColumnsAction();

	public static final BaseAction hideSelectedRowsAction = 
		new HideSelectedRowsAction();
	
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
