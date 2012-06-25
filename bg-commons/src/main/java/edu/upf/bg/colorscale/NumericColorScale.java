package edu.upf.bg.colorscale;


import edu.upf.bg.color.utils.ColorUtils;
import edu.upf.bg.colorscale.util.ColorConstants;
import org.apache.commons.collections.iterators.SingletonIterator;

import java.awt.*;
import java.util.ArrayList;

public abstract class NumericColorScale implements IColorScale, IColorScaleHtml {

    private Color notANumberColor;
    private Color posInfinityColor;
    private Color negInfinityColor;
    private Color emptyColor;

    private ArrayList<ColorScaleRange> rangesList;

    protected NumericColorScale() {
        super();
        rangesList = new ArrayList<ColorScaleRange>();
    }

    @Override
    public Color valueColor(double value) {
        Color color = limitsColor(value);
        if (color != null)
            return color;

        return colorOf(value);
    }

    protected abstract Color colorOf(double value);

    public abstract double[] getPoints();

    public double getMinValue() {
        return getPoints()[0];
    }

    public double getMaxValue() {
        return getPoints()[getPoints().length - 1];
    }

    @Override
    public String valueRGBHtmlColor(double value) {
        Color color = valueColor(value);
        return ColorUtils.colorToRGBHtml(color);
    }

    @Override
    public String valueHexHtmlColor(double value) {
        Color color = valueColor(value);
        return ColorUtils.colorToHexHtml(color);
    }

    public Color limitsColor(double value) {
        if (Double.isNaN(value))
            return getNotANumberColor();
        else if (value == Double.POSITIVE_INFINITY)
            return getPosInfinityColor();
        else if (value == Double.NEGATIVE_INFINITY)
            return getNegInfinityColor();

        return null;
    }

    public Color getNotANumberColor() {
        if (notANumberColor == null) {
            return ColorConstants.notANumberColor;
        }
        return notANumberColor;
    }

    public void setNotANumberColor(Color notANumberColor) {
        this.notANumberColor = notANumberColor;
    }

    public Color getPosInfinityColor() {
        if (posInfinityColor == null) {
            return ColorConstants.posInfinityColor;
        }
        return posInfinityColor;
    }

    public void setPosInfinityColor(Color posInfinityColor) {
        this.posInfinityColor = posInfinityColor;
    }

    public Color getNegInfinityColor() {
        if (negInfinityColor == null) {
            return ColorConstants.negInfinityColor;
        }
        return negInfinityColor;
    }

    public void setNegInfinityColor(Color negInfinityColor) {
        this.negInfinityColor = negInfinityColor;
    }

    public Color getEmptyColor() {
        if (emptyColor == null) {
            return ColorConstants.emptyColor;
        }
        return emptyColor;
    }

    public void setEmptyColor(Color emptyColor) {
        this.emptyColor = emptyColor;
    }
    
    public final ArrayList<ColorScaleRange> getScaleRanges() {

        updateRangesList();

        return rangesList;
    }

    protected ArrayList<ColorScaleRange> getInternalScaleRanges() {
        if (rangesList == null) {
            rangesList = new ArrayList<ColorScaleRange>();
        }
        return rangesList;
    }

    protected void updateRangesList() {
        rangesList.clear();

        double[] points = getPoints();
        double min = Double.NaN;
        double max = Double.NaN;
        for (int i = 0; i < points.length; i++) {
            min = max;
            max = points[i];

            if (Double.isNaN(min))
                continue;

            Object leftLabel = (i==1) ? min : "";
            Object rightLabel = max;
            ColorScaleRange range = new ColorScaleRange(min,max,(max-min));
            range.setLeftLabel(leftLabel);
            range.setRightLabel(rightLabel);

            rangesList.add(range);
        }
    }
}
