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

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public abstract class DynamicActionSet extends ActionSet {

    private IEditor editor;

    public DynamicActionSet(String name, int mnemonic, String icon) {
        super(name, mnemonic, icon, null);
    }

    @Override
    public boolean updateEnabledByEditor(IEditor editor) {

        this.editor = editor;

        return isEnabledByEditor(editor);
    }

    protected <T extends BaseAction> T updateEnable(T action) {

        if (editor!=null) {
            action.updateEnabledByEditor(editor);
        }

        return action;
    }

    public JMenu createJMenu() {
        JMenu menu = new JMenu(this);
        menu.addMenuListener(new DynamicMenuListener());
        return menu;
    }

    protected abstract void populateMenu(JMenu menu);

    private class DynamicMenuListener implements MenuListener {

        public void menuCanceled(MenuEvent e) { }

        public void menuDeselected(MenuEvent e) { }

        public void menuSelected(MenuEvent e) {
            JMenu menu = (JMenu) e.getSource();
            populateMenu(menu);
        }
    }

}
