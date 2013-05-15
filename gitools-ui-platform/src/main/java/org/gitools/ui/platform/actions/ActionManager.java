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

import org.gitools.ui.platform.editor.IEditor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.*;

public class ActionManager {

    private static ActionManager defaultActionManager;

    public static ActionManager getDefault() {
        if (defaultActionManager != null) {
            return defaultActionManager;
        }

        defaultActionManager = new ActionManager();
        return defaultActionManager;
    }

    private final Set<BaseAction> rootActions;
    private final Set<BaseAction> baseActions;

    private final Map<String, BaseAction> actionMap;

    private ActionManager() {
        rootActions = new HashSet<BaseAction>();
        baseActions = new HashSet<BaseAction>();
        actionMap = new HashMap<String, BaseAction>();
    }

    public BaseAction getAction(String id) {
        return actionMap.get(id);
    }

    void addAction(@NotNull BaseAction action) {
        baseActions.add(action);
        actionMap.put(action.getClass().getName(), action);
    }

    void addAction(BaseAction action, String id) {
        baseActions.add(action);
        actionMap.put(id, action);
    }

    public void addActionsFromClass(@NotNull Class<?> cls) {
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
