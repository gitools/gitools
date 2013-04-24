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

import org.gitools.core.exporter.TextMatrixViewExporter;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.IMatrixLayers;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.attributes.AttributesSelectionDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @noinspection ALL
 */
public class ExportTableAction extends BaseAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportTableAction() {
        super("Export table ...");

        setDesc("Export a table");
        setMnemonic(KeyEvent.VK_A);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = ActionUtils.getMatrixView();
        if (matrixView == null) {
            return;
        }

        final IMatrixLayers properties = matrixView.getLayers();
        final String[] attributeNames = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++)
            attributeNames[i] = properties.get(i).getName();

        AttributesSelectionDialog dlg = new AttributesSelectionDialog(AppFrame.get(), attributeNames);
        dlg.setVisible(true);

        if (dlg.getReturnStatus() != AttributesSelectionDialog.RET_OK) {
            AppFrame.get().setStatusText("Table export cancelled.");
            return;
        }

        final File file = FileChooserUtils.selectFile("Select destination file", Settings.getDefault().getLastExportPath(), FileChooserUtils.MODE_SAVE);

        if (file == null) {
            return;
        }

        Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

        final List<Integer> selectedIndices = dlg.getSelectedIndices();

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting table ...", 1);
                    monitor.info("File: " + file.getName());

                    int[] attributeIndices = new int[selectedIndices.size()];
                    for (int i = 0; i < selectedIndices.size(); i++)
                        attributeIndices[i] = selectedIndices.get(i);

                    TextMatrixViewExporter.exportTable(matrixView, attributeIndices, file);

                    monitor.end();
                } catch (IOException ex) {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Table exported.");
    }

}
