package org.gitools.ui.actions;

import org.gitools.ui.actions.table.MtcAction;

import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.Bonferroni;

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
