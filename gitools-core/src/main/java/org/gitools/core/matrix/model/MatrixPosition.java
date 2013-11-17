package org.gitools.core.matrix.model;

import org.gitools.core.matrix.model.iterable.IdentifierSourceIterable;
import org.gitools.core.matrix.model.iterable.ValueSourceIterable;
import org.gitools.core.matrix.model.matrix.element.LayerAdapter;

import java.util.HashMap;
import java.util.Map;

public class MatrixPosition implements IMatrixPosition {

    private IMatrix matrix;

    private MatrixDimensionKey[] dimensions;
    private String[] identifiers;
    private Map<MatrixDimensionKey, Integer> positions;

    public MatrixPosition(IMatrix matrix) {
        super();

        this.matrix = matrix;
        this.dimensions = matrix.getDimensions();
        this.identifiers = new String[dimensions.length];
        this.positions = new HashMap<>(dimensions.length);

        for (int i=0; i < dimensions.length; i++) {
            this.positions.put(dimensions[i], i);
        }
    }

    @Override
    public IMatrix getMatrix() {
        return matrix;
    }

    @Override
    public String get(IMatrixDimension dimension) {
        return get(dimension.getId());
    }

    @Override
    public String get(MatrixDimensionKey dimension) {
        return identifiers[positions.get(dimension)];
    }

    @Override
    public IMatrixPosition set(IMatrixDimension dimension, String identifier) {
        return set(dimension.getId(), identifier);
    }

    @Override
    public MatrixPosition set(MatrixDimensionKey dimension, String identifier) {

        if (identifier != null) {
            identifiers[positions.get(dimension)] = identifier;
        }

        return this;
    }

    @Override
    public String[] toVector() {
        return identifiers;
    }

    public MatrixPosition set(String... identifiers) {
        assert identifiers.length == dimensions.length : "This matrix position has " + dimensions.length + " and your identifier vector " + identifiers.length;
        this.identifiers = identifiers;
        return this;
    }

    @Override
    public IMatrixIterable<String> iterate(IMatrixDimension dimension) {
        return iterate(dimension.getId());
    }

    @Override
    public IMatrixIterable<String> iterate(MatrixDimensionKey dimensionKey) {
        return new IdentifierSourceIterable(this, dimensionKey);
    }

    @Override
    public <T> IMatrixIterable<T> iterate(LayerAdapter<T> layerAdapter, IMatrixDimension dimension) {
        return new AdapterSourceIterable<>(this, dimension.getId(), layerAdapter);
    }

    @Override
    public <T> IMatrixIterable<T> iterate(IMatrixLayer<T> layer, IMatrixDimension dimension) {
        return new ValueSourceIterable<>(this, dimension.getId(), layer);
    }

}
