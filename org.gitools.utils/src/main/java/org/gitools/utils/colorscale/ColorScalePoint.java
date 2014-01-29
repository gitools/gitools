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
package org.gitools.utils.colorscale;

import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class ColorScalePoint implements Serializable, Comparable<ColorScalePoint> {

    private double value;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color color;

    private String name;

    public ColorScalePoint() {
        super();
        // JAXB requirement
    }

    public ColorScalePoint(double value, Color color, String name) {
        this.value = value;
        this.color = color;
        this.name = name;
    }

    public ColorScalePoint(double value, Color color) {
        this.value = value;
        this.color = color;
        this.name = "";
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(ColorScalePoint o) {
        if (this.value < o.getValue()) {
            return -1;
        }
        if (value > o.getValue()) {
            return 1;
        }
        return 0;
    }
}
