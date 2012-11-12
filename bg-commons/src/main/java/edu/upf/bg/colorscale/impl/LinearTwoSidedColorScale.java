package edu.upf.bg.colorscale.impl;


import edu.upf.bg.aggregation.IAggregator;
import edu.upf.bg.aggregation.MeanAggregator;
import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.ColorScalePoint;
import edu.upf.bg.colorscale.ColorScaleRange;
import edu.upf.bg.colorscale.NumericColorScale;

import java.awt.*;
import java.util.ArrayList;

public class LinearTwoSidedColorScale extends NumericColorScale {


    private ColorScalePoint min;
    private ColorScalePoint mid;
    private ColorScalePoint max;

    public LinearTwoSidedColorScale(ColorScalePoint min, ColorScalePoint mid, ColorScalePoint max) {
        super();
        this.min = min;
        this.mid = mid;
        this.max = max;
        updateRangesList();
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

        if (value == mid.getValue()) {
            return mid.getColor();
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
        updateRangesList();
    }

    public ColorScalePoint getMid() {
        return mid;
    }

    public void setMid(ColorScalePoint mid) {
        this.mid = mid;
        updateRangesList();
    }

    public ColorScalePoint getMax() {
        return max;
    }

    public void setMax(ColorScalePoint max) {
        this.max = max;
        updateRangesList();
    }

    @Override
    protected void updateRangesList() {

        ArrayList<ColorScaleRange> rangesList = getInternalScaleRanges();
        rangesList.clear();

        double[] points = getPoints();
        double min = getMinValue();
        double max = getMaxValue();
        double mid = getMid().getValue();

        rangesList.add(new ColorScaleRange(min,mid,1,min,"",mid,ColorScaleRange.LINEAR_TYPE));
        rangesList.add(new ColorScaleRange(mid,max,1,"","",max,ColorScaleRange.LINEAR_TYPE));

    }

    @Override
    public IAggregator defaultAggregator() {
        return MeanAggregator.INSTANCE;
    }
}
