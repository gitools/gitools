/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
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
package org.gitools.utils.aggregation;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;
import org.jetbrains.annotations.NotNull;

abstract class AbstractAggregator implements IAggregator
{

    double aggregate(@NotNull double[] data, @NotNull DoubleDoubleFunction reduceFunc, @NotNull DoubleFunction mapFunc, double nanValue)
    {
        if (data.length == 0)
        {
            return 0;
        }
        else if (data.length == 1)
        {
            return mapFunc.apply(checkNaN(data[0], nanValue));
        }

        double value = reduceFunc.apply(mapFunc.apply(checkNaN(data[0], nanValue)), mapFunc.apply(checkNaN(data[1], nanValue)));

        for (int i = 2; i < data.length; i++)
            value = reduceFunc.apply(value, mapFunc.apply(checkNaN(data[i], nanValue)));

        return value;
    }

    double aggregate(@NotNull double[] data, @NotNull DoubleDoubleFunction reduceFunc, double nanValue)
    {
        if (data.length == 0)
        {
            return 0;
        }
        else if (data.length == 1)
        {
            return checkNaN(data[0], nanValue);
        }

        double value = reduceFunc.apply(checkNaN(data[0], nanValue), checkNaN(data[1], nanValue));

        for (int i = 2; i < data.length; i++)
            value = reduceFunc.apply(value, checkNaN(data[i], nanValue));

        return value;
    }

    private double checkNaN(double d, double nanValue)
    {
        return Double.isNaN(d) ? nanValue : d;
    }
}
