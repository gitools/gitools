package org.gitools.ui.panels.matrix;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.gitools.model.decorator.HeaderDecoration;
import org.gitools.model.decorator.HeaderDecorator;

public class RowsMatrixRenderer 
		implements TableCellRenderer {
	
	private HeaderDecorator decorator;
	
	private DefaultTableCellRenderer tableRenderer = 
		new DefaultTableCellRenderer();
	
	public RowsMatrixRenderer(
			HeaderDecorator decorator) {
		this.decorator = decorator;
	}
	
	public Component getTableCellRendererComponent(
			JTable table, Object value, boolean isSelected, 
			boolean hasFocus, int row, int column) {
		
		JLabel label = (JLabel) tableRenderer
				.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
		label.setOpaque(true);
		
		configureRenderer(label, value);
		
		if (isSelected)
			label.setBackground(label.getBackground().darker());
		
		return label;
	}

	private void configureRenderer(
			JLabel label,
			Object value) {
		
		final HeaderDecoration decoration = new HeaderDecoration();
		decorator.decorate(decoration, value);
		
		label.setText(decoration.getText());
		label.setToolTipText(decoration.getToolTip());
		label.setForeground(decoration.getFgColor());
		label.setBackground(decoration.getBgColor());

		switch (decoration.textAlign) {
		case left: label.setHorizontalAlignment(SwingConstants.LEFT); break;
		case right: label.setHorizontalAlignment(SwingConstants.RIGHT); break;
		case center: label.setHorizontalAlignment(SwingConstants.CENTER); break;
		}
	}
}