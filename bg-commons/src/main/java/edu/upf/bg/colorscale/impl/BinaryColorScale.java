package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.SimpleColorScale;
import edu.upf.bg.colorscale.util.ColorConstants;
import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.xml.adapter.CutoffCmpXmlAdapter;
import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BinaryColorScale extends SimpleColorScale {

	private ColorScalePoint cutoff;

	@XmlJavaTypeAdapter(CutoffCmpXmlAdapter.class)
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
		Color color = simpleLimitsColor(value);
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
