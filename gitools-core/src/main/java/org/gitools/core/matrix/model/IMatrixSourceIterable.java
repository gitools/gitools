package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;

public interface IMatrixSourceIterable extends IMatrixIterable<String> {

    IMatrixSourceIterable from(String fromIdentifier);

    IMatrixSourceIterable to(String toIdentifier);

    IMatrixSourceIterable between(String fromIdentifier, String toIdentifier);

    <F> IMatrixIterable<F> adapt(LayerAdapter<F> layerAdapter);

}
