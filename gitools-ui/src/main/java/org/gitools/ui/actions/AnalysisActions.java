package org.gitools.ui.actions;

import java.awt.Window;
import org.gitools.ui.platform.actions.UnimplementedAction;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.analysis.MtcAction;

import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.Bonferroni;
import org.gitools.ui.actions.analysis.CombinationsAction;
import org.gitools.ui.actions.analysis.CorrelationsAction;
import org.gitools.ui.actions.analysis.HierarchicalClusteringAction;
import org.gitools.ui.platform.AppFrame;

public class AnalysisActions {

	public static final BaseAction combinations = new CombinationsAction();
	
	public static final BaseAction correlations = new CorrelationsAction();

	public static final BaseAction clusteringHierarchicalAction = new HierarchicalClusteringAction();
	
	public static final BaseAction mtcBonferroniAction = new MtcAction(new Bonferroni());
	
	public static final BaseAction mtcBenjaminiHochbergFdrAction = new MtcAction(new BenjaminiHochbergFdr());
	
	public static final BaseAction mtcBenjaminiYekutieliFdrAction = new UnimplementedAction("Benjamini & Yekutieli FDR", false) {
		@Override protected Window getParent() {
			return AppFrame.instance();
		}
	};

}
