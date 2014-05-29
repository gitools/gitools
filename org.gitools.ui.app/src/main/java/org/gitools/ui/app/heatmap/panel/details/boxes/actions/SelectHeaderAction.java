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


public class SelectHeaderAction extends HeatmapAction implements IHeatmapDimensionAction {


    private HeatmapDimension dimension;
    private HeatmapHeader header;

    public SelectHeaderAction(HeatmapDimension dimension, HeatmapHeader header) {
        super("<html><i>Select</i> heatmap header</html>");
        this.dimension = dimension;
        this.header = header;
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                dimension.setSelectedHeader(header);
            }
        };


    }


    @Override
    public void onConfigure(HeatmapDimension object, HeatmapPosition position) {
        dimension = object;
    }

}
