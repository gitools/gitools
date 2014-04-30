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
package org.gitools.plugins.mutex.ui;

import org.gitools.heatmap.AbstractMatrixViewDimension;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.utils.formatter.ITextFormatter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ScheduledFuture;

public class MutualExclusiveBox extends DetailsBox {

    private ScheduledFuture<?> updating = null;

    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public MutualExclusiveBox(String title, ActionSet actions, Heatmap heatmap) {
        super(title, actions, heatmap);
    }

    @Override
    public void registerListeners() {
        getHeatmap().getRows().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        getHeatmap().getColumns().addPropertyChangeListener(AbstractMatrixViewDimension.PROPERTY_SELECTED, new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
        getHeatmap().getLayers().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                update();
            }
        });
    }

    @Override
    public void setCollapsed(boolean collapsed) {
        super.setCollapsed(collapsed);
        update();
    }

    @Override
    public void update() {

        this.setVisible(isVisible());

        if (!isVisible() || isCollapsed()) {
            return;
        }

        if (updating != null && !updating.isDone()) {
            updating.cancel(true);
            //return;
        }

    }

    private String valueString(Double value, ITextFormatter longFormatter) {
        if (value == null) {
            return "None";
        }

        return longFormatter.format(value);
    }

    @Override
    public boolean isVisible() {
        return (getHeatmap().getRows().getSelected().size() > 0 ||
                getHeatmap().getColumns().getSelected().size() > 0);
    }

    @Override
    protected void onMouseDblClick(DetailsDecoration detail) {

    }
}
