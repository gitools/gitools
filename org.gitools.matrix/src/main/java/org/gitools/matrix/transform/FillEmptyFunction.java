package org.gitools.matrix.transform;

import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.DoubleParameter;

public class FillEmptyFunction extends ConfigurableTransformFunction {

    public static final String REPLACEMENT_PARAM = "Replacement";
    private DoubleParameter replaceValue;

    public FillEmptyFunction() {
        super("Fill empty", "Fill empty cells with a defined value");
    }

    @Override
    @SuppressWarnings("unchecked")
    public FillEmptyFunction createNew() {
        return new FillEmptyFunction();
    }

    @Override
    protected void createDefaultParameters() {
        replaceValue = new DoubleParameter();
        replaceValue.setParameterValue(1.0);
        replaceValue.setDescription("The value to replace the empty values with");
        addParameter(REPLACEMENT_PARAM, replaceValue);
    }

    @Override
    public String getName() {
        return name + " with " + replaceValue.getParameterValue();
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value == null) {
            return replaceValue.getParameterValue();
        }
        return value;
    }
}
