/*
 *  Copyright 2009 cperez.
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

package org.gitools.ui.panels.heatmap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.gitools.heatmap.AbstractDrawer;
import org.gitools.model.figure.heatmap.Heatmap;

public class AbstractHeatmapPanel extends JPanel {

	protected Heatmap heatmap;
	protected AbstractDrawer drawer;

	public AbstractHeatmapPanel(Heatmap heatmap, AbstractDrawer drawer) {
		this.heatmap = heatmap;
		this.drawer = drawer;

		heatmapChanged();

		setBorder(null);
	}

	public Heatmap getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(Heatmap heatmap) {
		this.heatmap = heatmap;
		heatmapChanged();
	}

	public AbstractDrawer getDrawer() {
		return drawer;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension size = drawer.getSize();
		Rectangle box = new Rectangle(0, 0, size.width, size.height);
		Rectangle clip = g.getClipBounds();
		drawer.draw((Graphics2D) g, box, clip);
	}

	private void heatmapChanged() {
		setPreferredSize(drawer.getSize());
		
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getSource().equals(heatmap)
						|| evt.getSource().equals(heatmap.getColumnHeader())
						|| evt.getSource().equals(heatmap.getRowHeader())) {

					heatmapPropertyChanged(evt);
				}
				else
					repaint();
			}
		};

		heatmap.addPropertyChangeListener(listener);
		heatmap.getColumnHeader().addPropertyChangeListener(listener);
		heatmap.getRowHeader().addPropertyChangeListener(listener);
		heatmap.getMatrixView().addPropertyChangeListener(listener);
	}

	private void heatmapPropertyChanged(PropertyChangeEvent evt) {
		Dimension ps = getPreferredSize();
		if (ps.width != heatmap.getCellWidth()
				|| ps.height != heatmap.getCellHeight()) {
			
			setPreferredSize(drawer.getSize());
			
			revalidate();
		}

		repaint();
	}
}
