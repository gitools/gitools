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

package org.gitools.ui.actions.edit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;

public class UnselectAllAction extends BaseAction {

	private static final long serialVersionUID = 1581417292789818975L;

	public UnselectAllAction() {
		super("Unselect all");
		
		setDesc("Unselect all");
		setSmallIconFromResource(IconNames.unselectAll16);
		setLargeIconFromResource(IconNames.unselectAll24);
		setMnemonic(KeyEvent.VK_U);
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = ActionUtils.getMatrixView();
		
		if (matrixView != null)
			matrixView.clearSelection();
		
		AppFrame.instance()
			.setStatusText("Unselected all.");
	}

}
