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

public class ColorUtils
{

    public static Color mix(Color src, Color dst, double factor)
    {

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

    public static String colorToRGBHtml(Color color)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("rgb(");
        sb.append(color.getRed()).append(',');
        sb.append(color.getGreen()).append(',');
        sb.append(color.getBlue()).append(')');

        return sb.toString();
    }

    public static String colorToHexHtml(Color color)
    {
        return "#" + Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
    }

    public static Color invert(Color color)
    {
        int r = 255 - color.getRed();
        int g = 255 - color.getGreen();
        int b = 255 - color.getBlue();
        return new Color(r, g, b);
    }

	/*public static Color getColorForIndex(int index) {
        return Color.WHITE;*/

		/*
		// calculate a color with the hsl color wheel
		// imagine 10 equally distributed points on the color wheel
		// with a given saturation and lightness. They will be
		// generated 5 and 5 at a time, and after 10 colors
		// saturation and lightness are adjusted


		if (index >= 40)
		return Color.WHITE;

		float rotation1 =  ((int) (index / 5) % 2) * 0.1f;
		float rotation2 = (float) ((index % 5) / 5.0f);
		float hue = rotation1 + rotation2;

		float[] saturations = new float[]{1.0f,0.5f,0.45f,0.35f};
		float[] lightnesses = new float[]{0.5f,0.45f,0.4f,0.4f};
		int step = index / 10;

		float lightness =  lightnesses[step];
		float saturation = saturations[step];



		float[] hls = new float[]{hue, lightness, saturation};

		ColorSpace csHLS = new HLSColorSpace();
		float[] rgb = csHLS.toRGB(hls);
		Color color = new Color(rgb[0],rgb[1],rgb[2]);

		return color;*/
    //}


}
