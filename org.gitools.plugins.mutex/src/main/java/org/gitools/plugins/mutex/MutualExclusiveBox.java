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
package org.gitools.plugins.mutex;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.components.boxes.DetailsBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MutualExclusiveBox extends DetailsBox {


    private final MutualExclusivePlugin plugin;

    /**
     * @param title   Optional title of the details table
     * @param actions
     */
    public MutualExclusiveBox(String title, ActionSet actions, Heatmap heatmap) {
        super(title, actions, heatmap);
        this.plugin = (MutualExclusivePlugin) heatmap.getPluggedBoxes().get(MutualExclusivePlugin.NAME);
    }

    @Override
    public void registerListeners() {
        plugin.addPropertyChangeListener(new PropertyChangeListener() {
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

        final List<DetailsDecoration> details = new ArrayList<>();

        for (String s : plugin.getKeys()) {
            details.add(new DetailsDecoration(s, Double.toString(plugin.getResult(s).getTwoTailPvalue())));
        }

    }

    @Override
    public boolean isVisible() {
        return (plugin.isActive() && plugin.isNotEmpty());
    }

    @Override
    protected void onMouseDblClick(DetailsDecoration detail) {

    }
}

