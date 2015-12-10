package org.gitools.api.matrix;


import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ConfigurableTransformFunction extends TransformFunction {

    Map<String, IFunctionParameter> functionParameters;

    public ConfigurableTransformFunction(String name) {
        this(name, "");
    }

    public abstract <T extends ConfigurableTransformFunction> T createNew();

    public Map<String, IFunctionParameter> getParameters() {
        if (functionParameters == null) {
            functionParameters = new LinkedHashMap<>();
        }
        return functionParameters;
    }

    public ConfigurableTransformFunction(String name, String description) {
        super(name, description);
        createDefaultParameters();
    }

    public void addParameter(String name, IFunctionParameter parameter) {
        if (functionParameters == null) {
            functionParameters = new LinkedHashMap<>();
        }
        functionParameters.put(name, parameter);
    }

    public IFunctionParameter getParameter(String key){
        if(functionParameters == null) {
            return null;
        }

        return functionParameters.get(key);
    }

    protected abstract void createDefaultParameters();

    public int getParamterCount() {
        return getParameters().size();
    }

    public boolean validate() {
        for (IFunctionParameter parameter : getParameters().values()) {
            if (!parameter.validate()) {
                return false;
            }
        }
        return true;
    }
}
