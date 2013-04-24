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

import org.gitools.core.persistence._DEPRECATED.FileFormat;
import org.gitools.core.persistence._DEPRECATED.FileFormats;
import org.gitools.core.persistence.formats.analysis.*;
import org.gitools.ui.IconNames;
import org.gitools.ui.commands.CommandLoadFile;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.utils.FileChooserUtils;
import org.gitools.ui.utils.FileFormatFilter;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * @noinspection ALL
 */
public class OpenAction extends BaseAction {

    private static final long serialVersionUID = -6528634034161710370L;

    public OpenAction() {
        super("from filesystem...");
        setDesc("Open a heatmap or an analysis from the filesystem");
        setSmallIconFromResource(IconNames.openMatrix16);
        setLargeIconFromResource(IconNames.openMatrix24);
        setMnemonic(KeyEvent.VK_M);
        setDefaultEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileFormatFilter[] filters = new FileFormatFilter[]{

                new FileFormatFilter("All known formats",
                        new FileFormat[]{
                                HeatmapFormat.FILE_FORMAT,
                                FileFormats.MULTIVALUE_DATA_MATRIX,
                                FileFormats.DOUBLE_MATRIX,
                                FileFormats.DOUBLE_BINARY_MATRIX,
                                FileFormats.GENE_CLUSTER_TEXT,
                                FileFormats.GENE_MATRIX,
                                FileFormats.GENE_MATRIX_TRANSPOSED,
                                FileFormats.COMPRESSED_MATRIX,
                                EnrichmentAnalysisFormat.FILE_FORMAT,
                                OncodriveAnalysisFormat.FILE_FORMAT,
                                CorrelationAnalysisFormat.FILE_FORMAT,
                                CombinationAnalysisFormat.FILE_FORMAT,
                                OverlappingAnalysisFormat.FILE_FORMAT,
                                GroupComparisonAnalysisFormat.FILE_FORMAT
                        }),
                new FileFormatFilter("Heatmaps",
                        new FileFormat[]{
                                HeatmapFormat.FILE_FORMAT,
                                FileFormats.MULTIVALUE_DATA_MATRIX,
                                FileFormats.DOUBLE_MATRIX,
                                FileFormats.DOUBLE_BINARY_MATRIX,
                                FileFormats.GENE_CLUSTER_TEXT,
                                FileFormats.GENE_MATRIX,
                                FileFormats.GENE_MATRIX_TRANSPOSED
                        }),
                new FileFormatFilter("Analysis",
                        new FileFormat[]{
                                EnrichmentAnalysisFormat.FILE_FORMAT,
                                OncodriveAnalysisFormat.FILE_FORMAT,
                                CorrelationAnalysisFormat.FILE_FORMAT,
                                CombinationAnalysisFormat.FILE_FORMAT,
                                OverlappingAnalysisFormat.FILE_FORMAT,
                                GroupComparisonAnalysisFormat.FILE_FORMAT
                        }),

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
        };

        final File file = FileChooserUtils.selectFile("Select file", FileChooserUtils.MODE_OPEN, filters);

        if (file == null) {
            return;
        }

        Settings.getDefault().setLastPath(file.getParent());
        Settings.getDefault().save();

        JobRunnable loadFile = new CommandLoadFile(file.getAbsolutePath());
        JobThread.execute(AppFrame.get(), loadFile);

        AppFrame.get().setStatusText("Done.");
    }
}
