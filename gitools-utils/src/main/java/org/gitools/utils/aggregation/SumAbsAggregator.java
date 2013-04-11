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

import cern.jet.math.Functions;
import org.jetbrains.annotations.NotNull;

/**
 * Absolute Sum
 */
public class SumAbsAggregator extends AbstractAggregator
{

    public final static IAggregator INSTANCE = new SumAbsAggregator();

    private SumAbsAggregator()
    {
    }

    @Override
    public double aggregate(@NotNull double[] data)
    {
        int nanValue = 0;
        if (data.length == 1)
        {
            return checkNaN(data[0], nanValue);
        }
        else
        {
            return aggregate(data, Functions.plusAbs, nanValue);
        }
    }

    @NotNull
    @Override
    public String toString()
    {
        return "Absolute Sum";
    }

    private double checkNaN(double d, int nanValue)
    {
        return Double.isNaN(d) ? nanValue : Math.abs(d);
    }
}