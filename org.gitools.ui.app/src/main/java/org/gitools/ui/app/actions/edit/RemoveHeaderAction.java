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
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;

public class RemoveHeaderAction extends HeatmapDimensionAction implements IHeatmapHeaderAction {

    private HeatmapHeader header;

    public RemoveHeaderAction(MatrixDimensionKey dimensionKey, String name) {
        super(dimensionKey, name);
        setSmallIconFromResource(IconNames.remove16);
    }

    public RemoveHeaderAction(HeatmapHeader header) {
        super(header.getHeatmapDimension().getId(), header.getTitle());
        setSmallIconFromResource(IconNames.remove16);
        this.header = header;
    }

    public HeatmapHeader getHeader() {
        return header;
    }

    public void setHeader(HeatmapHeader header) {
        this.header = header;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        HeatmapDimension heatmapDimension = getDimension();
        heatmapDimension.getHeaders().remove(header);
        heatmapDimension.updateHeaders();

    }

    @Override
    public void onConfigure(HeatmapHeader object, HeatmapPosition position) {
        setHeader(object);
    }
}
