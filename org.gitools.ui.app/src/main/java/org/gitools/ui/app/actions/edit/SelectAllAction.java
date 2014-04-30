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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SelectAllAction extends HeatmapAction {

    private static final long serialVersionUID = 3088237733885396229L;

    public SelectAllAction() {
        super("<html><i>Select</i> all</html>");

        setSmallIconFromResource(IconNames.selectAll16);
        setLargeIconFromResource(IconNames.selectAll24);
        setMnemonic(KeyEvent.VK_A);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IMatrixView matrixView = getHeatmap();

        if (matrixView != null) {
            matrixView.getRows().selectAll();
            matrixView.getColumns().selectAll();
        }

        Application.get().setStatusText("Selected all.");
    }

}
