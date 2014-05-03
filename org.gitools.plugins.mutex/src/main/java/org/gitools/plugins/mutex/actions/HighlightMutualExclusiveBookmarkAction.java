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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class HighlightMutualExclusiveBookmarkAction extends HeatmapAction implements IMutualExclusiveAction {

    MutualExclusiveBookmark bookmark;

    private static ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> updating = null;

    public HighlightMutualExclusiveBookmarkAction(MutualExclusiveBookmark bookmark) {
        super("<html><i>Apply</i> mutual exclusive order (top)</html>");
        this.bookmark = bookmark;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Heatmap heatmap = getHeatmap();

        final List<String> rows = new ArrayList<>(bookmark.getRows());


        final List<String> columns = new ArrayList<>(bookmark.getColumns());

        final Color color = getHeatmap().getRows().getHighlightingColor();

        if (updating != null && !updating.isDone()) {
            updating.cancel(true);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int step = 256 / 16;

                getHeatmap().getRows().setHighlightedLabels(new HashSet<String>(rows));
                getHeatmap().getColumns().setHighlightedLabels(new HashSet<String>(columns));

                for (int i = 0; i < 256; i = i + step) {
                    //try {
                    //Thread.currentThread().sleep(1);
                    Color c = new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                            255 - i);
                    //getHeatmap().getRows().setHighlightingColor(c);
                    getHeatmap().getColumns().setHighlightingColor(c);

                    // } catch (InterruptedException e1) {
                    // e1.printStackTrace();
                    // }
                }

                getHeatmap().getRows().setHighlightedLabels(new HashSet<String>());
                getHeatmap().getColumns().setHighlightedLabels(new HashSet<String>());
                getHeatmap().getRows().setHighlightingColor(color);
            }
        };
        updating = EXECUTOR.schedule(runnable, 10, TimeUnit.MILLISECONDS);


    }

    @Override
    public void onConfigure(MutualExclusiveBookmark object, HeatmapPosition position) {
        bookmark = object;
    }
}
