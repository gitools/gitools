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

import org.gitools.ui.core.components.editor.IEditor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        rootActions = new HashSet<>();
        baseActions = new HashSet<>();
        actionMap = new HashMap<>();
    }

    public void addRootAction(BaseAction action) {
        rootActions.add(action);
    }

    public void updateEnabledByEditor(IEditor editor) {
        for (BaseAction action : rootActions)
            action.updateEnabledByEditor(editor);
    }
}
