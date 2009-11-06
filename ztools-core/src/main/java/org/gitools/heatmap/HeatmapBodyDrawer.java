package org.gitools.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import org.gitools.model.decorator.ElementDecoration;
import org.gitools.model.decorator.ElementDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;

public class HeatmapBodyDrawer {

	private MatrixFigure heatmap;
	
	private ElementDecorator decorator;
	
	public HeatmapBodyDrawer(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public MatrixFigure getHeatmap() {
		return heatmap;
	}
	
	public void setHeatmap(MatrixFigure heatmap) {
		this.heatmap = heatmap;
	}
	
	public void draw(Graphics2D g) {
		Dimension size = getSize();
		Rectangle box = new Rectangle(0, 0, size.width, size.height);
		Rectangle clip = g.getClipBounds();
		
		int extBorderSize = 1;
		
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		
		// Clear background to grid color
		g.setColor(heatmap.getGridColor());
		g.fillRect(box.x, box.y, box.width - 1, box.height - 1);
		
		// Draw borders and grid background
		g.setColor(Color.BLACK);
		g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
		box.grow(-extBorderSize, -extBorderSize);
		
		IMatrixView data = heatmap.getMatrixView();
		
		int cellWidth = heatmap.getCellWidth() + gridSize;
		int cellHeight = heatmap.getCellHeight() + gridSize;
		
		int rowStart = clip.y / cellHeight;
		int rowEnd = (clip.y + clip.height + cellHeight - 1) / cellHeight;
		rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();
		
		int colStart = clip.x / cellWidth;
		int colEnd = (clip.x + clip.width + cellWidth - 1) / cellWidth;
		colEnd = colEnd < data.getColumnCount() ? colEnd : data.getColumnCount();
		
		ElementDecorator deco = heatmap.getCellDecorator();
		ElementDecoration decoration = new ElementDecoration();
		
		int y = box.y + rowStart * cellHeight;
		for (int row = rowStart; row < rowEnd; row++) {
			int x = box.x + colStart * cellWidth;
			for (int col = colStart; col < colEnd; col++) {
				Object element = data.getCell(row, col);
				deco.decorate(decoration, element);
				g.setColor(decoration.getBgColor());
				g.fillRect(x, y, cellWidth - gridSize, cellHeight - gridSize);
				x += cellWidth;
			}
			y += cellHeight;
		}
	}

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
	
	public ElementDecorator getDecorator() {
		return decorator;
	}
	
	public void setDecorator(ElementDecorator decorator) {
		this.decorator = decorator;
	}
}
