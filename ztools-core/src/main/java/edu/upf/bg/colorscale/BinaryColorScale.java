package edu.upf.bg.colorscale;

import java.awt.Color;

import org.gitools.datafilters.CutoffCmp;

public class BinaryColorScale extends AbstractColorScale {

	private double cutoff = 1.0;
	private CutoffCmp cutoffCmp = CutoffCmp.EQ;;

	public BinaryColorScale(double minPoint, double maxPoint, double cutoff,
			Color minColor, Color maxColor, Color nonSignificantColor) {
		super(minPoint, maxPoint);
		this.cutoff = cutoff;
		this.minColor = minColor;
		this.maxColor = maxColor;
	}

	public BinaryColorScale() {
		super(0.0, 1.0);
	}

	@Override
	public Color valueColor(double value) {
		if (Double.isNaN(value))
			return notANumberColor;
		else if (value > maxPoint || value == Double.POSITIVE_INFINITY)
			return posInfinityColor;
		else if (value < minPoint || value == Double.NEGATIVE_INFINITY)
			return negInfinityColor;

		boolean isSig = cutoffCmp.compare(value, cutoff);
		return isSig ? maxColor : minColor;
	}

	public double getCutoff() {
		return cutoff;
	}

	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}

	public CutoffCmp getCutoffCmp() {
		return cutoffCmp;
	}

	public void setCutoffCmp(CutoffCmp cutoffCmp) {
		this.cutoffCmp = cutoffCmp;
	}

}
