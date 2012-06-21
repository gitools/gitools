/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package edu.upf.bg.colorscale.drawer;

import edu.upf.bg.colorscale.IColorScale;
import edu.upf.bg.colorscale.NumericColorScale;
import edu.upf.bg.colorscale.impl.CategoricalColorScale;
import edu.upf.bg.formatter.GenericFormatter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ColorScaleDrawer {

	private NumericColorScale scale;

	private Color bgColor;

	private double zoomRangeMin;
	private double zoomRangeMax;

	private int widthPadding;
	private int heightPadding;
	
	private int barSize;
	private boolean barBorderEnabled;
	private Color barBorderColor;

	private boolean legendEnabled;
	private Color legendPointColor;
	private int legendPadding;
	private Font legendFont;
	private String legendFormat;

	public ColorScaleDrawer(IColorScale scale) {
		setScale(scale);

		this.bgColor = Color.WHITE;

		this.widthPadding = 8;
		this.heightPadding = 8;

		this.barSize = 18;
		this.barBorderEnabled = true;
		this.barBorderColor = Color.BLACK;

		this.legendEnabled = true;
		this.legendPointColor = Color.BLACK;
		this.legendPadding = 4;
		this.legendFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		this.legendFormat = "%.2f";
	}

	public IColorScale getScale() {
		return scale;
	}

	public void setScale(IColorScale scale) {
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

	public void draw(Graphics2D g, Rectangle bounds, Rectangle clip) {

		g.setColor(bgColor);
		g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
		
		bounds.x += widthPadding;
		bounds.width -= widthPadding * 2;
		bounds.y += heightPadding;
		bounds.height -= heightPadding * 2;

		int bxs = bounds.x;
		int bxe = bxs + bounds.width - 1;
		int bys = bounds.y;
		int bye = bys + barSize - 1;

		double delta = (zoomRangeMax - zoomRangeMin) / (bxe - bxs);
		double value = zoomRangeMin;

		for (int x = bxs; x <= bxe; x++) {
			final Color color = scale.valueColor(value);
			g.setColor(color);
			g.drawLine(x, bys, x, bye);
			value += delta;
		}

		if (barBorderEnabled) {
			g.setColor(barBorderColor);
			g.drawRect(bxs, bys, bxe - bxs, bye - bys);
		}

        if (legendEnabled) {
			int fontHeight = g.getFontMetrics().getHeight();
			int ys = bye + legendPadding;
			int ye = ys + fontHeight;
			double invDelta = 1.0 / delta;

			GenericFormatter gf = new GenericFormatter();

			g.setColor(legendPointColor);

            int lastX = 0;
            int minusWidth = g.getFontMetrics().stringWidth("-");
            double[] points = scale.getPoints();
			for (int i=0; i < points.length ; i++ ) {
                double point = points[i];
                /*experimental*/
                if (scale instanceof CategoricalColorScale) {

                    
                }
                    /*end-experimental*/           
                
				if (point >= zoomRangeMin && point <= zoomRangeMax) {
					
                    int x = bxs + (int) ((point - zoomRangeMin) * invDelta);
					String legend = gf.format(legendFormat, point);
                    /*experimental*/
                    if (scale instanceof CategoricalColorScale) {
                        scale = (CategoricalColorScale) scale;
                        String name = ((CategoricalColorScale) scale).getPointObjects()[i].getName();
                        legend = (name == "") ? legend : name;
                    }
                    /*end-experimental*/
                    int fontWidth = g.getFontMetrics().stringWidth(legend);
                    int positiveOffset = (point >= 0 ? minusWidth : 0);

                    int optimalPosition = x - 2 - fontWidth - positiveOffset;
                    if (optimalPosition < lastX) {

                        // Check if it fits in the next range
                        if ( x != bxs && i + 1 < points.length) {
                            
                           double nextPoint = points[i + 1];
                           int nextX = bxs + (int) ((nextPoint - zoomRangeMin) * invDelta);

                           if ( x + (positiveOffset + fontWidth)*2 + 2 > nextX) {

                               g.drawLine(x, bys, x, ye - fontHeight);

                                // Skip this point because there is no space for it.
                                continue;
                           }
                        }

                        g.drawLine(x, bys, x, ye);
                        lastX = x + fontWidth + 2;
                        x = x + 2;
                        
                    } else {

                       g.drawLine(x, bys, x, ye);
                       lastX = x + 2;
                       x = x - fontWidth - 2;

                    }

                    /*
					if (x + 2 + fontWidth > bxe)
						x = x - fontWidth - 2;
					else
						x = x + 2;
						*/
                    if (x < bxe)
                        g.drawString(legend, x, ye);

				}
			}
		}
	}

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
