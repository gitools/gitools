package org.gitools.matrix.model;


public interface IMatrixViewLayers<L extends IMatrixLayer> extends IMatrixLayers<L>
{

    int getTopLayerIndex();

    void setTopLayerIndex(int index);
}
