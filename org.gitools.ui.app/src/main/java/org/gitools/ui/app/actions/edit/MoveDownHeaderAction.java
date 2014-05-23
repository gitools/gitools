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
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.actions.HeatmapDimensionAction;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;

import java.awt.event.ActionEvent;
import java.util.List;

public class MoveDownHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public MoveDownHeaderAction(String title, MatrixDimensionKey dim, String iconName) {
        super(dim, title);
        if (iconName != null) {
            setSmallIconFromResource(iconName);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Move the header one position up
        List<HeatmapHeader> headers = getDimension().getHeaders();
        int index = getHeaderCurrentIndex();
        headers.set(index, headers.get(index + 1));
        headers.set(index + 1, header);

        // Fire headers events
        getDimension().updateHeaders();

        Application.get().setStatusText("Move down header '" + header.getTitle() + "'");
    }

    private int getHeaderCurrentIndex() {
        HeatmapDimension dimension = getDimension();
        return dimension.getHeaders().indexOf(header);
    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {

        this.header = object;

        setEnabled(header != null && (getHeaderCurrentIndex() + 1) < getDimension().getHeaders().size());
    }
}
