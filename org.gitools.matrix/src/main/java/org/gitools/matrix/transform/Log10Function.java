package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;

public class Log10Function extends ConfigurableTransformFunction {

    public Log10Function() {
        super("Log10", "Returns base 10 Log of given value");
    }

    @Override
    public Log10Function createNew() {
        return new Log10Function();
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return Math.log10(value);
        }
        return null;
    }

    @Override
    protected void createDefaultParameters() {

    }
}
