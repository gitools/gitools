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

package org.gitools.ui.heatmap.panel;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import javax.swing.JViewport;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.matrix.model.MatrixView;

public class HeatmapPanel extends JPanel {

	private static final long serialVersionUID = 5817479437770943868L;

	private Heatmap heatmap;
	private final PropertyChangeListener heatmapListener;

	private HeatmapBodyPanel bodyPanel;
	private HeatmapHeaderPanel columnHeaderPanel;
	private HeatmapHeaderPanel rowHeaderPanel;

	private JViewport bodyVP;
	private JViewport colVP;
	private JViewport rowVP;

	private JScrollBar colSB;
	private JScrollBar rowSB;

	private List<HeatmapMouseListener> mouseListeners = new ArrayList<HeatmapMouseListener>();

	public HeatmapPanel(Heatmap heatmap) {
		this.heatmap = heatmap;

		heatmapListener = new PropertyChangeListener() {
			@Override public void propertyChange(PropertyChangeEvent evt) {
				heatmapPropertyChanged(evt);
			}
		};

		createComponents();

		heatmapChanged(null);

		setFocusable(true);
	}

	public Heatmap getHeatmap() {
		return heatmap;
	}

	public void setHeatmap(Heatmap heatmap) {
		Heatmap old = this.heatmap;
		this.heatmap = heatmap;
		bodyPanel.setHeatmap(heatmap);
		columnHeaderPanel.setHeatmap(heatmap);
		rowHeaderPanel.setHeatmap(heatmap);
		heatmapChanged(old);
	}

	private void createComponents() {
		bodyPanel = new HeatmapBodyPanel(heatmap);
		columnHeaderPanel = new HeatmapHeaderPanel(heatmap, true);
		rowHeaderPanel = new HeatmapHeaderPanel(heatmap, false);
	
		bodyVP = new JViewport();
		bodyVP.setView(bodyPanel);

		HeatmapMouseListener mouseListenerProxy = new HeatmapMouseListener() {
			@Override public void mouseMoved(int row, int col, MouseEvent e) {
				for (HeatmapMouseListener l : mouseListeners)
					l.mouseMoved(row, col, e); }

			@Override public void mouseClicked(int row, int col, MouseEvent e) {
				for (HeatmapMouseListener l : mouseListeners)
					l.mouseClicked(row, col, e); }
		};

		HeatmapBodyMouseController bodyController =
				new HeatmapBodyMouseController(this);
		bodyController.addHeatmapMouseListener(mouseListenerProxy);
	
		colVP = new JViewport();
		colVP.setView(columnHeaderPanel);

		HeatmapHeaderMouseController colMouseCtrl =
				new HeatmapHeaderMouseController(this, true);
		colMouseCtrl.addHeatmapMouseListener(mouseListenerProxy);

		rowVP = new JViewport();
		rowVP.setView(rowHeaderPanel);

		HeatmapHeaderMouseController rowMouseCtrl =
				new HeatmapHeaderMouseController(this, false);
		rowMouseCtrl.addHeatmapMouseListener(mouseListenerProxy);

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

		new HeatmapKeyboardController(this);

		addMouseListener(new MouseAdapter() {
			@Override public void mousePressed(MouseEvent e) {
				heatmap.getMatrixView().setLeadSelection(-1, -1);
				heatmap.getMatrixView().clearSelection();
			}
		});
	}

	private void updateScrolls() {
		Dimension totalSize = bodyVP.getViewSize();
		Dimension visibleSize = bodyVP.getSize();

		int scrollWidth = totalSize.width - visibleSize.width;
		int scrollHeight = totalSize.height - visibleSize.height;

		IMatrixView mv = heatmap.getMatrixView();
		int row = mv.getLeadSelectionRow();
		int col = mv.getLeadSelectionColumn();

		Point leadPoint = bodyPanel.getDrawer()
				.getPoint(new HeatmapPosition(row, col));

		HeatmapDim rowDim = heatmap.getRowDim();
		HeatmapDim colDim = heatmap.getColumnDim();

		int leadPointXEnd = leadPoint.x + heatmap.getCellWidth()
				+ (colDim.isGridEnabled() ? colDim.getGridSize() : 0);
		int leadPointYEnd = leadPoint.y + heatmap.getCellHeight()
				+ (rowDim.isGridEnabled() ? rowDim.getGridSize() : 0);

		colSB.setValueIsAdjusting(true);
		colSB.setMinimum(0);
		colSB.setMaximum(totalSize.width - 1);

		if (colSB.getValue() > scrollWidth)
			colSB.setValue(scrollWidth);

		if (leadPoint.x < colSB.getValue())
			colSB.setValue(leadPoint.x);
		else if (leadPointXEnd > colSB.getValue() + visibleSize.width)
			colSB.setValue(leadPointXEnd - visibleSize.width);

		colSB.setVisibleAmount(visibleSize.width);
		colSB.setValueIsAdjusting(false);

		rowSB.setValueIsAdjusting(true);
		rowSB.setMinimum(0);
		rowSB.setMaximum(totalSize.height - 1);

		if (rowSB.getValue() > scrollHeight)
			rowSB.setValue(scrollHeight);

		if (leadPoint.y < rowSB.getValue())
			rowSB.setValue(leadPoint.y);
		else if (leadPointYEnd > rowSB.getValue() + visibleSize.height)
			rowSB.setValue(leadPointYEnd - visibleSize.height);

		rowSB.setVisibleAmount(visibleSize.height);
		rowSB.setValueIsAdjusting(false);
	}

	private void updateViewPorts() {
		Dimension totalSize = bodyVP.getViewSize();
		Dimension visibleSize = bodyVP.getSize();

		int colValue = colSB.getValue();
		if (colValue + visibleSize.width > totalSize.width)
			colValue = totalSize.width - visibleSize.width;
		
		int rowValue = rowSB.getValue();
		if (rowValue + visibleSize.height > totalSize.height)
			rowValue = totalSize.height -visibleSize.height;

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

	private void heatmapChanged(Heatmap old) {

		if (old != null)
			old.removePropertyChangeListener(heatmapListener);

		heatmap.addPropertyChangeListener(heatmapListener);
	}

	private void heatmapPropertyChanged(PropertyChangeEvent evt) {
		String pname = evt.getPropertyName();
		Object src = evt.getSource();

		boolean updateAll =
				(src.equals(heatmap)
					&& Heatmap.CELL_SIZE_CHANGED.equals(pname))
				|| ((src instanceof HeatmapDim)
					&& (HeatmapDim.GRID_PROPERTY_CHANGED.equals(pname)
						|| HeatmapDim.HEADERS_CHANGED.equals(pname)
						|| HeatmapDim.HEADER_SIZE_CHANGED.equals(pname)))
				|| (src instanceof HeatmapHeader
					&& (HeatmapHeader.SIZE_CHANGED.equals(pname)
						|| HeatmapHeader.VISIBLE_CHANGED.equals(pname)))
				|| (src.equals(heatmap.getMatrixView())
					&& (MatrixView.VISIBLE_COLUMNS_CHANGED.equals(pname)
						|| MatrixView.VISIBLE_ROWS_CHANGED.equals(pname)
						|| MatrixView.SELECTED_LEAD_CHANGED.equals(pname)));
		
		if (updateAll) {
			bodyPanel.updateSize();
			rowHeaderPanel.updateSize();
			columnHeaderPanel.updateSize();
			updateScrolls();
			revalidate();
			repaint();
		}

		//System.out.println(getClass().getSimpleName() + " " + src + " " + pname + " update: " + updateAll);
	}

    @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension sz = getSize();
		Rectangle r = new Rectangle(new Point(0, 0), sz);
		g.setColor(Color.WHITE);
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	public void addHeatmapMouseListener(HeatmapMouseListener listener) {
		mouseListeners.add(listener);
	}

	public void removeHeatmapMouseListener(HeatmapMouseListener listener) {
		mouseListeners.remove(listener);
	}
}
