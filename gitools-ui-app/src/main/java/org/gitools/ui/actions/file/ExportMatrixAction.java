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

import org.gitools.exporter.TextMatrixViewExporter;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

/**
 * @noinspection ALL
 */
public class ExportMatrixAction extends BaseAction
{

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportMatrixAction()
    {
        super("Export matrix ...");

        setDesc("Export a matrix");
        setMnemonic(KeyEvent.VK_P);
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        final IMatrixView matrixView = ActionUtils.getHeatmapMatrixView();
        if (matrixView == null)
        {
            return;
        }

        final IMatrixLayers properties = matrixView.getLayers();
        final String[] propNames = new String[properties.size()];
        for (int i = 0; i < properties.size(); i++)
            propNames[i] = properties.get(i).getName();

        int selectedPropIndex = matrixView.getLayers().getTopLayer();
        selectedPropIndex = selectedPropIndex >= 0 ? selectedPropIndex : 0;
        selectedPropIndex = selectedPropIndex < properties.size() ? selectedPropIndex : 0;

        final String selected = (String) JOptionPane.showInputDialog(AppFrame.get(), "What do you want to export ?", "Export table data", JOptionPane.QUESTION_MESSAGE, null, propNames, propNames[selectedPropIndex]);

        if (selected == null || selected.isEmpty())
        {
            return;
        }

        int index = 0;
        for (int j = 0; j < propNames.length; j++)
            if (propNames[j].equals(selected))
            {
                index = j;
            }

        final int propIndex = index;

        final File file = FileChooserUtils.selectFile("Select destination file", Settings.getDefault().getLastExportPath(), FileChooserUtils.MODE_SAVE);

        if (file == null)
        {
            return;
        }

        Settings.getDefault().setLastExportPath(file.getParentFile().getAbsolutePath());

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Exporting to image ...", 1);
                    monitor.info("File: " + file.getName());

                    TextMatrixViewExporter.exportMatrix(matrixView, propIndex, file);

                    monitor.end();
                } catch (IOException ex)
                {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText(selected + " exported.");
    }
}
