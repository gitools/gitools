package org.gitools.heatmap.drawer;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapBodyDrawer extends AbstractHeatmapDrawer {
	
	//private ElementDecorator decorator;

	public HeatmapBodyDrawer(Heatmap heatmap) {
		super(heatmap);
	}

	/*public ElementDecorator getDecorator() {
		return decorator;
	}

	public void setDecorator(ElementDecorator decorator) {
		this.decorator = decorator;
	}
	*/
	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

		int borderSize = getBorderSize();
		int gridSize = getGridSize();
		
		// Clear background to grid color
		g.setColor(heatmap.getGridColor());
		g.fillRect(clip.x, clip.y, clip.width, clip.height);

		// Draw borders and grid background
		if (heatmap.isShowBorders()) {
			g.setColor(Color.BLACK);
			g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
			box.grow(-borderSize, -borderSize);
		}
		
		IMatrixView data = heatmap.getMatrixView();
		
		int cellWidth = heatmap.getCellWidth() + gridSize;
		int cellHeight = heatmap.getCellHeight() + gridSize;

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

		int leadRow = heatmap.getMatrixView().getSelectionLeadRow();
		int leadColumn = heatmap.getMatrixView().getSelectionLeadColumn();

		int y = box.y + rowStart * cellHeight;
		for (int row = rowStart; row < rowEnd; row++) {
			int x = box.x + colStart * cellWidth;
			for (int col = colStart; col < colEnd; col++) {
				Object element = data.getCell(row, col);
				deco.decorate(decoration, element);
				
				Color color = decoration.getBgColor();
				g.setColor(color);

				g.fillRect(x, y, cellWidth - gridSize, cellHeight - gridSize);

				if (!pictureMode && row == leadRow && col == leadColumn) {
					g.setColor(ColorUtils.invert(color));
					g.drawRect(x, y, cellWidth - gridSize - 1, cellHeight - gridSize - 1);
				}

				x += cellWidth;
			}
			y += cellHeight;
		}
	}

	@Override
	public Dimension getSize() {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int cellWidth = heatmap.getCellWidth() + gridSize;
		int cellHeight = heatmap.getCellHeight() + gridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int extBorder = /*2 * 1 - 1*/ 0;
		return new Dimension(
				cellWidth * columnCount + extBorder,
				cellHeight * rowCount + extBorder);
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

		int cellHeight = heatmap.getCellHeight() + gridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int cellWidth = heatmap.getCellWidth() + gridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int totalWidth = cellWidth * columnCount + extBorder;
		
		int row = p.y >= 0 && p.y < totalHeight ? (p.y - extBorder) / cellHeight : -1;
		int column = p.x >= 0 && p.x < totalWidth ? (p.x - extBorder) / cellWidth : -1;

		return new HeatmapPosition(row, column);
	}

	@Override
	public Point getPoint(HeatmapPosition p) {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

		int cellHeight = heatmap.getCellHeight() + gridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int totalHeight = cellHeight * rowCount + extBorder;

		int cellWidth = heatmap.getCellWidth() + gridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int totalWidth = cellWidth * columnCount + extBorder;

		int x = p.column >= 0 ? p.column * cellWidth : 0;
		if (x > totalWidth)
			x = totalWidth;

		int y = p.row >= 0 ? p.row * cellWidth : 0;
		if (y > totalHeight)
			y = totalHeight;

		return new Point(x, y);
	}
}
