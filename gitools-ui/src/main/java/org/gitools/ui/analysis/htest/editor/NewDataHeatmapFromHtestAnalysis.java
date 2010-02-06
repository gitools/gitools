/*
 *  Copyright 2010 chris.
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

package org.gitools.ui.analysis.htest.editor;

import java.awt.event.ActionEvent;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;

public class NewDataHeatmapFromHtestAnalysis extends BaseAction {

	public NewDataHeatmapFromHtestAnalysis() {
		super("New heatmap from data");

		setDesc("New heatmap from data");
		setSmallIconFromResource(IconNames.newDataHeatmap16);
		setLargeIconFromResource(IconNames.newDataHeatmap24);

		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
