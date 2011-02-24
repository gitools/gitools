/*
 *  Copyright 2011 chris.
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

public class HeatmapHeaderDrawer extends AbstractHeatmapDrawer {

	private boolean horizontal;
	//private HeatmapLabelsDrawer labelsDrawer;

	private AbstractHeatmapDrawer[] drawers;

	public HeatmapHeaderDrawer(Heatmap heatmap, boolean horizontal) {
		super(heatmap);

		this.horizontal = horizontal;

		this.drawers = new AbstractHeatmapDrawer[] {
			new HeatmapLabelsDrawer(heatmap, horizontal) };
	}

	@Override
	public Dimension getSize() {
		int w = 0;
		int h = 0;
		if (horizontal) {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				if (sz.width > w)
					w = sz.width;
				h += sz.height;
			}
		}
		else {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				if (sz.height > h)
					h = sz.height;
				w += sz.width;
			}
		}
		
		return new Dimension(w, h);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		int x = 0;
		int y = 0;
		if (horizontal) {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
				d.draw(g, box2, clip.intersection(box2));
				y += sz.height;
			}
		}
		else {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
				d.draw(g, box2, clip.intersection(box2));
				x += sz.width;
			}
		}
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		int x = 0;
		int y = 0;
		if (horizontal) {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
				if (box2.contains(p)) {
					p.translate(-x, -y);
					return d.getPosition(p);
				}
				y += sz.height;
			}
		}
		else {
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				Rectangle box2 = new Rectangle(x, y, sz.width, sz.height);
				if (box2.contains(p)) {
					p.translate(-x, -y);
					return d.getPosition(p);
				}
				x += sz.width;
			}
		}
		return new HeatmapPosition(0, 0);
	}

	@Override
	public Point getPoint(HeatmapPosition p) {
		//throw new UnsupportedOperationException("Not supported yet.");
		return new Point(0, 0);
	}

}
