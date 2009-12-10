/*
 *  Copyright 2009 chris.
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

package org.gitools.heatmap;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import org.gitools.model.figure.HeatmapFigure;

public abstract class AbstractDrawer {

	protected HeatmapFigure heatmap;

	public AbstractDrawer(HeatmapFigure heatmap) {
		this.heatmap = heatmap;
	}

	public HeatmapFigure getHeatmap() {
		return heatmap;
	}

	public void setHeatmap(HeatmapFigure heatmap) {
		this.heatmap = heatmap;
	}

	protected int getGridSize() {
		return heatmap.isShowGrid() ? 1 : 0;
	}

	protected int getBorderSize() {
		return heatmap.isShowBorders() ? 1 : 0;
	}

	public abstract Dimension getSize();
	
	public abstract void draw(Graphics2D g, Rectangle box, Rectangle clip);

	public abstract HeatmapPosition getPosition(Point p);

	public abstract Point getPoint(HeatmapPosition p);
}
