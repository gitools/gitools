/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.actions;

import java.awt.Window;
import org.gitools.ui.platform.actions.UnimplementedAction;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.actions.analysis.MtcAction;

import org.gitools.stats.mtc.BenjaminiHochbergFdr;
import org.gitools.stats.mtc.Bonferroni;
import org.gitools.ui.actions.analysis.CombinationsAction;
import org.gitools.ui.actions.analysis.CorrelationsAction;
import org.gitools.ui.platform.AppFrame;

public class AnalysisActions {

	public static final BaseAction combinations = new CombinationsAction();
	
	public static final BaseAction correlations = new CorrelationsAction();
	
	public static final BaseAction mtcBonferroniAction = new MtcAction(new Bonferroni());
	
	public static final BaseAction mtcBenjaminiHochbergFdrAction = new MtcAction(new BenjaminiHochbergFdr());
	
	public static final BaseAction mtcBenjaminiYekutieliFdrAction = new UnimplementedAction("Benjamini & Yekutieli FDR", false) {
		@Override protected Window getParent() {
			return AppFrame.instance();
		}
	};

}
