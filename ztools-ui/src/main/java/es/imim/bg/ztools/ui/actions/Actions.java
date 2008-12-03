package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.stats.mtc.BenjaminiHochbergFdr;
import es.imim.bg.ztools.stats.mtc.Bonferroni;
import es.imim.bg.ztools.ui.actions.table.MtcAction;

public class Actions {
	
	public static final MenuActionSet menuActionSet = 
		new MenuActionSet();
	
	public static final ToolBarActionSet toolBarActionSet = 
		new ToolBarActionSet();
	
	// Unclassified
	
	public static final BaseAction zcalcAnalysisAction =
		new ZcalcAnalysisAction();
	
	private Actions() {
	}

	/*public static void disableAll() {
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
	}*/
}
