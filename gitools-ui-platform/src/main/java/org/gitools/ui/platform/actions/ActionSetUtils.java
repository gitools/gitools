/*
 *  Copyright 2010 chris.
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

import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

public class ActionSetUtils {

	public static JToolBar createToolBar(ActionSet actionSet) {
		JToolBar toolBar = new JToolBar();

		for (BaseAction a : actionSet.getActions())
			if (a instanceof SeparatorAction)
				toolBar.addSeparator();
			else
				toolBar.add(a);

		return toolBar;
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
