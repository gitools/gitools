package org.gitools.ui.actions.edit;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.Actions;
import org.gitools.ui.actions.HeatmapDynamicActionSet;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static com.google.common.collect.Lists.reverse;

public class ColumnsActionSet extends HeatmapDynamicActionSet {

    public ColumnsActionSet() {
        super("Columns", KeyEvent.VK_C, IconNames.empty16);
    }

    @Override
    protected void populateMenu(Heatmap heatmap, JMenu menu) {
        menu.removeAll();

        for (HeatmapHeader header : reverse(heatmap.getColumns().getHeaders())) {
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
        menu.add(Actions.showAllColumns);
        menu.add(updateEnable(Actions.hideSelectedColumns));

        // New header
        menu.addSeparator();
        menu.add(Actions.addColumnHeader);
    }
}
