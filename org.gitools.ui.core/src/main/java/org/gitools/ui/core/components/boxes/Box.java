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
package org.gitools.ui.core.components.boxes;

import com.alee.laf.scroll.WebScrollPane;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.core.actions.ActionSet;
import org.gitools.ui.core.actions.ActionSetUtils;
import org.gitools.ui.core.actions.BaseAction;
import org.gitools.ui.core.actions.SeparatorAction;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXTaskPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public abstract class Box extends JXTaskPane {

    protected ArrayList<JPanel> bottomPanels;
    protected JPopupMenu popupMenu;
    private Heatmap heatmap;

    private String id;
    protected final ActionSet contextActionSet;
    protected final ActionSet bottomActionSet;

    public Box(String id, String title, ActionSet contextActionSet, Heatmap heatmap) {
        this(id, title, contextActionSet, null, heatmap);
    }

    public Box(String id, String title, ActionSet contextActionSet, ActionSet bottomActionSet, Heatmap heatmap) {

        this.id = id;
        this.contextActionSet = contextActionSet;
        this.bottomActionSet = bottomActionSet;
        setTitle(title);
        setSpecial(true);

        this.heatmap = heatmap;

        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(new WebScrollPane(getContainer(), false, false));

        if (contextActionSet != null) {
            popupMenu = ActionSetUtils.createPopupMenu(contextActionSet);
        }

        createBottomActionButtons(bottomActionSet);
    }

    private void createBottomActionButtons(ActionSet bottomActionSet) {
        bottomPanels = new ArrayList<>();
        if (bottomActionSet != null) {
            JPanel iconActionsPanel = new JPanel(new HorizontalLayout(2), true);
            iconActionsPanel.setBackground(Color.white);
            bottomPanels.add(iconActionsPanel);
            addBaseActions(bottomActionSet, iconActionsPanel);
        }
    }

    private void addBaseActions(ActionSet bottomActionSet, JPanel iconActionsPanel) {
        for (BaseAction action : bottomActionSet.getActions()) {
            if (action instanceof SeparatorAction) {
                continue;
            } else if (action instanceof ActionSet) {
                addBaseActions(((ActionSet) action), iconActionsPanel);
            }
            JComponent btn = ActionSetUtils.createActionButton(action);
            btn.addMouseListener(getBottomActionMouseAdapter());
            iconActionsPanel.add(btn);
        }
    }

    abstract Container getContainer();

    abstract void initContainer();

    public abstract void registerListeners();

    public abstract void update();

    public abstract boolean isVisible();

    public Heatmap getHeatmap() {
        return heatmap;
    }

    public String getId() {
        return id;
    }

    public abstract MouseListener getBottomActionMouseAdapter();
}
