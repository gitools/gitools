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

import org.gitools.ui.app.actions.AbstractAction;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class OpenFromURLAction extends AbstractAction {

    private static final long serialVersionUID = -6528634034161710370L;

    public OpenFromURLAction() {
        super("Open from URL...");
        setDesc("Open a heatmap or an analysis from a URL");
        setMnemonic(KeyEvent.VK_U);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String fileURL = JOptionPane.showInputDialog(null, "Specify URL:", "Open URL", JOptionPane.QUESTION_MESSAGE);

        if (!isEmpty(fileURL)) {
            JobRunnable loadFile = new CommandLoadFile(fileURL);
            JobThread.execute(Application.get(), loadFile);
            Application.get().setStatusText("Done.");
        }
    }
}
