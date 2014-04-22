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
package org.gitools.ui.app.actions;

import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractAction extends BaseAction {

    private String actionName;
    private Window parentWindow;

    public AbstractAction(String name, ImageIcon icon, String desc, Integer mnemonic, boolean checkMode, boolean selected, String actionGroup) {
        super(name, icon, desc, mnemonic, checkMode, selected, actionGroup);
    }

    public AbstractAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
        super(name, icon, desc, mnemonic);
    }

    public AbstractAction(String name, ImageIcon icon, boolean checkMode, boolean checked, String actionGroup) {
        super(name, icon, checkMode, checked, actionGroup);
    }

    public AbstractAction(String name, boolean checkMode, boolean checked, String actionGroup) {
        super(name, checkMode, checked, actionGroup);
    }

    public AbstractAction(String name, ImageIcon icon, String desc) {
        super(name, icon, desc);
    }

    public AbstractAction(String name, ImageIcon icon) {
        super(name, icon);
    }

    public AbstractAction(String name) {
        super(name);
    }

    @Override
    protected void track(String category, String label) {

        if (actionName == null) {
            getClass().getSimpleName().replace("Action", "");
        }

        Application.get().trackEvent(category, actionName, label);
    }


    public void setParentWindow(Window parentWindow) {
        this.parentWindow = parentWindow;
    }

    public Window getParentWindow() {
        if (parentWindow == null) {
            return Application.get();
        }
        return parentWindow;
    }
}
