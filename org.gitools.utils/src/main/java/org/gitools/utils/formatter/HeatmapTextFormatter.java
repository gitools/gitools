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

import java.text.DecimalFormat;
import java.util.Formatter;

public class HeatmapTextFormatter implements ITextFormatter {

    public static HeatmapTextFormatter NO_DECIMALS = new HeatmapTextFormatter("No decimals", new DecimalFormat("########"));
    public static HeatmapTextFormatter ONE_DECIMALS = new HeatmapTextFormatter("One decimal", new DecimalFormat("########.#"));
    public static HeatmapTextFormatter FOUR_DECIMALS = new HeatmapTextFormatter("Four decimals", new DecimalFormat("########.####"));
    public static HeatmapTextFormatter TWO_DECIMALS = new HeatmapTextFormatter("Two decimals", new DecimalFormat("########.##"));


    private String name;
    private final StringBuilder sb;
    private final Formatter fmt;
    private final DecimalFormat countFormat;

    public HeatmapTextFormatter(String name, DecimalFormat format) {
        this.sb = new StringBuilder(8);
        this.fmt = new Formatter(sb);
        this.name = name;
        this.countFormat = format;
    }

    protected String decimal(double value) {
         return countFormat.format(value);
    }

    @Override
    public String format(Object value) {

        if (value == null) {
            return "None";
        }

        if (value instanceof Double) {
            return decimal((Double) value);
        }

        if (value instanceof Float) {
            return decimal(((Float) value).doubleValue());
        }

        sb.setLength(0);
        fmt.format("%s", value);
        return sb.toString();
    }

    protected DecimalFormat getFormat() {
        return countFormat;
    }

    @Override
    public String toString() {
        return name;
    }
}
