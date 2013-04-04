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
package org.gitools.ui.welcome;

import org.gitools.ui.actions.file.*;
import org.gitools.ui.dialog.UnimplementedDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.dialog.ExceptionDialog;
import org.gitools.ui.platform.editor.HtmlEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class WelcomeEditor extends HtmlEditor
{

    private static final long serialVersionUID = 6851947500231401412L;

    public WelcomeEditor()
    {
        super("Welcome");

        try
        {
            URL url = getClass().getResource("/html/welcome.html");
            navigate(url);
        } catch (Exception e)
        {
            ExceptionDialog.show(AppFrame.get(), e);
        }
    }

    @Override
    protected void performUrlAction(@NotNull String name, @NotNull Map<String, String> params)
    {
        if (name.equals("goHome"))
        {
            try
            {
                Desktop.getDesktop().browse(new URI("http://www.gitools.org"));
            } catch (Exception ex)
            {
                ExceptionDialog.show(AppFrame.get(), ex);
            }
        }
        else if (name.equals("importIntogen"))
        {
            IntogenTypeDialog dlg = new IntogenTypeDialog(AppFrame.get());
            dlg.setVisible(true);
            if (!dlg.isCancelled())
            {
                switch (dlg.getSelection())
                {
                    case IntogenTypeDialog.MATRIX:
                        new ImportIntogenMatrixAction()
                                .actionPerformed(new ActionEvent(this, 0, name));
                        break;

                    case IntogenTypeDialog.ONCOMODULES:
                        new ImportIntogenOncomodulesAction()
                                .actionPerformed(new ActionEvent(this, 0, name));
                        break;
                }
            }
        }
        else if (name.equals("importGo"))
        {
            new ImportGoModulesAction()
                    .actionPerformed(new ActionEvent(this, 0, name));
        }
        else if (name.equals("importKegg"))
        {
            new ImportKeggModulesAction()
                    .actionPerformed(new ActionEvent(this, 0, name));
        }
        else if (name.equals("importBiomart"))
        {
            BiomartTypeDialog dlg = new BiomartTypeDialog(AppFrame.get());
            dlg.setVisible(true);
            if (!dlg.isCancelled())
            {
                switch (dlg.getSelection())
                {
                    case BiomartTypeDialog.TABLE:
                        new ImportBiomartTableAction()
                                .actionPerformed(new ActionEvent(this, 0, name));
                        break;

                    case BiomartTypeDialog.MODULES:
                        new ImportBiomartModulesAction()
                                .actionPerformed(new ActionEvent(this, 0, name));
                        break;
                }
            }
        }
        else if (name.equals("importExcel"))
        {
            new ImportExcelMatrixAction()
                    .actionPerformed(new ActionEvent(this, 0, name));
        }
        else if (name.equals("analysis"))
        {
            final Map<String, Class<? extends BaseAction>> actions =
                    new HashMap<String, Class<? extends BaseAction>>();

            actions.put("Enrichment", NewEnrichmentAnalysisAction.class);
            actions.put("Oncodrive", NewOncodriveAnalysisAction.class);
            actions.put("Correlations", NewCorrelationAnalysisAction.class);
            actions.put("Overlapping", NewOverlappingAnalysisAction.class);
            actions.put("Combination", NewCombinationAnalysisAction.class);

            String ref = params.get("ref");
            Class<? extends BaseAction> actionClass = actions.get(ref);
            if (actionClass != null)
            {
                try
                {
                    ActionEvent event = new ActionEvent(this, 0, name);
                    actionClass.newInstance().actionPerformed(event);
                } catch (Exception ex)
                {
                    ExceptionDialog.show(AppFrame.get(), ex);
                }
            }
            else
            {
                UnimplementedDialog.show(AppFrame.get());
            }
        }
        else if (name.equals("open"))
        {
            String ref = params.get("ref");
            if (ref.equals("Analysis"))
            {
                new OpenAnalysisAction().actionPerformed(new ActionEvent(this, 0, name));
            }
            else if (ref.equals("DataHeatmap"))
            {
                new OpenHeatmapAction().actionPerformed(new ActionEvent(this, 0, name));
            }
            else if (ref.equals("DataHeatmapGS"))
            {
                new OpenGenomeSpaceMatrixAction().actionPerformed(new ActionEvent(this, 0, name));
            }
        }
        else if (name.equals("example"))
        {
            LoggerFactory.getLogger(WelcomeEditor.class).debug("example: " + params);
        }
        else if (name.equals("downloadExamples"))
        {
            DownloadExamplesDialog dlg = new DownloadExamplesDialog(AppFrame.get());
            dlg.setPath(Settings.getDefault().getLastWorkPath());
            dlg.setVisible(true);
            downloadExamples(dlg.getPath());
        }
        else if (name.equals("dataMatrices")
                || name.equals("dataModules")
                || name.equals("dataTables"))
        {
            DataHelpDialog dlg = new DataHelpDialog(AppFrame.get());
            dlg.setVisible(true);
        }
    }

    @Override
    public void doVisible()
    {
    }

    private void downloadExamples(final String path)
    {
        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                try
                {
                    monitor.begin("Connecting ...", 1);

                    URL url = new URL("http://webstart.gitools.org/examples.zip");

                    ZipInputStream zin = new ZipInputStream(url.openStream());

                    File pathFile = new File(path);

                    monitor.end();

                    monitor.begin("Downloading ...", 1);

                    ZipEntry ze;
                    while ((ze = zin.getNextEntry()) != null)
                    {
                        IProgressMonitor mnt = monitor.subtask();

                        long totalKb = ze.getSize() / 1024;

                        String name = ze.getName();

                        mnt.begin("Extracting " + name + " ...", (int) ze.getSize());

                        File outFile = new File(pathFile, name);
                        if (!outFile.getParentFile().exists())
                        {
                            outFile.getParentFile().mkdirs();
                        }

                        OutputStream fout = new FileOutputStream(outFile);

                        final int BUFFER_SIZE = 4 * 1024;
                        byte[] data = new byte[BUFFER_SIZE];
                        int partial = 0;
                        int count;
                        while ((count = zin.read(data, 0, BUFFER_SIZE)) != -1)
                        {
                            fout.write(data, 0, count);
                            partial += count;
                            mnt.info((partial / 1024) + " Kb read");
                            mnt.worked(count);
                        }

                        zin.closeEntry();
                        fout.close();

                        mnt.end();
                    }

                    zin.close();

                    monitor.end();
                } catch (Exception ex)
                {
                    monitor.exception(ex);
                }
            }
        });
    }
}
