package es.imim.bg.ztools.zcalc.ui.old.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import es.imim.bg.colorscale.ColorScale;
import es.imim.bg.colorscale.LogColorScale;

public class MatrixPanel extends JPanel {

	private static final long serialVersionUID = 1122657712951763672L;

	private JTable table;
	
	private ColorScale scale;
	
	public class ColorRenderer implements TableCellRenderer {
		
		private ColorScale scale;
		
		private DefaultTableCellRenderer tableRenderer = 
			new DefaultTableCellRenderer();
		
		public ColorRenderer(ColorScale scale) {
			this.scale = scale;
		}
		
		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {
			
			JLabel label = (JLabel) tableRenderer
					.getTableCellRendererComponent(
							table, value, isSelected, hasFocus, row, column);
			
			configureRenderer(tableRenderer, value);
			
			return label;
		}

		private void configureRenderer(
				JLabel label,
				Object value) {

			Double v = (Double) value;
			
			if (v != null) {
				if (scale != null) {
					Color color = scale.getColor(v);
					
					label.setText("");
			        label.setBackground(color);
			        label.setToolTipText(v.toString());
			        
			        /*label.setBorder(
			        		BorderFactory.createMatteBorder(
			        				2, 
			        				(column == 0) ? 2 : 0, 
			        				2, 
			        				(table.getColumnCount() - 1 == column) ? 2 : 0, 
			        				(isSelected) ? color.darker() : color));*/
				}
				else {
					label.setText(Double.toString(v));
					label.setBackground(Color.WHITE);
					label.setToolTipText("");
				}
			}
			
		}
		
		/*@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value, boolean isSelected, 
				boolean hasFocus, int row, int column) {
			
			JLabel label = new JLabel();
			
			Double v = (Double) value;
			
			if (v != null) {
				if (scale != null) {
					Color color = scale.getColor(v);
					
			        label.setBackground(color);
			        
			        /*label.setBorder(
			        		BorderFactory.createMatteBorder(
			        				2, 
			        				(column == 0) ? 2 : 0, 
			        				2, 
			        				(table.getColumnCount() - 1 == column) ? 2 : 0, 
			        				(isSelected) ? color.darker() : color));*//*
				}
				else {
					label.setText(Double.toString(v));
				}
			}
	        
			return label;
		}*/
	}
	
	public MatrixPanel() {
	
		createComponents();
	}

	private void createComponents() {
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setDefaultRenderer(Double.class, new ColorRenderer(
				new LogColorScale()));
		
		final JScrollPane scroll = new JScrollPane();
		scroll.setAutoscrolls(true);
		scroll.setViewportView(table);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	public void setModel(TableModel model) {
		table.setModel(model);
	}
	
	public void setScale(ColorScale scale) {
		this.scale = scale;
	}
}
