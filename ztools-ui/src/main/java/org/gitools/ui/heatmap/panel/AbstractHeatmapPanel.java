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

package org.gitools.ui.heatmap.panel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import org.gitools.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.matrix.model.IMatrixView;

public class AbstractHeatmapPanel extends JPanel {

	protected Heatmap heatmap;

	protected AbstractHeatmapDrawer drawer;
	private final PropertyChangeListener heatmapListener;

	public AbstractHeatmapPanel(Heatmap heatmap, AbstractHeatmapDrawer drawer) {
		this.heatmap = heatmap;
		this.drawer = drawer;

		heatmapListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				heatmapPropertyChanged(evt);
			}
		};

		updateSubscriptions(null);

		setPreferredSize(drawer.getSize());

		setBorder(null);
	}

	public Heatmap getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(Heatmap heatmap) {
		Heatmap old = this.heatmap;
		this.heatmap = heatmap;
		this.drawer.setHeatmap(heatmap);
		updateSubscriptions(old);
		setPreferredSize(drawer.getSize());
	}

	public AbstractHeatmapDrawer getDrawer() {
		return drawer;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Dimension size = drawer.getSize();
		Rectangle box = new Rectangle(0, 0, size.width, size.height);
		Rectangle clip = g.getClipBounds();
		drawer.draw((Graphics2D) g, box, clip);
	}

	private void updateSubscriptions(Heatmap old) {
		if (old != null) {
			old.removePropertyChangeListener(heatmapListener);
			old.getColumnHeader().removePropertyChangeListener(heatmapListener);
			old.getRowHeader().removePropertyChangeListener(heatmapListener);
			old.getCellDecorator().removePropertyChangeListener(heatmapListener);
			old.getMatrixView().removePropertyChangeListener(heatmapListener);
		}

		heatmap.addPropertyChangeListener(heatmapListener);
		heatmap.getColumnHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getRowHeader().addPropertyChangeListener(heatmapListener);
		heatmap.getCellDecorator().addPropertyChangeListener(heatmapListener);
		heatmap.getMatrixView().addPropertyChangeListener(heatmapListener);
	}

	private void heatmapPropertyChanged(PropertyChangeEvent evt) {
		String pname = evt.getPropertyName();

		if (evt.getSource().equals(heatmap)) {
			if (Heatmap.CELL_DECORATOR_CHANGED.equals(pname)) {
				((ElementDecorator) evt.getOldValue()).removePropertyChangeListener(heatmapListener);
				heatmap.getCellDecorator().addPropertyChangeListener(heatmapListener);
			} else if (Heatmap.COLUMN_DECORATOR_CHANGED.equals(pname)) {
				((HeatmapHeader) evt.getOldValue()).removePropertyChangeListener(heatmapListener);
				heatmap.getColumnHeader().addPropertyChangeListener(heatmapListener);
			} else if (Heatmap.ROW_DECORATOR_CHANGED.equals(pname)) {
				((HeatmapHeader) evt.getOldValue()).removePropertyChangeListener(heatmapListener);
				heatmap.getRowHeader().addPropertyChangeListener(heatmapListener);
			} else if (Heatmap.MATRIX_VIEW_CHANGED.equals(pname)) {
				((IMatrixView) evt.getOldValue()).removePropertyChangeListener(heatmapListener);
				heatmap.getMatrixView().addPropertyChangeListener(heatmapListener);
			}

			if (Heatmap.CELL_SIZE_CHANGED.equals(pname)
					|| Heatmap.GRID_PROPERTY_CHANGED.equals(pname)) {
				
				setPreferredSize(drawer.getSize());
				revalidate();
			}
		}
		else if (evt.getSource().equals(heatmap.getMatrixView())) {
			if (IMatrixView.VISIBLE_COLUMNS_CHANGED.equals(pname)
					|| IMatrixView.VISIBLE_ROWS_CHANGED.equals(pname)) {

				setPreferredSize(drawer.getSize());
				revalidate();
			}
		}

		repaint();
	}
}
