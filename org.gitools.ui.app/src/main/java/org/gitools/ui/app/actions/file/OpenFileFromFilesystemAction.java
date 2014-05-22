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

import org.gitools.api.resource.IResourceFormat;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.AbstractAction;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;

import java.awt.event.ActionEvent;
import java.io.File;

public class OpenFileFromFilesystemAction extends AbstractAction {

    private File file;

    public OpenFileFromFilesystemAction(File file) {
        super(file.getName());
        this.file = file;
        setDesc("Open " + file.getAbsolutePath());
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        Settings.get().setLastPath(file.getParent());
        Settings.get().save();

        IResourceFormat format = null;

        String fileName = file.getAbsolutePath();
        CommandLoadFile loadFile = new CommandLoadFile(fileName, format);
        JobThread.execute(Application.get(), loadFile);

        Settings.get().addRecentFile(fileName);
        Settings.get().save();
        Application.get().setStatusText("Loaded " + fileName + ".");
    }
}
