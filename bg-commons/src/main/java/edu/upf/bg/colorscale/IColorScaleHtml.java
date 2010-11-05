package edu.upf.bg.colorscale;

public interface IColorScaleHtml {

	/**
	 * @param value
	 * @return The color as rgb(red, green, blue) string
	 */
	String valueRGBHtmlColor(double value);
	
	
	/**
	 * @param value
	 * @return The color in a Hexadecimal format. Ex: #FFA01C
	 */
	String valueHexHtmlColor(double value);
}
