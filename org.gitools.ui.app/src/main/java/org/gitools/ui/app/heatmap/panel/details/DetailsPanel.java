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
package org.gitools.ui.app.heatmap.panel.details;

import org.apache.commons.collections.map.ListOrderedMap;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.app.heatmap.panel.details.boxes.DimensionBox;
import org.gitools.ui.app.heatmap.panel.details.boxes.HeatmapInfoBox;
import org.gitools.ui.app.heatmap.panel.details.boxes.LayerValuesBox;
import org.gitools.ui.app.heatmap.popupmenus.PopupMenuActions;
import org.gitools.ui.core.components.boxes.Box;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.ui.core.plugins.IBoxPlugin;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A details panel with three collapsible panels
 */
public class DetailsPanel extends JXTaskPaneContainer {

    private DetailsBox columnsBox;
    private DetailsBox rowsBox;
    private DetailsBox layersBox;
    private DetailsBox infoBox;
    private JLabel hintLabel;
    private JLabel titleLabel;
    private Map<String, Box> boxes;


    /**
     * Instantiates a new details panel.
     *
     * @param heatmap the heatmap
     */
    public DetailsPanel(final Heatmap heatmap) {
        super();

        try {
            LookAndFeelAddons.setAddon(MetalLookAndFeelAddons.class);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        setBackground(Color.WHITE);

        boxes = new ListOrderedMap();

        infoBox = new HeatmapInfoBox(heatmap);
        infoBox.update();
        registerBox(infoBox);

        //add(new JSeparator());

        layersBox = new LayerValuesBox("Values", PopupMenuActions.DETAILS_LAYERS, heatmap);
        registerBox(layersBox);

        columnsBox = new DimensionBox("Column", PopupMenuActions.DETAILS_COLUMNS, heatmap, heatmap.getColumns());
        columnsBox.setCollapsed(true);
        registerBox(columnsBox);

        rowsBox = new DimensionBox("Row", PopupMenuActions.DETAILS_ROWS, heatmap, heatmap.getRows());
        rowsBox.setCollapsed(true);
        registerBox(rowsBox);

        initPluginBoxes(heatmap);

        for (Box b : boxes.values()) {
            add(b);
            b.update();
        }

        hintLabel = new JLabel();
        hintLabel.setText("<html><body><p><i>Right click</i> on any layer or header id to<br>" +
                "<b>adjust visualization and other settings</b>.</p></body></html>");
        add(hintLabel);

    }

    private void initPluginBoxes(Heatmap heatmap) {

        List<IBoxPlugin> boxPlugins = heatmap.getPluggedBoxes().filter(IBoxPlugin.class);
        for (IBoxPlugin p : boxPlugins) {
            if (p.isEnabled()) {
                for (Box b : p.getBoxes(heatmap)) {
                    registerBox(b);
                }
            }
        }
    }

    public void registerBox(Box... newBoxes) {
        for (Box box : newBoxes) {
            box.registerListeners();
            boxes.put(box.getTitle(), box);
        }
    }

    public Collection<Box> getBoxes() {
        return boxes.values();
    }
}
