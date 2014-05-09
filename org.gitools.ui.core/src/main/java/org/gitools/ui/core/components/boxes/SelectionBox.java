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
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixIterable;
import org.gitools.heatmap.AbstractMatrixViewDimension;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapLayer;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.utils.aggregation.NonNullCountAggregator;
import org.gitools.utils.aggregation.StdDevAggregator;
import org.gitools.utils.formatter.ITextFormatter;
import org.gitools.utils.progressmonitor.NullProgressMonitor;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SelectionBox extends DetailsBox {


    private static ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> updating = null;

    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public SelectionBox(String title, ActionSet actions, Heatmap heatmap) {
        super(title, actions, heatmap);
    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration propertyItem) {

    }

    @Override
    public void registerListeners() {
        getHeatmap().getRows().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        getHeatmap().getColumns().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        getHeatmap().getLayers().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
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

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<DetailsDecoration> details = new ArrayList<>();
                Heatmap heatmap = getHeatmap();

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
                    details.add(new DetailsDecoration("Column sel.",
                            columns.size() + " columns"));
                }
                if (selectedRows > 0) {
                    details.add(new DetailsDecoration("Rows sel.",
                            rows.size() + " rows"));
                }


                HeatmapLayer layer = getHeatmap().getLayers().getTopLayer();
                IMatrix data = getHeatmap().getContents();

                //aggregator
                IMatrixIterable<Double> cellValues = getHeatmap().newPosition()
                        .iterate(layer, data.getRows().subset(rows), data.getColumns().subset(columns))
                        .monitor(new NullProgressMonitor(), "Aggregating values of layer '" + layer.getId() + "'");
                Double agg = layer.getAggregator().aggregate(cellValues);
                details.add(new DetailsDecoration(layer.getAggregator().toString(), valueString(agg, layer.getLongFormatter())));


                IMatrixIterable<Double> eventsIt = getHeatmap().newPosition()
                        .iterate(layer, data.getRows().subset(rows), data.getColumns().subset(columns))
                        .transform(layer.getDecorator().getEventFunction());
                Double events = NonNullCountAggregator.INSTANCE.aggregate(eventsIt);
                details.add(
                        new DetailsDecoration("Events",
                                layer.getDecorator().getEventFunction().toString(),
                                valueString(events, layer.getLongFormatter())));


                //Double var = VarianceAggregator.INSTANCE.aggregate(cellValues);
                Double stDev = StdDevAggregator.INSTANCE.aggregate(cellValues);
                //details.add(new DetailsDecoration("Variance", var.toString()));
                details.add(new DetailsDecoration("St. Dev", valueString(stDev, layer.getLongFormatter())));

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
