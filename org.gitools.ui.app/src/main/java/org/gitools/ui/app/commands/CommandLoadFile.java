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
package org.gitools.ui.app.commands;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.analysis.correlation.CorrelationAnalysis;
import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.analysis.htest.oncodrive.OncodriveAnalysis;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IAnnotations;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.modulemap.IModuleMap;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.api.resource.ResourceReference;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.impl.BinaryDecorator;
import org.gitools.heatmap.header.ColoredLabel;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.format.AnnotationMatrixFormat;
import org.gitools.matrix.model.matrix.AnnotationMatrix;
import org.gitools.matrix.modulemap.ModuleMapUtils;
import org.gitools.persistence.locators.UrlResourceLocator;
import org.gitools.ui.app.analysis.combination.editor.CombinationAnalysisEditor;
import org.gitools.ui.app.analysis.correlation.editor.CorrelationAnalysisEditor;
import org.gitools.ui.app.analysis.groupcomparison.editor.GroupComparisonAnalysisEditor;
import org.gitools.ui.app.analysis.htest.editor.EnrichmentAnalysisEditor;
import org.gitools.ui.app.analysis.htest.editor.OncodriveAnalysisEditor;
import org.gitools.ui.app.analysis.overlapping.OverlappingAnalysisEditor;
import org.gitools.ui.app.fileimport.ImportManager;
import org.gitools.ui.app.fileimport.ImportWizard;
import org.gitools.ui.app.genomespace.GsResourceLocator;
import org.gitools.ui.app.genomespace.dm.HttpUtils;
import org.gitools.ui.app.heatmap.editor.HeatmapEditor;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.dialog.MessageUtils;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.utils.color.ColorRegistry;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.CancellationException;

import static org.gitools.api.ApplicationContext.getPersistenceManager;

public class CommandLoadFile extends AbstractCommand implements ImportWizard.Callback {

    //private final String file;
    private IResourceLocator locator;
    private IResourceFormat format = null;
    private final String rowsAnnotations;
    private final String columnsAnnotations;

    public CommandLoadFile(ResourceReference reference) {
        this(reference.getLocator(), reference.getResourceFormat());
    }

    public CommandLoadFile(IResourceLocator locator, IResourceFormat resourceFormat) {
        this(locator, null, null);
        this.format = resourceFormat;
    }

    public CommandLoadFile(String file) {
        this(file, null, null);
    }

    public CommandLoadFile(String file, IResourceFormat resourceFormat) {
        this(file);
        this.format = resourceFormat;
    }

    public CommandLoadFile(String file, String rowsAnnotations, String columnsAnnotations) {
        this(new GsResourceLocator(new UrlResourceLocator(file)), rowsAnnotations, columnsAnnotations);
    }

    public CommandLoadFile(IResourceLocator locator, String rowsAnnotations, String columnsAnnotations) {
        this.locator = locator;
        this.rowsAnnotations = rowsAnnotations;
        this.columnsAnnotations = columnsAnnotations;
    }

    @Override
    public void execute(IProgressMonitor monitor) throws CommandException {

        if (isConfigurable()) {
            try {
                getConfigWizard().run(monitor);
            } catch (Exception e) {
                new CommandException(e);
            }
            return;
        }

        final IResource resource;

        try {
            monitor.begin("Loading ...", 1);
            resource = loadResource(monitor);
        } catch (Exception e) {

            if (!(e.getCause() instanceof CancellationException)) {
                MessageUtils.showErrorMessage(Application.get(), "<html>This file format either not supported or is malformed.<br>" +
                        (!StringUtils.isEmpty(e.getCause().getMessage()) ? "<div style='margin: 5px 0px; padding:10px; width:300px; border: 1px solid black;'><strong>" + e.getCause().getMessage() + "</strong></div>" : "") +
                        "Check the supported file formats at the <strong>'User guide'</strong> on <a href='http://www.gitools.org'>www.gitools.org</a><br></html>", e);

                Application.get().trackException("Bad file format: " + e.getCause().getMessage());
            }
            setExitStatus(1);
            return;
        }

        afterLoad(resource, monitor);
    }

    public void afterLoad(IResource resource, IProgressMonitor monitor) throws CommandException {

        monitor.begin("Initializing editor ...", 1);
        final AbstractEditor editor = createEditor(resource, monitor);
        editor.setName(getResourceLocator().getBaseName());

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Application.get().getEditorsPanel().addEditor(editor);
                Application.get().refresh();
            }
        });

        setExitStatus(0);
    }

    protected IResource loadResource(IProgressMonitor monitor) {
        return getPersistenceManager().load(getResourceLocator(), getFormat(), monitor);
    }

    protected IResourceLocator getResourceLocator() {
        return locator;
    }

    public boolean isConfigurable() {
        return getFormat() == null && ImportManager.get().isImportable(getResourceLocator());
    }

    public ImportWizard getConfigWizard() {

        IResourceLocator locator = getResourceLocator();
        locator = getPersistenceManager().applyCache( locator );
        locator = getPersistenceManager().applyFilters( locator );

        ImportWizard wizard = ImportManager.get().getWizard(locator);
        wizard.setCallback(this);

        return wizard;
    }

    private IResourceFormat getFormat() {
        if (format == null) {
            format = getPersistenceManager().getFormat(getResourceLocator().getName(), IResource.class);
        }

        // We don't want to Open annotation matrices
        //TODO remove all IAnnotations stuff and use normal IMatrix
        if (format != null && IAnnotations.class.isAssignableFrom(format.getResourceClass())) {
            format = null;
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
        } else if (IModuleMap.class.isAssignableFrom(resource.getClass())) {
            Heatmap heatmap = new Heatmap(ModuleMapUtils.convertToMatrix((IModuleMap) resource));
            heatmap.getLayers().iterator().next().setDecorator(new BinaryDecorator());
            return new HeatmapEditor(heatmap);
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
        hdim.addAnnotations(new ResourceReference<>(new UrlResourceLocator(file), getPersistenceManager().getFormat(AnnotationMatrixFormat.EXTENSION, AnnotationMatrix.class)).get());
    }

}
