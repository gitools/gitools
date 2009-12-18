package edu.upf.bg.colorscale;

import java.awt.Color;
import java.util.List;

public interface IColorScale {
	
	Color valueColor(double value);

	ColorScaleRange getRange();
	List<ColorScalePoint> getPoints();
}
