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
        menu.add(Actions.filterRowsByValue);

        // Sort
        menu.addSeparator();
        menu.add(Actions.sortByRowsAnnotation);
        menu.add(Actions.sortRowsByValue);
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
