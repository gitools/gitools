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
package org.gitools.ui.actions.data;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.matrix.model.MatrixDimensionKey;
import org.gitools.core.matrix.sort.MatrixViewSorter;
import org.gitools.ui.actions.HeatmapAction;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.ui.settings.Settings;
import org.gitools.ui.sort.MutualExclusionSortPage;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

public class SortByMutualExclusionAction extends HeatmapAction {

    public SortByMutualExclusionAction() {
        super("Sort by mutual exclusion ...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap hm = getHeatmap();

        final MutualExclusionSortPage page = new MutualExclusionSortPage(hm);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);

        if (hm.getColumns().getSelected().size() > 0) {
            page.setDimension(MatrixDimensionKey.COLUMNS);
        }

        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                monitor.begin("Sorting ...", 1);

                MatrixViewSorter.sortByMutualExclusion(
                        hm,
                        page.getPattern(),
                        page.getValues(),
                        page.isUseRegexChecked(),
                        page.getDimension().equals(MatrixDimensionKey.COLUMNS),
                        monitor,
                        Settings.getDefault().isShowMutualExclusionProgress()
                );

                monitor.end();
            }
        });

        AppFrame.get().setStatusText("Sorted.");
    }
}
