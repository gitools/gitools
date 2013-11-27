package org.gitools.ui.actions.edit;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.HeatmapDynamicActionSet;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class RowsActionSet extends HeatmapDynamicActionSet {

    public RowsActionSet() {
        super("Rows", KeyEvent.VK_R);
    }

    @Override
    protected void populateMenu(Heatmap heatmap, JMenu menu) {
        menu.removeAll();

        // Headers
        for (HeatmapHeader header : heatmap.getRows().getHeaders()) {
            menu.add(new EditHeaderAction(header));
        }

        // Filter
        menu.addSeparator();
        menu.add(Actions.filterByLabel);
        menu.add(Actions.filterByValue);

        // Sort
        menu.addSeparator();
        menu.add(Actions.sortByRowsAnnotation);
        menu.add(Actions.sortByValue);
        menu.add(Actions.sortByMutualExclusion);

        // Visibility
        menu.addSeparator();
        menu.add(Actions.showAllRowsAction);
        menu.add(updateEnable(Actions.hideSelectedRowsAction));

        // New header
        menu.addSeparator();
        menu.add(Actions.addRowHeader);
    }
}
