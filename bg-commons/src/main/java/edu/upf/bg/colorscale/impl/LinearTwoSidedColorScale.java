package edu.upf.bg.colorscale.impl;

import edu.upf.bg.colorscale.ColorScaleFragment;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.CompositeColorScale;
import java.awt.Color;

public class LinearTwoSidedColorScale extends CompositeColorScale {

	protected ColorScalePoint mid;
	
	private ColorScaleFragment leftScaleFrag;
	private ColorScaleFragment rightScaleFrag;
	
	private LinearColorScale leftScale;
	private LinearColorScale rightScale;
	
	public LinearTwoSidedColorScale(
			ColorScalePoint min,
			ColorScalePoint mid,
			ColorScalePoint max) {
		
		super(min, max);
	
		this.mid = mid;

		addPoint(mid);
		
		leftScale = new LinearColorScale(min, mid);
		leftScaleFrag = new ColorScaleFragment(min, mid, leftScale);
		
		rightScale = new LinearColorScale(mid, max);
		rightScaleFrag = new ColorScaleFragment(mid, max, rightScale);
				
		setScaleRanges(new ColorScaleFragment[] {
				leftScaleFrag,
				rightScaleFrag });
	}
	
	public LinearTwoSidedColorScale() {
		this(
				new ColorScalePoint(-2, Color.GREEN),
				new ColorScalePoint(0, Color.BLACK),
				new ColorScalePoint(2, Color.RED));
	}

	public ColorScalePoint getMid() {
		return mid;
	}
}
