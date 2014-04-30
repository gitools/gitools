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
import org.gitools.ui.app.actions.edit.EditHeaderAction;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.ui.core.actions.ActionSet;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class DimensionBox extends DetailsBox {
    private final HeatmapDimension dimension;

    /**
     * @param title     Optional title of the details table
     * @param actions
     * @param dimension
     */
    public DimensionBox(String title, ActionSet actions, Heatmap heatmap, HeatmapDimension dimension) {
        super(title, actions, heatmap);
        this.dimension = dimension;
    }

    @Override
    public void registerListeners() {
        dimension.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
    }

    @Override
    public void update() {
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
    protected void onMouseDblClick(DetailsDecoration detail) {
        Object reference = detail.getReference();

        if (reference instanceof HeatmapHeader) {
            new EditHeaderAction((HeatmapHeader) reference).actionPerformed(null);
        }
    }
}
