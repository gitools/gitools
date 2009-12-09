package org.gitools.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;

public class HeatmapBodyDrawer extends AbstractDrawer {
	
	private ElementDecorator decorator;

	public HeatmapBodyDrawer(MatrixFigure heatmap) {
		super(heatmap);
	}

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
		int rowStart = clip.y / cellHeight;
		int rowEnd = (clip.y + clip.height + cellHeight - 1) / cellHeight;
		rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();

		//TODO take into account extBorderSize
		int colStart = clip.x / cellWidth;
		int colEnd = (clip.x + clip.width + cellWidth - 1) / cellWidth;
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
				if (row == leadRow && col == leadColumn)
					color = color.darker();
				g.setColor(color);
				g.fillRect(x, y, cellWidth - gridSize, cellHeight - gridSize);
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
		int extBorder = 2 * 1 - 1;
		return new Dimension(
				cellWidth * columnCount + extBorder,
				cellHeight * rowCount + extBorder);
	}
	
	public int getRowFromY(int y) {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int cellHeight = heatmap.getCellHeight() + gridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int extBorder = 2 * 1 - 1;
		int row = y > 0 && y < cellHeight * rowCount + extBorder ? (y - 1) / cellHeight : -1;
		return row;
	}
	
	public int getColumnFromX(int x) {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int cellWidth = heatmap.getCellWidth() + gridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int extBorder = 2 * 1 - 1;
		int column = x > 0 && x < cellWidth * columnCount + extBorder ? (x - 1) / cellWidth : -1;
		return column;
	}

	public Point getCoordinates(Point p) {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int cellHeight = heatmap.getCellHeight() + gridSize;
		int rowCount = heatmap.getMatrixView().getRowCount();
		int cellWidth = heatmap.getCellWidth() + gridSize;
		int columnCount = heatmap.getMatrixView().getColumnCount();
		int extBorder = /*2 * 1 - 1*/ 0;
		int row = p.y > 0 && p.y < cellHeight * rowCount + extBorder ? (p.y - 1) / cellHeight : -1;
		int column = p.x > 0 && p.x < cellWidth * columnCount + extBorder ? (p.x - 1) / cellWidth : -1;
		return new Point(row, column);
	}

	public ElementDecorator getDecorator() {
		return decorator;
	}
	
	public void setDecorator(ElementDecorator decorator) {
		this.decorator = decorator;
	}
}
