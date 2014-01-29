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

import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.app.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.app.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.Application;

import java.awt.event.ActionEvent;
import java.util.List;

public class MoveUpHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public MoveUpHeaderAction(String title, MatrixDimensionKey dim) {
        super(dim, title);

        setSmallIconFromResource(IconNames.add16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Move the header one position up
        HeatmapDimension dimension = getDimension();

        List<HeatmapHeader> headers = getDimension().getHeaders();

        int index = getHeaderCurrentIndex();
        headers.set(index, headers.get(index - 1));
        headers.set(index - 1, header);

        // Fire headers events
        getDimension().updateHeaders();

        Application.get().setStatusText("Move up header '" + header.getTitle() + "'");
    }

    private int getHeaderCurrentIndex() {
        HeatmapDimension dimension = getDimension();
        return dimension.getHeaders().indexOf(header);
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {

        this.header = object;

        setEnabled(header != null && getHeaderCurrentIndex() > 0);
    }
}
