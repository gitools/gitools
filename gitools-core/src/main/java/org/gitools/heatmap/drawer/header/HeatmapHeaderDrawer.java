/*
 *  Copyright 2011 Universitat Pompeu Fabra.
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

package org.gitools.heatmap.drawer.header;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.header.HeatmapColoredLabelsHeader;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.heatmap.header.HeatmapTextLabelsHeader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HeatmapHeaderDrawer extends AbstractHeatmapDrawer {

	private boolean horizontal;
	
	private List<AbstractHeatmapDrawer> drawers;

	public HeatmapHeaderDrawer(Heatmap heatmap, boolean horizontal) {
		super(heatmap);

		this.horizontal = horizontal;

		updateDrawers();
	}

	public final void updateDrawers() {
		List<HeatmapHeader> headers = horizontal ?
			heatmap.getColumnDim().getHeaders()
			: heatmap.getRowDim().getHeaders();

		drawers = new ArrayList<AbstractHeatmapDrawer>(headers.size());

		for (int i = 0; i < headers.size(); i++) {
			HeatmapHeader h = headers.get(i);
			if (!h.isVisible())
				continue;

			AbstractHeatmapDrawer d = null;
			if (h instanceof HeatmapTextLabelsHeader)
				d = new HeatmapTextLabelsDrawer(heatmap, (HeatmapTextLabelsHeader) h, horizontal);
			else if (h instanceof HeatmapColoredLabelsHeader)
				d = new HeatmapColoredLabelsDrawer(heatmap, (HeatmapColoredLabelsHeader) h, horizontal);
            else if (h instanceof HeatmapDataHeatmapHeader)
                d = new HeatmapDataHeatmapDrawer(heatmap, (HeatmapDataHeatmapHeader) h, horizontal);
			
			if (d != null) {
				d.setPictureMode(pictureMode);
				drawers.add(d);
			}
		}
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

	protected static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		if (horizontal) {
			int x = box.y;
			int y = box.x;
			int totalSize = box.height;
			Rectangle clip2 = new Rectangle(clip.y, clip.x, clip.height, clip.width);
			g.rotate(radianAngle90);
			g.translate(-totalSize, 0);
			g.fillRect(box.x, box.y, box.width, box.height);
			for (AbstractHeatmapDrawer d : drawers) {
				Dimension sz = d.getSize();
				Rectangle box2 = new Rectangle(x, y, sz.height, sz.width);
				d.draw(g, box2, clip2.intersection(box2));
				x += box2.width;
			}
		}
		else {
			int x = box.x;
			int y = box.y;
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

	@Override
	public void setPictureMode(boolean pictureMode) {
		for (AbstractHeatmapDrawer d : drawers)
			d.setPictureMode(pictureMode);
	}

}
