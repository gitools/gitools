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

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.decorator.DetailsDecoration;
import org.gitools.plugins.mutex.MutualExclusivePlugin;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.MouseEvent;
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
    public MutualExclusiveBox(String title, ActionSet actions, Heatmap heatmap, MutualExclusivePlugin plugin) {
        super(title, actions, heatmap);
        this.plugin = plugin;
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

        for (String key : plugin.getKeys()) {
            DetailsDecoration d = new DetailsDecoration(key, Double.toString(plugin.getResult(key).getTwoTailPvalue()));
            d.setReference(key);
            details.add(d);
        }

        MutualExclusiveBox.this.draw(details);
    }

    @Override
    public boolean isVisible() {
        return (plugin.isEnabled() && plugin.isNotEmpty());
    }

    @Override
    protected void onMouseDoubleClick(DetailsDecoration detail) {
        String key = (String) detail.getReference();
        MutualExclusiveResultPage page =
                new MutualExclusiveResultPage(getHeatmap(),
                        plugin.getResult(key),
                        plugin.getBookmark(key));
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);
    }

    @Override
    protected void onMouseRightClick(DetailsDecoration propertyItem, MouseEvent e) {

    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration propertyItem) {

    }
}

