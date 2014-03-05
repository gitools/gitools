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

public class ScientificHeatmapTextFormatter extends HeatmapTextFormatter {

    public static final ScientificHeatmapTextFormatter INSTANCE = new ScientificHeatmapTextFormatter();

    private final StringBuilder sb;
    private final Formatter fmt;

    public ScientificHeatmapTextFormatter() {
        super("Scientific notation", new DecimalFormat("#.#########"));
        sb = new StringBuilder(14);
        fmt = new Formatter(sb);
    }

    @Override
    protected String decimal(double value) {
        if (value != 0 && value < 1e-99 && value > -1e-99) {
            return "~0.00";
        }

        if (value < 1 && value > -1) {
            sb.setLength(0);
            fmt.format("%.2g", value);
            return sb.toString();
        } else {
            return getFormat().format(value);
        }
    }

}
