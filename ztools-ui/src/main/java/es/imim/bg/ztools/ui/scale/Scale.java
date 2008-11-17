package es.imim.bg.ztools.ui.scale;

import javax.swing.JPanel;

import es.imim.bg.colorscale.ColorScale;

public interface Scale {

	ColorScale getColorScale();
	
	JPanel getConfigurationPanel();
}
