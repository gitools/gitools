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
package org.gitools.utils.translators;

import org.gitools.api.matrix.ValueTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DoubleTranslator implements ValueTranslator<Double> {

    private static final Logger log = LoggerFactory.getLogger(DoubleTranslator.class);

    // Singleton pattern
    private static final DoubleTranslator INSTANCE = new DoubleTranslator();

    public static DoubleTranslator get() {
        return INSTANCE;
    }

    private DoubleTranslator() {
    }


    @Override
    public Double stringToValue(String str) {
        return stringToValue(str, true);
    }


    public Double stringToValue(String str, boolean allowNull) {
        if (allowNull) {
            if (str == null || str.isEmpty() || str.equals("-")) {
                return null;
            }
        }

        double value;
        try {
            value = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            //log.error("Malformed number will be set to null: '" + str + "'");
            return null;
        }
        return value;
    }

    @Override
    public String valueToString(Double value) {
        if (value == null) {
            return "";
        }

        if (Double.isNaN(value)) {
            return "-";
        }

        String s = String.valueOf(value);
        return s.endsWith(".0") ? s.substring(0, s.length() - 2) : s;
    }

}
