package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;

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
    public LogNFunction createNew() {
        return new LogNFunction();
    }

    @Override
    protected void createDefaultParameters() {

    }
}
