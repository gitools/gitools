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
package org.gitools.ui.app.heatmap.panel.details.boxes;

import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapDimensionAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.gitools.ui.core.interaction.Interaction.highlighting;
import static org.gitools.ui.core.interaction.Interaction.none;
import static org.gitools.ui.core.interaction.InteractionStatus.setInteractionStatus;


public class DimensionHeaderHighlightAction extends HeatmapAction implements IHeatmapDimensionAction {


    private static ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private HeatmapDimension dimension;
    private HeatmapHeader header;
    private ScheduledFuture<?> updating = null;

    public DimensionHeaderHighlightAction(HeatmapDimension dimension, HeatmapHeader header) {
        super("<html><i>Highlight</i> heatmap header</html>");
        this.dimension = dimension;
        this.header = header;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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
                if (step < 1) {
                    step = 1;
                }

                HashSet<String> highlighted = new HashSet<>();
                highlighted.add(header.getTitle());
                dimension.setHighlightedHeaders(highlighted);

                for (int i = 0; i < maxAlpha; i = i + step) {
                    try {
                        setInteractionStatus(highlighting);
                        Thread.currentThread().sleep(100);
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


                dimension.setHighlightedHeaders(new HashSet<String>());
            }
        };
        updating = EXECUTOR.schedule(runnable, 10, TimeUnit.MILLISECONDS);


    }

    private void reset() {
        getHeatmap().getRows().resetHighlightColor();
        getHeatmap().getColumns().resetHighlightColor();
        setInteractionStatus(none);
    }

    @Override
    public void onConfigure(HeatmapDimension object, HeatmapPosition position) {
        dimension = object;
    }
}
