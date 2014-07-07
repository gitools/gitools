/*
 * #%L
 * org.gitools.ui.app
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

import org.apache.commons.lang.StringUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader;
import org.gitools.ui.app.actions.edit.AddHeaderAction;
import org.gitools.ui.app.actions.edit.AnnotationAction;
import org.gitools.ui.app.actions.edit.EditHeaderAction;
import org.gitools.ui.app.heatmap.panel.details.boxes.actions.DimensionHeaderHighlightAction;
import org.gitools.ui.app.heatmap.panel.details.boxes.actions.SelectHeaderAction;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.dynamicactions.DynamicActionsManager;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.core.components.boxes.DetailsBox;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.gitools.heatmap.HeatmapDimension.*;
import static org.gitools.heatmap.header.HierarchicalClusterHeatmapHeader.PROPERTY_INTERACTION_LEVEL;
import static org.gitools.ui.core.interaction.Interaction.highlighting;
import static org.gitools.ui.core.interaction.Interaction.movingSelected;
import static org.gitools.ui.core.interaction.InteractionStatus.isInteracting;
import static org.gitools.utils.events.EventUtils.isAny;

public class DimensionBox extends DetailsBox {
    private final HeatmapDimension dimension;
    private MouseAdapter bottomActionMouseAdapter;

    /**
     * @param title     Optional title of the details table
     * @param actions
     * @param dimension
     */
    public DimensionBox(String title, ActionSet actions, Heatmap heatmap, HeatmapDimension dimension) {
        super(dimension.getId().name(),
                title,
                actions,
                new ActionSet(new AnnotationAction(dimension.getId()), new AddHeaderAction(dimension.getId()), actions),
                heatmap);
        this.dimension = dimension;
    }

    @Override
    public void registerListeners() {
        dimension.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ((isAny(evt, HeatmapDimension.class,
                        PROPERTY_FOCUS,
                        PROPERTY_HEADERS,
                        PROPERTY_SELECTED,
                        PROPERTY_SELECTED_HEADER,
                        PROPERTY_VISIBLE)) ||
                        isAny(evt, HierarchicalClusterHeatmapHeader.class,
                                PROPERTY_INTERACTION_LEVEL)) {
                    update();
                }
            }
        });
    }

    @Override
    public void update() {
        update(false);
    }

    private void update(boolean force) {

        if (!force && isInteracting(movingSelected, highlighting)) {
            return;
        }

        String lead = dimension.getFocus();
        String label = StringUtils.capitalize(dimension.getId().getLabel());

        if (lead != null) {
            this.setTitle(label + ": " + lead + " [" + (dimension.indexOf(lead) + 1) + "]");
        } else {
            this.setTitle(label);
        }
        List<DetailsDecoration> details = new ArrayList<>();
        dimension.populateDetails(details);
        this.draw(details);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration propertyItem) {
        Object reference = propertyItem.getReference();
        if (reference instanceof HeatmapHeader) {
            HeatmapHeader header = (HeatmapHeader) reference;
            dimension.setSelectedHeader(header);
            new SelectHeaderAction(dimension, header).actionPerformed(null);
            new DimensionHeaderHighlightAction(dimension, header).actionPerformed(null);
            update(true);
        }
    }

    @Override
    protected void onMouseDoubleClick(DetailsDecoration detail) {
        Object reference = detail.getReference();

        if (reference instanceof HeatmapHeader) {
            new EditHeaderAction((HeatmapHeader) reference).actionPerformed(null);
        }
    }

    @Override
    protected void onMouseRightClick(DetailsDecoration propertyItem, MouseEvent e) {
        if (propertyItem.getReference() instanceof HeatmapHeader) {
            DynamicActionsManager.updatePopupMenu(popupMenu, IHeatmapHeaderAction.class, (HeatmapHeader) propertyItem.getReference(), null);
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    @Override
    public MouseListener getBottomActionMouseAdapter() {
        if (bottomActionMouseAdapter == null) {
            bottomActionMouseAdapter = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    DynamicActionsManager.updateDynamicActionSet(bottomActionSet, IHeatmapHeaderAction.class, dimension.getSelectedHeader(), null);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    DynamicActionsManager.updateDynamicActionSet(bottomActionSet, IHeatmapHeaderAction.class, dimension.getSelectedHeader(), null);
                }

            };
        }
        return bottomActionMouseAdapter;
    }
}
