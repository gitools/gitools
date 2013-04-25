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
package org.gitools.core.heatmap.drawer;

import org.apache.commons.lang.StringUtils;
import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.model.decorator.Decoration;
import org.gitools.utils.color.utils.ColorUtils;

import java.awt.*;

public abstract class AbstractHeatmapDrawer {

    private Heatmap heatmap;

    private boolean pictureMode;

    protected AbstractHeatmapDrawer(Heatmap heatmap) {
        this.heatmap = heatmap;
        this.pictureMode = false;
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
     * @param g the g
     * @param box the box
     */
    protected static void paintBackground(Color backgroundColor, Graphics2D g, Rectangle box) {
        g.setColor(backgroundColor);
        g.fillRect(box.x, box.y, box.width, box.height);
    }

    /**
     * Automatically change the font size to fit in the cell height.
     *
     * @param g the Graphics2D object
     * @param cellHeight the cell height
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

        String text = decoration.getText();
        if (!StringUtils.isEmpty(text)) {

            Font font = g.getFont();

            boolean isRotated = !font.getTransform().isIdentity();

            int fontHeight = (int) font.getSize2D();

            if (fontHeight <= (isRotated ? width : height)) {

                int textWidth = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();

                if (textWidth > (isRotated ? height : width)) {
                    text = "...";
                    fontHeight = 2;
                    textWidth = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
                }


                int leftMargin = ((width - textWidth) / 2) + 1;
                int bottomMargin = ((height - fontHeight) / 2) + 1;

                if (isRotated) {
                    leftMargin = ((width - fontHeight) / 2) + 1;
                    bottomMargin = height - (((height - textWidth) / 2));
                }

                g.setColor(ColorUtils.bestForegroundColor(decoration.getBgColor()));
                g.drawString(text, x + leftMargin, y + height - bottomMargin);
            }
        }

    }

}
