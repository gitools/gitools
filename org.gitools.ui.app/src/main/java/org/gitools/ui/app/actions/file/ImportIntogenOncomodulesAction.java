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

import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.datasources.intogen.dialog.IntogenImportDialog;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;

import java.awt.event.ActionEvent;

public class ImportIntogenOncomodulesAction extends BaseAction {

    private static final long serialVersionUID = 668140963768246841L;

    public ImportIntogenOncomodulesAction() {
        super("IntOGen oncomodules...");
        setLargeIconFromResource(IconNames.intogen24);
        setSmallIconFromResource(IconNames.intogen16);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IntogenImportDialog dlg = new IntogenImportDialog(Application.get(), IntogenImportDialog.ImportType.ONCOMODULES);

        dlg.setVisible(true);
    }

}
