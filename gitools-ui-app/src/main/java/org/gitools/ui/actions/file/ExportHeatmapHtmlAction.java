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

import org.gitools.exporter.HtmlHeatmapExporter;
import org.gitools.heatmap.Heatmap;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.UnimplementedDialog;
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

/**
 * @noinspection ALL
 */
public class ExportHeatmapHtmlAction extends BaseAction
{

    private static final long serialVersionUID = -7288045475037410310L;

    public ExportHeatmapHtmlAction()
    {
        super("Export heatmap as html ...");

        setDesc("Export a matrix figure in html format");
        setMnemonic(KeyEvent.VK_H);
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        UnimplementedDialog.show(AppFrame.get());
        if (true)
        {
            return;
        }

        final Heatmap figure = ActionUtils.getHeatmap();
        if (figure == null)
        {
            return;
        }

        final File basePath = FileChooserUtils.selectPath("Select destination folder", Settings.getDefault().getLastExportPath());

        if (basePath == null)
        {
            return;
        }

        Settings.getDefault().setLastExportPath(basePath.getAbsolutePath());

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Exporting html ...", 1);

                    HtmlHeatmapExporter exporter = new HtmlHeatmapExporter();
                    exporter.setBasePath(basePath);
                    exporter.setIndexName("index.html");
                    exporter.exportHeatmap(figure);

                    monitor.end();
                } catch (Exception ex)
                {
                    monitor.exception(ex);
                }
            }
        });

        AppFrame.get().setStatusText("Html exported.");
    }
}
