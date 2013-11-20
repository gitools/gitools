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

import org.gitools.ui.IconNames;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * @noinspection ALL
 */
public class CloseAction extends BaseAction {

    private static final long serialVersionUID = 2399811452235609343L;

    public CloseAction() {
        super("Close");

        setDesc("Close current tab");
        setSmallIconFromResource(IconNames.close16);
        setLargeIconFromResource(IconNames.close24);
        setMnemonic(KeyEvent.VK_O);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        EditorsPanel editorsPanel = AppFrame.get().getEditorsPanel();
        AbstractEditor currentEditor = editorsPanel.getSelectedEditor();
        if (currentEditor != null) {
            editorsPanel.removeEditor(currentEditor);
        }

        AppFrame.get().refresh();
        AppFrame.get().setStatusText("View closed.");
    }

    @Override
    public boolean isEnabledByEditor(IEditor editor) {
        return editor != null;
    }
}
