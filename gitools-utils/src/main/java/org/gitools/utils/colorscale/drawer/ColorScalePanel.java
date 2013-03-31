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

package org.gitools.utils.colorscale.drawer;

import org.gitools.utils.colorscale.IColorScale;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class ColorScalePanel extends JPanel {

	private IColorScale scale;
	private ColorScaleDrawer drawer;

	public ColorScalePanel(IColorScale scale) {
		setScale(scale);
	}

	public IColorScale getScale() {
		return scale;
	}

	public void setScale(IColorScale scale) {
		this.scale = scale;

		this.drawer = new ColorScaleDrawer(scale);
		setPreferredSize(this.drawer.getSize());

		repaint();
	}

	public void update() {
		drawer.resetZoom();
		
		setPreferredSize(drawer.getSize());
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension size = getSize();
		Rectangle box = new Rectangle(0, 0, size.width, size.height);
		Rectangle clip = g.getClipBounds();
		drawer.draw((Graphics2D) g, box, clip);
	}
}
