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
package org.gitools.ui.app.actions.edit;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResource;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.format.HeatmapFormat;
import org.gitools.matrix.FileFormats;
import org.gitools.matrix.format.CdmMatrixFormat;
import org.gitools.matrix.format.TdmMatrixFormat;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.fileimport.ImportManager;
import org.gitools.ui.core.Application;
import org.gitools.ui.core.actions.HeatmapAction;
import org.gitools.ui.core.utils.FileChoose;
import org.gitools.ui.core.utils.FileChooserUtils;
import org.gitools.ui.core.utils.FileFormatFilter;
import org.gitools.ui.platform.icons.IconNames;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.settings.Settings;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.gitools.api.ApplicationContext.getPersistenceManager;

public class AddNewLayersFromFileAction extends HeatmapAction {

    public static FileFormat[] FORMAT_HEATMAPS = new FileFormat[]{
            HeatmapFormat.FILE_FORMAT,
            FileFormats.MULTIVALUE_DATA_MATRIX,
            FileFormats.DOUBLE_MATRIX,
            FileFormats.DOUBLE_BINARY_MATRIX,
            FileFormats.GENE_CLUSTER_TEXT,
            FileFormats.GENE_MATRIX,
            FileFormats.GENE_MATRIX_TRANSPOSED,
            FileFormats.DOUBLE_BINARY_MATRIX,
            FileFormats.MODULES_INDEXED_MAP,
            FileFormats.MODULES_2C_MAP
    };

    public static FileFormatFilter[] FILE_FORMAT_FILTERS;

    static {

        List<FileFormatFilter> filters = new ArrayList<>();

        Collections.addAll(filters,
                new FileFormatFilter("All known formats", FileFormat.concat(
                        ImportManager.get().getFileFormats(),
                        FORMAT_HEATMAPS
                )),
                new FileFormatFilter(HeatmapFormat.FILE_FORMAT),
                new FileFormatFilter("All files"),
                new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX),
                new FileFormatFilter(FileFormats.DOUBLE_MATRIX),
                new FileFormatFilter(FileFormats.GENE_CLUSTER_TEXT),
                new FileFormatFilter(FileFormats.DOUBLE_BINARY_MATRIX),
                new FileFormatFilter(FileFormats.GENE_MATRIX),
                new FileFormatFilter(FileFormats.GENE_MATRIX_TRANSPOSED)
        );

        filters.addAll(ImportManager.get().getFileFormatFilters());

        FILE_FORMAT_FILTERS = filters.toArray(new FileFormatFilter[filters.size()]);
    }

    public AddNewLayersFromFileAction() {
        super("New data from file...");
        setSmallIconFromResource(IconNames.add16);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return (model instanceof Heatmap) && (((Heatmap) model).getContents() instanceof HashMatrix);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final FileChoose fileChoose = FileChooserUtils.selectFile("Select file", FileChooserUtils.MODE_OPEN, FILE_FORMAT_FILTERS);

        if (fileChoose == null) {
            return;
        }

        Settings.get().setLastPath(fileChoose.getFile().getParent());
        Settings.get().save();

        IResourceFormat format = null;
        if (fileChoose.getFilter() != null) {
            if (fileChoose.getFilter().getDescription().startsWith(FileFormats.MULTIVALUE_DATA_MATRIX.getTitle())) {
                format = getPersistenceManager().getFormat(TdmMatrixFormat.EXSTENSION, IMatrix.class);
            }
            if (fileChoose.getFilter().getDescription().startsWith(FileFormats.DOUBLE_MATRIX.getTitle())) {
                format = getPersistenceManager().getFormat(CdmMatrixFormat.EXTENSION, IMatrix.class);
            }
        }

        CommandLoadFile loadFile = new CommandLoadFile(fileChoose.getFile().getAbsolutePath(), format) {
            @Override
            public void afterLoad(IResource resource, IProgressMonitor monitor) throws CommandException {

                // A IMatrix
                if (IMatrix.class.isAssignableFrom(resource.getClass())) {

                    HashMatrix mainData = (HashMatrix) getHeatmap().getContents();
                    IMatrix newData = (IMatrix) resource;

                    for (IMatrixLayer newLayer : newData.getLayers()) {

                        IMatrixLayer mainLayer = mainData.getLayers().get(newLayer.getId());
                        if (mainLayer != null) {
                            //TODO Support data fusion ??
                            throw new UnsupportedOperationException("Overwrite layer data is not allowed. The layer '" + newLayer.getId() + "' already exist.");
                        }

                        mainData.addLayer(newLayer);
                        getHeatmap().getLayers().initLayer(newLayer);
                        mainLayer = mainData.getLayers().get(newLayer.getId());

                        copyLayerValues(newData, newLayer, mainData, mainLayer);

                    }

                    getHeatmap().getLayers().updateLayers();

                    super.afterLoad(resource, monitor);
                }
            }
        };
        JobThread.execute(Application.get(), loadFile);
        Application.get().showNotification("New data layer added");
    }

    private static void copyLayerValues(IMatrix fromMatrix, IMatrixLayer fromLayer, IMatrix toMatrix, IMatrixLayer toLayer) {

        if ((fromMatrix instanceof HashMatrix) && (toMatrix instanceof HashMatrix)) {
            HashMatrix fromHM = (HashMatrix) fromMatrix;
            HashMatrix toHM = (HashMatrix) toMatrix;
            toHM.copyLayerValues(fromLayer.getId(), fromHM);
        } else {
            for (String column : fromMatrix.getColumns()) {
                for (String row : fromMatrix.getRows()) {
                    toMatrix.set(toLayer, fromMatrix.get(fromLayer, row, column), row, column);
                }
            }
        }

    }

}
