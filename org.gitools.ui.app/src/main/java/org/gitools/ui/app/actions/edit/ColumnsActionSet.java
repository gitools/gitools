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
package org.gitools.ui.app.actions.edit;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.app.actions.Actions;
import org.gitools.ui.app.actions.HeatmapDynamicActionSet;
import org.gitools.ui.platform.icons.IconNames;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static com.google.common.collect.Lists.reverse;

public class ColumnsActionSet extends HeatmapDynamicActionSet {

    public ColumnsActionSet(String name) {
        super(name, KeyEvent.VK_C, IconNames.empty16);
    }

    @Override
    protected void populateMenu(Heatmap heatmap, JMenu menu) {
        menu.removeAll();

        for (HeatmapHeader header : reverse(heatmap.getColumns().getHeaders())) {
            menu.add(new EditHeaderAction(header));
        }

        // Filter
        menu.addSeparator();
        menu.add(Actions.filterColumnsByLabel);
        menu.add(Actions.filterColumnsByValue);

        // Sort
        menu.addSeparator();
        menu.add(Actions.sortByColumnsAnnotation);
        menu.add(Actions.sortColumnsByValue);
        menu.add(Actions.sortColumnsByMutualExclusion);
        menu.add(Actions.sortByPredefinedListColumnsAction);

        // Visibility
        menu.addSeparator();
        menu.add(Actions.showAllColumns);
        menu.add(updateEnable(Actions.hideSelectedColumns));

        // New header
        menu.addSeparator();
        menu.add(Actions.addColumnHeader);
    }
}
