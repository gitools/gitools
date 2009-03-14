package es.imim.bg.colorscale;

import java.awt.Color;

import es.imim.bg.colorscale.util.ColorConstants;

public class ZScoreColorScale extends CompositeColorScale {

	public ZScoreColorScale(
			double center,
			double halfAmplitude, 
			double sigHalfAmplitude,
			Color leftMinColor,
			Color leftMaxColor,
			Color rightMinColor,
			Color rightMaxColor,
			Color nonSigColor) {
		
		super(0.0, 0.0, 
				leftMinColor, rightMaxColor);
		
		double min = -halfAmplitude + center;
		double max = halfAmplitude + center;
		
		double sigMin = -sigHalfAmplitude + center;
		double sigMax = sigHalfAmplitude + center;
		
		setMinPoint(min);
		setMaxPoint(max);
		
		ScaleRange[] scaleRanges = new ScaleRange[] {
				new ScaleRange(sigMin, sigMax, 
						new UniformColorScale(nonSigColor)),
				new ScaleRange(min, center, 
						new LinearColorScale(
								min, center, 
								leftMinColor, 
								leftMaxColor)),
				new ScaleRange(center, max, 
						new LinearColorScale(
								center, max, 
								rightMinColor, 
								rightMaxColor))
		};
		
		setScaleRanges(scaleRanges);
	}

	public ZScoreColorScale() {
		this(0, 10, 1.96, 
				Color.BLUE, Color.CYAN,
				Color.YELLOW, Color.RED,
				ColorConstants.nonSignificantColor);
	}
}
