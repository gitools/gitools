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

import java.util.HashMap;
import java.util.Map;

public class ActionManager {

	private static ActionManager defaultActionManager;

	public static final ActionManager getDefault() {
		if (defaultActionManager != null)
			return defaultActionManager;

		defaultActionManager = new ActionManager();
		return defaultActionManager;
	}

	private Map<String, BaseAction> actions;

	public ActionManager() {
		actions = new HashMap<String, BaseAction>();
	}

	public BaseAction getAction(String id) {
		return actions.get(id);
	}

	public void addAction(BaseAction action) {
		actions.put(action.getClass().getName(), action);
	}

	public void addAction(BaseAction action, String id) {
		actions.put(id, action);
	}
}
