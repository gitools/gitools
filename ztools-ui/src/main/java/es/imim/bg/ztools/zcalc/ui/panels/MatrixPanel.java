package es.imim.bg.ztools.zcalc.ui.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

public class MatrixPanel extends JPanel {

	private static final long serialVersionUID = 1122657712951763672L;

	private JTable table;
	
	public MatrixPanel() {
	
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
	
	public void setModel(TableModel model) {
		table.setModel(model);
	}
}
