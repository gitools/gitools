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

import org.gitools.biomart.BiomartService;
import org.gitools.biomart.restful.model.Query;
import org.gitools.ui.IconNames;
import org.gitools.ui.biomart.wizard.BiomartModulesWizard;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.WizardDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.io.File;

/**
 * @noinspection ALL
 */
public class ImportBiomartModulesAction extends BaseAction {

    private static final long serialVersionUID = 668140963768246841L;

    public ImportBiomartModulesAction() {
        super("Biomart Modules (advanced users) ...");
        setLargeIconFromResource(IconNames.biomart24);
        setSmallIconFromResource(IconNames.biomart16);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final BiomartModulesWizard wizard = new BiomartModulesWizard();
        WizardDialog wdlg = new WizardDialog(AppFrame.get(), wizard);
        wdlg.open();
        if (wdlg.isCancelled()) {
            return;
        }

        final File file = wizard.getSelectedFile();
        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                monitor.begin("Downloading data...", 1);
                Query query = wizard.getQuery();
                String format = (String) wizard.getFormat().getExtension();
                format = (format.endsWith("gz") ? BiomartModulesWizard.FORMAT_COMPRESSED_GZ : BiomartModulesWizard.FORMAT_PLAIN);
                BiomartService service = wizard.getService();
                try {
                    service.queryModule(query, file, format, monitor);
                } catch (Exception ex) {
                    monitor.exception(ex);
                }
                monitor.end();
            }
        });
    }
}
