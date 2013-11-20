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
package org.gitools.ui.actions.edit;

import org.gitools.core.matrix.model.MatrixDimensionKey;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.HeatmapDimensionAction;
import org.gitools.ui.heatmap.panel.settings.headers.HeadersEditPanel;

import java.awt.event.ActionEvent;

public class EditHeaderAction extends HeatmapDimensionAction {

    public EditHeaderAction(MatrixDimensionKey dim) {
        super(dim, "Edit " + dim.getLabel() + " headers");
        setSmallIconFromResource(IconNames.edit16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        HeadersEditPanel dialog = new HeadersEditPanel(getHeatmap(), getDimension());
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
    }

}
