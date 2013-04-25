/*
 * #%L
 * gitools-ui-app
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
package org.gitools.ui.actions.help;

import org.gitools.ui.dialog.ShortcutsDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * @noinspection ALL
 */
public class ShortcutsAction extends BaseAction {

    public ShortcutsAction() {
        super("Show shortcuts");
        setDesc("See all the shortcuts for Gitools");
        setMnemonic(KeyEvent.VK_A);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ShortcutsDialog(AppFrame.get()).setVisible(true);
    }

}
