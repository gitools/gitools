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
import org.gitools.core.heatmap.header.HeatmapHeader;
import org.gitools.ui.platform.idea.JBScrollBar;
import org.gitools.ui.heatmap.drawer.HeatmapPosition;
import org.gitools.ui.heatmap.popupmenus.PopupMenuActions;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.DynamicActionsManager;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapDimensionAction;
import org.gitools.ui.heatmap.popupmenus.dynamicactions.IHeatmapHeaderAction;
import org.gitools.ui.platform.actions.ActionSetUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static org.gitools.utils.events.EventUtils.isAny;

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

        colSB = new JBScrollBar(JScrollBar.HORIZONTAL);
        colSB.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                updateViewPorts();
            }
        });
        rowSB = new JBScrollBar(JScrollBar.VERTICAL);
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                heatmap.getRows().setFocus(null);
                heatmap.getColumns().setFocus(null);
                heatmap.getColumns().getSelected().clear();
                heatmap.getRows().getSelected().clear();
            }
        });

        popupMenuRows = ActionSetUtils.createPopupMenu(PopupMenuActions.ROWS);
        popupMenuColumns = ActionSetUtils.createPopupMenu(PopupMenuActions.COLUMNS);
    }

    private void updateScrolls() {
        Dimension totalSize = bodyPanel.getDrawer().getSize();
        Dimension visibleSize = bodyVP.getSize();

        int scrollWidth = totalSize.width - visibleSize.width;
        int scrollHeight = totalSize.height - visibleSize.height;

        HeatmapPosition wheelPosition = HeatmapBodyMouseController.wheelPosition;
        Point wheelPoint = HeatmapBodyMouseController.wheelPoint;

        int colValue = 0, rowValue = 0;
        int leadPointXEnd = 0, leadPointYEnd = 0;
        Point leadPoint = null;
        if (wheelPoint != null) {
            Point targetPoint = bodyPanel.getDrawer().getPoint(wheelPosition);
            colValue = (targetPoint.x - wheelPoint.x);
            rowValue = (targetPoint.y - wheelPoint.y);
        } else {
            String row = heatmap.getRows().getFocus();
            String col = heatmap.getColumns().getFocus();

            leadPoint = bodyPanel.getDrawer().getPoint(new HeatmapPosition(heatmap, heatmap.getRows().indexOf(row), heatmap.getColumns().indexOf(col)));
            leadPointXEnd = leadPoint.x + heatmap.getColumns().getFullSize();
            leadPointYEnd = leadPoint.y + heatmap.getRows().getFullSize();
        }

        colSB.setValueIsAdjusting(true);
        colSB.setMinimum(0);
        colSB.setMaximum(totalSize.width - 1);

        if (colSB.getValue() > scrollWidth) {
            colSB.setValue(scrollWidth);
        }

        if (wheelPoint != null) {
            colSB.setValue(colValue);
        } else {
            if (leadPoint.x < colSB.getValue())
                colSB.setValue(leadPoint.x);
            else if (leadPointXEnd > colSB.getValue() + visibleSize.width)
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

        if (wheelPoint != null) {
            rowSB.setValue(rowValue);
        } else {
            if (leadPoint.y < rowSB.getValue())
                rowSB.setValue(leadPoint.y);
            else if (leadPointYEnd > rowSB.getValue() + visibleSize.height)
                rowSB.setValue(leadPointYEnd - visibleSize.height);
        }

        rowSB.setVisibleAmount(visibleSize.height);
        rowSB.setValueIsAdjusting(false);
    }

    private void updateViewPorts() {

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


    public Point getScrollValue() {
        return new Point(colSB.getValue(), rowSB.getValue());
    }

    public void setScrollColumnPosition(int index) {
        Point pos = bodyPanel.getDrawer().getPoint(new HeatmapPosition(heatmap, 0, index));

        colSB.setValue(pos.x);
    }

    public void setScrollColumnValue(int value) {
        colSB.setValue(value);
    }

    public void setScrollRowPosition(int index) {
        Point pos = bodyPanel.getDrawer().getPoint(new HeatmapPosition(heatmap, index, 0));

        rowSB.setValue(pos.y);
    }

    public void setScrollRowValue(int value) {
        rowSB.setValue(value);
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
        inputProcessor.addHeatmapMouseListener(listener);
    }

    public void mouseReleased(MouseEvent e) {
        if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) != 0) {
            showPopup(e);
        }
    }

    private void showPopup(MouseEvent e) {

        if (e.getComponent() == this.rowVP) {

            Point point = e.getPoint();
            HeatmapHeader header = rowHeaderPanel.getHeaderDrawer().getHeader(point);
            Point viewPosition = rowVP.getViewPosition();
            point.translate(viewPosition.x, viewPosition.y);
            HeatmapPosition headerPostion = rowHeaderPanel.getHeaderDrawer().getPosition(point);

            DynamicActionsManager.updatePopupMenu(popupMenuRows, IHeatmapDimensionAction.class, heatmap.getRows(), headerPostion);
            DynamicActionsManager.updatePopupMenu(popupMenuRows, IHeatmapHeaderAction.class, header, headerPostion);

            popupMenuRows.show(e.getComponent(), e.getX(), e.getY());
        }

        if (e.getComponent() == this.colVP) {

            Point point = e.getPoint();
            HeatmapHeader header = columnHeaderPanel.getHeaderDrawer().getHeader(point);
            Point viewPosition = rowVP.getViewPosition();
            point.translate(viewPosition.x, viewPosition.y);
            HeatmapPosition headerPostion = columnHeaderPanel.getHeaderDrawer().getPosition(point);

            DynamicActionsManager.updatePopupMenu(popupMenuColumns, IHeatmapDimensionAction.class, heatmap.getColumns(), headerPostion);
            DynamicActionsManager.updatePopupMenu(popupMenuColumns, IHeatmapHeaderAction.class, header, headerPostion);

            popupMenuColumns.show(e.getComponent(), e.getX(), e.getY());
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        boolean updateAll =
                isAny(evt, HeatmapDimension.class,
                        HeatmapDimension.PROPERTY_CELL_SIZE,
                        HeatmapDimension.PROPERTY_VISIBLE,
                        HeatmapDimension.PROPERTY_FOCUS,
                        HeatmapDimension.PROPERTY_GRID_SIZE,
                        HeatmapDimension.PROPERTY_GRID_COLOR
                ) ||
                        isAny(evt, HeatmapLayer.class,
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
