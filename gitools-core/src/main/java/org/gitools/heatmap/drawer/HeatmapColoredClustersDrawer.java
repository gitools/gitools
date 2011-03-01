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

import edu.upf.bg.colorscale.util.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.List;

import org.gitools.heatmap.model.HeatmapLabelsHeader;
import org.gitools.heatmap.model.Heatmap;
import org.gitools.heatmap.model.HeatmapCluster;
import org.gitools.heatmap.model.HeatmapColoredClustersHeader;
import org.gitools.heatmap.model.HeatmapDim;
import org.gitools.matrix.model.IMatrixView;


public class HeatmapColoredClustersDrawer extends AbstractHeatmapDrawer {

	protected static final double radianAngle90 = (-90.0 / 180.0) * Math.PI;

	private HeatmapColoredClustersHeader header;
	
	private boolean horizontal;

	private int headerTotalSize = 0;

	public HeatmapColoredClustersDrawer(Heatmap heatmap, HeatmapColoredClustersHeader header, boolean horizontal) {
		super(heatmap);

		this.header = header;
		this.horizontal = horizontal;
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		
		HeatmapDim dim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();

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

		HeatmapLabelsHeader hdr = dim.getLabelsHeader();
		headerTotalSize = header.getSize();

		g.setFont(hdr.getFont());
		
		final Color gridColor = dim.getGridColor();

		int gridSize = getGridSize(horizontal);
		
		int width = headerTotalSize;
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

		int[] clusterIndices = header.getClusterIndices();
		HeatmapCluster[] clusters = header.getClusters();

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

		HeatmapDim dim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
		headerTotalSize = header.getSize();

		if (horizontal) {
			int cellWidth = heatmap.getCellWidth() + gridSize;
			int columnCount = heatmap.getMatrixView().getColumnCount();
			return new Dimension(
					cellWidth * columnCount + extBorder, headerTotalSize);
		}
		else {
			int cellHeight = heatmap.getCellHeight() + gridSize;
			int rowCount = heatmap.getMatrixView().getRowCount();
			return new Dimension(
					headerTotalSize, cellHeight * rowCount + extBorder);
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

	/*private int calcClusterSizes(List<HeatmapClusterBand> clusterSets) {
		int size = 0;
		for (HeatmapClusterBand hcs : clusterSets) {
			if (hcs.isVisible()) {
				size += getGridSize(!horizontal);
				size += hcs.getSize();
				if (hcs.isLabelVisible())
					size += hcs.getFont().getSize();
			}
		}
		return size;
	}*/
}
