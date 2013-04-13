package org.gitools.matrix.model;

import org.gitools.matrix.model.element.ILayerDescriptor;

public interface IMatrixLayers<L extends ILayerDescriptor> extends Iterable<L>
{

    public L get(int layerIndex);

    public int findId(String id);

    public int size();
}
