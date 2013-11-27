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

import com.google.common.collect.ObjectArrays;
import org.gitools.api.matrix.IMatrix;
import org.gitools.api.resource.IResourceFormat;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence.formats.FileFormat;
import org.gitools.persistence.formats.FileFormats;
import org.gitools.persistence.formats.analysis.*;
import org.gitools.persistence.formats.matrix.CdmMatrixFormat;
import org.gitools.persistence.formats.matrix.TdmMatrixFormat;
import org.gitools.ui.IconNames;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.fileimport.ImportManager;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChoose;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.FileFormatFilter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            FileFormats.GENE_MATRIX_TRANSPOSED
    };



    private static final long serialVersionUID = -6528634034161710370L;

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
        List<FileFormatFilter> filters = new ArrayList<>();

        Collections.addAll(filters,
                new FileFormatFilter("All known formats", FileFormat.concat(
                            ImportManager.get().getFileFormats(),
                            ObjectArrays.concat(FORMAT_HEATMAPS, FORMAT_ANALYSIS, FileFormat.class)
                        )),
                new FileFormatFilter("Heatmaps", FORMAT_HEATMAPS),
                new FileFormatFilter("Analysis", FORMAT_ANALYSIS),

                new FileFormatFilter(HeatmapFormat.FILE_FORMAT),
                new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX),
                new FileFormatFilter(FileFormats.MULTIVALUE_DATA_MATRIX.getTitle() + " (*.*)"),
                new FileFormatFilter(FileFormats.DOUBLE_MATRIX),
                new FileFormatFilter(FileFormats.DOUBLE_MATRIX.getTitle() + " (*.*)"),
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

        filters.addAll( ImportManager.get().getFileFormatFilters());

        final FileChoose fileChoose = FileChooserUtils.selectFile("Select file", FileChooserUtils.MODE_OPEN, filters.toArray(new FileFormatFilter[filters.size()]));

        if (fileChoose == null) {
            return;
        }

        Settings.getDefault().setLastPath(fileChoose.getFile().getParent());
        Settings.getDefault().save();

        IResourceFormat format = null;
        if (fileChoose.getFilter() != null) {
            if (fileChoose.getFilter().getDescription().startsWith(FileFormats.MULTIVALUE_DATA_MATRIX.getTitle())) {
                format = PersistenceManager.get().getFormat(TdmMatrixFormat.EXSTENSION, IMatrix.class);
            }
            if (fileChoose.getFilter().getDescription().startsWith(FileFormats.DOUBLE_MATRIX.getTitle())) {
                format = PersistenceManager.get().getFormat(CdmMatrixFormat.EXTENSION, IMatrix.class);
            }
        }

        CommandLoadFile loadFile = new CommandLoadFile(fileChoose.getFile().getAbsolutePath(), format);
        JobThread.execute(Application.get(), loadFile);
        Application.get().setStatusText("Done.");
    }
}
