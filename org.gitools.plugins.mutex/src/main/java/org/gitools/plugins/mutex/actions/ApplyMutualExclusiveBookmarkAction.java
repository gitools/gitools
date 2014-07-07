/*
 * #%L
 * org.gitools.mutex
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
package org.gitools.plugins.mutex.actions;

import org.gitools.heatmap.Heatmap;
import org.gitools.plugins.mutex.MutualExclusiveBookmark;
import org.gitools.plugins.mutex.ui.IMutualExclusiveAction;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;


public class ApplyMutualExclusiveBookmarkAction extends HeatmapAction implements IMutualExclusiveAction {

    MutualExclusiveBookmark bookmark;

    public ApplyMutualExclusiveBookmarkAction() {
        super("<html><i>Apply</i> mutual exclusive order (top)</html>");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Heatmap heatmap = getHeatmap();

        List<String> rows = new ArrayList<>(bookmark.getRows());
        for (String r : heatmap.getRows().toList()) {
            if (!rows.contains(r)) {
                rows.add(r);
            }
        }
        heatmap.getRows().show(rows);

        List<String> columns = new ArrayList<>(bookmark.getColumns());
        for (String c : heatmap.getColumns()) {
            if (!columns.contains(c)) {
                columns.add(c);
            }
        }
        heatmap.getColumns().show(columns);

        heatmap.getLayers().setTopLayer(
                heatmap.getLayers().get(bookmark.getLayerId()));

    }

    @Override
    public void onConfigure(MutualExclusiveBookmark object, HeatmapPosition position) {
        bookmark = object;
    }
}
