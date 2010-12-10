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

package org.gitools.ui.actions.file;

import java.awt.Window;
import java.awt.event.ActionEvent;
import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;

import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.actions.UnimplementedAction;

public class ImportIntogenHeatmapAction extends BaseAction {

	private static final long serialVersionUID = 668140963768246841L;

	public ImportIntogenHeatmapAction() {
		super("Heatmap figure ...");
		setLargeIconFromResource(IconNames.intogen24);
		setSmallIconFromResource(IconNames.intogen16);
		setDefaultEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new UnimplementedAction() {
			@Override protected Window getParent() {
				return AppFrame.instance(); }
		}.actionPerformed(e);
	}

}
