/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.ui.platform.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class ActionSetUtils {

	public static JToolBar createToolBar(ActionSet actionSet) {
		JToolBar toolBar = new JToolBar();

		Map<String, ButtonGroup> groups =
				new HashMap<String, ButtonGroup>();

		for (BaseAction a : actionSet.getActions())
			if (a instanceof SeparatorAction)
				toolBar.addSeparator();
			else
				toolBar.add(createTool(a, groups));

		return toolBar;
	}

	private static JComponent createTool(BaseAction a, Map<String, ButtonGroup> groups) {
		JComponent tool = null;
		if (a.isCheckMode()) {
			JToggleButton tbtn = new JToggleButton(a);
			String actionGroup = a.getActionGroup();
			if (actionGroup == null) {
				tbtn.setSelected(a.isSelected());
			}
			else {
				ButtonGroup g = groups.get(actionGroup);
				if (g == null) {
					g = new ButtonGroup();
					groups.put(actionGroup, g);
				}

				g.add(tbtn);
				g.setSelected(tbtn.getModel(), a.isSelected());
			}
			tool = tbtn;
		}
		else {
			JButton btn = new JButton(a);
			btn.setHideActionText(
					a.getSmallIcon() != null || a.getLargeIcon() != null);
			tool = btn;
		}

		tool.setFocusable(false);
		tool.setRequestFocusEnabled(false);
		return tool;
	}

	public static JMenuBar createMenuBar(ActionSet actionSet) {
		JMenuBar menuBar = new JMenuBar();
		for (BaseAction a : actionSet.getActions())
			menuBar.add(createMenu(a));
		return menuBar;
	}

	public static JMenu createMenu(BaseAction action) {
		JMenu menu = new JMenu(action);

		List<BaseAction> actions = ((ActionSet)action).getActions();
		for (BaseAction a : actions)
			if (a instanceof SeparatorAction)
				menu.addSeparator();
			else if (a instanceof ActionSet)
				menu.add(createMenu(a));
			else
				menu.add(a);
		
		return menu;
	}
}
