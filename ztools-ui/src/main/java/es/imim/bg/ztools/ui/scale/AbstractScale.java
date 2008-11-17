package es.imim.bg.ztools.ui.scale;

import javax.swing.JPanel;

import es.imim.bg.colorscale.ColorScale;

public abstract class AbstractScale implements Scale {

	protected ColorScale colorScale;
	protected JPanel configPanel;
	
	public AbstractScale(ColorScale colorScale, JPanel configPanel) {
		this.colorScale = colorScale;
		this.configPanel = configPanel;
	}
	
	@Override
	public ColorScale getColorScale() {
		return colorScale;
	}

	@Override
	public JPanel getConfigurationPanel() {
		return configPanel;
	}

}
