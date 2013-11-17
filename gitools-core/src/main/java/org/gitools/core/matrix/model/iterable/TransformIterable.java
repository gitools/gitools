package org.gitools.core.matrix.model.iterable;

import org.gitools.core.matrix.model.IMatrixFunction;

import java.util.Iterator;

public class TransformIterable<F, T> extends AbstractChainIterable<F, T> {

    private IMatrixFunction<F, T> function;

    public TransformIterable(AbstractIterable<T> parentIterable, IMatrixFunction<F, T> function) {
        super(parentIterable);

        this.function = function;
    }

    @Override
    public Iterator<F> iterator() {
        function.onBeforeIterate(getParentIterable());
        return new TransformIterator(newParentIterator());
    }

    private class TransformIterator extends AbstractChainIterator<F, T> {

        public TransformIterator(Iterator<T> parentIterator) {
            super(parentIterator);
        }

        @Override
        public boolean hasNext() {

            if (!super.hasNext()) {
                function.onAfterIterate(getParentIterable());
                return false;
            }

            return true;
        }

        @Override
        public F next() {
            return function.apply(parentNext(), getPosition());
        }
    }
}
