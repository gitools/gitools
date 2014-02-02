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
package org.gitools.matrix.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ClassXmlAdapter extends XmlAdapter<String, Class> {
    @Override
    public Class unmarshal(String v) throws Exception {

        if (v.equalsIgnoreCase("double")) {
            return double.class;
        }

        if (v.equalsIgnoreCase("integer")) {
            return int.class;
        }

        if (v.equalsIgnoreCase("boolean")) {
            return boolean.class;
        }

        return String.class;
    }

    @Override
    public String marshal(Class v) throws Exception {

        if (v.equals(double.class) || v.equals(Double.class)) {
            return "double";
        }

        if (v.equals(int.class) || v.equals(Integer.class)) {
            return "integer";
        }

        if (v.equals(boolean.class) || v.equals(Boolean.class)) {
            return "boolean";
        }

        return "string";

    }
}
