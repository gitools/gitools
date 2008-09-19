package es.imim.bg.ztools.ui.colormatrix;

import java.awt.Color;

public class DefaultColorMatrixCellDecorator implements
		ColorMatrixCellDecorator {

	private CellDecorationConfig config;
	
	public DefaultColorMatrixCellDecorator() {
		this(new CellDecorationConfig());
	}
	
	public DefaultColorMatrixCellDecorator(CellDecorationConfig config) {
		this.config = config;
	}
	
	@Override
	public void decorate(CellDecoration decoration, Double value) {
		if (config.showColors) {
			Color c = config.scale.getColor(value);
			decoration.setBgColor(c);
			decoration.setToolTip(value.toString());
		}
		else {
			String txt = config.textFormat.format(value);
			decoration.setText(txt);
			decoration.setTextAlign(config.textAlign);
			decoration.setToolTip(txt);
		}
	}

	public CellDecorationConfig getConfig() {
		return config;
	}
	
	public void setConfig(CellDecorationConfig config) {
		this.config = config;
	}
}
