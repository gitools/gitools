package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;

public class AbsoluteFunction extends ConfigurableTransformFunction {


    public AbsoluteFunction() {
        super("Absolute value", "Returns positive absolute value");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return Math.abs(value);
        }
        return null;
    }

    @Override
    public AbsoluteFunction createNew() {
        return new AbsoluteFunction();
    }

    @Override
    protected void createDefaultParameters() {
    }

}
