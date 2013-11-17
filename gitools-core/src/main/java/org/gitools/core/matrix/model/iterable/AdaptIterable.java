package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.matrix.element.LayerAdapter;

import java.util.Iterator;

public class AdaptIterable<F> extends AbstractChainIterable<F, String> {

    private LayerAdapter<F> layerAdapter;

    public AdaptIterable(AbstractIterable<String> parentIterable, LayerAdapter<F> layerAdapter) {
        super(parentIterable);

        this.layerAdapter = layerAdapter;
    }

    @Override
    public Iterator<F> iterator() {
        return new AdaptIterator(newParentIterator());
    }

    private class AdaptIterator extends AbstractChainIterator<F, String> {

        public AdaptIterator(Iterator<String> parentIterator) {
            super(parentIterator);
        }

        @Override
        public F next() {

            parentNext();

            return layerAdapter.get(getMatrix(), getPosition());
        }
    }
}
