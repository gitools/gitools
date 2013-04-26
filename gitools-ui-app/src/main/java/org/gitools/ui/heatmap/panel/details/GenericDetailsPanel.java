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
package org.gitools.ui.heatmap.panel.details;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.ui.heatmap.panel.details.boxes.Cell;
import org.gitools.ui.heatmap.panel.details.boxes.PropertiesBox;
import org.gitools.ui.heatmap.panel.details.boxes.PropertyItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A Generic details panel with a properties table of all the cell values,
 * the row, the column and the matrix size.
 */
public class GenericDetailsPanel extends AbstractDetailsPanel {
    /**
     * Instantiates a new Generic details panel.
     *
     * @param heatmap the heatmap
     */
    public GenericDetailsPanel(Heatmap heatmap) {
        super(heatmap);
    }

    @Override
    protected void updateBoxes() {
        List<PropertyItem> properties = new ArrayList<PropertyItem>();

        Cell cell = getSelectedCell();

        properties.add(cell.getColumn());
        properties.add(cell.getRow());
        properties.add(new PropertyItem("Size", "Matrix size: Columns x Rows", String.valueOf(getColumnsCount()) + " x " + String.valueOf(getRowsCount())));
        properties.add(new PropertyItem("", ""));
        properties.addAll(cell.getValues());

        Dimension size = getSize();
        add(new PropertiesBox(size.width, properties) {
            @Override
            protected void onMouseClick(PropertyItem propertyItem) {
                getHeatmap().getLayers().setTopLayerIndex(propertyItem.getIndex());
            }
        });

    }


}
