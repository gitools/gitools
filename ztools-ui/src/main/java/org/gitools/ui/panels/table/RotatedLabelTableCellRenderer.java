package org.gitools.ui.panels.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class RotatedLabelTableCellRenderer 
		extends JLabel 
		implements TableCellRenderer {

	private static final long serialVersionUID = 8878769979396041532L;

	protected static final double radianAngle = (-90.0 / 180.0) * Math.PI;

	protected boolean highlightSelected;

	protected boolean isSelected;

	public RotatedLabelTableCellRenderer(boolean highlightSelected) {
		this.highlightSelected = highlightSelected;
		setOpaque(true);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		this.setText(value.toString());

		int[] selColumns = table.getSelectedColumns();
		Arrays.sort(selColumns);
		int i = Arrays.binarySearch(selColumns, column);

		// System.out.println(column + " : " + i + " : cols:" +
		// Arrays.toString(selColumns));

		this.isSelected = i >= 0;

		return this;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		final int w = this.getWidth();
		final int h = this.getHeight();

		g2.setClip(0, 0, w, h);
		g2.setFont(this.getFont());

		if (highlightSelected && isSelected)
			g2.setBackground(Color.ORANGE);

		g2.clearRect(0, 0, w, h);

		g2.setColor(Color.WHITE);
		g2.drawRect(0, 0, w, h);

		AffineTransform at = new AffineTransform();
		at.setToTranslation(this.getWidth(), this.getHeight());
		g2.transform(at);
		at.setToRotation(radianAngle);
		g2.transform(at);

		Rectangle2D r = g2.getFontMetrics().getStringBounds(this.getText(), g2);
		float textHeight = (float) r.getHeight();

		g2.setColor(Color.BLACK);
		g2.drawString(this.getText(), 4.0f, -(w + 8 - textHeight) / 2);
	}
}