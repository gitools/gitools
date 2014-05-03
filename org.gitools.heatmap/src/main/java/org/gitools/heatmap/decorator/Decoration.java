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
package org.gitools.heatmap.decorator;

import java.awt.*;
import java.io.Serializable;

public class Decoration implements Serializable {

    private static final long serialVersionUID = 5204451046972665249L;

    public static final String NONE = "None";

    private String value;
    private Color bgColor;
    private Color highlightColor;

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

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void highlight() {
        bgColor = blend(bgColor, getHighlightColor(), 0.5f);
    }

    Color blend(Color c1, Color c2, float ratio) {
        if (ratio > 1f) ratio = 1f;
        else if (ratio < 0f) ratio = 0f;
        float iRatio = 1.0f - ratio;

        int i1 = c1.getRGB();
        int i2 = c2.getRGB();

        int a1 = (i1 >> 24 & 0xff);
        int r1 = ((i1 & 0xff0000) >> 16);
        int g1 = ((i1 & 0xff00) >> 8);
        int b1 = (i1 & 0xff);

        int a2 = (i2 >> 24 & 0xff);
        int r2 = ((i2 & 0xff0000) >> 16);
        int g2 = ((i2 & 0xff00) >> 8);
        int b2 = (i2 & 0xff);

        int a = (int) ((a1 * iRatio) + (a2 * ratio));
        int r = (int) ((r1 * iRatio) + (r2 * ratio));
        int g = (int) ((g1 * iRatio) + (g2 * ratio));
        int b = (int) ((b1 * iRatio) + (b2 * ratio));

        return new Color(a << 24 | r << 16 | g << 8 | b);
    }
}
