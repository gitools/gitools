package org.gitools.ui.commands;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import edu.upf.bg.progressmonitor.NullProgressMonitor;
import org.apache.commons.io.FilenameUtils;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.util.HeatmapUtil;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;
import org.gitools.persistence.*;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.genomespace.GsResourceLocator;
import org.gitools.ui.genomespace.dm.HttpUtils;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.platform.editor.AbstractEditor;

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

        IResourceLocator resourceLocator;
        final IResource resource;
        try {
            resourceLocator = new GsResourceLocator(new UrlResourceLocator(file));
            resource = PersistenceManager.get().load(resourceLocator, IResource.class, monitor);
        } catch (Exception e) {
            MessageUtils.showErrorMessage(AppFrame.instance(), "This file format is not supported. Check the supported file formats at the 'User guide' on www.gitools.org", e);
            return;
        }

        final AbstractEditor editor = createEditor(resource, monitor);
        editor.setName(resourceLocator.getName());

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

    private AbstractEditor createEditor(IResource resource, IProgressMonitor progressMonitor) throws CommandException {

        if (resource instanceof EnrichmentAnalysis)
            return new EnrichmentAnalysisEditor((EnrichmentAnalysis) resource);
        else if (resource instanceof OncodriveAnalysis)
            return new OncodriveAnalysisEditor((OncodriveAnalysis) resource);
        else if (resource instanceof CorrelationAnalysis)
            return new CorrelationAnalysisEditor((CorrelationAnalysis) resource);
        else if (resource instanceof CombinationAnalysis)
            return new CombinationAnalysisEditor((CombinationAnalysis) resource);
        else if (resource instanceof OverlappingAnalysis)
            return new OverlappingAnalysisEditor((OverlappingAnalysis) resource);
        else if (resource instanceof GroupComparisonAnalysis)
            return new GroupComparisonAnalysisEditor((GroupComparisonAnalysis) resource);

        return createHeatmapEditor((IMatrix) resource, progressMonitor);
    }

    private AbstractEditor createHeatmapEditor(IMatrix resource, IProgressMonitor progressMonitor) throws CommandException {

        final IMatrixView matrixView = new MatrixView(resource);
        Heatmap figure = HeatmapUtil.createFromMatrixView(matrixView);

        if (rowsAnnotations != null) {
            File rowsFile = download(rowsAnnotations, progressMonitor);
            loadAnnotations(rowsFile, figure.getRowDim());
        }

        if (columnsAnnotations != null) {
            File colsFile = download(columnsAnnotations, progressMonitor);
            loadAnnotations(colsFile, figure.getColumnDim());

        }

        return new HeatmapEditor(figure);
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
                annMatrix = (AnnotationMatrix) PersistenceManager.get().load(
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
