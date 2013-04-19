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
import org.gitools.matrix.filter.MatrixViewAnnotationsFilter;
import org.gitools.matrix.filter.MatrixViewAnnotationsFilter.FilterDimension;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.matrix.IAnnotations;
import org.gitools.ui.dialog.filter.StringAnnotationsFilterPage;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;
import org.gitools.utils.progressmonitor.IProgressMonitor;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;

/**
 * @noinspection ALL
 */
public class FilterByAnnotations extends BaseAction {

    private static final long serialVersionUID = -1582437709508438222L;

    public FilterByAnnotations() {
        super("Filter by annotations");
        setDesc("Filter by annotations");
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IEditor editor = AppFrame.get().getEditorsPanel().getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap)) {
            return;
        }

        final Heatmap hm = (Heatmap) model;

        final StringAnnotationsFilterPage page = new StringAnnotationsFilterPage(hm);
        PageDialog dlg = new PageDialog(AppFrame.get(), page);
        dlg.setVisible(true);

        if (dlg.isCancelled()) {
            return;
        }

        final IMatrixView matrixView = hm;

        JobThread.execute(AppFrame.get(), new JobRunnable() {
            @Override
            public void run(@NotNull IProgressMonitor monitor) {
                monitor.begin("Filtering ...", 1);

                IAnnotations am = null;
                FilterDimension dim = page.getFilterDimension();
                switch (dim) {
                    case ROWS:
                        am = hm.getRows().getAnnotations();
                        break;
                    case COLUMNS:
                        am = hm.getColumns().getAnnotations();
                        break;
                }

                MatrixViewAnnotationsFilter.filter(matrixView, dim, page.getPattern(), am, page.getValues(), page.isUseRegexChecked());

                monitor.end();
            }
        });

        AppFrame.get().setStatusText("Filter by annotations done.");
    }
}
