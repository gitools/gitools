package org.gitools.matrix.model;


import org.gitools.matrix.model.element.ILayerDescriptor;

public interface IMatrixViewLayers<L extends ILayerDescriptor> extends IMatrixLayers<L>
{

    int getTopLayer();

    void setTopLayer(int index);
}
