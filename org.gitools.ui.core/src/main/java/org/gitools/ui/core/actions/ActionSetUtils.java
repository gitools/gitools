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
package org.gitools.ui.core.actions;

import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            } else if (a instanceof IPanelAction) {
                toolBar.add(((IPanelAction) a).getPanel());
            } else {
                toolBar.add(createActionButton(a));
            }
        }

        return toolBar;
    }

    public static JComponent createActionButton(BaseAction a) {
        WebButton button = WebButton.createIconWebButton(a.getSmallIcon(), StyleConstants.smallRound, true);
        button.setAction(a);
        button.addActionListener(new TrackingListener("ToolBar", a));
        button.setHideActionText(true);
        TooltipManager.setTooltip(button, a.getDesc(), TooltipWay.down, 0);
        button.setFocusable(false);
        button.setRequestFocusEnabled(false);
        return button;
    }


    public static JPopupMenu createPopupMenu(ActionSet actionSet) {
        JPopupMenu popupMenu = new JPopupMenu();

        for (BaseAction a : actionSet.getActions()) {
            if (a instanceof SeparatorAction) {
                popupMenu.addSeparator();
            } else if (a instanceof PopupSectionTitleAction) {
                popupMenu.add(createHeader(a));

            } else {
                popupMenu.add(createPopupMenuItem(a));
            }
        }

        return popupMenu;

    }


    private static JMenuItem createPopupMenuItem(BaseAction a) {
        JMenuItem item = new JMenuItem(a) {
            public void setToolTipText(String text) {
                // Ignore!  Actions (e.g. undo/redo) set this when changing
                // their text due to changing enabled state.
            }
        };
        item.addActionListener(new TrackingListener("PopupMenu", a));
        item.setAccelerator(null);
        return item;
    }

    private static JLabel createHeader(BaseAction a) {
        JLabel label = new JLabel(a.getName());
        label.setFont(label.getFont().deriveFont(10f));
        label.setForeground(Color.gray);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setHorizontalTextPosition(SwingConstants.RIGHT);
        label.setBorder(BorderFactory.createEmptyBorder(5,0,0,10));
        label.setSize(new Dimension(125, 14));
        return label;
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
                JMenuItem item = new JMenuItem(a);
                item.addActionListener(new TrackingListener("MainMenu", a));
                menu.add(item);
            }
        }

        return menu;
    }

    private static class TrackingListener implements ActionListener {

        private String source;
        private BaseAction action;

        public TrackingListener(String source, BaseAction action) {
            super();

            this.source = source;
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            action.track("action", source);
        }
    }
}
