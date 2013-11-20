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
