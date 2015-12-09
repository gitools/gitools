package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.api.matrix.TransformFunction;

public class LogNFunction extends ConfigurableTransformFunction {

    public LogNFunction() {
        super("LogN", "Returns natural Log of any given value");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return Math.log(value);
        }
        return null;
    }

    @Override
    protected void createDefaultParameters() {

    }
}
