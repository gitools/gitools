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

import com.google.common.collect.Sets;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.api.matrix.view.IMatrixView;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.matrix.filter.FilterByLabelPredicate;
import org.gitools.matrix.filter.PatternFunction;
import org.gitools.ui.app.dialog.filter.StringAnnotationsFilterPage;
import org.gitools.ui.platform.Application;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.IEditor;
import org.gitools.ui.platform.progress.JobRunnable;
import org.gitools.ui.platform.progress.JobThread;
import org.gitools.ui.platform.wizard.PageDialog;

import java.awt.event.ActionEvent;

public class FilterByAnnotationsAction extends BaseAction {

    private MatrixDimensionKey dimension;

    public FilterByAnnotationsAction(MatrixDimensionKey dimension) {
        super("<html><i>Filter</i> by annotations</html>");
        setDesc("Filter by annotations");

        this.dimension = dimension;
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap || model instanceof IMatrixView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        IEditor editor = Application.get().getEditorsPanel().getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (!(model instanceof Heatmap)) {
            return;
        }

        final Heatmap hm = (Heatmap) model;

        final StringAnnotationsFilterPage page = new StringAnnotationsFilterPage(hm, dimension);
        PageDialog dlg = new PageDialog(Application.get(), page);
        dlg.open();

        if (dlg.isCancelled()) {
            return;
        }

        JobThread.execute(Application.get(), new JobRunnable() {
            @Override
            public void run(IProgressMonitor monitor) {
                monitor.begin("Filtering ...", 1);

                HeatmapDimension heatmapDimension = hm.getDimension(page.getFilterDimension());

                heatmapDimension.show(
                        new FilterByLabelPredicate(
                                new PatternFunction(page.getPattern(), heatmapDimension.getAnnotations()),
                                Sets.newHashSet(page.getValues()),
                                page.isUseRegexChecked()
                        )
                );

            }
        });

        Application.get().setStatusText("Filter by annotations done.");
    }
}
