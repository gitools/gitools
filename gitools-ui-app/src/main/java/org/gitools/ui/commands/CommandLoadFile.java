package org.gitools.ui.commands;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import org.apache.commons.io.FilenameUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.ui.genomespace.dm.HttpUtils;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.settings.Settings;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class CommandLoadFile extends AbstractCommand {

    private String file;
    private String mime;
    private String rowsAnnotations;
    private String columnsAnnotations;

    public CommandLoadFile(String file) {
        this(file, null);
    }

    public CommandLoadFile(String file, String mime) {
        this(file, mime, null, null);
    }

    public CommandLoadFile(String file, String rowsAnnotations, String columnsAnnotations) {
        this(file, null, rowsAnnotations, columnsAnnotations);
    }

    public CommandLoadFile(String file, String mime, String rowsAnnotations, String columnsAnnotations) {
        this.file = file;
        this.mime = mime;
        this.rowsAnnotations = rowsAnnotations;
        this.columnsAnnotations = columnsAnnotations;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        monitor.begin("Loading ...", 1);

        File file = download(this.file, monitor);

        if (mime == null)
            mime = PersistenceManager.getDefault().getMimeFromFile(file.getName());

        final IMatrix matrix;
        try {
            matrix = (IMatrix) PersistenceManager.getDefault().load(file, mime, monitor);
        } catch (Exception e) {
            MessageUtils.showErrorMessage(AppFrame.instance(), "This file format is not supported. Check the supported file formats at the 'User guide' on www.gitools.org", e);
            return;
        }

        final IMatrixView matrixView = new MatrixView(matrix);

        Heatmap figure = HeatmapUtil.createFromMatrixView(matrixView);

        if (rowsAnnotations != null) {

            File rowsFile = download(rowsAnnotations, monitor);
            loadAnnotations(rowsFile, figure.getRowDim());


        }

        if (columnsAnnotations != null) {
            File colsFile = download(columnsAnnotations, monitor);
            loadAnnotations(colsFile, figure.getColumnDim());

        }

        final HeatmapEditor editor = new HeatmapEditor(figure);

        editor.setName(PersistenceUtils.getFileName(file.getName()));
        editor.abbreviateName(Settings.getDefault().getEditorTabLength());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AppFrame.instance().getEditorsPanel().addEditor(editor);
                AppFrame.instance().refresh();
            }
        });

        // Force a GC to release free memory
        System.gc();

        monitor.end();

    }

    private static File download(String file, IProgressMonitor monitor) throws CommandException {

        URL url = null;
        try {
            url = new URL(file);
        } catch (Exception e) {
            // Try to load as a file
            try {
                url = (new File(file)).getAbsoluteFile().toURI().toURL();
            } catch (MalformedURLException e1) {
                throw new CommandException(e1);
            }
        }
        File resultFile;
        String fileName = FilenameUtils.getName(url.getFile());
        if (url.getProtocol().equals("file")) {
            monitor.info("File: " + fileName);
            try {
                resultFile = new File(url.toURI());
            } catch (URISyntaxException e) {
                throw new CommandException(e);
            }
        } else {
            try {
                resultFile = File.createTempFile("gitools", fileName);
                monitor.info("Downloading " + url.toString());

                HttpUtils.getInstance().downloadFile(url.toString(), resultFile);

            } catch (IOException e) {
                throw new CommandException(e);
            }
        }

        return resultFile;
    }

    private static void loadAnnotations(File file, HeatmapDim hdim) throws CommandException {

        if (file != null) {
            AnnotationMatrix annMatrix =
                    null;
            try {
                annMatrix = (AnnotationMatrix) PersistenceManager.getDefault().load(
                        file, MimeTypes.ANNOTATION_MATRIX,
                        new NullProgressMonitor());
                annMatrix.setTitle(file.getName());
            } catch (PersistenceException e) {
                throw new CommandException(e);
            }

            hdim.setAnnotations(annMatrix);
        }
    }
}
