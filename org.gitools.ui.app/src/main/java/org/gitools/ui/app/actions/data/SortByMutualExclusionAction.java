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
package org.gitools.ui.app.actions.data;

import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.MatrixViewSorter;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.settings.Settings;
import org.gitools.ui.app.sort.MutualExclusionSortPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;

public class SortByMutualExclusionAction extends HeatmapAction {

    private MatrixDimensionKey dimensionKey;

    public SortByMutualExclusionAction(MatrixDimensionKey dimensionKey) {
        super("<html><i>Sort</i> by mutual exclusion</html>");

        this.dimensionKey = dimensionKey;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final Heatmap hm = getHeatmap();

        final MutualExclusionSortPage page = new MutualExclusionSortPage(hm, dimensionKey);
        PageDialog dlg = new PageDialog(Application.get(), page);

        /*if (hm.getColumns().getSelected().size() > 0) {
            page.setDimension(MatrixDimensionKey.COLUMNS);
        }*/

        dlg.open();

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
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

            }
        });

        Application.get().setStatusText("Sorted.");
    }
}
