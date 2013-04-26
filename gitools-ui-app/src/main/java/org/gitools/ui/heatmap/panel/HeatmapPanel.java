/*
 * #%L
 * gitools-ui-app
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.ui.heatmap.panel;

import org.gitools.core.heatmap.Heatmap;
import org.gitools.core.heatmap.HeatmapDimension;
import org.gitools.core.heatmap.HeatmapLayer;
import org.gitools.core.heatmap.drawer.HeatmapPosition;
import org.gitools.core.matrix.model.IMatrixView;
import org.gitools.core.matrix.model.IMatrixViewDimension;
import org.gitools.core.utils.EventUtils;
import org.gitools.ui.heatmap.editor.HeatmapPopupmenus;
import org.gitools.ui.platform.actions.ActionSetUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class HeatmapPanel extends JPanel implements PropertyChangeListener {

    // Bean
    private Heatmap heatmap;

    // Components
    private HeatmapBodyPanel bodyPanel;
    private HeatmapHeaderPanel columnHeaderPanel;
    private HeatmapHeaderPanel rowHeaderPanel;
    private HeatmapHeaderIntersectionPanel headerIntersectPanel;

    private HeatmapPanelInputProcessor inputProcessor;

    private JPopupMenu popupMenuRows;
    private JPopupMenu popupMenuColumns;

    private JViewport bodyVP;
    private JViewport colVP;
    private JViewport rowVP;
    private JViewport intersectVP;

    private JScrollBar colSB;
    private JScrollBar rowSB;

    public HeatmapPanel(Heatmap heatmap) {
        this.heatmap = heatmap;

        createComponents();

        // Listen
        heatmap.addPropertyChangeListener(this);
        heatmap.getColumns().addPropertyChangeListener(this);
        heatmap.getRows().addPropertyChangeListener(this);
        heatmap.getLayers().addPropertyChangeListener(this);
        heatmap.getLayers().getTopLayer().addPropertyChangeListener(this);

        setFocusable(true);
        setBackground(Color.WHITE);
    }

    public Heatmap getHeatmap() {
        return heatmap;
    }

    private void createComponents() {
        bodyPanel = new HeatmapBodyPanel(heatmap);
        columnHeaderPanel = new HeatmapHeaderPanel(heatmap, heatmap.getColumns());
        rowHeaderPanel = new HeatmapHeaderPanel(heatmap, heatmap.getRows());
        headerIntersectPanel = new HeatmapHeaderIntersectionPanel(heatmap, columnHeaderPanel.getHeaderDrawer(), rowHeaderPanel.getHeaderDrawer());

        bodyVP = new JViewport();
        bodyVP.setView(bodyPanel);

        colVP = new JViewport();
        colVP.setView(columnHeaderPanel);


        rowVP = new JViewport();
        rowVP.setView(rowHeaderPanel);

        intersectVP = new JViewport();
        intersectVP.setView(headerIntersectPanel);

        inputProcessor = new HeatmapPanelInputProcessor(this);

        colSB = new JScrollBar(JScrollBar.HORIZONTAL);
        colSB.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                updateViewPorts();
            }
        });
        rowSB = new JScrollBar(JScrollBar.VERTICAL);
        rowSB.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                updateViewPorts();
            }
        });

        setLayout(new HeatmapLayout());
        add(colVP);
        add(rowVP);
        add(bodyVP);
        add(colSB);
        add(rowSB);
        add(intersectVP);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateScrolls();
            }
        });

        //new HeatmapKeyboardController(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                heatmap.getRows().setSelectionLead(-1);
                heatmap.getColumns().setSelectionLead(-1);
                heatmap.getColumns().clearSelection();
                heatmap.getRows().clearSelection();
            }
        });

        popupMenuRows = ActionSetUtils.createPopupMenu(HeatmapPopupmenus.ROWS);
        popupMenuColumns = ActionSetUtils.createPopupMenu(HeatmapPopupmenus.COLUMNS);
    }

    private void updateScrolls() {
        Dimension totalSize = bodyVP.getViewSize();
        Dimension visibleSize = bodyVP.getSize();

        int scrollWidth = totalSize.width - visibleSize.width;
        int scrollHeight = totalSize.height - visibleSize.height;

        IMatrixView mv = heatmap;
        int row = mv.getRows().getSelectionLead();
        int col = mv.getColumns().getSelectionLead();

        Point leadPoint = bodyPanel.getDrawer().getPoint(new HeatmapPosition(row, col));

        HeatmapDimension rowDim = heatmap.getRows();
        HeatmapDimension colDim = heatmap.getColumns();

        int leadPointXEnd = leadPoint.x + heatmap.getColumns().getCellSize() + colDim.getGridSize();
        int leadPointYEnd = leadPoint.y + heatmap.getRows().getCellSize() + rowDim.getGridSize();

        colSB.setValueIsAdjusting(true);
        colSB.setMinimum(0);
        colSB.setMaximum(totalSize.width - 1);

        if (colSB.getValue() > scrollWidth) {
            colSB.setValue(scrollWidth);
        }

        if (leadPoint.x < colSB.getValue()) {
            colSB.setValue(leadPoint.x);
        } else if (leadPointXEnd > colSB.getValue() + visibleSize.width) {
            colSB.setValue(leadPointXEnd - visibleSize.width);
        }

        colSB.setVisibleAmount(visibleSize.width);
        colSB.setValueIsAdjusting(false);

        rowSB.setValueIsAdjusting(true);
        rowSB.setMinimum(0);
        rowSB.setMaximum(totalSize.height - 1);

        if (rowSB.getValue() > scrollHeight) {
            rowSB.setValue(scrollHeight);
        }

        if (leadPoint.y < rowSB.getValue()) {
            rowSB.setValue(leadPoint.y);
        } else if (leadPointYEnd > rowSB.getValue() + visibleSize.height) {
            rowSB.setValue(leadPointYEnd - visibleSize.height);
        }

        rowSB.setVisibleAmount(visibleSize.height);
        rowSB.setValueIsAdjusting(false);
    }

    private long lastUpdateViewPorts = -1;

    private void updateViewPorts() {
        // Maximum of 20 updates per second
        if (lastUpdateViewPorts != -1) {
            long interval = System.currentTimeMillis() - lastUpdateViewPorts;

            if (interval < 50) {
                return;
            }
        }

        lastUpdateViewPorts = System.currentTimeMillis();

        Dimension totalSize = bodyVP.getViewSize();
        Dimension visibleSize = bodyVP.getSize();

        int colValue = colSB.getValue();
        if (colValue + visibleSize.width > totalSize.width) {
            colValue = totalSize.width - visibleSize.width;
        }

        int rowValue = rowSB.getValue();
        if (rowValue + visibleSize.height > totalSize.height) {
            rowValue = totalSize.height - visibleSize.height;
        }

        colVP.setViewPosition(new Point(colValue, 0));
        rowVP.setViewPosition(new Point(0, rowValue));
        bodyVP.setViewPosition(new Point(colValue, rowValue));
        intersectVP.setViewPosition(new Point(0, 0));

        lastUpdateViewPorts = System.currentTimeMillis();
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
        Point pos = new Point(colSB.getValue(), rowSB.getValue());

        return bodyPanel.getDrawer().getPosition(pos);
    }

    @NotNull
    public Point getScrollValue() {
        return new Point(colSB.getValue(), rowSB.getValue());
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

    @Override
    protected void paintComponent(@NotNull Graphics g) {
        super.paintComponent(g);
        Dimension sz = getSize();
        Rectangle r = new Rectangle(new Point(0, 0), sz);
        g.setColor(Color.WHITE);
        g.fillRect(r.x, r.y, r.width, r.height);
    }

    public void addHeatmapMouseListener(HeatmapMouseListener listener) {
        inputProcessor.addHeatmapMouseListener(listener);
    }

    public void mouseReleased(@NotNull MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            showPopup(e);
        }
    }

    private void showPopup(@NotNull MouseEvent e) {
        if (e.getComponent() == this.rowVP) {
            popupMenuRows.show(e.getComponent(), e.getX(), e.getY());
        }

        if (e.getComponent() == this.colVP) {
            popupMenuColumns.show(e.getComponent(), e.getX(), e.getY());
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        boolean updateAll =
                EventUtils.isAny(evt, HeatmapDimension.class,
                        HeatmapDimension.PROPERTY_CELL_SIZE,
                        HeatmapDimension.PROPERTY_VISIBLE,
                        HeatmapDimension.PROPERTY_SELECTION_LEAD,
                        HeatmapDimension.PROPERTY_GRID_SIZE,
                        HeatmapDimension.PROPERTY_GRID_COLOR
                ) ||
                        EventUtils.isAny(evt, HeatmapLayer.class,
                                HeatmapLayer.PROPERTY_DECORATOR
                        );

        if (updateAll) {
            bodyPanel.updateSize();
            rowHeaderPanel.updateSize();
            columnHeaderPanel.updateSize();
            headerIntersectPanel.updateSize();
            updateScrolls();
            revalidate();
            repaint();
        }
    }


}
