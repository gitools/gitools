package org.gitools.ui.actions.edit;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.heatmap.HeatmapLayers;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.HeatmapDynamicActionSet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class LayersActionSet extends HeatmapDynamicActionSet {

    public LayersActionSet() {
        super("Layers", KeyEvent.VK_L);
    }

    @Override
    protected void populateMenu(Heatmap heatmap, JMenu menu) {
        menu.removeAll();

        HeatmapLayers layers = heatmap.getLayers();
        int i=1;
        for (HeatmapLayer layer : layers) {

            JMenuItem menuItem = new JMenuItem(new EditLayerAction(layer));
            if (layers.getTopLayer().equals(layer)) {
                menuItem.setFont(menuItem.getFont().deriveFont(Font.BOLD));
            }

            if (i < 10) {
                menuItem.setAccelerator(KeyStroke.getKeyStroke('0' + i, InputEvent.CTRL_MASK));
            }

            menu.add(menuItem);
            i++;
        }

        menu.addSeparator();
        menu.add(Actions.addLayerHeader);
    }
}
