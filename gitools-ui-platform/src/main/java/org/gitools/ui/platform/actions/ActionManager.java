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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.gitools.ui.platform.editor.IEditor;

public class ActionManager {

	private static ActionManager defaultActionManager;

	public static final ActionManager getDefault() {
		if (defaultActionManager != null)
			return defaultActionManager;

		defaultActionManager = new ActionManager();
		return defaultActionManager;
	}

	private Set<BaseAction> rootActions;
	private Set<BaseAction> baseActions;

	private Map<String, BaseAction> actionMap;

	public ActionManager() {
		rootActions = new HashSet<BaseAction>();
		baseActions = new HashSet<BaseAction>();
		actionMap = new HashMap<String, BaseAction>();
	}

	public BaseAction getAction(String id) {
		return actionMap.get(id);
	}

	public void addAction(BaseAction action) {
		baseActions.add(action);
		actionMap.put(action.getClass().getName(), action);
	}

	public void addAction(BaseAction action, String id) {
		baseActions.add(action);
		actionMap.put(id, action);
	}

	public void addActionsFromClass(Class<?> cls) {
		for (Field field : cls.getDeclaredFields()) {
			if (BaseAction.class.isAssignableFrom(field.getType())) {
				try {
					addAction((BaseAction) field.get(null), field.getName());
				} catch (Exception ex) {
					// do nothing
				}
			}
		}
	}

	public void addActionsFromActionSet(ActionSet actionSet) {
		Stack<BaseAction> actionStack = new Stack<BaseAction>();
		actionStack.push(actionSet);
		while (actionStack.size() > 0) {
			BaseAction action = actionStack.pop();
			addAction(action);
			if (action instanceof ActionSet) {
				ActionSet as = (ActionSet) action;
				for (BaseAction a : as.getActions())
					actionStack.push(a);
			}
		}
	}

	public void addRootAction(BaseAction action) {
		rootActions.add(action);
	}

	public void updateEnabledByEditor(IEditor editor) {
		for (BaseAction action : rootActions)
			action.updateEnabledByEditor(editor);
	}
}
