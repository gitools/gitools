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

import org.gitools.api.components.IEditor;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActionSet extends BaseAction {

    private List<BaseAction> actions;

    public ActionSet(BaseAction... actions) {
        this("", actions);
    }

    public ActionSet(String name, BaseAction[] actions) {
        this(name, null, actions);
    }

    public ActionSet(String name, int mnemonic, String icon, BaseAction[] actions) {
        this(name, mnemonic, actions);

        if (icon != null) {
            setSmallIconFromResource(icon);
        }
    }

    public ActionSet(String name, Integer mnemonic, BaseAction[] actions) {
        super(name, null);

        if (actions != null) {
            this.actions = Arrays.asList(actions);
        } else {
            this.actions = Collections.EMPTY_LIST;
        }

        setEnabled(true);

        if (mnemonic != null) {
            setMnemonic(mnemonic);
        }
    }

    public List<BaseAction> getActions() {
        return actions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (actions != null) {
            for (BaseAction action : actions)
                action.actionPerformed(e);
        }
    }

    @Override
    public boolean updateEnabledByEditor(IEditor editor) {
        boolean someEnabled = false;

        for (BaseAction action : actions)
            someEnabled |= action.updateEnabledByEditor(editor);

        setEnabled(someEnabled);
        return someEnabled;
    }
}
