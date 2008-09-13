package es.imim.bg.ztools.zcalc.ui.colormatrix;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class ColorMatrix extends JPanel {

	private static final long serialVersionUID = 1122420366217373359L;

	private class ColorMatrixModelAddapter implements TableModel {

		private ColorMatrixModel model;
		
		public ColorMatrixModelAddapter(ColorMatrixModel model) {
			this.model = model;
		}
		
		public int getRowCount() {
			return model.getRowCount();
		}
		
		public int getColumnCount() {
			return model.getColumnCount();
		}

		public String getColumnName(int col) {
			return col < model.getColumnCount() - 1 ? 
					model.getColumnName(col) : " ";
		}

		public Object getValueAt(int row, int col) {
			return col < model.getColumnCount() - 1 ?
					model.getValue(row, col) :
					model.getRowName(row);
		}
		
		public void setValueAt(Object value, int row, int col) {
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		public void addTableModelListener(TableModelListener arg0) {
		}
		
		public void removeTableModelListener(TableModelListener arg0) {
		}
		
		public Class<?> getColumnClass(int col) {
			return col < model.getColumnCount() - 1 ? 
					Double.class : String.class;
		}
	}
	
	public class TableCellRendererAdapter implements TableCellRenderer {
		
		private ColorMatrixCellDecorator decorator;
		
		private DefaultTableCellRenderer tableRenderer = 
			new DefaultTableCellRenderer();
		
		public TableCellRendererAdapter(ColorMatrixCellDecorator decorator) {
			this.decorator = decorator;
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

			ColorMatrixCellDecoration decoration = new ColorMatrixCellDecoration();
			decorator.decorate(decoration, (Double) value);
			label.setText(decoration.getText());
			label.setToolTipText(decoration.getToolTip());
			label.setForeground(decoration.getFgColor());
			label.setBackground(decoration.getBgColor());
		}
	}

	private JTable table;
	
	public ColorMatrix() {
		
		createComponents();
	}
	
	private void createComponents() {
		
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		final JScrollPane scroll = new JScrollPane();
		scroll.setAutoscrolls(true);
		scroll.setViewportView(table);
		
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	public void refresh() {
		table.repaint();
	}
	
	public void setModel(ColorMatrixModel model) {
		table.setModel(new ColorMatrixModelAddapter(model));
		table.repaint();
	}
	
	public void setCellDecorator(ColorMatrixCellDecorator decorator) {
		table.setDefaultRenderer(Double.class, new TableCellRendererAdapter(decorator));
		table.repaint();
	}
}
