package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.CutoffCmpParameter;
import org.gitools.matrix.transform.parameters.DoubleParameter;
import org.gitools.utils.cutoffcmp.CutoffCmp;

public class ReplaceValueFunction extends ConfigurableTransformFunction {

    public static final String CUTOFF = "Cutoff";
    public static final String CUTOFF_OPERATOR = "Cutoff operator";
    public static final String REPLACEMENT = "Replacement";
    DoubleParameter cutoffParameter;
    DoubleParameter replacementParameter;
    CutoffCmpParameter cutoffCmpParameter;

    public ReplaceValueFunction() {
        super("Replace value(s)", "Replace defined value(s) with a given value");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            if (cutoffCmpParameter.getParameterValue().compare(value, cutoffParameter.getParameterValue().doubleValue())) {
                return replacementParameter.getParameterValue().doubleValue();
            } else {
                return value;
            }
        }
        return null;
    }

    @Override
    protected void createDefaultParameters() {
        cutoffParameter = new DoubleParameter();
        cutoffParameter.setDescription("Define the cutoff value");
        cutoffParameter.setParameterValue(1.0);
        addParameter(CUTOFF, cutoffParameter);

        cutoffCmpParameter = new CutoffCmpParameter();
        cutoffCmpParameter.setDescription("Define the cutoff operator");
        cutoffCmpParameter.setChoices(CutoffCmp.comparators);
        cutoffCmpParameter.setParameterValue(CutoffCmp.ABS_GE);
        addParameter(CUTOFF_OPERATOR, cutoffCmpParameter);

        replacementParameter = new DoubleParameter();
        replacementParameter.setDescription("Define a constant which will be used as replacement");
        replacementParameter.setParameterValue(1.0);
        addParameter(REPLACEMENT, replacementParameter);
    }

    @Override
    public String getName() {

        String name = "Replace values "
                + cutoffCmpParameter.getParameterValue() + " "
                + cutoffParameter.getParameterValue() + " with "
                + replacementParameter.getParameterValue();

        return name;
    }

}
