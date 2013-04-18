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
package org.gitools.ui.actions;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapAnnotatedMatrixView;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.editor.AbstractEditor;
import org.gitools.ui.platform.editor.IEditor;
import org.jetbrains.annotations.Nullable;

@Deprecated // TODO we should find a better place/way to do this
public class ActionUtils {

    public static AbstractEditor getSelectedEditor() {
        return AppFrame.get().getEditorsPanel().getSelectedEditor();
    }

    @Nullable
    public static IMatrixView getMatrixView() {
        AbstractEditor editor = getSelectedEditor();
        if (editor == null) {
            return null;
        }

        IMatrixView matrixView = null;

        Object model = editor.getModel();
        if (model instanceof IMatrixView) {
            matrixView = (IMatrixView) model;
        } else if (model instanceof Heatmap) {
            matrixView = ((Heatmap) model);
        }

        return matrixView;
    }

    @Nullable
    public static IMatrixView getHeatmapMatrixView() {
        IMatrixView matrixView;
        IEditor editor = ActionUtils.getSelectedEditor();
        Object model = editor.getModel();
        if (model instanceof Heatmap) {
            matrixView = new HeatmapAnnotatedMatrixView((Heatmap) model);
        } else {
            matrixView = getMatrixView();
        }
        return matrixView;
    }

    @Nullable
    public static Heatmap getHeatmap() {
        AbstractEditor editor = getSelectedEditor();
        if (editor == null) {
            return null;
        }

        Heatmap figure = null;

        Object model = editor.getModel();
        if (model instanceof Heatmap) {
            figure = (Heatmap) model;
        }

        return figure;
    }
}
