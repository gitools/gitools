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
package org.gitools.ui.app.actions.file;

import org.gitools.ui.core.actions.DynamicActionSet;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.settings.Settings;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import static com.google.common.collect.Lists.reverse;

public class OpenFromFileSystemActionSet extends DynamicActionSet {

    public OpenFromFileSystemActionSet() {
        super("Open file", KeyEvent.VK_O, null);
        setDesc("Open from the filesystem");
        setSmallIconFromResource(IconNames.openMatrix16);
        setLargeIconFromResource(IconNames.openMatrix24);
        setDefaultEnabled(true);
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
    }

    @Override
    protected void populateMenu(JMenu menu) {

        menu.removeAll();
        List<String> recentFiles = reverse(Settings.get().getRecentFiles());

        menu.add(new JMenuItem(new OpenFromFilesystemBrowseAction()));
        if (recentFiles.size() > 0) {
            menu.addSeparator();
        }

        for (String fileName : recentFiles) {

            JMenuItem menuItem = new JMenuItem(new OpenFileFromFilesystemAction(new File(fileName)));
            menu.add(menuItem);
        }

    }

}
