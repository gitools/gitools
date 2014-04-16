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
package org.gitools.ui.app.actions.help;

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.actions.AbstractAction;
import org.gitools.ui.app.dialog.AboutDialog;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


public class AboutAction extends AbstractAction {

    private static final long serialVersionUID = 8302818623988394433L;

    public AboutAction() {
        super("About " + Application.getAppName() + "...");
        setDesc("Know more about this application");
        setMnemonic(KeyEvent.VK_A);
        setDefaultEnabled(true);
        setLargeIconFromResource(IconNames.logo24);
        setSmallIconFromResource(IconNames.logo16);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new AboutDialog(Application.get()).setVisible(true);
    }

}
