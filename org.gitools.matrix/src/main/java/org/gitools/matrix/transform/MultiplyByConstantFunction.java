package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.DoubleParameter;

public class MultiplyByConstantFunction extends ConfigurableTransformFunction {

    public static final String CONSTANT = "Constant";
    DoubleParameter doubleParameter;

    public MultiplyByConstantFunction() {
        super("Multiply", "Multiplies the value by the defined constant");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return value * doubleParameter.getParameterValue();
        }
        return null;
    }

    @Override
    public MultiplyByConstantFunction createNew() {
        return new MultiplyByConstantFunction();
    }

    @Override
    protected void createDefaultParameters() {
        doubleParameter = new DoubleParameter();
        doubleParameter.setDescription("Define a constant which will be summed to all values");
        doubleParameter.setParameterValue(2.0);
        addParameter(CONSTANT, doubleParameter);
    }

    @Override
    public String getName() {
        return name + " by " + doubleParameter.getParameterValue();
    }
}
