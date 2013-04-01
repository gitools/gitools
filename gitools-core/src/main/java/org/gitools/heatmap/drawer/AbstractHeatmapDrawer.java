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
package org.gitools.heatmap.drawer;

import org.gitools.heatmap.Heatmap;

import java.awt.*;

public abstract class AbstractHeatmapDrawer
{

    protected Heatmap heatmap;

    protected boolean pictureMode;

    public AbstractHeatmapDrawer(Heatmap heatmap)
    {
        this.heatmap = heatmap;
        this.pictureMode = false;
    }

    public Heatmap getHeatmap()
    {
        return heatmap;
    }

    public void setHeatmap(Heatmap heatmap)
    {
        this.heatmap = heatmap;
    }

	/*protected int getRowsGridSize() {
        return heatmap.isRowsGridEnabled() ? heatmap.getRowsGridSize() : 0;
	}

	protected int getColumnsGridSize() {
		return heatmap.isColumnsGridEnabled() ? heatmap.getColumnsGridSize() : 0;
	}*/

    protected int getBorderSize()
    {
        return heatmap.isShowBorders() ? 1 : 0;
    }

    public boolean isPictureMode()
    {
        return pictureMode;
    }

    public void setPictureMode(boolean pictureMode)
    {
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
}
