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

import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.filter.MatrixViewValueFilter;
import org.gitools.matrix.filter.ValueFilterCriteria;
import org.gitools.matrix.model.IMatrixLayers;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.actions.ActionUtils;
import org.gitools.ui.dialog.filter.ValueFilterDialog;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.utils.cutoffcmp.CutoffCmp;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 * @noinspection ALL
 */
public class FilterByValueAction extends BaseAction
{

    private static final long serialVersionUID = -1582437709508438222L;

    public FilterByValueAction()
    {
        super("Filter by values...");
        setDesc("Filter by values");
    }

    @Override
    public boolean isEnabledByModel(Object model)
    {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        final IMatrixView matrixView = ActionUtils.getMatrixView();
        if (matrixView == null)
        {
            return;
        }

        IMatrixLayers attributes = matrixView.getContents().getLayers();

        int pvalueIndex = -1;
        String[] attrNames = new String[attributes.size()];
        for (int i = 0; i < attributes.size(); i++)
        {
            attrNames[i] = attributes.get(i).getName();
            if (pvalueIndex == -1 && attrNames[i].contains("p-value"))
            {
                pvalueIndex = i;
            }
        }
        if (pvalueIndex == -1)
        {
            pvalueIndex = 0;
        }

        ArrayList<ValueFilterCriteria> initialCriteria = new ArrayList<ValueFilterCriteria>(1);
        initialCriteria.add(new ValueFilterCriteria(attrNames[pvalueIndex], pvalueIndex, CutoffCmp.LT, 0.05));

        final ValueFilterDialog dlg = new ValueFilterDialog(AppFrame.get(), attrNames, CutoffCmp.comparators, initialCriteria);

        dlg.setVisible(true);

        if (dlg.getReturnStatus() != ValueFilterDialog.RET_OK)
        {
            AppFrame.get().setStatusText("Filter cancelled.");
            return;
        }

        JobThread.execute(AppFrame.get(), new JobRunnable()
        {
            @Override
            public void run(@NotNull IProgressMonitor monitor)
            {
                monitor.begin("Filtering ...", 1);

                MatrixViewValueFilter.filter(matrixView, dlg.getCriteriaList(), dlg.isAllCriteriaChecked(), dlg.isAllElementsChecked(), dlg.isInvertCriteriaChecked(), dlg.isApplyToRowsChecked(), dlg.isApplyToColumnsChecked());
            }
        });

        AppFrame.get().setStatusText("Filter applied.");
    }
}
