package org.gitools.ui.panels.heatmap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import org.gitools.heatmap.HeatmapBodyDrawer;
import org.gitools.model.figure.MatrixFigure;

public class HeatmapBodyPanel extends JPanel implements Scrollable {

	private static final long serialVersionUID = 930370133535101914L;

	private MatrixFigure heatmap;
	
	private HeatmapBodyDrawer drawer;
		
	public HeatmapBodyPanel(MatrixFigure heatmap) {
		this.heatmap = heatmap;
		
		this.drawer = new HeatmapBodyDrawer(heatmap);
		
		heatmapChanged();
	}
	
	public MatrixFigure getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(MatrixFigure heatmap) {
		this.heatmap = heatmap;
		heatmapChanged();
	}
	
	private void heatmapChanged() {
		setPreferredSize(drawer.getSize());
		heatmap.addPropertyChangeListener(new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(MatrixFigure.PROPERTY_CHANGED))
					heatmapPropertyChanged();
				else
					repaint();
			}
		});
	}
	
	private void heatmapPropertyChanged() {
		Dimension ps = getPreferredSize();
		if (ps.width != heatmap.getCellWidth()
				|| ps.height != heatmap.getCellHeight()) {
			setPreferredSize(drawer.getSize());
			repaint();
			revalidate();
		}
		else
			repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		drawer.draw((Graphics2D) g);
	}

	@Override
	public int getScrollableBlockIncrement(
			Rectangle visibleRect, int orientation, int direction) {
		
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int size = orientation == SwingConstants.HORIZONTAL ?
			heatmap.getCellWidth() : heatmap.getCellHeight();
		return size + gridSize;
	}

	@Override
	public int getScrollableUnitIncrement(
			Rectangle visibleRect, int orientation, int direction) {
		
		return 1;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		Dimension sz = getPreferredSize();
		return sz;
	}

	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	@Override
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
}
