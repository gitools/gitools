package es.imim.bg.ztools.ui;

import java.util.HashMap;
import java.util.Map;

import es.imim.bg.ztools.ui.actions.BaseAction;

public class ActionManager {

	public static final String openAnalysisAction = "openAnalysis";
	
	protected Map<String, BaseAction> actions;
	
	public ActionManager() {
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
