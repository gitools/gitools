/*
 * #%L
 * gitools-ui-platform
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.ui.platform.actions;

import com.alee.laf.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;

import javax.swing.*;
import java.util.List;

public class ActionSetUtils {

    public static JToolBar createToolBar(ActionSet actionSet) {
        WebToolBar toolBar = new WebToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(false);
        toolBar.setToolbarStyle(ToolbarStyle.attached);
        return createToolBar(toolBar, actionSet);
    }

    private static JToolBar createToolBar(JToolBar toolBar, ActionSet actionSet) {

        for (BaseAction a : actionSet.getActions()) {
            if (a instanceof SeparatorAction) {
                toolBar.addSeparator();
            } else {
                toolBar.add(createTool(a));
            }
        }

        return toolBar;
    }

    private static JComponent createTool(BaseAction a) {
        WebButton tool = WebButton.createIconWebButton(a.getSmallIcon(), StyleConstants.smallRound, true);
        tool.setAction(a);
        tool.setHideActionText(true);
        TooltipManager.setTooltip(tool, a.getDesc(), TooltipWay.down, 0);
        tool.setFocusable(false);
        tool.setRequestFocusEnabled(false);
        return tool;
    }


    public static JPopupMenu createPopupMenu(ActionSet actionSet) {
        JPopupMenu popupMenu = new JPopupMenu();

        for (BaseAction a : actionSet.getActions()) {
            if (a instanceof SeparatorAction) {
                popupMenu.addSeparator();
            } else {
                popupMenu.add(createPopupMenuItem(a));
            }
        }

        return popupMenu;

    }


    private static JMenuItem createPopupMenuItem(Action a) {
        JMenuItem item = new JMenuItem(a) {
            public void setToolTipText(String text) {
                // Ignore!  Actions (e.g. undo/redo) set this when changing
                // their text due to changing enabled state.
            }
        };
        item.setAccelerator(null);
        return item;
    }


    public static JMenuBar createMenuBar(ActionSet actionSet) {
        JMenuBar menuBar = new JMenuBar();
        for (BaseAction a : actionSet.getActions()) {
            menuBar.add(createMenu(a));
        }

        return menuBar;
    }


    private static JMenu createMenu(BaseAction action) {

        if (action instanceof DynamicActionSet) {
            return ((DynamicActionSet) action).createJMenu();
        }

        JMenu menu = new JMenu(action);
        List<BaseAction> actions = ((ActionSet) action).getActions();
        for (BaseAction a : actions) {
            if (a instanceof SeparatorAction) {
                menu.addSeparator();
            } else if (a instanceof ActionSet) {
                menu.add(createMenu(a));
            } else {
                menu.add(new JMenuItem(a));
            }
        }

        return menu;
    }
}
