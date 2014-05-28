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

import static org.gitools.ui.core.interaction.Interaction.highlighting;
import static org.gitools.ui.core.interaction.Interaction.none;
import static org.gitools.ui.core.interaction.InteractionStatus.setInteractionStatus;

public class HighlightMutualExclusiveBookmarkAction extends HeatmapAction implements IMutualExclusiveAction {

    MutualExclusiveBookmark bookmark;

    private static ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> updating = null;

    public HighlightMutualExclusiveBookmarkAction(MutualExclusiveBookmark bookmark) {
        super("<html><i>Highlight</i> mutual exclusive</html>");
        this.bookmark = bookmark;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        setInteractionStatus(highlighting);

        final List<String> rows = new ArrayList<>(bookmark.getRows());

        final List<String> columns = new ArrayList<>(bookmark.getColumns());

        final Color color = getHeatmap().getRows().getHighlightingColor();

        if (updating != null && !updating.isDone()) {
            reset();
            updating.cancel(true);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {


                int maxAlpha = color.getAlpha();
                int step = maxAlpha / 16;

                getHeatmap().getRows().setHighlightedLabels(new HashSet<String>(rows));
                getHeatmap().getColumns().setHighlightedLabels(new HashSet<String>(columns));

                for (int i = 0; i < maxAlpha; i = i + step) {
                    try {
                        Thread.currentThread().sleep(50);
                        Color c = new Color(
                            color.getRed(),
                            color.getGreen(),
                            color.getBlue(),
                                maxAlpha - i);
                        getHeatmap().getRows().setHighlightingColor(c);
                        getHeatmap().getColumns().setHighlightingColor(c);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

                reset();

                setInteractionStatus(none);

            }
        };
        updating = EXECUTOR.schedule(runnable, 10, TimeUnit.MILLISECONDS);


    }

    private void reset() {
        getHeatmap().getRows().setHighlightedLabels(new HashSet<String>());
        getHeatmap().getColumns().setHighlightedLabels(new HashSet<String>());
        getHeatmap().getRows().resetHighlightColor();
    }

    @Override
    public void onConfigure(MutualExclusiveBookmark object, HeatmapPosition position) {
        bookmark = object;
    }
}
