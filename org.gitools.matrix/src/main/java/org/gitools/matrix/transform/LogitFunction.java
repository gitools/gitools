package org.gitools.matrix.transform;


import org.apache.commons.math3.analysis.function.Logit;
import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.DoubleParameter;

public class LogitFunction extends ConfigurableTransformFunction {

    public static final String LOW = "Low";
    private Logit logit;
    private DoubleParameter lowParameter;
    private DoubleParameter highParameter;

    public LogitFunction() {
        super("Logit", "Returns the Logit transformation");
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            return getLogit().value(value);
        }
        return null;
    }

    @Override
    public LogitFunction createNew() {
        return new LogitFunction();
    }

    @Override
    protected void createDefaultParameters() {
        lowParameter = new DoubleParameter();
        lowParameter.setParameterValue(0.0);
        lowParameter.setDescription("Low threshold");
        addParameter("Low", lowParameter);

        highParameter = new DoubleParameter();
        highParameter.setParameterValue(1.0);
        highParameter.setDescription("High threshold");
        addParameter("High", highParameter);

    }

    public Logit getLogit() {
        if (logit == null) {
            logit = new Logit(lowParameter.getParameterValue(), highParameter.getParameterValue());
        }
        return logit;
    }

    @Override
    public String getName() {
        return super.getName()  + " (" + lowParameter.getParameterValue() + ", " + highParameter.getParameterValue() +  ")";
    }
}
