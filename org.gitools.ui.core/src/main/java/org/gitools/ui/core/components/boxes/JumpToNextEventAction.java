/*
 * #%L
 * org.gitools.ui.core
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.core.components.boxes;

import com.google.common.collect.Sets;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.BaseAction;
import org.gitools.ui.platform.icons.IconNames;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;

public class JumpToNextEventAction extends BaseAction {

    private Heatmap heatmap;
    private Iterator eventIterator = null;
    private NonEventToNullFunction eventFunction = null;
    private int eventCount = 0;
    private MatrixDimensionKey dimensionKey;


    public JumpToNextEventAction(MatrixDimensionKey dimensionKey) {
        super("Jump to next event");
        this.dimensionKey = dimensionKey;
        this.heatmap = heatmap;
        setSmallIconFromResource(
                dimensionKey.equals(MatrixDimensionKey.ROWS) ?
                        IconNames.nextEventRight16 :
                        IconNames.nextEventDown16
        );

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        IMatrixPosition position = null;

        HeatmapDimension rowsDim = heatmap.getRows();
        HeatmapDimension columnsDim = heatmap.getColumns();
        HeatmapLayer layer = heatmap.getLayers().getTopLayer();

        int rowFocus = rowsDim.indexOf(rowsDim.getFocus());
        int colFocus = columnsDim.indexOf(columnsDim.getFocus());
        HeatmapPosition focusPosition = new HeatmapPosition(heatmap, rowFocus, colFocus);
        boolean continueFromFocus = false;
        continueFromFocus = focusInSelection(rowsDim, columnsDim);

        if (eventIterator == null) {
            createIterator(rowsDim, columnsDim, layer, focusPosition);
        }

        boolean found = false;

        while (!found) {
            if (eventIterator.hasNext()) {
                /*if (continueFromFocus) {
                    // do not set focus until iterator has passed the last set focus in selection
                    continueFromFocus =  !(position.get(rowsDim).equals(focusPosition.get(rowsDim))
                            && position.get(columnsDim).equals(focusPosition.get(columnsDim)));
                    position = null;
                } */

                if (eventIterator.next() != null) {
                    position = eventFunction.getPosition();
                    eventCount++;
                    found = true;
                }
            } else {
                found = true;
                reset();
                Application.get().showNotification("Last event reached", 3000);
            }
        }

        if (position != null) {
            heatmap.getRows().setFocus(position.get(heatmap.getRows()));
            heatmap.getColumns().setFocus(position.get(heatmap.getColumns()));
            Application.get().showNotification("Jumped to event " + eventCount, 1000);
        }
    }

    private boolean focusInSelection(HeatmapDimension rowsDim, HeatmapDimension columnsDim) {
        if (rowsDim.getSelected().size() == 0 || rowsDim.getSelected().contains(rowsDim.getFocus()) &&
                columnsDim.getSelected().size() == 0 || columnsDim.getSelected().contains(columnsDim.getFocus())) {
            return true;
        }
        return false;
    }

    private void createIterator(HeatmapDimension rowsDim, HeatmapDimension columnsDim, HeatmapLayer layer, HeatmapPosition eventPosition) {
        Set<String> rows = heatmap.getRows().getSelected();
        Set<String> columns = heatmap.getColumns().getSelected();
        if (rows.isEmpty()) {
            rows = Sets.newHashSet(heatmap.getRows());
        }

        if (columns.isEmpty()) {
            columns = Sets.newHashSet(heatmap.getColumns());
        }

        eventFunction = layer.getDecorator().getEventFunction();

        if (dimensionKey.equals(MatrixDimensionKey.ROWS)) {
            eventIterator =
                    eventPosition.iterate(layer, rowsDim.subset(rows), columnsDim.subset(columns))
                            .transform(eventFunction).iterator();
        } else {
            eventIterator =
                    eventPosition.iterate(layer, columnsDim.subset(columns), rowsDim.subset(rows))
                            .transform(eventFunction).iterator();
        }
    }

    public void setHeatmap(Heatmap heatmap) {
        this.heatmap = heatmap;
    }

    public void reset() {
        eventFunction = null;
        eventIterator = null;
        eventCount = 0;
    }
}
