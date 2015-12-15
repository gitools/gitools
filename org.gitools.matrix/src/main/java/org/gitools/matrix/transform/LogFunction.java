package org.gitools.matrix.transform;


import org.gitools.api.matrix.ConfigurableTransformFunction;
import org.gitools.api.matrix.IMatrixPosition;
import org.gitools.matrix.transform.parameters.DoubleParameter;
import org.gitools.matrix.transform.parameters.LogParameter;

public class LogFunction extends ConfigurableTransformFunction {

    public static final String LOG_BASE = "Log base";
    public static final String CUSTOM_BASE = "Custom base";
    private LogParameter logParameter;
    private DoubleParameter customBaseParameter;

    public LogFunction() {
        super("Log", "Returns Log of given value and chosen base");
    }

    @Override
    public LogFunction createNew() {
        return new LogFunction();
    }

    @Override
    public Double apply(Double value, IMatrixPosition position) {
        if (value != null) {
            LogParameter.LogEnum parameterValue = logParameter.getParameterValue();
            if (parameterValue.equals(LogParameter.LogEnum.baseN)) {
                return Math.log(value);
            } else if (parameterValue.equals(LogParameter.LogEnum.base10)) {
                return Math.log10(value);
            } else if (parameterValue.equals(LogParameter.LogEnum.base2)) {
                return Math.log(value) / Math.log(2.0);
            } else if (parameterValue.equals(LogParameter.LogEnum.custom)) {
                return Math.log(value) / Math.log(customBaseParameter.getParameterValue());
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void createDefaultParameters() {
        logParameter = new LogParameter();
        logParameter.setChoices(LogParameter.LogEnum.values());
        logParameter.setDescription("Choose base for the Log function");
        addParameter(LOG_BASE, logParameter);

        customBaseParameter = new DoubleParameter();
        customBaseParameter.setParameterValue(5.0);
        customBaseParameter.setDescription("Define a custom base");
        addParameter(CUSTOM_BASE, customBaseParameter);
    }

    @Override
    public String getName() {

        if (logParameter.getParameterValue().equals(LogParameter.LogEnum.custom)) {
            return  "Log" + customBaseParameter.getParameterValue();
        } else {
            return logParameter.getParameterValue().toString();
        }
    }
}
