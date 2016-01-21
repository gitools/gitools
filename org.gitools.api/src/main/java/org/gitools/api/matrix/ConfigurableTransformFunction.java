/*
 * #%L
 * org.gitools.api
 * %%
 * Copyright (C) 2013 - 2016 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
