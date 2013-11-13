package org.gitools.core.matrix.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MatrixIterator implements IMatrixIterator {

    private boolean build = false;

    private Map<String, Iterable<String>> iterables = new HashMap<>();
    private Map<String, Iterator<String>> iterators = new HashMap<>();
    private Map<String, String> positions = new HashMap<>(3);

    @Override
    public IMatrixIterator iterate(IMatrixDimension dimension, Iterable<String> identifiers) {
        assert !build : "It's not possible to change the identifiers to iterate after the iterator is build";

        iterables.put(dimension.getId(), identifiers);
        return this;
    }

    @Override
    public IMatrixIterator build() {
        assert !build : "This iterator is already build";

        for (String dimension : iterables.keySet()) {
            iterators.put(dimension, iterables.get(dimension).iterator());
        }

        build = true;

        return this;
    }

    @Override
    public boolean next(IMatrixDimension dimension) {
        assert build : "Iterator must be build first";
        assert iterators.containsKey(dimension.getId()) : "Unknown iteration dimension";

        Iterator<String> iterator = iterators.get(dimension.getId());

        if (iterator.hasNext()) {
            positions.put(dimension.getId(), iterator.next());
            return true;
        }

        return false;
    }

    @Override
    public void reset(IMatrixDimension dimension) {
        assert build : "First you must call iterator build";
        assert iterables.containsKey(dimension.getId()) : "Unknown iteration dimension";

        iterators.put(dimension.getId(), iterables.get(dimension.getId()).iterator());
    }

    @Override
    public String get(IMatrixDimension dimension) {
        return positions.get(dimension.getId());
    }


}
