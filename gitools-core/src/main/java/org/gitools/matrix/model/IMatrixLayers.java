package org.gitools.matrix.model;

public interface IMatrixLayers<L extends IMatrixLayer> extends Iterable<L>
{

    public L get(int layerIndex);

    public int findId(String id);

    public int size();
}
