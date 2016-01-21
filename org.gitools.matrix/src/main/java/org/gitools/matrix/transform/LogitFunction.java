/*
 * #%L
 * org.gitools.matrix
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
