/*
 * #%L
 * gitools-core
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
package org.gitools.heatmap.drawer;

import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDimension;
import org.gitools.heatmap.header.HeatmapHeader;
import org.gitools.model.decorator.Decoration;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class AbstractHeatmapHeaderDrawer<HT extends HeatmapHeader> extends AbstractHeatmapDrawer {

    protected static final Color highlightingColor = Color.YELLOW;

    private final HT header;
    private HeatmapDimension heatmapDimension;

    protected AbstractHeatmapHeaderDrawer(Heatmap heatmap, HeatmapDimension heatmapDimension, HT header) {
        super(heatmap);

        this.header = header;
        this.heatmapDimension = heatmapDimension;
    }

    protected HT getHeader() {
        return header;
    }

    @Override
    public Point getPoint(HeatmapPosition p) {
        int index = (isHorizontal() ? p.row : p.column);
        int point = getHeaderPoint(index);
        return (isHorizontal() ? new Point(point, 0) : new Point(0, point));
    }

    protected int getHeaderPoint(int index) {

        HeatmapDimension hdim = getHeatmapDimension();
        int cellSize = hdim.getCellSize() + hdim.getGridSize();
        int totalSize = cellSize * hdim.size();

        int point = index >= 0 ? index * cellSize : 0;
        if (point > totalSize) {
            point = totalSize;
        }

        return point;
    }

    @Override
    public HeatmapPosition getPosition(Point p) {
        int point = (isHorizontal() ? p.x : p.y);
        int index = getHeaderPosition(point);
        return (isHorizontal() ? new HeatmapPosition(-1, index) : new HeatmapPosition(index, -1));
    }

    protected int getHeaderPosition(int point) {
        HeatmapDimension hdim = getHeatmapDimension();
        int index = -1;
        int cellSize = hdim.getCellSize() + hdim.getGridSize();
        int totalSize = cellSize * hdim.size();
        if (point >= 0 && point < totalSize) {
            index = point / cellSize;
        }
        return index;
    }

    @NotNull
    @Override
    public Dimension getSize() {
        HeatmapDimension hdim = getHeatmapDimension();
        int gridSize = hdim.getGridSize();
        int total = (hdim.getCellSize() + gridSize) * hdim.size();
        return (isHorizontal()? new Dimension(total, getHeader().getSize()) : new Dimension(getHeader().getSize(), total));
    }

    @Deprecated
    public boolean isHorizontal() {
        return getHeatmap().getColumns() == heatmapDimension;
    }

    public void drawHeaderLegend(Graphics2D g, Rectangle headerIntersection, HeatmapHeader heatmapHeader) {
        return;
    }

    public HeatmapDimension getHeatmapDimension() {
        return heatmapDimension;
    }

    protected int fullCellSize() {
        return heatmapDimension.getCellSize() + heatmapDimension.getGridSize();
    }

    protected boolean isSelected(int index) {
        return !isPictureMode() &&  heatmapDimension.isSelected(index);
    }

    protected void prepareDraw(Graphics2D g, Rectangle box) {
        paintBackground(header.getBackgroundColor(), g, box);
        calculateFontSize(g, header.getHeatmapDimension().getCellSize(), 8);
    }

    protected void paintCell(Decoration decoration, int index, int offset, int width, Graphics2D g, Rectangle box) {

        paintCell(
                decoration,
                isSelected(index),
                heatmapDimension.getGridColor(),
                heatmapDimension.getGridSize(),
                offset, index*(heatmapDimension.getCellSize() + heatmapDimension.getGridSize()),
                width,
                heatmapDimension.getCellSize(),
                g,
                box
        );

    }

    protected int firstVisibleIndex(Rectangle box, Rectangle clip) {
        int size = fullCellSize();
        int clipStart = clip.y - box.y;
        return ((clipStart - size) / size) + 1;
    }

    protected int lastVisibleIndex(Rectangle box, Rectangle clip) {
        int size = fullCellSize();
        int clipStart = clip.y - box.y;
        int clipEnd = clipStart + clip.height;
        return ((clipEnd + size - 1) / size) - 1;
    }

}
