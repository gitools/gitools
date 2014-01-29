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
package org.gitools.analysis._DEPRECATED.model.decorator;

import java.awt.*;
import java.io.Serializable;

public class Decoration implements Serializable {

    private static final long serialVersionUID = 5204451046972665249L;

    public static final String NONE = "None";

    private String value;
    private Color bgColor;

    public Decoration(String value, Color bgColor) {
        this.value = value;
        this.bgColor = bgColor;
    }

    public Decoration() {
        reset();
    }

    public Decoration reset() {
        this.value = "";
        this.bgColor = Color.WHITE;
        return this;
    }

    public String getValue() {
        return value;
    }

    public String getFormatedValue() {
        if (value == null) {
            return NONE;
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Color getBgColor() {
        return bgColor;
    }

    public void setBgColor(Color bgColor) {
        this.bgColor = bgColor;
    }
}
