package es.imim.bg.ztools.ui.views.results;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class ResultsCellParamsPanel extends JPanel {

	private static final long serialVersionUID = -2448266362573432064L;

	private JTable table;

	private String[] names;
	private String[] values;
	
	public ResultsCellParamsPanel(String[] names) {
		this.names = names;
		
		createComponents();
	}
	
	private void createComponents() {
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setModel(new TableModel() {
			
			@Override
			public int getColumnCount() {
				return 2;
			}

			@Override
			public int getRowCount() {
				return names.length;
			}

			@Override
			public String getColumnName(int columnIndex) {
				switch (columnIndex) {
				case 0:
					return "name";
				case 1:
					return "value";
				}
				return null;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				switch (columnIndex) {
				case 0:
					return names[rowIndex];
				case 1:
					return values != null ? values[rowIndex] : "";
				}
				return null;
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				return String.class;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}

			@Override
			public void setValueAt(Object value, int rowIndex, int columnIndex) {
			}

			@Override
			public void addTableModelListener(TableModelListener l) {
			}

			@Override
			public void removeTableModelListener(TableModelListener l) {				
			}

		});
		
		table.setDefaultRenderer(String.class, new DefaultTableCellRenderer() {

			private static final long serialVersionUID = 1972045998524527140L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				
				final JLabel label = (JLabel) super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				
				label.setToolTipText(label.getText());
				return label;
			}
			
		});
		
		final JScrollPane scroll = new JScrollPane(table);
		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

	public void refresh() {
		table.repaint();
	}
	
	public void setValues(String[] values) {
		this.values = values;
		refresh();
	}
}
