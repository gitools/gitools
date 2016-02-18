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
    public ReplaceValueFunction createNew() {
        ReplaceValueFunction newInstance = new ReplaceValueFunction();
        return newInstance;
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
