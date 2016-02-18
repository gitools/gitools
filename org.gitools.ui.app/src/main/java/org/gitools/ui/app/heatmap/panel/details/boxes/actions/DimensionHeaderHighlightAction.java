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
package org.gitools.ui.app.heatmap.panel.details.boxes.actions;

import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.ui.core.Application;
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
    private static ScheduledFuture<?> updating = null;
    private static Highlighter highlighter = null;

    public DimensionHeaderHighlightAction(HeatmapDimension dimension, HeatmapHeader header) {
        super("<html><i>Highlight</i> heatmap header</html>");
        this.dimension = dimension;
        this.header = header;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (highlighter == null) {
            highlighter = new Highlighter(dimension, header);
        } else {
            highlighter.switchTarget(dimension, header);
        }

        if (header.isVisible()) {

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    while (highlighter.getAlpha() > 0) {
                        try {

                            setInteractionStatus(highlighting);
                            Thread.currentThread().sleep(100);
                            highlighter.highlight();

                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }

                    highlighter.resetDimension();
                    highlighter = null;
                    setInteractionStatus(none);

                }
            };
            updating = EXECUTOR.schedule(runnable, 10, TimeUnit.MILLISECONDS);
        } else {
            // header is not visible
            Application.get().showNotification("Header '" + header.getTitle() + "' is hidden.", 2000);
        }


    }


    @Override
    public void onConfigure(HeatmapDimension object, HeatmapPosition position) {
        dimension = object;
    }


    static class Highlighter {
        static private HeatmapDimension dimension;
        static private Color color;
        final int maxAlpha;
        static int alpha;
        static int step;
        static String header;

        Highlighter(HeatmapDimension dimension, HeatmapHeader header) {

            switchTarget(dimension, header);

            this.color = dimension.getHighlightingColor();

            maxAlpha = color.getAlpha();

            alpha = maxAlpha;
            step = maxAlpha / 16;
            if (step < 1) {
                step = 1;
            }


        }

        private void highlight() {
            Color c = new Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    alpha);
            alpha -= step;
            dimension.setHighlightingColor(c);

        }

        public int getAlpha() {
            return alpha;
        }

        public void switchTarget(HeatmapDimension dimension, HeatmapHeader target) {

            header = target.getTitle();

            if (this.dimension == null) {
                this.dimension = dimension;
            } else if (!this.dimension.equals(dimension)) {
                resetDimension();
                this.dimension = dimension;
            }
            resetAlpha();

            HashSet<String> highlighted = new HashSet<>();
            highlighted.add(target.getTitle());
            this.dimension.setHighlightedHeaders(highlighted);
        }

        public void resetAlpha() {
            alpha = maxAlpha;
        }

        private void resetDimension() {
            dimension.setHighlightedHeaders(new HashSet<String>());
            dimension.resetHighlightColor();
        }

    }
}
