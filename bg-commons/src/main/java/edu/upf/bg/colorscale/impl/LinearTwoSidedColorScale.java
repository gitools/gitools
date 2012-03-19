package edu.upf.bg.colorscale.impl;


import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.NumericColorScale;

import java.awt.*;

public class LinearTwoSidedColorScale extends NumericColorScale {


    private ColorScalePoint min;
    private ColorScalePoint mid;
    private ColorScalePoint max;

    public LinearTwoSidedColorScale(ColorScalePoint min, ColorScalePoint mid, ColorScalePoint max) {
        super();
        this.min = min;
        this.mid = mid;
        this.max = max;
    }

    public LinearTwoSidedColorScale() {
        this(
                new ColorScalePoint(-2, Color.GREEN),
                new ColorScalePoint(0, Color.BLACK),
                new ColorScalePoint(2, Color.RED)
        );
    }

    @Override
    protected Color colorOf(double value) {

        if (value < min.getValue()) {
            return min.getColor();
        }

        if (value > max.getValue()) {
            return max.getColor();
        }


        if (value < mid.getValue()) {
            double f = (value - min.getValue()) / (mid.getValue() - min.getValue());
            return ColorUtils.mix(mid.getColor(), min.getColor(), f);
        } else {
            double f = (value - mid.getValue()) / (max.getValue() - mid.getValue());
            return ColorUtils.mix(max.getColor(), mid.getColor() , f);
        }

    }

    @Override
    public double[] getPoints() {
        return new double[]{ min.getValue(), mid.getValue(), max.getValue()};
    }

    public ColorScalePoint getMin() {
        return min;
    }

    public void setMin(ColorScalePoint min) {
        this.min = min;
    }

    public ColorScalePoint getMid() {
        return mid;
    }

    public void setMid(ColorScalePoint mid) {
        this.mid = mid;
    }

    public ColorScalePoint getMax() {
        return max;
    }

    public void setMax(ColorScalePoint max) {
        this.max = max;
    }
}
