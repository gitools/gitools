package edu.upf.bg.colorscale.impl;


import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.NumericColorScale;

import java.awt.*;
import java.util.Arrays;

public class CategoricalColorScale extends NumericColorScale {

    private ColorScalePoint[] points;
    private boolean cagetoricalSpans = true;

    public CategoricalColorScale() {
        this(
                new double[] {1.0,2.0}
        );
    }

    public CategoricalColorScale(ColorScalePoint[] points) {
        super();
        this.points = points;
    }
    
    public CategoricalColorScale(double pointValues[]) {

        setValues(pointValues);
    }

    public void setValues(double[] pointValues) {
        Arrays.sort(pointValues);
        this.points = new ColorScalePoint[pointValues.length];

        
        Color[] palette = generateColors(pointValues.length);
        for (int i = 0; i<pointValues.length; i++) {
            this.points[i] =
                    new ColorScalePoint(pointValues[i],palette[i]);
        }

    }

    public Color[] generateColors(int n)
    {
        Color[] cols = new Color[n];

        if (n == 3) {
            cols[0] = Color.BLUE;
            cols[1] = Color.green.darker();
            cols[2] = Color.RED;
            return cols;
        }

        for(int i = 0; i < n; i++)
        {
            cols[i] = Color.getHSBColor((float) i / (float) n, 0.85f, 1.0f);
        }
        return cols;
    }

    public ColorScalePoint getColorScalePoint(double value) {
        for (ColorScalePoint p: points) {
            if (cagetoricalSpans) {
                if (p.getValue() >= value)
                    return p;
            } else {
                if (p.getValue() == value)
                    return p;
            }
        }
        return null;
    }

    @Override
    protected Color colorOf(double value) {

        Color c = super.getEmptyColor();
        ColorScalePoint firstPoint = getMin();
        for (ColorScalePoint p: points) {
            if (cagetoricalSpans) {
                if (p.getValue() >= value)
                    return p.getColor();
            } else {
                if (p.getValue() == value)
                    return p.getColor();
            }
        }
        return c;
    }

    @Override
    public double[] getPoints() {
        double[] pointValues = new double[points.length];
        for(int i = 0; i<points.length;i++)
            pointValues[i] = points[i].getValue();

        /*
        Double spectrum = getMaxValue() - getMinValue();
        Double step = spectrum/(points.length-1);
        pointValues[points.length] = pointValues[points.length-1] + step;  */
        return pointValues;
    }

    public ColorScalePoint getMin() {
        return points[0];
    }
    
    @Override
    public double getMinValue() {
        Double spectrum = points[points.length-1].getValue() - points[0].getValue();
        Double step = spectrum/(points.length-1);
        return points[0].getValue()-step;
    }

    public ColorScalePoint getMax() {
        ColorScalePoint last = points[points.length-1];
        return last;
    }

    @Override
    public double getMaxValue() {
        return points[points.length-1].getValue();
    }

    public boolean isCagetoricalSpans() {
        return cagetoricalSpans;
    }

    public void setCagetoricalSpans(boolean cagetoricalSpans) {
        this.cagetoricalSpans = cagetoricalSpans;
    }

}
