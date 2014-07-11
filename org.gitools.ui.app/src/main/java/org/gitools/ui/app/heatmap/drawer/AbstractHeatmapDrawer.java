/*
 * #%L
 * gitools-core
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
package org.gitools.ui.app.heatmap.drawer;

import org.apache.commons.lang.StringUtils;
import org.gitools.api.matrix.MatrixDimensionKey;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.decorator.Decoration;
import org.gitools.ui.core.HeatmapPosition;
import org.gitools.utils.color.Colors;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;

public abstract class AbstractHeatmapDrawer {

    public static final Color SELECTED_COLOR = new Color(0, 0, 128, 60);

    protected Heatmap heatmap;
    protected HeatmapDimension rows;
    protected HeatmapDimension columns;
    private static OnScreenRect onScreenRect;

    public static final int HIGHLIGHT_POLICY_NORMAL = 0;
    public static final int HIGHLIGHT_POLICY_FORCE = 1;
    public static final int HIGHLIGHT_POLICY_AVOID = 2;

    private static final double radianAngle90 = (90.0 / 180.0) * Math.PI;

    private boolean pictureMode;

    protected AbstractHeatmapDrawer(Heatmap heatmap) {
        this.heatmap = heatmap;
        this.columns = heatmap.getColumns();
        this.rows = heatmap.getRows();
        this.pictureMode = false;
        if (onScreenRect == null) {
            this.onScreenRect = new OnScreenRect();
        }
    }

    public Heatmap getHeatmap() {
        return heatmap;
    }

    public void setHeatmap(Heatmap heatmap) {
        this.heatmap = heatmap;
    }

    public boolean isPictureMode() {
        return pictureMode;
    }

    public void setPictureMode(boolean pictureMode) {
        this.pictureMode = pictureMode;
    }

    public abstract Dimension getSize();

    /**
     * Draw contents on the rectangle delimited by box using the clip.
     *
     * @param g    Drawing device
     * @param box  The bounds of the total canvas
     * @param clip The clip (inside the box)
     */
    public abstract void draw(Graphics2D g, Rectangle box, Rectangle clip);

    public abstract HeatmapPosition getPosition(Point p);

    public abstract Point getPoint(HeatmapPosition p);

    /**
     * Paint background.
     *
     * @param backgroundColor the background color
     * @param g               the g
     * @param box             the box
     */
    protected static void paintBackground(Color backgroundColor, Graphics2D g, Rectangle box) {
        g.setColor(backgroundColor);
        g.fillRect(box.x, box.y, box.width, box.height);
    }

    /**
     * Automatically change the font size to fit in the cell height.
     *
     * @param g           the Graphics2D object
     * @param cellHeight  the cell height
     * @param minFontSize the min font size
     * @return Returns true if the new font size fits in the cell height, false if it doesn't fit.
     */
    protected static boolean calculateFontSize(Graphics2D g, int cellHeight, int minFontSize) {

        float fontHeight = g.getFontMetrics().getHeight();

        float fontSize = g.getFont().getSize();
        while (fontHeight > (cellHeight - 2) && fontSize > minFontSize) {
            fontSize--;
            g.setFont(g.getFont().deriveFont(fontSize));
            fontHeight = g.getFontMetrics().getHeight();
        }

        return fontHeight <= (cellHeight - 2);
    }

    protected static void paintCell(Decoration decoration, Color gridColor, int gridSize, int offsetX, int offsetY, int width, int height, Graphics2D g, Rectangle box) {

        int y = box.y + offsetY;
        int x = box.x + offsetX;

        g.setColor(decoration.getBgColor());
        g.fillRect(x, y, width, height);

        g.setColor(gridColor);
        g.fillRect(x, y + height, width, gridSize);

        String text = decoration.getFormatedValue();
        if (!StringUtils.isEmpty(text)) {

            Font font = g.getFont();

            boolean isRotated = decoration.isRotate();

            int fontHeight = (int) font.getSize2D();

            if (fontHeight <= (isRotated ? width : height)) {

                int textWidth = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
                //TODO: textWidth depends on SuperScript

                if (textWidth < (isRotated ? height : width)) {

                    int leftMargin = ((width - textWidth) / 2) + 1;
                    int bottomMargin = ((height - fontHeight) / 2) + 1;

                    if (isRotated) {
                        leftMargin = ((width - fontHeight) / 2) + 1;
                        bottomMargin = height - (((height - textWidth) / 2));
                    }

                    g.setColor(Colors.bestForegroundColor(decoration.getBgColor()));
                    if (text.matches("[0-9\\.]+e-?[0-9]+")) {
                        int e_pos = text.indexOf("e") + 3;
                        text = text.replaceAll("e(-?[0-9]+)", "·10$1");
                        int superscriptEnd = text.length();
                        AttributedString attText = new AttributedString(text);
                        attText.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, e_pos, superscriptEnd);
                        if (isRotated) {
                            g.rotate(radianAngle90);
                            g.drawString(attText.getIterator(), y + height - bottomMargin, - x - leftMargin - 1);
                            g.rotate(-radianAngle90);
                        } else {
                            g.drawString(attText.getIterator(), x + leftMargin, y + height - bottomMargin);
                        }
                    } else {

                        if (isRotated) {
                            g.rotate(radianAngle90);
                            g.drawString(text, y + height - bottomMargin, - x - leftMargin - 1);

                            if ("CoCA-08".equals(text)) {
                                System.out.println("x = " + x + " leftMargin = " + leftMargin + " width = " + width + " fontHeight = " + fontHeight);
                            }

                            g.rotate(-radianAngle90);
                        } else {
                            g.drawString(text, x + leftMargin, y + height - bottomMargin);
                        }
                    }
                }
            }
        }

    }


    protected void drawSelectedHighlightedAndFocus(Graphics2D g, Rectangle box, HeatmapDimension dimension, boolean drawingRows) {
        drawSelectedHighlightedAndFocus(g, box, dimension, drawingRows, HIGHLIGHT_POLICY_NORMAL);
    }

    protected void drawSelectedHighlightedAndFocus(Graphics2D g, Rectangle box, HeatmapDimension dimension, boolean drawingRows, int highlightPolicy) {

        // Draw selected
        Color selectedColor = SELECTED_COLOR;
        Color highlightedColor = dimension.getHighlightingColor();

        int start = -1;
        int end = -1;
        if (dimension.getId().equals(MatrixDimensionKey.ROWS)) {
            start = this.onScreenRect.rowStart;
            end = this.onScreenRect.rowEnd;
        } else {
            start = this.onScreenRect.colStart;
            end = this.onScreenRect.colEnd;
        }

        int cellSize = dimension.getFullSize();
        for (int i = start; i <= end; i++) {
            String id = dimension.getLabel(i);
            Color paint = null;

            // is selected
            if (dimension.getSelected().contains(id)) {
                paint = SELECTED_COLOR;
            }

            // is highlighted
            if (highlightPolicy == HIGHLIGHT_POLICY_FORCE ||
                    (dimension.isHighlighted(id) && highlightPolicy != HIGHLIGHT_POLICY_AVOID)) {
                paint = (paint == null) ? highlightedColor : blend(SELECTED_COLOR, highlightedColor);
            }

            // draw selection and highlight
            if (paint != null) {
                g.setColor(paint);
                fillLine(g, box, i * cellSize, cellSize, drawingRows);
            }
        }

        // Draw row lead
        g.setColor(Color.DARK_GRAY);
        int lead = dimension.indexOf(dimension.getFocus());
        if (lead != -1) {
            fillLine(g, box, (lead * cellSize) - 1, 1, drawingRows);
            fillLine(g, box, ((lead + 1) * cellSize) - 1, 1, drawingRows);
        }
    }

    /**
     * Paint a full horizontal or vertical line
     */
    protected void fillLine(Graphics2D g, Rectangle box, int position, int size, boolean drawingRows) {
        if (drawingRows) {
            g.fillRect(box.x, box.y + position, box.width, size);
        } else {
            g.fillRect(box.x + position, box.y, size, box.height);
        }
    }


    public OnScreenRect getOnScreenRect() {
        return this.onScreenRect;
    }

    public void setOnScreenRect(OnScreenRect onScreenRect) {
        this.onScreenRect = onScreenRect;
    }


    public static Color blend(Color c0, Color c1) {
        double totalAlpha = c0.getAlpha() + c1.getAlpha();
        double weight0 = c0.getAlpha() / totalAlpha;
        double weight1 = c1.getAlpha() / totalAlpha;

        double r = weight0 * c0.getRed() + weight1 * c1.getRed();
        double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
        double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
        double a = Math.max(c0.getAlpha(), c1.getAlpha());

        return new Color((int) r, (int) g, (int) b, (int) a);
    }

}
