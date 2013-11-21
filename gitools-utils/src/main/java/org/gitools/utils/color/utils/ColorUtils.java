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
package org.gitools.utils.color.utils;

import java.awt.*;

public class ColorUtils {


    public static Color mix(Color src, Color dst, double factor) {

        double fs = factor / 255.0;
        double fd = (1.0 - factor) / 255.0;

        double r = src.getRed() * fs + dst.getRed() * fd;
        double g = src.getGreen() * fs + dst.getGreen() * fd;
        double b = src.getBlue() * fs + dst.getBlue() * fd;

        int ir = Math.max(0, Math.min(255, (int) Math.round(r * 255)));
        int ig = Math.max(0, Math.min(255, (int) Math.round(g * 255)));
        int ib = Math.max(0, Math.min(255, (int) Math.round(b * 255)));

        return new Color(ir, ig, ib);
    }


    public static String colorToRGBHtml(Color color) {
        StringBuilder sb = new StringBuilder();

        sb.append("rgb(");
        sb.append(color.getRed()).append(',');
        sb.append(color.getGreen()).append(',');
        sb.append(color.getBlue()).append(')');

        return sb.toString();
    }


    public static String colorToHexHtml(Color color) {
        return "#" + Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

    /**
     * Best foreground color. Returns WHITE or BLACK color depending on the 'backgroundColor' luminescence level.
     *
     * @param backgroundColor the background color
     * @return the color
     */
    public static Color bestForegroundColor(Color backgroundColor) {
        if (luminescenceLevel(backgroundColor) >= 128) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    /**
     * Luminescence level between 0 and 255
     *
     * @param color the color
     * @return the luminescence level
     */
    public static double luminescenceLevel(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        return .299 * r + .587 * g + .114 * b;
    }


}
