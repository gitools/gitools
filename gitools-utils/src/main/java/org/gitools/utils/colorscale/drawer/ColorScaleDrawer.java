/*
 * #%L
 * gitools-utils
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.utils.colorscale.drawer;

import org.gitools.utils.colorscale.ColorScaleRange;
import org.gitools.utils.colorscale.IColorScale;
import org.gitools.utils.colorscale.NumericColorScale;
import org.gitools.utils.formatter.GenericFormatter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @noinspection ALL
 */
public class ColorScaleDrawer {

    private NumericColorScale scale;

    private final Color bgColor;

    private double zoomRangeMin;
    private double zoomRangeMax;

    private final int widthPadding;
    private final int heightPadding;

    private int barSize;
    private final Color barBorderColor;

    private final boolean legendEnabled;
    private final Color legendPointColor;
    private final int legendPadding;
    private final Font legendFont;
    private final String legendFormat;

    public ColorScaleDrawer(IColorScale scale) {
        setScale(scale);

        this.bgColor = Color.WHITE;

        this.widthPadding = 8;
        this.heightPadding = 8;

        this.barSize = 18;
        this.barBorderColor = Color.BLACK;

        this.legendEnabled = true;
        this.legendPointColor = Color.BLACK;
        this.legendPadding = 4;
        this.legendFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        this.legendFormat = "%.3g";
    }

    public IColorScale getScale() {
        return scale;
    }

    void setScale(IColorScale scale) {
        this.scale = (NumericColorScale) scale;
        resetZoom();
    }

    public void resetZoom() {
        zoomRangeMin = scale.getMinValue();
        zoomRangeMax = scale.getMaxValue();
    }

    public double getZoomRangeMin() {
        return zoomRangeMin;
    }

    public void setZoomRangeMin(double zoomRangeMin) {
        this.zoomRangeMin = zoomRangeMin;
    }

    public double getZoomRangeMax() {
        return zoomRangeMax;
    }

    public void setZoomRangeMax(double zoomRangeMax) {
        this.zoomRangeMax = zoomRangeMax;
    }

    public int getBarSize() {
        return barSize;
    }

    public void setBarSize(int barSize) {
        this.barSize = barSize;
    }

    public void draw(@NotNull Graphics2D g, @NotNull Rectangle bounds, Rectangle clip) {

        g.setColor(bgColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        bounds.x += widthPadding;
        bounds.width -= widthPadding * 2;
        bounds.y += heightPadding;
        bounds.height -= heightPadding * 2;

        int scaleXLeft = bounds.x;
        int scaleXRight = scaleXLeft + bounds.width - 1;
        int scaleYTop = bounds.y;
        int scaleYBottom = scaleYTop + barSize - 1;
        int scaleWidth = scaleXRight - scaleXLeft;
        int scaleHeight = scaleYBottom - scaleYTop;

        ArrayList<ColorScaleRange> ranges = scale.getScaleRanges();

        adjustRangeWidths(scaleWidth, ranges);

        int rangeXLeft = scaleXLeft;

        for (ColorScaleRange range : ranges) {


            int rangeWidth = (int) range.getWidth();

            int rangeEnd = rangeXLeft + rangeWidth;

            //paint the colored box of scale range
            if (!(range.getType().equals(ColorScaleRange.EMPTY_TYPE))) {
                for (int x = rangeXLeft; x <= rangeEnd; x++) {
                    double value = getValueForX(x, range.getType(), rangeXLeft, rangeEnd, range.getMinValue(), range.getMaxValue());
                    final Color color = scale.valueColor(value);
                    g.setColor(color);
                    g.drawLine(x, scaleYTop, x, scaleYBottom);
                }
            }

            //paint Border
            if (range.isBorderEnabled()) {
                g.setColor(barBorderColor);
                g.drawRect(rangeXLeft, scaleYTop, rangeWidth, scaleHeight);
            }

            //paint legend
            if (legendEnabled) {

                int lastX = rangeXLeft;
                int fontHeight = g.getFontMetrics().getHeight();
                int legendYTop = scaleYBottom + legendPadding;
                int ye = legendYTop + fontHeight;
                GenericFormatter gf = new GenericFormatter();
                g.setColor(legendPointColor);


                if (range.getLeftLabel() != null) {
                    String legend = gf.format(range.getLeftLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + legendPadding;
                    g.drawString(legend, legendStart, ye);
                    lastX = fontWidth + legendPadding;
                }
                if (range.getCenterLabel() != null) {
                    String legend = gf.format(range.getCenterLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeEnd - (rangeWidth / 2 + fontWidth / 2);
                    if (legendStart > lastX + legendPadding * 2) {
                        g.drawString(legend, legendStart, ye);
                        lastX = legendStart + fontWidth + legendPadding;
                    }
                }
                if (range.getRightLabel() != null) {
                    String legend = gf.format(range.getRightLabel());
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int legendStart = rangeXLeft + rangeWidth - legendPadding - fontWidth;
                    if (legendStart > lastX + legendPadding) {
                        g.drawString(legend, legendStart, ye);
                    }
                }
            }

            rangeXLeft = rangeEnd;

        }
    }

    private double getValueForX(int x, @NotNull String type, int minX, int maxX, double minValue, double maxValue) {
        if (type.equals(ColorScaleRange.LINEAR_TYPE)) {

            double delta = (maxValue - minValue) / (maxX - minX);
            return minValue + delta * (x - minX);

        } else if (type.equals(ColorScaleRange.LOGARITHMIC_TYPE)) {

            double exp = 1.0 * (x - minX) / (maxX - minX);
            return minValue + ((Math.pow(10, exp) - 1) / 9) * (maxValue - minValue);

        } else if (type.equals(ColorScaleRange.CONSTANT_TYPE)) {

            return maxValue;

        }


        return 0;
    }

    private void adjustRangeWidths(int scaleWidth, @NotNull ArrayList<ColorScaleRange> ranges) {
        double rangesWidth = 0;
        for (ColorScaleRange r : ranges) {
            rangesWidth += r.getWidth();
        }

        HashSet rangesSet = new HashSet<ColorScaleRange>(ranges);

        double resizeFactor = scaleWidth / rangesWidth;

        Iterator iter = rangesSet.iterator();
        while (iter.hasNext()) {
            ColorScaleRange r = (ColorScaleRange) iter.next();
            r.setWidth(r.getWidth() * resizeFactor);
        }
    }

    @NotNull
    public Dimension getSize() {
        int height = heightPadding * 2 + barSize;
        if (legendEnabled) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            height += g.getFontMetrics(legendFont).getHeight() + legendPadding;
        }

        int width = widthPadding + 20;

        return new Dimension(width, height);
    }
}
