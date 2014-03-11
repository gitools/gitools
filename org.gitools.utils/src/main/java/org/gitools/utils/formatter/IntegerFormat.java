/*
 * #%L
 * org.gitools.utils
 * %%
 * Copyright (C) 2013 - 2014 Universitat Pompeu Fabra - Biomedical Genomics group
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
import java.text.ParsePosition;

public class IntegerFormat extends DecimalFormat {

    private static IntegerFormat INSTANCE = new IntegerFormat();

    public static IntegerFormat get() {
        return INSTANCE;
    }

    private IntegerFormat() {
        super("#");
    }

    @Override
    public Number parse(String text, ParsePosition pos) {

        Number result = super.parse(text, pos);
        if (result instanceof Long) {
            return result.intValue();
        }

        return result;
    }
}
