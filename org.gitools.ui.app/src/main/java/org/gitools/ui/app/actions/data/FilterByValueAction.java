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
import org.gitools.api.matrix.IMatrixLayer;
import org.gitools.api.matrix.IMatrixLayers;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.analysis._DEPRECATED.matrix.filter.MatrixViewValueFilter;
import org.gitools.analysis._DEPRECATED.matrix.filter.ValueFilterCriteria;
import org.gitools.ui.app.actions.HeatmapAction;
import org.gitools.ui.app.dialog.filter.ValueFilterPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.utils.cutoffcmp.CutoffCmp;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class FilterByValueAction extends HeatmapAction {

    private static final long serialVersionUID = -1582437709508438222L;

    public FilterByValueAction() {
        super("Filter by values...");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        final IMatrixView matrixView = getHeatmap();

        IMatrixLayers attributes = matrixView.getContents().getLayers();


        String[] attrNames = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++) {
            attrNames[i] = attributes.get(i).getName();
        }
        IMatrixLayer selectedLayer = matrixView.getLayers().getTopLayer();


        ArrayList<ValueFilterCriteria> initialCriteria = new ArrayList<>(1);
        initialCriteria.add(new ValueFilterCriteria(selectedLayer, CutoffCmp.LT, 0.05));

        final ValueFilterPage page = new ValueFilterPage(Application.get(),
                attrNames,
                CutoffCmp.comparators,
                initialCriteria,
                selectedLayer);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Filtering ...", 1);

                MatrixViewValueFilter.filter(matrixView, page.getCriteriaList(),
                        page.isAllCriteriaMatch(),
                        page.isAllElementsMatch(),
                        page.isHideMatching(),
                        page.isApplyToRows(),
                        page.isApplyToColumns());
            }
        });

        Application.get().setStatusText("Filter applied.");
    }
}
