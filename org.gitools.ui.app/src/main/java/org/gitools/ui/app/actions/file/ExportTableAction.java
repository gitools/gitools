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

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.dialog.attributes.AttributesSelectionDialog;
import org.gitools.ui.app.export.TextMatrixViewExporter;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.utils.FileChooserUtils;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ExportTableAction extends HeatmapAction {

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportTableAction() {
        super("Values to text file (table)");
        setMnemonic(KeyEvent.VK_T);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = getHeatmap();

        final IMatrixLayers properties = matrixView.getLayers();
        final String[] attributeNames = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++)
            attributeNames[i] = properties.get(i).getName();

        AttributesSelectionDialog dlg = new AttributesSelectionDialog(Application.get(), attributeNames);
        dlg.setVisible(true);

        if (dlg.getReturnStatus() != AttributesSelectionDialog.RET_OK) {
            Application.get().setStatusText("Table export cancelled.");
            return;
        }

        final File file = FileChooserUtils.selectFile("Select destination file", Settings.get().getLastExportPath(), FileChooserUtils.MODE_SAVE).getFile();

        if (file == null) {
            return;
        }

        Settings.get().setLastExportPath(file.getParentFile().getAbsolutePath());

        final List<Integer> selectedIndices = dlg.getSelectedIndices();

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                try {
                    monitor.begin("Exporting to text file (table layout) ...", 1);
                    monitor.info("File: " + file.getName());

                    int[] attributeIndices = new int[selectedIndices.size()];
                    for (int i = 0; i < selectedIndices.size(); i++)
                        attributeIndices[i] = selectedIndices.get(i);

                    TextMatrixViewExporter.exportTable(matrixView, attributeIndices, file);

                } catch (IOException ex) {
                    monitor.exception(ex);
                }
            }
        });

        Application.get().setStatusText("Table exported.");
    }

}
