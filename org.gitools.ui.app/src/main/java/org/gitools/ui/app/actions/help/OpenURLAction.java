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

import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;

public class OpenURLAction extends BaseAction {

    private String url;

    public OpenURLAction(String name, String url, int mnemonic) {
        this(name, url, mnemonic, null);
    }

    public OpenURLAction(String name, String url, int mnemonic, KeyStroke accelerator) {
        super(name, null, null, mnemonic);

        if (accelerator!=null) {
            setAccelerator(accelerator);
        }

        setDefaultEnabled(true);
        this.url = url;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(uri);
            } else {
                JOptionPane.showInputDialog(Application.get(), "Copy this URL into your web browser", uri.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
