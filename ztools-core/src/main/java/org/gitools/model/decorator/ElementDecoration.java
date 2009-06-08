package org.gitools.model.decorator;

import java.awt.Color;

public class ElementDecoration {

	public enum TextAlignment {
		left, right, center
	}
	
	protected String text;
	public TextAlignment textAlign;
	protected String toolTip;
	protected Color fgColor;
	protected Color bgColor;
	
	public ElementDecoration() {
		reset();
	}

	public void reset() {
		this.text = "";
		this.textAlign = TextAlignment.left;
		this.toolTip = "";
		this.fgColor = Color.BLACK;
		this.bgColor = Color.WHITE;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public TextAlignment getTextAlign() {
		return textAlign;
	}
	
	public void setTextAlign(TextAlignment textAlign) {
		this.textAlign = textAlign;
	}

	public String getToolTip() {
		return toolTip;
	}

	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	public Color getFgColor() {
		return fgColor;
	}

	public void setFgColor(Color fgColor) {
		this.fgColor = fgColor;
	}

	public Color getBgColor() {
		return bgColor;
	}

	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}
}
