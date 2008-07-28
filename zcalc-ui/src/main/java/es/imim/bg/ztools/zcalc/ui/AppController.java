package es.imim.bg.ztools.zcalc.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.ztools.zcalc.ui.views.matrix.DoubleMatrixTableModel;
import es.imim.bg.ztools.zcalc.ui.views.matrix.MatrixView;

public class AppController {

	private AppView gui;

	public AppController(AppView gui) {
		this.gui = gui;
		
		registerActions();
		
		MatrixView view = new MatrixView();
		
		DoubleMatrix2D data = DoubleFactory2D.dense.random(40, 12);
		
		final String[] rowNames = new String[data.rows()];
		for (int i = 0; i < rowNames.length; i++)
			rowNames[i] = "row " + (i + 1);
		
		final String[] colNames = new String[data.columns()];
		for (int i = 0; i < colNames.length; i++)
			colNames[i] = "col " + (i + 1);
		
		DoubleMatrixTableModel model = 
			new DoubleMatrixTableModel(rowNames, colNames, data);
		view.setModel(model);
		view.setName("unnamed");
		
		gui.addWorkspaceView(view);
		
		gui.setStatusText("Ok");
	}

	private void registerActions() {
		registerMenuActions();
		
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Options.instance().save();
				System.exit(0);
			}
		});
	}

	private void registerMenuActions() {
		
	}

	public void start() {
		gui.setLocationByPlatform(true);
		gui.setVisible(true);
	}

}
