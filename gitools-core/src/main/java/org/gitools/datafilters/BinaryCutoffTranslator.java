/*
 * #%L
 * gitools-core
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
package org.gitools.datafilters;

import cern.colt.function.DoubleFunction;

/**
 * @noinspection ALL
 */
public class BinaryCutoffTranslator implements ValueTranslator<Double>
{

    private static final long serialVersionUID = 4964176171201274622L;

    private final DoubleFunction filter;

    public BinaryCutoffTranslator(DoubleFunction filter)
    {
        this.filter = filter;
    }

    @Override
    public Double stringToValue(String str)
    {
        double value = Double.NaN;
        try
        {
            value = Double.parseDouble(str);
            value = filter.apply(value);
        } catch (NumberFormatException e)
        {
        }
        return value;
    }

    @Override
    public String valueToString(Double value)
    {
        if (Double.isNaN(value))
        {
            return "-";
        }

        String str = String.valueOf(value);
        double frac = value - Math.floor(value);
        if (frac == 0.0)
        {
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

}
