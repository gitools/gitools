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

import org.apache.commons.lang.StringUtils;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.core.actions.AbstractAction;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.genomespace.GSFileBrowser;
import org.gitools.ui.core.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.json.JSONException;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class OpenFromGenomeSpaceAction extends AbstractAction {

    private static final long serialVersionUID = 668140963768246841L;

    public OpenFromGenomeSpaceAction() {
        super("Open from Genomespace...");
        setDesc("Open a heatmap or an analysis from Genomespace");
        setLargeIconFromResource(IconNames.gs24);
        setSmallIconFromResource(IconNames.gs16);
        setMnemonic(KeyEvent.VK_G);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        GSFileBrowser fileBrowser = null;
        try {
            fileBrowser = new GSFileBrowser(Application.get(), GSFileBrowser.Mode.OPEN);
            fileBrowser.setVisible(true);

            String fileURL = fileBrowser.getFileURL();

            if (!StringUtils.isEmpty(fileURL)) {
                JobRunnable loadFile = new CommandLoadFile(fileURL);
                JobThread.execute(Application.get(), loadFile);
                Application.get().setStatusText("Done.");
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

    }

}
