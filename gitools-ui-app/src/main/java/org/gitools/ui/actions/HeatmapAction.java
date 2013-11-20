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

import org.gitools.core.heatmap.Heatmap;
import org.gitools.ui.platform.AppFrame;
import org.gitools.ui.platform.actions.BaseAction;
import org.gitools.ui.platform.editor.EditorsPanel;
import org.gitools.ui.platform.editor.IEditor;

public abstract class HeatmapAction extends BaseAction {

    public HeatmapAction(String name) {
        super(name);

        setDesc(name);
    }

    @Override
    public boolean isEnabledByModel(Object model) {
        return model instanceof Heatmap;
    }

    protected IEditor getSelectedEditor() {
        return getEditorsPanel().getSelectedEditor();
    }

    protected EditorsPanel getEditorsPanel() {
        return AppFrame.get().getEditorsPanel();
    }

    protected Heatmap getHeatmap() {

        IEditor editor = getSelectedEditor();

        Object model = editor != null ? editor.getModel() : null;
        if (model == null || !(model instanceof Heatmap)) {
            throw new UnsupportedOperationException("This action is only valid on a heatmap editor");
        }

        return (Heatmap) model;
    }
}
