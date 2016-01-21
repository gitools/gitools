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
import org.gitools.api.matrix.IMatrixLayer;

import java.util.ArrayList;
import java.util.List;

public class TransformFunctionFactory {

    public static List<ConfigurableTransformFunction> get(IMatrixLayer<Double> resultLayer) {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new AbsoluteFunction());
        funcs.add(new FillEmptyFunction());
        funcs.add(new FoldChangeFunction(resultLayer));
        funcs.add(new LogitFunction());
        funcs.add(new LogFunction());
        funcs.add(new MultiplyByConstantFunction());
        funcs.add(new ReplaceValueFunction());
        funcs.add(new ScaleFunction(resultLayer));
        funcs.add(new SumConstantFunction());
        funcs.add(new ZScoreFunction());
        return funcs;
    }

    public static List<ConfigurableTransformFunction> get() {
        List<ConfigurableTransformFunction> funcs = new ArrayList<>();
        funcs.add(new FillEmptyFunction());
        funcs.add(new LogitFunction());
        funcs.add(new LogFunction());
        funcs.add(new ReplaceValueFunction());
        funcs.add(new SumConstantFunction());
        return funcs;
    }

    public static <T extends ConfigurableTransformFunction> T createFromTemplate(T template) {
        T newInstance = template.createNew();
        copyParameters(template, newInstance);
        return newInstance;
    }

    protected static <T extends ConfigurableTransformFunction> void copyParameters(T from, T to) {
        for (String key : from.getParameters().keySet()) {
            to.getParameter(key).setParameterValue(from.getParameter(key).getParameterValue());
        }
    }
}
