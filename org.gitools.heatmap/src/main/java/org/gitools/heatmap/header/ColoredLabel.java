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
package org.gitools.heatmap.header;

import org.gitools.utils.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ColoredLabel {

    @XmlElement(name = "displayed-label")
    private String displayedLabel;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    private Color color;

    private String value;

    public ColoredLabel() {
        displayedLabel = "";
        color = Color.WHITE;
        value = "";
    }

    public ColoredLabel(String value, Color color) {
        this(value, value.replaceAll("_", " "), color);
    }

    private ColoredLabel(String value, String displayedLabel, Color color) {
        this.displayedLabel = displayedLabel;
        this.value = value;
        this.color = color;
    }

    public ColoredLabel(int value, String displayedLabel, Color color) {
        this.displayedLabel = displayedLabel;
        setValue(value);
        this.color = color;
    }

    public ColoredLabel(double value, String displayedLabel, Color color) {
        this.displayedLabel = displayedLabel;
        setValue(value);
        this.color = color;
    }

    public String getDisplayedLabel() {
        return displayedLabel;
    }

    public void setDisplayedLabel(String displayedLabel) {
        this.displayedLabel = displayedLabel;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    void setValue(double value) {
        this.value = Double.toString(value);
    }

    void setValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String toString() {
        return displayedLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredLabel that = (ColoredLabel) o;

        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
