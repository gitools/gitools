package org.gitools.ui.panels.heatmap;


import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import javax.swing.JViewport;
import org.gitools.heatmap.HeatmapPosition;
import org.gitools.model.figure.heatmap.Heatmap;

public class HeatmapPanel extends JPanel {

	private static final long serialVersionUID = 5817479437770943868L;
	
	private Heatmap heatmap;

	private HeatmapBodyPanel bodyPanel;
	private HeatmapHeaderPanel columnHeaderPanel;
	private HeatmapHeaderPanel rowHeaderPanel;

	private JViewport bodyVP;
	private JViewport colVP;
	private JViewport rowVP;

	private JScrollBar colSB;
	private JScrollBar rowSB;
	
	public HeatmapPanel(Heatmap heatmap) {
		this.heatmap = heatmap;

		heatmapChanged();

		createComponents();
	}
	
	public Heatmap getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(Heatmap heatmap) {
		this.heatmap = heatmap;
	}
	
	private void createComponents() {
		bodyPanel = new HeatmapBodyPanel(heatmap);
		columnHeaderPanel = new HeatmapHeaderPanel(heatmap, true);
		rowHeaderPanel = new HeatmapHeaderPanel(heatmap, false);

		bodyVP = new JViewport();
		bodyVP.setView(bodyPanel);

		HeatmapBodyController bodyController = new HeatmapBodyController(this);
		
		colVP = new JViewport();
		colVP.setView(columnHeaderPanel);

		HeatmapHeaderController colController = new HeatmapHeaderController(this, true);

		rowVP = new JViewport();
		rowVP.setView(rowHeaderPanel);

		HeatmapHeaderController rowController = new HeatmapHeaderController(this, false);

		colSB = new JScrollBar(JScrollBar.HORIZONTAL);
		colSB.addAdjustmentListener(new AdjustmentListener() {
			@Override public void adjustmentValueChanged(AdjustmentEvent e) {
				updateViewPorts(); } });
		rowSB = new JScrollBar(JScrollBar.VERTICAL);
		rowSB.addAdjustmentListener(new AdjustmentListener() {
			@Override public void adjustmentValueChanged(AdjustmentEvent e) {
				updateViewPorts(); } });

		setLayout(new HeatmapLayout());
		add(colVP);
		add(rowVP);
		add(bodyVP);
		add(colSB);
		add(rowSB);

		addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateScrolls();
			}

			@Override public void componentMoved(ComponentEvent e) {}
			@Override public void componentShown(ComponentEvent e) {}
			@Override public void componentHidden(ComponentEvent e) {}
		});
	}

	private void updateScrolls() {
		Dimension totalSize = bodyVP.getViewSize();
		Dimension visibleSize = bodyVP.getSize();

		int scrollWidth = totalSize.width - visibleSize.width;
		int scrollHeight = totalSize.height - visibleSize.height;

		colSB.setValueIsAdjusting(true);
		colSB.setMinimum(0);
		colSB.setMaximum(totalSize.width - 1);
		if (colSB.getValue() > scrollWidth)
			colSB.setValue(scrollWidth);
		colSB.setVisibleAmount(visibleSize.width);
		colSB.setValueIsAdjusting(false);

		rowSB.setValueIsAdjusting(true);
		rowSB.setMinimum(0);
		rowSB.setMaximum(totalSize.height - 1);
		if (rowSB.getValue() > scrollHeight)
			rowSB.setValue(scrollHeight);
		rowSB.setVisibleAmount(visibleSize.height);
		rowSB.setValueIsAdjusting(false);
	}

	private void updateViewPorts() {
		Dimension totalSize = bodyPanel.getPreferredSize();
		Rectangle visibleSize = bodyPanel.getBounds();

		int colValue = colSB.getValue();
		int rowValue = rowSB.getValue();

		colVP.setViewPosition(new Point(colValue, 0));
		rowVP.setViewPosition(new Point(0, rowValue));
		bodyVP.setViewPosition(new Point(colValue, rowValue));
	}

	public JViewport getBodyViewPort() {
		return bodyVP;
	}

	public JViewport getColumnViewPort() {
		return colVP;
	}

	public JViewport getRowViewPort() {
		return rowVP;
	}

	public HeatmapBodyPanel getBodyPanel() {
		return bodyPanel;
	}
	
	public HeatmapHeaderPanel getColumnPanel() {
		return columnHeaderPanel;
	}

	public HeatmapHeaderPanel getRowPanel() {
		return rowHeaderPanel;
	}

	public HeatmapPosition getScrollPosition() {
		Point pos = new Point(
				colSB.getValue(),
				rowSB.getValue());

		return bodyPanel.getDrawer().getPosition(pos);
	}

	public Point getScrollValue() {
		return new Point(
				colSB.getValue(),
				rowSB.getValue());
	}

	public void setScrollColumnPosition(int index) {
		Point pos = bodyPanel.getDrawer().getPoint(new HeatmapPosition(0, index));

		colSB.setValue(pos.x);
	}

	public void setScrollColumnValue(int value) {
		colSB.setValue(value);
	}

	public void setScrollRowPosition(int index) {
		Point pos = bodyPanel.getDrawer().getPoint(new HeatmapPosition(index, 0));

		rowSB.setValue(pos.y);
	}

	public void setScrollRowValue(int value) {
		rowSB.setValue(value);
	}

	private void heatmapChanged() {
		PropertyChangeListener listener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(Heatmap.PROPERTY_CHANGED)) {
					heatmapPropertyChanged();
				} else {
					repaint();
				}
			}
		};

		heatmap.addPropertyChangeListener(listener);
		heatmap.getMatrixView().addPropertyChangeListener(listener);
	}

	private void heatmapPropertyChanged() {
		Dimension ps = getPreferredSize();
		if (ps.width != heatmap.getCellWidth()
				|| ps.height != heatmap.getCellHeight()) {

			updateScrolls();;

			repaint();
			revalidate();
		}
		else
			repaint();
	}

    /*@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension sz = getSize();
		Rectangle r = new Rectangle(new Point(0, 0), sz);
		g.setColor(Color.BLACK);
		g.fillRect(r.x, r.y, r.width, r.height);
	}*/


}
