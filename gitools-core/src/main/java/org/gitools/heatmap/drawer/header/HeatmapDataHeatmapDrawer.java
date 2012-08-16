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

package org.gitools.heatmap.drawer.header;

import edu.upf.bg.color.utils.ColorUtils;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.drawer.AbstractHeatmapDrawer;
import org.gitools.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.HeatmapPosition;
import org.gitools.heatmap.header.HeatmapDataHeatmapHeader;
import org.gitools.matrix.model.IMatrixView;
import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;

import java.awt.*;

public class HeatmapDataHeatmapDrawer extends AbstractHeatmapHeaderDrawer<HeatmapDataHeatmapHeader> {

	public HeatmapDataHeatmapDrawer(Heatmap heatmap, HeatmapDataHeatmapHeader header, boolean horizontal) {
		super(heatmap,header,horizontal);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

        //TODO: adapt

        Heatmap headerHeatmap = header.getHeaderHeatmap();
		IMatrixView data = header.getHeaderHeatmap().getMatrixView();


        int borderSize = getBorderSize();
        int rowsGridSize;
        int columnsGridSize;
        int cellWidth;
        int cellHeight;
        int rowStart;
        int rowEnd;
        int colStart;
        int colEnd;

        // Clear background
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        // Draw borders and grid background
        if (heatmap.isShowBorders()) {
            g.setColor(Color.BLACK);
            g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
            box.grow(-borderSize, -borderSize);
        }


        cellWidth = header.getSize();
        columnsGridSize = header.getMargin();

        if (horizontal)  {

            rowsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;

            cellHeight = heatmap.getCellWidth() + rowsGridSize;

            //TODO take into account extBorderSize
            colStart = (clip.y - box.y) / cellHeight;
            colEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            colEnd = colEnd < data.getColumnCount() ? colEnd : data.getColumnCount();

            //TODO take into account extBorderSize
            rowStart = (clip.x - box.x) / cellHeight;
            rowEnd = (clip.x - box.x + clip.width + cellHeight - 1) / cellHeight;
            rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();

        } else {

            rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;

            cellHeight = heatmap.getCellHeight() + rowsGridSize;

            //TODO take into account extBorderSize
            rowStart = (clip.y - box.y) / cellHeight;
            rowEnd = (clip.y - box.y + clip.height + cellHeight - 1) / cellHeight;
            rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();

            //TODO take into account extBorderSize
            colStart = (clip.x - box.x) / cellWidth;
            colEnd = (clip.x - box.x + clip.width + cellWidth - 1) / cellWidth;
            colEnd = colEnd < data.getColumnCount() ? colEnd : data.getColumnCount();
        }


		ElementDecorator deco = headerHeatmap.getActiveCellDecorator();
		ElementDecoration decoration = new ElementDecoration();

		int leadRow = headerHeatmap.getMatrixView().getLeadSelectionRow();
		int leadColumn = headerHeatmap.getMatrixView().getLeadSelectionColumn();

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
						/*g.drawLine(x, y, x2, y);
						g.drawLine(x, y2, x2, y2);  */
					}
					else if (row != leadRow && col == leadColumn) {
						g.setColor(ColorUtils.invert(color));
						int x2 = x + cellWidth - columnsGridSize - 1;
						int y2 = y + cellHeight - rowsGridSize - 1;
						/*g.drawLine(x, y, x, y2);
						g.drawLine(x2, y, x2, y2);*/
					}
				}


                if (horizontal)
				    y += cellHeight;
                else
                    x += cellWidth;
			}
            if (horizontal)
			    x += cellWidth;
            else
                y += cellHeight;
		}
	}

	@Override
	public Dimension getSize() {

        Heatmap headerHeatmap = header.getHeaderHeatmap();

		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
        int rowCount = headerHeatmap.getMatrixView().getRowCount();
        int columnCount = headerHeatmap.getMatrixView().getColumnCount();
        int extBorder = /*2 * 1 - 1*/ 0;

        
        int cellWidth;
        int cellHeight;
        if (horizontal)  {
            cellWidth = heatmap.getCellWidth() + columnsGridSize;
            cellHeight = header.getSize();

        } else {
            cellWidth = header.getSize();
            cellHeight = heatmap.getCellHeight() + rowsGridSize;
        }
        return new Dimension(
                cellWidth * columnCount + extBorder,
                cellHeight * rowCount + extBorder);
	}

	@Override
	public HeatmapPosition getPosition(Point p) {

        Heatmap headerHeatmap = header.getHeaderHeatmap();

		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

        int cellWidth;
        int cellHeight;
        if (horizontal)  {
            cellWidth = heatmap.getCellWidth() + columnsGridSize;
            cellHeight = header.getSize();
        } else {
            cellWidth = header.getSize();
            cellHeight = heatmap.getCellHeight() + rowsGridSize;
        }


		int rowCount = headerHeatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int columnCount = headerHeatmap.getMatrixView().getColumnCount();
		int totalWidth = cellWidth * columnCount + extBorder;
		
		int row = p.y >= 0 && p.y < totalHeight ? (p.y - extBorder) / cellHeight : -1;
		int column = p.x >= 0 && p.x < totalWidth ? (p.x - extBorder) / cellWidth : -1;

		return new HeatmapPosition(row, column);
	}

	@Override
	public Point getPoint(HeatmapPosition p) {

        Heatmap headerHeatmap = header.getHeaderHeatmap();

		int rowsGridSize = heatmap.getRowDim().isGridEnabled() ? heatmap.getRowDim().getGridSize() : 0;
		int columnsGridSize = heatmap.getColumnDim().isGridEnabled() ? heatmap.getColumnDim().getGridSize() : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

        int cellWidth;
        int cellHeight;
        if (horizontal)  {
            cellWidth = heatmap.getCellWidth() + columnsGridSize;
            cellHeight = header.getSize();
        } else {
            cellWidth = header.getSize();
            cellHeight = heatmap.getCellHeight() + rowsGridSize;
        }

		int rowCount = headerHeatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int columnCount = headerHeatmap.getMatrixView().getColumnCount();
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
