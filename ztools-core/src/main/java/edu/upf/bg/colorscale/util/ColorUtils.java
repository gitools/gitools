package edu.upf.bg.colorscale.util;

import java.awt.Color;

public class ColorUtils {

	public static Color mix(Color src, Color dst, double factor) {
		
		double fs = factor / 255.0;
		double fd = (1.0 - factor) / 255.0;
		
		double r = src.getRed() * fs + dst.getRed() * fd;
		double g = src.getGreen() * fs + dst.getGreen() * fd;
		double b = src.getBlue() * fs + dst.getBlue() * fd;
		
		int ir = Math.max(0, Math.min(255, (int)Math.round(r * 255)));
		int ig = Math.max(0, Math.min(255, (int)Math.round(g * 255)));
		int ib = Math.max(0, Math.min(255, (int)Math.round(b * 255)));
		
		return new Color(ir, ig, ib);
	}
	
	public static String colorToHtml(Color color) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("rgb(");
		sb.append(color.getRed()).append(',');
		sb.append(color.getGreen()).append(',');
		sb.append(color.getBlue()).append(')');
		
		return sb.toString();
	}
}
