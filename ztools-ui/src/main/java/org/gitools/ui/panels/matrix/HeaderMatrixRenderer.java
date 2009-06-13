package org.gitools.ui.panels.matrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;

public class HeaderMatrixRenderer 
		extends JLabel 
		implements TableCellRenderer {

	private static final long serialVersionUID = -1567024460723517390L;

	protected HeaderDecorator decorator;
	
	protected boolean showGrid;
	protected Color gridColor = Color.WHITE;
	
	protected boolean highlightSelected;
	protected boolean isSelected;
	
	protected int minimumRequiredTextSize;
	
	public HeaderMatrixRenderer(
			HeaderDecorator decorator,
			boolean highlightSelected) {
		this.decorator = decorator;
		this.highlightSelected = highlightSelected;
		this.minimumRequiredTextSize = 8;
		setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		configureRenderer(this, value);

		int[] selColumns = table.getSelectedColumns();
		Arrays.sort(selColumns);
		int i = Arrays.binarySearch(selColumns, column);

		this.isSelected = i >= 0;
		
		if (isSelected)
			setBackground(getBackground().darker());

		return this;
	}
	
	private void configureRenderer(
			JLabel label, Object value) {

		final HeaderDecoration decoration = new HeaderDecoration();
		decorator.decorate(decoration, value);
		label.setText(decoration.getText());
		if (!decoration.getToolTip().isEmpty())
			label.setToolTipText(decoration.getToolTip());
		label.setForeground(decoration.getFgColor());
		label.setBackground(decoration.getBgColor());

		switch (decoration.textAlign) {
		case left: label.setHorizontalAlignment(SwingConstants.LEFT); break;
		case right: label.setHorizontalAlignment(SwingConstants.RIGHT); break;
		case center: label.setHorizontalAlignment(SwingConstants.CENTER); break;
		}
	}
	
	public void setGridColor(Color gridColor) {
		this.gridColor = gridColor;
	}
	
	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}
}
