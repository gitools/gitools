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
import org.gitools.plugins.mutex.MutualExclusiveBookmark;
import org.gitools.plugins.mutex.MutualExclusivePlugin;
import org.gitools.plugins.mutex.actions.*;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.BaseAction;
import org.gitools.ui.core.actions.dynamicactions.DynamicActionsManager;
import org.gitools.ui.core.components.boxes.DetailsBox;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class MutualExclusiveBox extends DetailsBox {


    private final MutualExclusivePlugin plugin;

    public static final ActionSet ACTIONS = new ActionSet(new BaseAction[]{
            new ViewMutualExclusiveResultsAction(),
            new ApplyMutualExclusiveBookmarkAction(),
            new SelectTestedMutualExclusiveAction(),
            BaseAction.separator,
            new RemoveMutualExclusiveAction()
    });


    public MutualExclusiveBox(String title, Heatmap heatmap, MutualExclusivePlugin plugin) {
        super(title, ACTIONS, heatmap);
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

        if (!isVisible() || isCollapsed()) {
            return;
        }

        final List<DetailsDecoration> details = new ArrayList<>();

        for (String key : plugin.getKeys()) {
            MutualExclusiveBookmark bookmark = plugin.getBookmark(key);
            DetailsDecoration d =
                    new DetailsDecoration(key + " p-value",
                            bookmark.getDescription(),
                            Double.toString(bookmark.getResult().getMutexPvalue()));
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
                        plugin.getBookmark(key));
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }
        plugin.forceUpdate();
    }

    @Override
    protected void onMouseRightClick(DetailsDecoration propertyItem, MouseEvent e) {
        DynamicActionsManager.updatePopupMenu(popupMenu, IMutualExclusiveAction.class,
                (MutualExclusiveBookmark) plugin.getBookmark((String) propertyItem.getReference()), null);
        popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    @Override
    protected void onMouseSingleClick(DetailsDecoration propertyItem) {
        MutualExclusiveBookmark bookmark = plugin.getBookmark((String) propertyItem.getReference());
        HighlightMutualExclusiveBookmarkAction action =
                new HighlightMutualExclusiveBookmarkAction(bookmark);
        action.actionPerformed(null);
    }
}

