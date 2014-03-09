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

import com.google.common.collect.ObjectArrays;
import org.gitools.analysis.combination.format.CombinationAnalysisFormat;
import org.gitools.analysis.correlation.format.CorrelationAnalysisFormat;
import org.gitools.analysis.groupcomparison.format.GroupComparisonAnalysisFormat;
import org.gitools.analysis.htest.enrichment.format.EnrichmentAnalysisFormat;
import org.gitools.analysis.htest.oncodrive.format.OncodriveAnalysisFormat;
import org.gitools.analysis.overlapping.format.OverlappingAnalysisFormat;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.heatmap.format.HeatmapFormat;
import org.gitools.matrix.FileFormats;
import org.gitools.matrix.format.CdmMatrixFormat;
import org.gitools.matrix.format.TdmMatrixFormat;
import org.gitools.ui.app.IconNames;
import org.gitools.ui.app.commands.CommandLoadFile;
import org.gitools.ui.app.fileimport.ImportManager;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.utils.FileChoose;
import org.gitools.ui.app.utils.FileChooserUtils;
import org.gitools.ui.app.utils.FileFormatFilter;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobThread;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.gitools.api.ApplicationContext.getPersistenceManager;

public class OpenFromFilesystemAction extends BaseAction {

    public static FileFormat[] FORMAT_ANALYSIS = new FileFormat[]{
            EnrichmentAnalysisFormat.FILE_FORMAT,
            OncodriveAnalysisFormat.FILE_FORMAT,
            CorrelationAnalysisFormat.FILE_FORMAT,
            CombinationAnalysisFormat.FILE_FORMAT,
            OverlappingAnalysisFormat.FILE_FORMAT,
            GroupComparisonAnalysisFormat.FILE_FORMAT
    };

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
                        ObjectArrays.concat(FORMAT_HEATMAPS, FORMAT_ANALYSIS, FileFormat.class)
                )),
                new FileFormatFilter("Analysis", FORMAT_ANALYSIS),

                new FileFormatFilter(HeatmapFormat.FILE_FORMAT),
                new FileFormatFilter("All files"),
                new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX),
                new FileFormatFilter(FileFormats.DOUBLE_MATRIX),
                new FileFormatFilter(FileFormats.GENE_CLUSTER_TEXT),
                new FileFormatFilter(FileFormats.DOUBLE_BINARY_MATRIX),
                new FileFormatFilter(FileFormats.GENE_MATRIX),
                new FileFormatFilter(FileFormats.GENE_MATRIX_TRANSPOSED),

                // Analysis
                new FileFormatFilter(EnrichmentAnalysisFormat.FILE_FORMAT),
                new FileFormatFilter(OncodriveAnalysisFormat.FILE_FORMAT),
                new FileFormatFilter(CorrelationAnalysisFormat.FILE_FORMAT),
                new FileFormatFilter(OverlappingAnalysisFormat.FILE_FORMAT),
                new FileFormatFilter(GroupComparisonAnalysisFormat.FILE_FORMAT),
                new FileFormatFilter(CombinationAnalysisFormat.FILE_FORMAT)
        );

        filters.addAll(ImportManager.get().getFileFormatFilters());

        FILE_FORMAT_FILTERS = filters.toArray(new FileFormatFilter[filters.size()]);
    }

    public OpenFromFilesystemAction() {
        super("Open...");
        setDesc("Open a heatmap or an analysis from the filesystem");
        setSmallIconFromResource(IconNames.openMatrix16);
        setLargeIconFromResource(IconNames.openMatrix24);
        setMnemonic(KeyEvent.VK_O);
        setDefaultEnabled(true);
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
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

        CommandLoadFile loadFile = new CommandLoadFile(fileChoose.getFile().getAbsolutePath(), format);
        JobThread.execute(Application.get(), loadFile);
        Application.get().setStatusText("Done.");
    }
}
