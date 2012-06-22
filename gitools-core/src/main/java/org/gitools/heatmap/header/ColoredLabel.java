/*
 *  Copyright 2011 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.gitools.heatmap.header;

import edu.upf.bg.xml.adapter.ColorXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.awt.*;

public class ColoredLabel {

    protected String description;

    @XmlJavaTypeAdapter(ColorXmlAdapter.class)
    protected Color color;

    protected String value;

    public ColoredLabel() {
        description = "";
        color = Color.WHITE;
        value = "";
    }

    public ColoredLabel(String value, Color color) {
        this(value,value, color);
    }

    public ColoredLabel( String value, String description, Color color) {
        this.description = description;
        this.value = value;
        this.color = color;
    }

    public ColoredLabel(int value, String description, Color color) {
        this.description = description;
        setValue(value);
        this.color = color;
    }

    public ColoredLabel(double value, String description, Color color) {
        this.description = description;
        setValue(value);
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setValue(double value) {
        this.value = Double.toString(value);
    }

    public void setValue(int value) {
        this.value = Integer.toString(value);
    }

    @Override
    public String toString() {
        return description;
    }
}
