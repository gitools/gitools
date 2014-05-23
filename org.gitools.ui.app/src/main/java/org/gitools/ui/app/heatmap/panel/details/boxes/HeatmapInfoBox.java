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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.resource.Property;
import org.gitools.ui.app.actions.edit.EditHeaderAction;
import org.gitools.ui.app.actions.edit.HeatmapSettingsAction;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.dynamicactions.DynamicActionsManager;
import org.gitools.ui.core.actions.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.core.components.boxes.DetailsBox;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.capitalize;
import static org.gitools.heatmap.HeatmapDimension.PROPERTY_HEADERS;
import static org.gitools.heatmap.HeatmapDimension.PROPERTY_VISIBLE;
import static org.gitools.ui.app.heatmap.panel.HeatmapPanelInputProcessor.Mode.movingSelected;
import static org.gitools.ui.app.heatmap.panel.HeatmapPanelInputProcessor.getInteractionMode;
import static org.gitools.utils.events.EventUtils.isAny;


public class HeatmapInfoBox extends DetailsBox {

    public HeatmapInfoBox(Heatmap heatmap) {
        super(capitalize(heatmap.getTitle()), null, new ActionSet(new HeatmapSettingsAction()), heatmap);
    }

    @Override
    public void registerListeners() {

        PropertyChangeListener dimensionListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ((isAny(evt, HeatmapDimension.class,
                        PROPERTY_HEADERS,
                        PROPERTY_VISIBLE)) && (getInteractionMode() != movingSelected)) {
                    update();
                }
            }
        };

        getHeatmap().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        getHeatmap().getRows().addPropertyChangeListener(dimensionListener);
        getHeatmap().getColumns().addPropertyChangeListener(dimensionListener);
    }

    @Override
    public void update() {


        Heatmap heatmap = getHeatmap();
        this.setTitle(heatmap.getTitle());

        List<DetailsDecoration> details = new ArrayList<>();

        if (heatmap.getDescription() != null && heatmap.getDescription().length() > 0) {
            details.add(new DetailsDecoration("Description", heatmap.getDescription()));
        }

        int rows = heatmap.getRows().size();
        int visibleRows = heatmap.getContents().getRows().size();
        int columns = heatmap.getColumns().size();
        int visibleColumns = heatmap.getContents().getColumns().size();

        details.add(new DetailsDecoration("Size (all)", visibleRows + " x " + visibleColumns));

        if (rows != visibleRows || columns != visibleColumns) {
            details.add(new DetailsDecoration("Size (visible)", rows + " x " + columns));
        }

        for (Property property : heatmap.getProperties()) {
            details.add(new DetailsDecoration(property.getName(), property.getValue()));
        }

        this.draw(details);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration propertyItem) {
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
}
