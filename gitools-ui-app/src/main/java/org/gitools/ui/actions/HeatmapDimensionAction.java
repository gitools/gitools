package org.gitools.ui.actions;

import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.matrix.model.MatrixDimensionKey;

public abstract class HeatmapDimensionAction extends HeatmapAction {

    private MatrixDimensionKey dimensionKey;

    public HeatmapDimensionAction(MatrixDimensionKey dimensionKey, String name) {
        super(name);

        this.dimensionKey = dimensionKey;
    }

    protected String getDimensionLabel() {
        return dimensionKey.getLabel();
    }

    protected MatrixDimensionKey getDimensionKey() {
        return dimensionKey;
    }

    protected HeatmapDimension getDimension() {
        return getHeatmap().getIdentifiers(dimensionKey);
    }
}
