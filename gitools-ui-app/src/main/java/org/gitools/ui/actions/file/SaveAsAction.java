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
package org.gitools.ui.actions.file;

import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.Nullable;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class SaveAsAction extends BaseAction {

    private static final long serialVersionUID = -6528634034161710370L;

    public SaveAsAction() {
        super("Save As ...");
        setDesc("Save to another location");
        setMnemonic(KeyEvent.VK_A);
    }

    @Override
    public boolean isEnabledByEditor(@Nullable IEditor editor) {

        if (editor == null) {
            return false;
        }

        EditorsPanel editorPanel;
        editorPanel = AppFrame.get().getEditorsPanel();

        return editorPanel.getSelectedEditor().isSaveAsAllowed();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorsPanel editorPanel = AppFrame.get().getEditorsPanel();

        final IEditor currentEditor = editorPanel.getSelectedEditor();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                currentEditor.doSaveAs(monitor);
            }
        });
    }
}
