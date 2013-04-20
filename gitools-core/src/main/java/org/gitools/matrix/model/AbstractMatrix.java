package org.gitools.matrix.model;

import org.gitools.model.Resource;

public abstract class AbstractMatrix extends Resource implements IMatrix {

    @Override
    public Object getValue(int row, int column, int layerIndex) {
        return getValue(new int[]{ row, column }, layerIndex);
    }

    @Override
    public void setValue(int row, int column, int layer, Object value) {
        setValue(new int[]{row, column}, layer, value);
    }

    @Override
    public void detach() {
    }

}
