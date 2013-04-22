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

public class GenericFormatter implements Serializable {

    private final StringBuilder sb;
    private final Formatter fmt;

    public GenericFormatter() {
        sb = new StringBuilder(12);
        fmt = new Formatter(sb);
    }

    @NotNull
    private String decimal(double value) {

        if (value!=0 && value < 1e-16) {
            return "~0.00";
        }

        sb.setLength(0);
        fmt.format("%.2g", value);
        return sb.toString();
    }

    @NotNull
    public String format(@Nullable Object value) {

        if (value == null) {
            return "None";
        }

        if (value instanceof Double) {
            return decimal(((Double) value).doubleValue());
        }

        if (value instanceof Float) {
            return decimal(((Float) value).doubleValue());
        }

        sb.setLength(0);
        fmt.format("%s", value);
        return sb.toString();
    }
}
