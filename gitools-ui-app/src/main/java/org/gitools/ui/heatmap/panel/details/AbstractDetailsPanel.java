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

import com.alee.laf.panel.WebPanel;
import info.clearthought.layout.SingleFiledLayout;
import org.apache.commons.lang.StringUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.core.matrix.model.IMatrixLayer;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.model.decorator.Decorator;
import org.gitools.core.model.decorator.impl.CategoricalDecorator;
import org.gitools.ui.heatmap.panel.details.boxes.Cell;
import org.gitools.ui.heatmap.panel.details.boxes.PropertyItem;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.colorscale.impl.CategoricalColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * An abstract details panel of a {@link Heatmap}.
 */
public abstract class AbstractDetailsPanel extends WebPanel implements PropertyChangeListener, ComponentListener {

    private static final GenericFormatter FORMATTER = new GenericFormatter();
    public static final String NONE = "None";
    @NotNull
    private final Heatmap heatmap;

    /**
     * Instantiates a new Abstract details panel.
     *
     * @param heatmap the heatmap
     */
    AbstractDetailsPanel(@NotNull final Heatmap heatmap) {
        super();
        this.heatmap = heatmap;

        setBackground(Color.WHITE);
        setLayout(new SingleFiledLayout(SingleFiledLayout.COLUMN, SingleFiledLayout.CENTER, 5));

        // Changes to track
        heatmap.getRows().addPropertyChangeListener(HeatmapDimension.PROPERTY_SELECTION_LEAD, this);
        heatmap.getColumns().addPropertyChangeListener(HeatmapDimension.PROPERTY_SELECTION_LEAD, this);
        heatmap.getLayers().addPropertyChangeListener(HeatmapLayers.PROPERTY_TOP_LAYER, this);
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(HeatmapLayer.PROPERTY_DECORATOR, this);
        addComponentListener(this);

        updateBoxes();
    }

    private void updateDetailsPanel() {
        removeAll();
        updateBoxes();
    }

    /**
     * This method is called when the heatmap properties had changed. The
     * implementing classes had to add all the boxes again.
     */
    protected abstract void updateBoxes();

    /**
     * Returns true if there is one cell selected
     *
     * @return
     */
    final boolean isCellSelected() {
        return (getHeatmap().getColumns().getSelectionLead() != -1 && getHeatmap().getRows().getSelectionLead() != -1);
    }

    /**
     * Gets total rows count of the matrix.
     *
     * @return the rows count
     */
    final int getRowsCount() {
        return getHeatmap().getRows().size();
    }

    /**
     * Gets total columns count of the matrix.
     *
     * @return the columns count
     */
    final int getColumnsCount() {
        return getHeatmap().getColumns().size();
    }

    /**
     * Gets the current selected cell.
     *
     * @return the selected cell
     */
    @Nullable
    final Cell getSelectedCell() {

        int row = getHeatmap().getRows().getSelectionLead();
        String rowIdentifier = (row == -1 ? NONE : getHeatmap().getRows().getLabel(row));

        int column = getHeatmap().getColumns().getSelectionLead();
        String columnIdentifier = (column == -1 ? NONE : getHeatmap().getColumns().getLabel(column));

        return new Cell(
                new PropertyItem("Row [" + (row + 1) + "]", null, rowIdentifier, getLink(rowIdentifier, getHeatmap().getRows())),
                new PropertyItem("Column [" + (column + 1) + "]", null, columnIdentifier, getLink(columnIdentifier, getHeatmap().getColumns())),
                getProperties(row, column)
        );
    }

    @Nullable
    protected Heatmap getHeatmap() {
        return heatmap;
    }

    @NotNull
    @Deprecated
    private Collection<PropertyItem> getProperties(int row, int column) {
        int selectedIndex = getHeatmap().getLayers().getTopLayerIndex();
        Decorator selectedDecorator = getHeatmap().getLayers().getTopLayer().getDecorator();
        List<PropertyItem> values = new ArrayList<PropertyItem>();
        final IMatrixLayers properties = getHeatmap().getLayers();
        int topLayer = getHeatmap().getLayers().getTopLayerIndex();
        for (int index = 0; index < properties.size(); index++) {
            final IMatrixLayer prop = properties.get(index);
            Object value = null;
            if (row != -1 && column != -1) {
                value = getHeatmap().getValue(row, column, index);
            }
            PropertyItem item = new PropertyItem(prop.getName(), prop.getDescription(), FORMATTER.format(value));
            item.setSelectable(true);
            item.setIndex(index);

            if (index == topLayer) {
                item.setSelected(true);
            }

            if (heatmap.getLayers().get(index).getDecorator() instanceof CategoricalDecorator &&
                    value instanceof Double) {
                CategoricalColorScale scale = (CategoricalColorScale) heatmap.getLayers().get(index).getDecorator().getScale();
                Double v = (Double) value;
                ColorScalePoint point = scale.getColorScalePoint(v);
                if (point != null) {
                    String name = StringUtils.isEmpty(point.getName()) ? FORMATTER.format(value) : point.getName();
                    item = new PropertyItem(prop.getName(), prop.getDescription(), name);
                }
            }


            if (index == selectedIndex && value instanceof Double) {
                item.setColor(selectedDecorator.getScale().valueColor((Double) value));
            }


            values.add(item);
        }

        return values;
    }

    @Nullable
    private String getLink(String value, @NotNull HeatmapDimension dim) {
        //TODO
        return null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        updateDetailsPanel();
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void componentResized(ComponentEvent e) {
        updateDetailsPanel();
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        updateDetailsPanel();
    }
}


