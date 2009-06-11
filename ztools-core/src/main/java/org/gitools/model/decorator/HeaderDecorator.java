package org.gitools.model.decorator;

import java.awt.Color;

import org.gitools.model.AbstractModel;

public abstract class HeaderDecorator extends AbstractModel {

	private static final long serialVersionUID = -2580139666999968074L;
	
	protected Color foregroundColor;
	protected Color backgroundColor;
	
	public HeaderDecorator() {
		foregroundColor = Color.BLACK;
		backgroundColor = Color.WHITE;
	}
	
	public Color getForegroundColor() {
		return foregroundColor;
	}
	
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		firePropertyChange(PROPERTY_CHANGED);
	}
	
	public HeaderDecoration decorate(HeaderDecoration decoration, Object header) {
		decoration.setFgColor(foregroundColor);
		decoration.setBgColor(backgroundColor);
		decoration.setText(header.toString());
		decoration.setUrl(null);
		return decoration;
	}
}
