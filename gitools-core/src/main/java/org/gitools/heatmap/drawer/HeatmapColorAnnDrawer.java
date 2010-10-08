package org.gitools.heatmap.drawer;

import edu.upf.bg.colorscale.util.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import org.gitools.heatmap.model.HeatmapHeader;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapCluster;
import org.gitools.heatmap.model.HeatmapClusterSet;
import org.gitools.matrix.model.IMatrixView;


public class HeatmapColorAnnDrawer extends AbstractHeatmapDrawer {

	protected static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;
	
	private boolean horizontal;
	private int colorAnnSize = 0;
	


	public HeatmapColorAnnDrawer(Heatmap heatmap, boolean horizontal) {
		super(heatmap);
		this.horizontal = horizontal;
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		
		g.setRenderingHint(
				RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Clear background
		g.setColor(Color.WHITE);
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		
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
		colorAnnSize = horizontal ? 
			calcClusterSizes(heatmap.getColumnClusterSets())	:
			calcClusterSizes(heatmap.getRowClusterSets());
		

		g.setFont(hdr.getFont());
		
		final Color gridColor = horizontal ? heatmap.getColumnsGridColor() : heatmap.getRowsGridColor();

		int gridSize = getGridSize(horizontal);
		
		int width = colorAnnSize;
		int height = (horizontal ? heatmap.getCellWidth() : heatmap.getCellHeight()) + gridSize;


		int clipStart = horizontal ? clip.x - box.x : clip.y - box.y;
		int clipEnd = horizontal ? clipStart + clip.width : clipStart + clip.height;
		int count = horizontal ? data.getColumnCount() : data.getRowCount();

		//add 2 to the start and end to not paint errors while scrolling..
		int start = (clipStart - height) / height - 2;
		int end = (clipEnd + height - 1) / height + 2;

		start = start > 0 ? start : 0;
		end = end < count ? end : count;

		final AffineTransform at = new AffineTransform();
		if (horizontal) {
			at.setToTranslation(0, width);
			g.transform(at);
			at.setToRotation(radianAngle90);
			g.transform(at);
		}


		int leadRow = data.getLeadSelectionRow();
		int leadColumn = data.getLeadSelectionColumn();

		int x = box.x + getGridSize(!horizontal);
		int y = box.y + start * height;
		int padding = (horizontal ? 3 : 2);

		int[] visibleElements = horizontal ? data.getVisibleColumns() : data.getVisibleRows();

		HeatmapClusterSet[] clusterSet = horizontal ? heatmap.getColumnClusterSets() : heatmap.getRowClusterSets();
		int[] clusterIndices = clusterSet[0].getClusterIndices();
		HeatmapCluster[] clusters = clusterSet[0].getClusters();

		int clusterIndexPrevious = -1;

		for (int index = start; index < end; index++) {
			String element = horizontal ?
				heatmap.getColumnLabel(index) : heatmap.getRowLabel(index);

			//Color annColor = uniqueLabels.get(element);

			int clusterIndex = clusterIndices[visibleElements[index]];
			Color bgColor = clusters[clusterIndex].getColor();
			Color fgColor = Color.WHITE;

		/*	boolean selected = !pictureMode && (horizontal ?
				data.isColumnSelected(index) : data.isRowSelected(index));

			if (selected) {
				bgColor = bgColor.darker();
				fgColor = fgColor.darker();
			}*/

			boolean lead = !pictureMode && (horizontal ?
				(leadColumn == index) /*&& (leadRow == -1)*/ :
				(leadRow == index) /*&& (leadColumn == -1)*/);

			g.setColor(gridColor);
			g.fillRect(x, y + height - gridSize, width, gridSize);

		/*	Correction if two elements are in the same cluster set */
			int sameClusterCorrection = 0;
			if (clusterIndex == clusterIndexPrevious)
				sameClusterCorrection = getGridSize(horizontal);
			
			
			g.setColor(bgColor);
			g.fillRect(x, y - sameClusterCorrection, width, height - gridSize + sameClusterCorrection);

			if (lead) {
				g.setColor(ColorUtils.invert(bgColor));
				g.drawRect(x, y, width, height - gridSize - 1);
			}

			y += height;

			clusterIndexPrevious = clusterIndex;
		}
	}

	@Override
	public Dimension getSize() {
		int gridSize = getGridSize(horizontal);
		int extBorder = /*2 * 1 - 1*/ 0;

		colorAnnSize = horizontal ?
			calcClusterSizes(heatmap.getColumnClusterSets())	:
			calcClusterSizes(heatmap.getRowClusterSets());

		if (horizontal) {
			int cellWidth = heatmap.getCellWidth() + gridSize;
			int columnCount = heatmap.getMatrixView().getColumnCount();
			return new Dimension(
					cellWidth * columnCount + extBorder, colorAnnSize);
		}
		else {
			int cellHeight = heatmap.getCellHeight() + gridSize;
			int rowCount = heatmap.getMatrixView().getRowCount();
			return new Dimension(
					colorAnnSize, cellHeight * rowCount + extBorder);
		}
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		int gridSize = getGridSize(horizontal);

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
		int gridSize = getGridSize(horizontal);

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

	private int getGridSize(boolean horizontalGrid) {
			if (horizontalGrid)
				return getColumnsGridSize();
			else
				return getRowsGridSize();

	}

	private int calcClusterSizes(HeatmapClusterSet[] clusterSets) {
		int size = 0;
		for (HeatmapClusterSet hcs : clusterSets) {
			if (hcs.isVisible()) {
				size += getGridSize(!horizontal);
				size += hcs.getSize();
				if (hcs.isLabelVisible())
					size += hcs.getFont().getSize();
			}
		}
		return size;
	}
}
