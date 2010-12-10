/*
 *  Copyright 2009 Universitat Pompeu Fabra.
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

package org.gitools.heatmap.drawer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import org.gitools.heatmap.model.Heatmap;

public abstract class AbstractHeatmapDrawer {

	protected Heatmap heatmap;
	
	protected boolean pictureMode;

	public AbstractHeatmapDrawer(Heatmap heatmap) {
		this.heatmap = heatmap;
		this.pictureMode = false;
	}

	public Heatmap getHeatmap() {
		return heatmap;
	}

	public void setHeatmap(Heatmap heatmap) {
		this.heatmap = heatmap;
	}

	protected int getRowsGridSize() {
		return heatmap.isRowsGridEnabled() ? heatmap.getRowsGridSize() : 0;
	}

	protected int getColumnsGridSize() {
		return heatmap.isColumnsGridEnabled() ? heatmap.getColumnsGridSize() : 0;
	}

	protected int getBorderSize() {
		return heatmap.isShowBorders() ? 1 : 0;
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
	 * @param g Drawing device
	 * @param box The bounds of the total canvas
	 * @param clip The clip (inside the box)
	 */
	public abstract void draw(Graphics2D g, Rectangle box, Rectangle clip);

	public abstract HeatmapPosition getPosition(Point p);

	public abstract Point getPoint(HeatmapPosition p);
}
