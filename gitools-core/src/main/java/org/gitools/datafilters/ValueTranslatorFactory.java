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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ValueTranslatorFactory {

    @Nullable
    public static ValueTranslator createValueTranslator(Class<?> cls) {
        ValueTranslator vt = null;

        if (double.class.equals(cls) || Double.class.equals(cls)) {
            vt = new DoubleTranslator();
        } else if (int.class.equals(cls) || Integer.class.equals(cls)) {
            vt = new IntegerTranslator();
        } else {
            vt = new ValueTranslator() {
                @Override
                public Object stringToValue(String str) {
                    return str;
                }

                @NotNull
                @Override
                public String valueToString(@Nullable Object value) {
                    return value != null ? value.toString() : "";
                }
            };
        }

        return vt;
    }
}
