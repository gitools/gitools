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

	public static Color invert(Color color) {
		int r = 255 - color.getRed();
		int g = 255 - color.getGreen();
		int b = 255 - color.getBlue();
		return new Color(r, g, b);
	}

	public static Color getColorForIndex(int index) {


		int[] seriesColors = { 0x4bb2c5, 0xEAA228, 0xc5b47f, 0x546D61, 0x958c12,
								0x953579, 0xc12e2e, 0x4b5de4, 0xd8b83f, 0xff5800,
								0x0085cc, 0xc747a3, 0xcddf54, 0xFBD178, 0x26B4E3,
								0xbd70c7, 0xabdbeb, 0x40D800, 0x8AFF00, 0xD9EB00,
								0xFFFF71, 0x777B00};
        int[] negativeSeriesColors = { 0x498991, 0xC08840, 0x9F9274, 0x579575, 0x646C4A,
										0x6F6621, 0x6E3F5F, 0x4F64B0, 0xA89050, 0xC45923,
										0x187399, 0x945381, 0x959E5C, 0xAF5714, 0x478396,
										0x907294, 0x426c7a,	0x878166, 0xAEA480, 0xFFFFD3,
										0xE9D5A4, 0xA29877};


		index = index % (seriesColors.length + negativeSeriesColors.length);


		boolean pair = (index % 2) == 0;
		int colorIndex = (int) index / 2;

		int colorInt = pair ? seriesColors[colorIndex] : negativeSeriesColors[colorIndex];
		return new Color(colorInt);


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
	}


}
