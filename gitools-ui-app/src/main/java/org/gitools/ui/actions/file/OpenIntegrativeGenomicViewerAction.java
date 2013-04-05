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

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.IconNames;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.commands.AbstractCommand;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * This actions sends a command to IGV (Integrative Genomic Viewer) to do a search
 * of the current selected row identifiers. The user must have IGV running and a
 * track with the row identifiers loaded.
 */
public class OpenIntegrativeGenomicViewerAction extends BaseAction
{

    /**
     * Instantiates a new Open integrative genomic viewer action.
     */
    public OpenIntegrativeGenomicViewerAction()
    {
        super("open current selection in IGV");
        setLargeIconFromResource(IconNames.igv24);
        setSmallIconFromResource(IconNames.igv16);
        setDefaultEnabled(Settings.getDefault().isShowIGVLink());
    }

    /**
     * Show a popup dialog with an informative message
     *
     * @param message the message
     */
    private static void showMessage(String message)
    {
        AppFrame frame = AppFrame.get();
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        AbstractEditor editor = ActionUtils.getSelectedEditor();

        // No heatmap editor
        if (!(editor instanceof HeatmapEditor))
        {
            showMessage("This feature only works on a heatmap editor");
            return;
        }

        Heatmap heatmap = (Heatmap) editor.getModel();
        IMatrixView matrixView = heatmap.getMatrixView();

        // No row selected
        int[] rows = matrixView.getSelectedRows();
        if (matrixView.getLeadSelectionRow() == -1 && rows.length == 0)
        {
            showMessage("Please select one or more rows");
            return;
        }

        // If there are no rows selected use the row of the current
        // cell selection
        if (rows.length == 0)
        {
            rows = new int[]{matrixView.getLeadSelectionRow()};
        }

        String rowQuery = "";
        for (int i : rows)
        {
            rowQuery = rowQuery + heatmap.getMatrixView().getRowLabel(i) + " ";
        }

        // Execute the command
        JobRunnable loadFile = new IgvCommand(rowQuery);
        JobThread.execute(AppFrame.get(), loadFile);
        AppFrame.get().setStatusText("Done.");

    }

    private class IgvCommand extends AbstractCommand
    {

        private final String rowQuery;

        private IgvCommand(String rowQuery)
        {
            this.rowQuery = rowQuery;
        }

        @Override
        public void execute(@NotNull IProgressMonitor monitor)
        {

            Socket socket = null;
            try
            {

                monitor.title("Connecting with Integrative Genomics Viewer");

                String igvUrl[] = Settings.getDefault().getIgvUrl().replace("http://", "").split(":");
                socket = new Socket(igvUrl[0], Integer.valueOf(igvUrl[1]));
                System.out.println();
                socket.setSoTimeout(30000);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                InputStream in = socket.getInputStream();

                String cmd;
                monitor.info("Locating '" + rowQuery + "'");
                cmd = "goto " + rowQuery;
                out.println(cmd);
                waitServerResponse(in, monitor);

            } catch (ConnectException e)
            {
                monitor.end();
                showMessage("Unable to connect with Integrative Genomics Viewer (IGV). " +
                        "\n It must be running on '" + Settings.getDefault().getIgvUrl() + "'. " +
                        "\n Install or launch it from 'http://www.broadinstitute.org/igv'.");
            } catch (SocketTimeoutException e)
            {
                monitor.end();
                showMessage("Timeout connecting with Integrative Genomics Viewer (IGV) on '" + Settings.getDefault().getIgvUrl() + "'. ");
            } catch (IOException e)
            {
                monitor.end();
                showMessage("Unknown problem 'e.getMessage()' connecting with Integrative Genomics Viewer (IGV). Check Gitools help.");
            } catch (UnsupportedOperationException e)
            {
            } finally
            {
                monitor.end();
                if (socket != null)
                {
                    try
                    {
                        socket.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }

        private void waitServerResponse(@NotNull InputStream in, @NotNull IProgressMonitor monitor) throws UnsupportedOperationException, IOException
        {
            while (in.available() == 0)
            {
                try
                {
                    Thread.sleep(1000);
                } catch (Exception ee)
                {
                }
                if (monitor.isCancelled())
                {
                    throw new UnsupportedOperationException("User cancel");
                }
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            reader.readLine();
        }
    }

}
