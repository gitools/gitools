package org.gitools.matrix.transform.parameters;


public class DoubleParameter extends AbstractFunctionParameter<Double> {

    @Override
    public boolean validate(Double parameter) {

        super.validate(parameter);

        if (parameter.isNaN() && parameter.isInfinite()) {
            return false;
        }

        return true;
    }

}
