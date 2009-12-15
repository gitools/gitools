package org.gitools.heatmap.drawer;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import org.gitools.heatmap.model.Heatmap;

public class HeatmapDrawer extends AbstractHeatmapDrawer {

	private HeatmapBodyDrawer body;
	private HeatmapHeaderDrawer rows;
	private HeatmapHeaderDrawer columns;

	public HeatmapDrawer(Heatmap heatmap) {
		super(heatmap);

		body = new HeatmapBodyDrawer(heatmap);
		rows = new HeatmapHeaderDrawer(heatmap, false);
		columns = new HeatmapHeaderDrawer(heatmap, true);
	}

	@Override
	public void draw(Graphics2D g, Rectangle box, Rectangle clip) {
		Dimension bodySize = body.getSize();
		Dimension rowsSize = rows.getSize();
		Dimension columnsSize = columns.getSize();

		Rectangle columnsBounds = new Rectangle(0, 0, columnsSize.width, columnsSize.height);
		Rectangle bodyBounds = new Rectangle(0, columnsSize.height, bodySize.width, bodySize.height);
		Rectangle rowsBounds = new Rectangle(bodySize.width, columnsSize.height, rowsSize.width, rowsSize.height);
		
		AffineTransform at = new AffineTransform();
		
		columns.draw(g, columnsBounds, columnsBounds);
		at.setToIdentity();
		g.setTransform(at);
		body.draw(g, bodyBounds, bodyBounds);
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
		return new Dimension(bodySize.width + rowsSize.width, bodySize.height + columnsSize.height);
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
	}

}
