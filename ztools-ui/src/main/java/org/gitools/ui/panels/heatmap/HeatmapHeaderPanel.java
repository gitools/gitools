package org.gitools.ui.panels.heatmap;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;

import org.gitools.heatmap.HeatmapHeaderDrawer;
import org.gitools.model.figure.MatrixFigure;

public class HeatmapHeaderPanel extends JPanel {

	private static final long serialVersionUID = 930370133535101914L;

	private MatrixFigure heatmap;
	
	private HeatmapHeaderDrawer drawer;
		
	public HeatmapHeaderPanel(MatrixFigure heatmap, boolean horizontal) {
		this.heatmap = heatmap;
		
		this.drawer = new HeatmapHeaderDrawer(heatmap, horizontal);
		
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
}
