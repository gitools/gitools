package org.gitools.matrix.model;

import org.gitools.matrix.model.element.ILayerDescriptor;

public interface IMatrixLayers extends Iterable<ILayerDescriptor>
{

    public ILayerDescriptor get(int layerIndex);

    public int findId(String id);

    public int size();
}
