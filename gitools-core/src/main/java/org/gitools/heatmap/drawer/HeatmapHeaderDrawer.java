package org.gitools.heatmap.drawer;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import org.gitools.heatmap.model.HeatmapHeaderDecoration;
import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.matrix.model.IMatrixView;

public class HeatmapHeaderDrawer extends AbstractHeatmapDrawer {

	protected static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;
	
	private boolean horizontal;

	public HeatmapHeaderDrawer(Heatmap heatmap, boolean horizontal) {
		super(heatmap);
		this.horizontal = horizontal;
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Draw borders
		if (heatmap.isShowBorders()) {
			int borderSize = getBorderSize();

			g.setColor(Color.BLACK);
			g.drawRect(box.x, box.y, box.width - 1, box.height - 1);
			box.x += horizontal ? 0 : borderSize;
			box.width -= borderSize * (horizontal ? 1 : 2);
			box.y += borderSize;
			box.height -= borderSize * (horizontal ? 1 : 2);
		}

		IMatrixView data = heatmap.getMatrixView();

		HeatmapHeader hdr = horizontal ? heatmap.getColumnHeader() : heatmap.getRowHeader();
		HeatmapHeaderDecoration decoration = new HeatmapHeaderDecoration();

		g.setFont(hdr.getFont());
		
		final Color gridColor = horizontal ? heatmap.getColumnsGridColor() : heatmap.getRowsGridColor();

		int gridSize = getGridSize();
		
		int maxWidth = horizontal ? clip.height : clip.width;
		int width = horizontal ? heatmap.getColumnHeaderSize() : heatmap.getRowHeaderSize();
		int height = (horizontal ? heatmap.getCellWidth() : heatmap.getCellHeight()) + gridSize;

		width = width < maxWidth ? maxWidth : width;

		int clipStart = horizontal ? clip.x - box.x : clip.y - box.y;
		int clipEnd = horizontal ? clipStart + clip.width : clipStart + clip.height;
		int count = horizontal ? data.getColumnCount() : data.getRowCount();

		int start = (clipStart - height) / height;
		int end = (clipEnd + height - 1) / height;

		start = start > 0 ? start : 0;
		end = end < count ? end : count;

		final AffineTransform at = new AffineTransform();
		if (horizontal) {
			at.setToTranslation(0, width);
			g.transform(at);
			at.setToRotation(radianAngle90);
			g.transform(at);
		}

		int fontHeight = g.getFontMetrics().getHeight();
		int fontOffset = ((fontHeight + height - gridSize) / 2) - 1;

		int leadRow = data.getLeadSelectionRow();
		int leadColumn = data.getLeadSelectionColumn();

		int x = box.x;
		int y = box.y + start * height;
		int padding = (horizontal ? 3 : 2);
		for (int index = start; index < end; index++) {
			String element = horizontal ?
				heatmap.getColumnLabel(index) : heatmap.getRowLabel(index);
			hdr.decorate(decoration, element);

			Color bgColor = decoration.getBgColor();
			Color fgColor = decoration.getFgColor();

			boolean selected = !pictureMode && (horizontal ?
				data.isColumnSelected(index) : data.isRowSelected(index));

			if (selected) {
				bgColor = bgColor.darker();
				fgColor = fgColor.darker();
			}

			boolean lead = !pictureMode && (horizontal ?
				(leadColumn == index) /*&& (leadRow == -1)*/ :
				(leadRow == index) /*&& (leadColumn == -1)*/);

			g.setColor(gridColor);
			g.fillRect(x, y + height - gridSize, width, gridSize);
			
			g.setColor(bgColor);
			g.fillRect(x, y, width, height - gridSize);

			if (lead) {
				g.setColor(ColorUtils.invert(bgColor));
				g.drawRect(x, y, width, height - gridSize);
			}

			if (fontHeight <= height - gridSize) {
				g.setColor(fgColor);
				g.drawString(element, x + padding, y + fontOffset);
			}

			y += height;
		}
	}

	@Override
	public Dimension getSize() {
		int gridSize = getGridSize();
		int extBorder = /*2 * 1 - 1*/ 0;

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

	@Override
	public HeatmapPosition getPosition(Point p) {
		int gridSize = getGridSize();

		int row = -1;
		int col = -1;

		if (horizontal) {
			int cellSize = heatmap.getCellWidth() + gridSize;
			int totalSize = cellSize * heatmap.getMatrixView().getColumnCount();
			if (p.x >= 0 && p.x < totalSize)
				col = p.x / cellSize;
		}
		else {
			int cellSize = heatmap.getCellHeight() + gridSize;
			int totalSize = cellSize * heatmap.getMatrixView().getRowCount();
			if (p.y >= 0 && p.y < totalSize)
				row = p.y / cellSize;
		}

		return new HeatmapPosition(row, col);
	}

	@Override
	public Point getPoint(HeatmapPosition p) {
		int gridSize = getGridSize();

		int x = 0;
		int y = 0;

		if (horizontal) {
			int cellSize = heatmap.getCellWidth() + gridSize;
			int totalSize = cellSize * heatmap.getMatrixView().getColumnCount();
			x = p.column >= 0 ? p.column * cellSize : 0;
			if (x > totalSize)
				x = totalSize;
		}
		else {
			int cellSize = heatmap.getCellHeight() + gridSize;
			int totalSize = cellSize * heatmap.getMatrixView().getRowCount();
			y = p.row >= 0 ? p.row * cellSize : 0;
			if (y > totalSize)
				y = totalSize;
		}

		return new Point(x, y);
	}

	private int getGridSize() {
		return horizontal ? getColumnsGridSize() : getRowsGridSize();
	}
}
