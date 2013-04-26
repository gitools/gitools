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
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.core.model.decorator.DetailsDecoration;
import org.gitools.ui.heatmap.panel.details.boxes.DetailsBox;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A details panel with three collapsible panels
 */
public class DetailsPanel extends JXTaskPaneContainer {

    @NotNull
    private final Heatmap heatmap;

    private DetailsBox columnsBox;
    private DetailsBox rowsBox;
    private DetailsBox layersBox;


    /**
     * Instantiates a new details panel.
     *
     * @param heatmap the heatmap
     */
    public DetailsPanel(Heatmap heatmap) {
        super();

        try {
            LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        this.heatmap = heatmap;

        setBackground(Color.WHITE);

        // Changes to track
        heatmap.getRows().addPropertyChangeListener(HeatmapDimension.PROPERTY_SELECTION_LEAD, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateRows();
                updateLayers();
            }
        });
        heatmap.getColumns().addPropertyChangeListener(HeatmapDimension.PROPERTY_SELECTION_LEAD, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateColumns();
                updateLayers();
            }
        });
        heatmap.getLayers().addPropertyChangeListener(HeatmapLayers.PROPERTY_TOP_LAYER, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateLayers();
            }
        });
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(HeatmapLayer.PROPERTY_DECORATOR, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateLayers();
            }
        });

        add(columnsBox = new DetailsBox("Columns"));
        columnsBox.setCollapsed(true);

        add(rowsBox = new DetailsBox("Rows"));
        rowsBox.setCollapsed(true);

        add(layersBox = new DetailsBox("Values") {
            @Override
            protected void onMouseClick(DetailsDecoration detail) {
                DetailsPanel.this.heatmap.getLayers().setTopLayerIndex(detail.getIndex());
            }
        });

        updateRows();
        updateColumns();
        updateLayers();
    }

    private void updateRows() {
        List<DetailsDecoration> rowsDetails = new ArrayList<DetailsDecoration>();
        heatmap.getRows().populateDetails(rowsDetails);
        rowsBox.draw(rowsDetails);
    }

    private void updateColumns() {
        List<DetailsDecoration> columnsDetails = new ArrayList<DetailsDecoration>();
        heatmap.getColumns().populateDetails(columnsDetails);
        columnsBox.draw(columnsDetails);
    }

    private void updateLayers() {
        List<DetailsDecoration> layersDetails = new ArrayList<DetailsDecoration>();
        heatmap.getLayers().populateDetails(layersDetails, heatmap, heatmap.getRows().getSelectionLead(), heatmap.getColumns().getSelectionLead());
        layersBox.draw(layersDetails);
    }





}
