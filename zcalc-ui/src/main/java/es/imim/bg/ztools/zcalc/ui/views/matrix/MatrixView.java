package es.imim.bg.ztools.zcalc.ui.views.matrix;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;

import es.imim.bg.ztools.zcalc.ui.views.AbstractView;

public class MatrixView extends AbstractView {

	private static final long serialVersionUID = -3339816379068318821L;

	private JTable table;
	
	public MatrixView() {
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
