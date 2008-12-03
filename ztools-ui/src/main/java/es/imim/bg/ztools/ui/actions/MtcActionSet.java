package es.imim.bg.ztools.ui.actions;

import es.imim.bg.ztools.stats.mtc.BenjaminiHochbergFdr;
import es.imim.bg.ztools.stats.mtc.Bonferroni;
import es.imim.bg.ztools.ui.actions.table.MtcAction;

public class MtcActionSet extends ActionSet {

	private static final long serialVersionUID = 3899141800427183894L;
	
	public static final BaseAction mtcBonferroniAction = 
		new MtcAction(new Bonferroni());
	
	public static final BaseAction mtcBenjaminiHochbergFdrAction = 
		new MtcAction(new BenjaminiHochbergFdr());
	
	public MtcActionSet() {
		super("MTC", new BaseAction[] {
				mtcBonferroniAction,
				mtcBenjaminiHochbergFdrAction });
	}

}
