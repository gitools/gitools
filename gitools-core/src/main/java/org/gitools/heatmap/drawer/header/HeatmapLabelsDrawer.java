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

import org.gitools.label.AnnotationsPatternProvider;
import edu.upf.bg.color.utils.ColorUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.LineMetrics;
import org.gitools.heatmap.drawer.AbstractHeatmapHeaderDrawer;
import org.gitools.heatmap.drawer.HeatmapPosition;

import org.gitools.heatmap.header.HeatmapTextLabelsHeader;
import org.gitools.heatmap.Heatmap;
import org.gitools.heatmap.HeatmapDim;
import org.gitools.label.LabelProvider;
import org.gitools.label.MatrixColumnsLabelProvider;
import org.gitools.label.MatrixRowsLabelProvider;
import org.gitools.matrix.model.AnnotationMatrix;
import org.gitools.matrix.model.IMatrixView;


public class HeatmapLabelsDrawer extends AbstractHeatmapHeaderDrawer<HeatmapTextLabelsHeader> {

	protected static class AnnotationProvider implements LabelProvider {

		private LabelProvider labelProvider;
		private AnnotationMatrix am;
		private String name;
		private int column;

		public AnnotationProvider(LabelProvider labelProvider, AnnotationMatrix am, String name) {
			this.labelProvider = labelProvider;
			this.am = am;
			this.name = name;
			if (am != null)
				this.column = am.getColumnIndex(name);
		}

		@Override
		public int getCount() {
			return labelProvider.getCount();
		}


		@Override
		public String getLabel(int index) {
			if (am == null)
				return name;

			String label = labelProvider.getLabel(index);
			int row = am.getRowIndex(label);
			return am.getCell(row, column);
		}
	}

	public HeatmapLabelsDrawer(Heatmap heatmap, HeatmapTextLabelsHeader header, boolean horizontal) {
		super(heatmap, header, horizontal);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {

		// Clear background
		g.setColor(header.getBackgroundColor());
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

		final HeatmapDim hdim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
		IMatrixView data = heatmap.getMatrixView();

		g.setFont(header.getFont());
		
		Color gridColor = hdim.getGridColor();

		int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

		int maxWidth = clip.width;
		int width = header.getSize();
		int cellHeight = horizontal ? heatmap.getCellWidth() : heatmap.getCellHeight();
		int height = cellHeight + gridSize;

		width = width < maxWidth ? maxWidth : width;

		int clipStart = clip.y - box.y;
		int clipEnd = clipStart + clip.height;
		int count = horizontal ? data.getColumnCount() : data.getRowCount();

		int start = (clipStart - height) / height;
		int end = (clipEnd + height - 1) / height;

		start = start > 0 ? start : 0;
		end = end < count ? end : count;

		LineMetrics lm = header.getFont().getLineMetrics("yÍ;|", g.getFontRenderContext());
		float fontHeight = lm.getHeight();
		float fontDesc = lm.getDescent();
		int fontOffset = (int) Math.ceil(((fontHeight + cellHeight) / 2) - fontDesc);

		int leadRow = data.getLeadSelectionRow();
		int leadColumn = data.getLeadSelectionColumn();

		LabelProvider matrixLabelProvider = null;

		if (horizontal)
			matrixLabelProvider = new MatrixColumnsLabelProvider(heatmap.getMatrixView());
		else
			matrixLabelProvider = new MatrixRowsLabelProvider(heatmap.getMatrixView());

		LabelProvider labelProvider = null;
		switch (header.getLabelSource()) {
			case ID:
				labelProvider = matrixLabelProvider;
				break;
			case ANNOTATION:
				labelProvider = new AnnotationProvider(
						matrixLabelProvider,
						hdim.getAnnotations(),
						header.getLabelAnnotation());
				break;
			case PATTERN:
				labelProvider = new AnnotationsPatternProvider(
						matrixLabelProvider,
						hdim.getAnnotations(),
						header.getLabelPattern());
				break;
		}

		int x = box.x;
		int y = box.y + start * height;
		int padding = 2;
		for (int index = start; index < end; index++) {
			String label = labelProvider.getLabel(index);

			Color bgColor = header.getBackgroundColor();
			Color fgColor = header.getForegroundColor();
			Color gColor = gridColor;

			boolean selected = !pictureMode && (horizontal ?
				data.isColumnSelected(index) : data.isRowSelected(index));

			if (selected) {
				bgColor = bgColor.darker();
				fgColor = fgColor.darker();
				gColor = gridColor.darker();
			}

			boolean lead = !pictureMode && (horizontal ?
				(leadColumn == index) /*&& (leadRow == -1)*/ :
				(leadRow == index) /*&& (leadColumn == -1)*/);

			g.setColor(gColor);
			g.fillRect(x, y + cellHeight, width, gridSize);
			
			g.setColor(bgColor);
			g.fillRect(x, y, width, cellHeight);

			if (lead) {
				g.setColor(ColorUtils.invert(bgColor));
				g.drawRect(x, y, width, cellHeight - 1);
			}

			if (fontHeight <= cellHeight) {
				g.setColor(fgColor);
				g.drawString(label, x + padding, y + fontOffset);
			}

			y += height;
		}
	}

	@Override
	public Dimension getSize() {
		HeatmapDim hdim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
		int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;
		int extBorder = /*2 * 1 - 1*/ 0;

		if (horizontal) {
			int cellWidth = heatmap.getCellWidth() + gridSize;
			int columnCount = heatmap.getMatrixView().getColumnCount();
			int headerSize = header.getSize();
			return new Dimension(
					cellWidth * columnCount + extBorder, headerSize /*- colorAnnSize*/);
		}
		else {
			int cellHeight = heatmap.getCellHeight() + gridSize;
			int rowCount = heatmap.getMatrixView().getRowCount();
			int headerSize = header.getSize();
			return new Dimension(
					headerSize, cellHeight * rowCount + extBorder);
		}
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		HeatmapDim hdim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
		int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

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
		HeatmapDim hdim = horizontal ? heatmap.getColumnDim() : heatmap.getRowDim();
		int gridSize = hdim.isGridEnabled() ? hdim.getGridSize() : 0;

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
}
