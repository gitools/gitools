package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.SimpleColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import java.awt.Color;

import org.gitools.datafilters.CutoffCmp;

public class BinaryColorScale extends SimpleColorScale {

	private ColorScalePoint cutoff;
	private CutoffCmp cutoffCmp;

	public BinaryColorScale(
			ColorScalePoint min,
			ColorScalePoint max,
			double cutoffValue,
			CutoffCmp cmp) {

		super(min, max);

		this.cutoff = new ColorScalePoint(cutoffValue);
		this.cutoffCmp = cmp;

		addPoint(cutoff);
	}

	public BinaryColorScale() {
		this(
				new ColorScalePoint(0, ColorConstants.nonSignificantColor),
				new ColorScalePoint(1, new Color(20, 120, 250)),
				0.25, CutoffCmp.LE);
	}

	@Override
	public Color valueColor(double value) {
		Color color = limitsColor(value);
		if (color != null)
			return color;

		boolean satisfies = cutoffCmp.compare(value, cutoff.getValue());
		return satisfies ? max.getColor() : min.getColor();
	}

	public ColorScalePoint getCutoff() {
		return cutoff;
	}

	public CutoffCmp getCutoffCmp() {
		return cutoffCmp;
	}

	public void setCutoffCmp(CutoffCmp cutoffCmp) {
		this.cutoffCmp = cutoffCmp;
	}
}
