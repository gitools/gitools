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
package org.gitools.ui.heatmap.editor;

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.HeatmapDimensionAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.gitools.api.matrix.MatrixDimensionKey.COLUMNS;

public class HeatmapSearchAction extends HeatmapDimensionAction {

    public HeatmapSearchAction(MatrixDimensionKey key) {
        super(key, "Search");

        setDesc("Search for a text in rows or columns");
        setSmallIconFromResource(IconNames.SEARCH16);
        setLargeIconFromResource(IconNames.SEARCH24);
        setMnemonic(KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        IEditor currentEditor = editorPanel.getSelectedEditor();

        if (!(currentEditor instanceof HeatmapEditor)) {
            return;
        }

        HeatmapEditor hmEditor = (HeatmapEditor) currentEditor;
        hmEditor.showSearch(getDimensionKey() == COLUMNS);
    }

}
