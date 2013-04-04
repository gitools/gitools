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
package org.gitools.utils.formatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class GenericFormatter implements Serializable
{

    private static final long oneMicrosecond = 1000;
    private static final long oneMilisecond = 1000 * oneMicrosecond;
    private static final long oneSecond = 1000 * oneMilisecond;
    private static final long oneMinute = 60 * oneSecond;
    private static final long oneHour = 60 * oneMinute;
    private static final long oneDay = 24 * oneHour;

    @NotNull
    private static Map<Class<?>, String> defaultGenericFormatMap =
            new HashMap<Class<?>, String>();

    @NotNull
    private Map<Class<?>, String> customFormatMap =
            new HashMap<Class<?>, String>();

    static
    {
        defaultGenericFormatMap.put(Float.class, "%.3g");
        defaultGenericFormatMap.put(Double.class, "%.3g");
    }

    private String ltString;
    private Map<Class<?>, String> genericFormatMap;

    private StringBuilder sb;
    private Formatter fmt;

    public GenericFormatter(String ltString)
    {
        this.ltString = ltString;
        genericFormatMap = defaultGenericFormatMap;
        sb = new StringBuilder(12);
        fmt = new Formatter(sb);
    }

    public GenericFormatter()
    {
        this("&lt;");
    }

    public void addCustomFormatter(Class<?> c, String s)
    {
        customFormatMap.put(c, s);
    }

    @NotNull
    public String pvalue(double value)
    {
        if (value < 1e-16)
        {
            return ltString + "1.0e-16";
        }

        sb.setLength(0);
        fmt.format("%.3g", value);
        return sb.toString();
    }

    @NotNull
    public String percentage(double value)
    {
        return format("%.2g%%", value * 100.0);
    }

    @NotNull
    public String elapsedTime(Long elapsedTime)
    {
        return elapsedTime + " ns";
    }

    @NotNull
    public String format(String format, Object... args)
    {
        sb.setLength(0);
        fmt.format(format, args);
        return sb.toString();
    }

    @NotNull
    public String format(@Nullable Object value)
    {
        if (value == null)
        {
            return "None";
        }

        String format = customFormatMap.get(value.getClass());
        if (format == null)
        {
            format = genericFormatMap.get(value.getClass());
        }
        if (format == null)
        {
            format = "%s";
        }

        sb.setLength(0);
        fmt.format(format, value);
        return sb.toString();
    }
}
