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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.gitools.heatmap.model.Heatmap;

public class HeatmapDrawer extends AbstractHeatmapDrawer {

	private HeatmapBodyDrawer body;
	private HeatmapLabelsDrawer rows;
	private HeatmapLabelsDrawer columns;
	private HeatmapClusterDrawer rowsClusterSet;
	private HeatmapClusterDrawer columnsClusterSet;

	public HeatmapDrawer(Heatmap heatmap) {
		super(heatmap);

		body = new HeatmapBodyDrawer(heatmap);
		rows = new HeatmapLabelsDrawer(heatmap, false);
		columns = new HeatmapLabelsDrawer(heatmap, true);
		rowsClusterSet = new HeatmapClusterDrawer(heatmap, false);
		columnsClusterSet = new HeatmapClusterDrawer(heatmap, true);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		Dimension bodySize = body.getSize();
		Dimension rowsSize = rows.getSize();
		Dimension columnsSize = columns.getSize();
		Dimension rowsCSSize = rowsClusterSet.getSize();
		Dimension columnsCSSize = columnsClusterSet.getSize();

		Rectangle columnsBounds = new Rectangle(0, 0, columnsSize.width, columnsSize.height);
		Rectangle columnsCSBounds = new Rectangle(0 - columnsSize.height, 0, columnsCSSize.width, columnsCSSize.height);
		Rectangle bodyBounds = new Rectangle(0, columnsSize.height + columnsCSSize.height, bodySize.width, bodySize.height);
		Rectangle rowsCSBounds = new Rectangle(bodySize.width, columnsSize.height + columnsCSSize.height, rowsCSSize.width, rowsCSSize.height);
		Rectangle rowsBounds = new Rectangle(bodySize.width + rowsCSBounds.width, columnsSize.height + columnsCSSize.height , rowsSize.width, rowsSize.height);

		AffineTransform at = new AffineTransform();

		columns.draw(g, columnsBounds, columnsBounds);
		at.setToIdentity();
		g.setTransform(at);
		columnsClusterSet.draw(g, columnsCSBounds, columnsCSBounds);
		at.setToIdentity();
		g.setTransform(at);
		body.draw(g, bodyBounds, bodyBounds);
		at.setToIdentity();
		g.setTransform(at);
		rowsClusterSet.draw(g, rowsCSBounds, rowsCSBounds);
		at.setToIdentity();
		g.setTransform(at);
		rows.draw(g, rowsBounds, rowsBounds);
		at.setToIdentity();
		g.setTransform(at);

	}

	@Override
	public Dimension getSize() {
		Dimension bodySize = body.getSize();
		Dimension rowsSize = rows.getSize();
		Dimension columnsSize = columns.getSize();
		Dimension rowsCSSize = rowsClusterSet.getSize();
		Dimension columnsCSSize = columnsClusterSet.getSize();
		return new Dimension(bodySize.width + rowsCSSize.width + rowsSize.width, bodySize.height + columnsCSSize.height + columnsSize.height);
	}

	@Override
	public HeatmapPosition getPosition(Point p) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Point getPoint(HeatmapPosition p) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void setPictureMode(boolean pictureMode) {
		super.setPictureMode(pictureMode);
		body.setPictureMode(pictureMode);
		rows.setPictureMode(pictureMode);
		columns.setPictureMode(pictureMode);
		rowsClusterSet.setPictureMode(pictureMode);
		columnsClusterSet.setPictureMode(pictureMode);
	}

}
