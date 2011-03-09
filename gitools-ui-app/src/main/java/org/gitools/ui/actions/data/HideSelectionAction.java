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

package org.gitools.ui.actions.data;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.AppFrame;

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;

public class HideSelectionAction extends BaseAction {

	private static final long serialVersionUID = 1453040322414160605L;

	public enum ElementType {
		ROWS, COLUMNS
	}
	
	private ElementType type;
	
	public HideSelectionAction(ElementType type) {
		super(null);
		
		this.type = type;
		switch (type) {
		case ROWS:
			setName("Hide selected rows");
			setDesc("Hide selected rows");
			setSmallIconFromResource(IconNames.rowHide16);
			setLargeIconFromResource(IconNames.rowHide24);
			setMnemonic(KeyEvent.VK_W);
			break;
		case COLUMNS:
			setName("Hide selected columns");
			setDesc("Hide selected columns");
			setSmallIconFromResource(IconNames.columnHide16);
			setLargeIconFromResource(IconNames.columnHide24);
			setMnemonic(KeyEvent.VK_O);
			break;
		}
	}
	
	@Override
	public boolean isEnabledByModel(Object model) {
		return model instanceof Heatmap
			|| model instanceof IMatrixView;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		IMatrixView matrixView = ActionUtils.getMatrixView();
		
		if (matrixView == null)
			return;		
		
		String msg = "";
		
		switch (type) {
		case ROWS:
			msg = "Selected rows hidden.";
			matrixView.hideRows(matrixView.getSelectedRows());
			/*matrixView.setVisibleRows(arrayRemove(
					matrixView.getVisibleRows(), 
					matrixView.getSelectedRows()));*/
			break;
		case COLUMNS:
			msg = "Selected columns hidden.";
			matrixView.hideColumns(matrixView.getSelectedColumns());
			/*matrixView.setVisibleColumns(arrayRemove(
					matrixView.getVisibleColumns(), 
					matrixView.getSelectedColumns()));*/
			break;
		}
		
		AppFrame.instance().setStatusText(msg);
	}
	
	/*private int[] arrayRemove(int[] array, int[] indices) {
		int j = 0;
		int lastIndex = 0;
		int[] newIndices = new int[array.length - indices.length];
		
		Arrays.sort(indices);
		for (int i = 0; i < indices.length; i++) {
			int len = indices[i] - lastIndex;
			System.arraycopy(array, lastIndex, newIndices, j, len);
			lastIndex = indices[i] + 1;
			j += len;
		}
		System.arraycopy(array, lastIndex, newIndices, j, array.length - lastIndex);
		
		return newIndices;
	}*/
}
