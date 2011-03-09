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

package org.gitools.heatmap.drawer;

import edu.upf.bg.color.utils.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapBodyDrawer extends AbstractHeatmapDrawer {

	public HeatmapBodyDrawer(Heatmap heatmap) {
		super(heatmap);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

		int borderSize = getBorderSize();
		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		
		// Clear background
		g.setColor(Color.WHITE);
		g.fillRect(clip.x, clip.y, clip.width, clip.height);

		// Draw borders and grid background
		if (heatmap.isShowBorders()) {
			g.setColor(Color.BLACK);
			g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
			box.grow(-borderSize, -borderSize);
		}
		
		IMatrixView data = heatmap.getMatrixView();
		
		int cellWidth = heatmap.getCellWidth() + columnsGridSize;
		int cellHeight = heatmap.getCellHeight() + rowsGridSize;

		//TODO take into account extBorderSize
		int rowStart = (clip.y - box.y) / cellHeight;
		int rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
		rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();

		//TODO take into account extBorderSize
		int colStart = (clip.x - box.x) / cellWidth;
		int colEnd = (clip.x - box.x + clip.width + cellWidth - 1) / cellWidth;
		colEnd = colEnd < data.getColumnCount() ? colEnd : data.getColumnCount();
		
		ElementDecorator deco = heatmap.getCellDecorator();
		ElementDecoration decoration = new ElementDecoration();

		int leadRow = heatmap.getMatrixView().getLeadSelectionRow();
		int leadColumn = heatmap.getMatrixView().getLeadSelectionColumn();

		int y = box.y + rowStart * cellHeight;
		for (int row = rowStart; row < rowEnd; row++) {
			int x = box.x + colStart * cellWidth;
			boolean rowSelected = data.isRowSelected(row);
			for (int col = colStart; col < colEnd; col++) {
				Object element = data.getCell(row, col);
				deco.decorate(decoration, element);
				
				Color color = decoration.getBgColor();
				Color rowsGridColor = heatmap.getRowDim().getGridColor();
				Color columnsGridColor = heatmap.getColumnDim().getGridColor();

				boolean selected = !pictureMode
						&& (rowSelected || data.isColumnSelected(col));

				if (selected) {
					color = color.darker();
					rowsGridColor = rowsGridColor.darker();
					columnsGridColor = columnsGridColor.darker();
				}

				g.setColor(color);

				g.fillRect(x, y, cellWidth - columnsGridSize, cellHeight - rowsGridSize);

				g.setColor(rowsGridColor);

				g.fillRect(x, y + cellHeight - rowsGridSize, cellWidth, rowsGridSize);

				g.setColor(columnsGridColor);

				g.fillRect(x + cellWidth - columnsGridSize, y, columnsGridSize, cellWidth - columnsGridSize);

				if (!pictureMode) {
					if (row == leadRow && col == leadColumn) {
						g.setColor(ColorUtils.invert(color));
						g.drawRect(x, y, cellWidth - columnsGridSize - 1, cellHeight - rowsGridSize - 1);
					}
					else if (row == leadRow && col != leadColumn) {
						g.setColor(ColorUtils.invert(color));
						int x2 = x + cellWidth - columnsGridSize - 1;
						int y2 = y + cellHeight - rowsGridSize - 1;
						g.drawLine(x, y, x2, y);
						g.drawLine(x, y2, x2, y2);
					}
					else if (row != leadRow && col == leadColumn) {
						g.setColor(ColorUtils.invert(color));
						int x2 = x + cellWidth - columnsGridSize - 1;
						int y2 = y + cellHeight - rowsGridSize - 1;
						g.drawLine(x, y, x, y2);
						g.drawLine(x2, y, x2, y2);
					}
				}

				x += cellWidth;
			}
			y += cellHeight;
		}
	}

	@Override
	public Dimension getSize() {
		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		int cellWidth = heatmap.getCellWidth() + columnsGridSize;
		int cellHeight = heatmap.getCellHeight() + rowsGridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int extBorder = /*2 * 1 - 1*/ 0;
		return new Dimension(
				cellWidth * columnCount + extBorder,
				cellHeight * rowCount + extBorder);
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

		int cellHeight = heatmap.getCellHeight() + rowsGridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int cellWidth = heatmap.getCellWidth() + columnsGridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int totalWidth = cellWidth * columnCount + extBorder;
		
		int row = p.y >= 0 && p.y < totalHeight ? (p.y - extBorder) / cellHeight : -1;
		int column = p.x >= 0 && p.x < totalWidth ? (p.x - extBorder) / cellWidth : -1;

		return new HeatmapPosition(row, column);
	}

	@Override
	public Point getPoint(HeatmapPosition p) {
		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

		int cellHeight = heatmap.getCellHeight() + rowsGridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int cellWidth = heatmap.getCellWidth() + columnsGridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int totalWidth = cellWidth * columnCount + extBorder;

		int x = p.column >= 0 ? p.column * cellWidth : 0;
		if (x > totalWidth)
			x = totalWidth;

		int y = p.row >= 0 ? p.row * cellHeight : 0;
		if (y > totalHeight)
			y = totalHeight;

		return new Point(x, y);
	}
}
