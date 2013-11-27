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
package org.gitools.ui.commands;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncozet.OncodriveAnalysis;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.header.ColoredLabel;
import org.gitools.core.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.core.matrix.model.matrix.AnnotationMatrix;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.ResourceReference;
import org.gitools.persistence.formats.annotations.TsvAnnotationMatrixFormat;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.fileimport.ImportManager;
import org.gitools.ui.fileimport.ImportWizard;
import org.gitools.ui.genomespace.GsResourceLocator;
import org.gitools.ui.genomespace.dm.HttpUtils;
import org.gitools.ui.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.utils.color.ColorRegistry;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CancellationException;

public class CommandLoadFile extends AbstractCommand {

    private final String file;
    private IResourceFormat format = null;
    private final String rowsAnnotations;
    private final String columnsAnnotations;

    public CommandLoadFile(String file) {
        this(file, null, null);
    }

    public CommandLoadFile(String file, IResourceFormat resourceFormat) {
        this(file);
        this.format = resourceFormat;
    }

    public CommandLoadFile(String file, String rowsAnnotations, String columnsAnnotations) {
        this.file = file;
        this.rowsAnnotations = rowsAnnotations;
        this.columnsAnnotations = columnsAnnotations;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        if (isConfigurable()) {
            try {
                getConfigWizard().run(monitor);
            } catch (IOException e) {
                new CommandException(e);
            }
            return;
        }

        IResourceLocator resourceLocator = getResourceLocator();
        final IResource resource;

        try {
            monitor.begin("Loading ...", 1);
            resource = loadResource(monitor);
        } catch (Exception e) {

            if (!(e.getCause() instanceof CancellationException)) {
                MessageUtils.showErrorMessage(Application.get(), "<html>This file format either not supported or is malformed.<br>" +
                        (!StringUtils.isEmpty(e.getCause().getMessage()) ? "<div style='margin: 5px 0px; padding:10px; width:300px; border: 1px solid black;'><strong>" + e.getCause().getMessage() + "</strong></div>" : "") +
                        "Check the supported file formats at the <strong>'User guide'</strong> on <a href='http://www.gitools.org'>www.gitools.org</a><br></html>", e);
            }
            setExitStatus(1);
            return;
        }

        monitor.begin("Initializing editor ...", 1);
        AbstractEditor editor = createEditor(resource, monitor);
        editor.setName(resourceLocator.getBaseName());

        Application.get().getEditorsPanel().addEditor(editor);
        Application.get().refresh();

        // Force a GC to release free memory
        System.gc();

        monitor.end();

        setExitStatus(0);
        return;
    }

    protected IResource loadResource( IProgressMonitor monitor ) {
        return PersistenceManager.get().load(getResourceLocator(), getFormat(), monitor);
    }

    protected IResourceLocator getResourceLocator() {
        return new GsResourceLocator(new UrlResourceLocator(file));
    }

    public boolean isConfigurable() {
        return getFormat() == null && ImportManager.get().isImportable(getResourceLocator());
    }

    public ImportWizard getConfigWizard() {
        return ImportManager.get().getWizard(getResourceLocator());
    }

    private IResourceFormat getFormat() {
        if (format == null) {
            format = PersistenceManager.get().getFormat(getResourceLocator().getName(), IResource.class);
        }

        return format;
    }


    private AbstractEditor createEditor(IResource resource, IProgressMonitor progressMonitor) throws CommandException {

        if (resource instanceof EnrichmentAnalysis) {
            return new EnrichmentAnalysisEditor((EnrichmentAnalysis) resource);
        } else if (resource instanceof OncodriveAnalysis) {
            return new OncodriveAnalysisEditor((OncodriveAnalysis) resource);
        } else if (resource instanceof CorrelationAnalysis) {
            return new CorrelationAnalysisEditor((CorrelationAnalysis) resource);
        } else if (resource instanceof CombinationAnalysis) {
            return new CombinationAnalysisEditor((CombinationAnalysis) resource);
        } else if (resource instanceof OverlappingAnalysis) {
            return new OverlappingAnalysisEditor((OverlappingAnalysis) resource);
        } else if (resource instanceof GroupComparisonAnalysis) {
            return new GroupComparisonAnalysisEditor((GroupComparisonAnalysis) resource);
        } else if (resource instanceof Heatmap) {
            ((Heatmap) resource).init();
            registerAssignedColors(((Heatmap) resource));
            return new HeatmapEditor((Heatmap) resource);
        }

        return createHeatmapEditor((IMatrix) resource, progressMonitor);
    }


    private AbstractEditor createHeatmapEditor(IMatrix resource, IProgressMonitor progressMonitor) throws CommandException {

        IMatrix matrix;
        try {
            //TODO MatrixConversion conversion = new MatrixConversion();
            //     matrix = conversion.convert(resource, progressMonitor);
            matrix = resource;
        } catch (Exception e) {
            e.printStackTrace();
            matrix = resource;
        }

        Heatmap heatmap = new Heatmap(matrix);

        if (rowsAnnotations != null) {
            File rowsFile = download(rowsAnnotations, progressMonitor);
            loadAnnotations(rowsFile, heatmap.getRows());
        }

        if (columnsAnnotations != null) {
            File colsFile = download(columnsAnnotations, progressMonitor);
            loadAnnotations(colsFile, heatmap.getColumns());
        }

        return new HeatmapEditor(heatmap);
    }

    private void registerAssignedColors(Heatmap heatmap) {

        ColorRegistry registry = ColorRegistry.get();

        for (HeatmapHeader h : heatmap.getColumns().getHeaders()) {
            if (h instanceof HeatmapColoredLabelsHeader) {
                HeatmapColoredLabelsHeader clh = (HeatmapColoredLabelsHeader) h;
                for (ColoredLabel cl : clh.getClusters()) {
                    registry.registerId(cl.getValue(), cl.getColor());
                }
            }
        }

        for (HeatmapHeader h : heatmap.getRows().getHeaders()) {
            if (h instanceof HeatmapColoredLabelsHeader) {
                HeatmapColoredLabelsHeader clh = (HeatmapColoredLabelsHeader) h;
                for (ColoredLabel cl : clh.getClusters()) {
                    registry.registerId(cl.getValue(), cl.getColor());
                }
            }
        }
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

    private static void loadAnnotations(File file, HeatmapDimension hdim) {
        hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), PersistenceManager.get().getFormat(TsvAnnotationMatrixFormat.EXTENSION, AnnotationMatrix.class)).get());
    }

}
