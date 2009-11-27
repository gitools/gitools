package org.gitools.heatmap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;
import org.gitools.model.figure.MatrixFigure;
import org.gitools.model.matrix.IMatrixView;

public class HeatmapHeaderDrawer {

	protected static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;
	
	private MatrixFigure heatmap;
	
	private boolean horizontal;
	
	public HeatmapHeaderDrawer(MatrixFigure heatmap, boolean horizontal) {
		this.heatmap = heatmap;
		this.horizontal = horizontal;
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
		
		// Draw borders and grid background
		g.setColor(Color.BLACK);
		
		/*g.drawLine(box.x, box.y, box.x + box.width - 1, box.y);
		g.drawLine(box.x, box.y, box.x, box.y + box.height - 1);
		g.drawLine(box.x, box.y + box.height - 1, box.x + box.width - 1, box.y + box.height - 1);*/
		
		g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
		
		box.x += horizontal ? 0 : extBorderSize; box.width -= extBorderSize * (horizontal ? 1 : 2);
		box.y += extBorderSize; box.height -= extBorderSize * (horizontal ? 1 : 2);
		g.setClip(box.x, box.y, box.width, box.height);
		
		IMatrixView data = heatmap.getMatrixView();
		
		int cellWidth = horizontal ? heatmap.getCellWidth() + gridSize : heatmap.getRowHeaderSize();
		
		int cellHeight = horizontal ? heatmap.getColumnHeaderSize() : heatmap.getCellHeight() + gridSize;
		
		HeaderDecorator deco = horizontal ? heatmap.getColumnDecorator() : heatmap.getRowDecorator();
		HeaderDecoration decoration = new HeaderDecoration();
		
		final Color gridColor = heatmap.getGridColor();
		
		if (horizontal) {
			int colStart = (clip.x - cellWidth) / cellWidth;
			colStart = colStart > 0 ? colStart : 0;
			int colEnd = (clip.x + clip.width + cellWidth - 1) / cellWidth;
			colEnd = colEnd < data.getColumnCount() ? colEnd : data.getColumnCount();
			
			final AffineTransform at = new AffineTransform();
			at.setToTranslation(0, cellHeight);
			g.transform(at);
			at.setToRotation(radianAngle90);
			g.transform(at);
			
			g.setColor(Color.RED);
			g.drawRect(0, 0, cellHeight, cellWidth * heatmap.getMatrixView().getColumnCount());
			g.drawLine(0, 0, cellHeight, cellWidth * heatmap.getMatrixView().getColumnCount());
			
			int tmp = cellWidth;
			cellWidth = cellHeight;
			cellHeight = tmp;
			
			int y = box.y + colStart * cellHeight;
			int x = box.x;
			for (int col = colStart; col < colEnd; col++) {
				String element = data.getColumnLabel(col);
				deco.decorate(decoration, element);
				
				g.setColor(decoration.getBgColor());
				g.fillRect(x, y, cellWidth - gridSize, cellHeight - gridSize);
				
				g.setColor(gridColor);
				g.drawLine(x, y + cellHeight - 1, x + cellWidth - 1, y + cellHeight - 1);
				
				g.setColor(decoration.getFgColor());
				int fh = g.getFontMetrics().getHeight();
				g.drawString(element, x, y + ((fh + cellHeight) / 2) - 1); //TODO extract constant
				
				y += cellHeight;
			}
		}
		else {
			int rowStart = (clip.y - cellHeight) / cellHeight;
			rowStart = rowStart > 0 ? rowStart : 0;
			int rowEnd = (clip.y + clip.height + cellHeight - 1) / cellHeight;
			rowEnd = rowEnd < data.getRowCount() ? rowEnd : data.getRowCount();
			
			int x = box.x;
			int y = box.y + rowStart * cellHeight;
			for (int row = rowStart; row < rowEnd; row++) {
				String element = data.getRowLabel(row);
				deco.decorate(decoration, element);
				
				g.setColor(decoration.getBgColor());
				g.fillRect(x, y, cellWidth - gridSize + 1, cellHeight - gridSize);
				
				g.setColor(gridColor);
				g.drawLine(x, y + cellHeight - 1, x + cellWidth - 1, y + cellHeight - 1);
				
				g.setColor(decoration.getFgColor());
				int fh = g.getFontMetrics().getHeight();
				g.drawString(element, x, y + ((fh + cellHeight) / 2) - 1); //TODO extract constant
				
				y += cellHeight;
			}
		}
	}
	
	public Dimension getSize() {
		int gridSize = heatmap.isShowGrid() ? 1 : 0;
		int extBorder = 2 * 1 - 1;
		if (horizontal) {
			int cellWidth = heatmap.getCellWidth() + gridSize;
			int columnCount = heatmap.getMatrixView().getColumnCount();
			int headerSize = heatmap.getColumnHeaderSize();
			return new Dimension(
					cellWidth * columnCount + extBorder, headerSize);
		}
		else {
			int cellHeight = heatmap.getCellHeight() + gridSize;
			int rowCount = heatmap.getMatrixView().getRowCount();
			int headerSize = heatmap.getRowHeaderSize();
			return new Dimension(
					headerSize, cellHeight * rowCount + extBorder);
		}
	}
}
