package edu.upf.bg.colorscale;

import java.awt.Color;

public class UniformColorScale implements IColorScale {

	protected Color color;
	
	public UniformColorScale(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	@Override
	public Color valueColor(double value) {
		return color;
	}

}
