/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.actions.data;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * @noinspection ALL
 */
public class HideSelectionAction extends BaseAction {

    private static final long serialVersionUID = 1453040322414160605L;

    public enum ElementType {
        ROWS, COLUMNS
    }

    private final ElementType type;

    public HideSelectionAction(@NotNull ElementType type) {
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
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMatrixView matrixView = ActionUtils.getMatrixView();

        if (matrixView == null) {
            return;
        }

        String msg = "";

        switch (type) {
            case ROWS:
                msg = "Selected rows hidden.";
                matrixView.getRows().hide(matrixView.getRows().getSelected());
            /*matrixView.setVisibleRows(arrayRemove(
                    matrixView.getVisibleRows(),
					matrixView.getSelectedRows()));*/
                break;
            case COLUMNS:
                msg = "Selected columns hidden.";
                matrixView.getColumns().hide(matrixView.getColumns().getSelected());
            /*matrixView.setVisibleColumns(arrayRemove(
                    matrixView.getVisibleColumns(),
					matrixView.getSelectedColumns()));*/
                break;
        }

        AppFrame.get().setStatusText(msg);
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
