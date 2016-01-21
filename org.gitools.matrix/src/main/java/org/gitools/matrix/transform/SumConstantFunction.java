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
