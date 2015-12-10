package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.DoubleParameter;

public class SumConstantFunction extends ConfigurableTransformFunction {

    public static final String CONSTANT = "Constant";
    DoubleParameter doubleParameter;

    public SumConstantFunction() {
        super("Sum/Subtract", "Sums/Subtracts the defined constant to given value");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return value + doubleParameter.getParameterValue();
        }
        return null;
    }

    @Override
    public SumConstantFunction createNew() {
        return new SumConstantFunction();
    }

    @Override
    protected void createDefaultParameters() {
        doubleParameter = new DoubleParameter();
        doubleParameter.setDescription("Define a constant which will be summed to all values");
        doubleParameter.setParameterValue(1.0);
        addParameter(CONSTANT, doubleParameter);
    }

    @Override
    public String getName() {
        String operation = doubleParameter.getParameterValue() < 0 ? "Subtract " : "Sum ";
        return operation + Math.abs(doubleParameter.getParameterValue());
    }

    public String getDescription() {
        return this.description.replace("constant", "constant (" + this.doubleParameter.getParameterValue() + ")");
    }
}
