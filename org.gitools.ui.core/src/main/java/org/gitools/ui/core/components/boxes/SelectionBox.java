/*
 * #%L
 * org.gitools.ui.app
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
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.AbstractMatrixViewDimension;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.decorator.JComponentDetailsDecoration;
import org.gitools.heatmap.decorator.impl.CategoricalDecorator;
import org.gitools.heatmap.decorator.impl.NonEventToNullFunction;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.utils.aggregation.NonNullCountAggregator;
import org.gitools.utils.aggregation.StdDevAggregator;
import org.gitools.utils.colorscale.ColorScalePoint;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SelectionBox extends DetailsBox {


    public static final String ID = "SELECTION";
    private static ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> updating = null;
    private static JumpToNextEventAction nextRowEventAction = new JumpToNextEventAction(MatrixDimensionKey.ROWS);
    private static JumpToNextEventAction nextColumnsEventAction = new JumpToNextEventAction(MatrixDimensionKey.COLUMNS);


    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public SelectionBox(String title, ActionSet actions, Heatmap heatmap) {
        super(ID, title, actions, new ActionSet(nextRowEventAction, nextColumnsEventAction), heatmap);
        nextRowEventAction.setHeatmap(heatmap);
        nextColumnsEventAction.setHeatmap(heatmap);

    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration detail) {
        if (detail.getName().equals("Events")) {
            nextRowEventAction.actionPerformed(new ActionEvent(this, 1, ""));
        }
    }

    @Override
    public void registerListeners() {

        PropertyChangeListener listener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        };

        getHeatmap().getRows().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, listener);
        getHeatmap().getColumns().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, listener);
        getHeatmap().getLayers().addPropertyChangeListener(listener);
        getHeatmap().getLayers().getTopLayer().addPropertyChangeListener(HeatmapLayer.PROPERTY_EVENT_FUNCTION,listener);
    }

    @Override
    public void setCollapsed(boolean collapsed) {
        super.setCollapsed(collapsed);
        update();
    }

    @Override
    public void update() {

        this.setVisible(isVisible());

        if (!isVisible() || isCollapsed()) {
            return;
        }

        if (updating != null && !updating.isDone()) {
            updating.cancel(true);
            //return;
        }
        drawUpdating();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<DetailsDecoration> details = new ArrayList<>();
                Heatmap heatmap = getHeatmap();

                nextRowEventAction.reset();
                nextColumnsEventAction.reset();

                Set<String> rows = heatmap.getRows().getSelected();
                Set<String> columns = heatmap.getColumns().getSelected();
                if (rows.isEmpty()) {
                    rows = Sets.newHashSet(heatmap.getRows());
                }

                if (columns.isEmpty()) {
                    columns = Sets.newHashSet(heatmap.getColumns());
                }

                int selectedRows = rows.size();
                int selectedColumns = columns.size();
                if (selectedColumns > 0) {
                    details.add(new DetailsDecoration("Columns sel.",
                            columns.size() + " columns"));
                }
                if (selectedRows > 0) {
                    details.add(new DetailsDecoration("Rows sel.",
                            rows.size() + " rows"));
                }


                HeatmapLayer layer = getHeatmap().getLayers().getTopLayer();
                IMatrix data = getHeatmap().getContents();

                IMatrixIterable<Double> cellValuesIterable = getHeatmap().newPosition()
                        .iterate(layer, data.getRows().subset(rows), data.getColumns().subset(columns))
                        .monitor(new NullProgressMonitor(), "Aggregating values of layer '" + layer.getId() + "'");


                //Layer events
                JComboBox eventsFunctionComboBox = new JComboBox();
                eventsFunctionComboBox.setModel(
                        new ComboBoxAdapter<>(
                                new SelectionInList<NonEventToNullFunction>(
                                        layer.getDecorator().getEventFunctionAlternatives(),
                                        new PropertyAdapter<>(layer, "eventFunction")
                                )
                        )
                );

                NonEventToNullFunction eventsFunction = layer.getEventFunction();
                IMatrixIterable<Double> eventsIt = getHeatmap().newPosition()
                        .iterate(layer, data.getRows().subset(rows), data.getColumns().subset(columns))
                        .transform(eventsFunction);
                Double events = NonNullCountAggregator.INSTANCE.aggregate(eventsIt);
                String eventsDetail = valueString(events, layer.getLongFormatter());


                if (events != null) {
                    Double freq = 100 * (double) events / ((double) selectedColumns * (double) selectedRows);
                    eventsDetail += " (" + valueString(freq, layer.getShortFormatter()) + "%)";
                }


                details.add(new JComponentDetailsDecoration(eventsFunctionComboBox, eventsFunction.getName(),
                                            eventsFunction.getDescription(), eventsDetail));

                Double stDev = StdDevAggregator.INSTANCE.aggregate(cellValuesIterable);

                if (layer.getDecorator() instanceof CategoricalDecorator) {

                    CategoricalDecorator decorator = (CategoricalDecorator) layer.getDecorator();
                    Map<Double, Integer> categoryCounts = new HashMap<>();
                    for (ColorScalePoint p : decorator.getCategories()) {
                        categoryCounts.put(p.getValue(), 0);
                    }
                    for (Double value : eventsIt) {
                        if (value != null) {
                            categoryCounts.put(value, categoryCounts.get(value) + 1);
                        }
                    }
                    for (ColorScalePoint p : decorator.getCategories()) {
                        double count = (double) categoryCounts.get(p.getValue());
                        if (count > 0) {
                            DetailsDecoration d = new DetailsDecoration(p.getName(), valueString(count, layer.getShortFormatter()));
                            d.setBgColor(p.getColor());
                            details.add(d);
                        }
                    }


                } else {
                    //default layer aggregator
                    Double layerAggregation = layer.getAggregator().aggregate(cellValuesIterable);
                    details.add(new DetailsDecoration(layer.getAggregator().toString(),
                            "Default data layer aggregator. Edit data layer to change",
                            valueString(layerAggregation, layer.getLongFormatter())));


                    details.add(new DetailsDecoration("St. Dev", valueString(stDev, layer.getLongFormatter())));
                    //details.add(new DetailsDecoration("Variance", var.toString()));
                }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        SelectionBox.this.draw(details);
                    }
                });
            }
        };
        updating = EXECUTOR.schedule(runnable, 50, TimeUnit.MILLISECONDS);

    }

    private String valueString(Double value, ITextFormatter longFormatter) {
        if (value == null) {
            return "None";
        }

        return longFormatter.format(value);
    }

    @Override
    public boolean isVisible() {
        return (getHeatmap().getRows().getSelected().size() > 0 ||
                getHeatmap().getColumns().getSelected().size() > 0);
    }

    @Override
    protected void onMouseDoubleClick(DetailsDecoration detail) {

    }

    @Override
    protected void onMouseRightClick(DetailsDecoration propertyItem, MouseEvent e) {

    }
}
