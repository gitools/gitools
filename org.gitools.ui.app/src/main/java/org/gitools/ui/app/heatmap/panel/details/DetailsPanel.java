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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import org.gitools.heatmap.Heatmap;
import org.gitools.resource.Resource;
import org.gitools.ui.app.heatmap.panel.details.boxes.Box;
import org.gitools.ui.app.heatmap.panel.details.boxes.DetailsBox;
import org.gitools.ui.app.heatmap.panel.details.boxes.DimensionBox;
import org.gitools.ui.app.heatmap.panel.details.boxes.LayersBox;
import org.gitools.ui.app.heatmap.popupmenus.PopupMenuActions;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.plaf.LookAndFeelAddons;
import org.jdesktop.swingx.plaf.metal.MetalLookAndFeelAddons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A details panel with three collapsible panels
 */
public class DetailsPanel extends JXTaskPaneContainer {

    private DetailsBox columnsBox;
    private DetailsBox rowsBox;
    private DetailsBox layersBox;
    private JLabel hintLabel;
    private JLabel titleLabel;
    private List<Box> boxes;


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

        titleLabel = new JLabel();
        PresentationModel<Heatmap> model = new PresentationModel<>(heatmap);
        Bindings.bind(titleLabel, model.getModel(Resource.PROPERTY_TITLE));
        Font f = titleLabel.getFont();
        titleLabel.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        add(titleLabel);
        add(new JSeparator());

        boxes = new ArrayList<>();


        add(columnsBox = new DimensionBox("Column", PopupMenuActions.DETAILS_COLUMNS, heatmap, heatmap.getColumns()));
        columnsBox.setCollapsed(true);
        columnsBox.registerListeners();
        boxes.add(columnsBox);

        add(rowsBox = new DimensionBox("Row", PopupMenuActions.DETAILS_ROWS, heatmap, heatmap.getRows()));
        rowsBox.setCollapsed(true);
        rowsBox.registerListeners();
        boxes.add(rowsBox);

        add(layersBox = new LayersBox("Values", PopupMenuActions.DETAILS_LAYERS, heatmap));
        layersBox.registerListeners();
        boxes.add(layersBox);

        hintLabel = new JLabel();
        hintLabel.setText("<html><body><p><i>Right click</i> on any layer or header id to<br>" +
                "<b>adjust visualization and other settings</b>.</p></body></html>");
        add(hintLabel);

        for (Box b : boxes) {
            b.update();
        }

    }

}
