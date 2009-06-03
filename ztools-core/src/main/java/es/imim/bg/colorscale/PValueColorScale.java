package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorConstants;

public class PValueColorScale extends CompositeColorScale {
	
	public static final double defaultLogFactor = 0.25;

	public PValueColorScale(
			double sigPoint, 
			Color minColor, 
			Color maxColor,
			Color nonSigColor) {
		
		super(0.0, 1.0, minColor, maxColor);
		
		final double epsilon = 1e-16;
		
		ScaleRange[] scaleRanges = new ScaleRange[] {
			new ScaleRange(sigPoint + epsilon, 1.0, 
					new UniformColorScale(nonSigColor)),
			new ScaleRange(0.0, sigPoint + epsilon,
					new LogColorScale(
							0.0, sigPoint + epsilon, 
							minColor, maxColor))
		};
		
		setScaleRanges(scaleRanges);
	}
	
	public PValueColorScale() {
		this(0.05, 
				ColorConstants.pvalueMinColor, 
				ColorConstants.pvalueMaxColor, 
				ColorConstants.nonSignificantColor);
	}
}
