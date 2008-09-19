package es.imim.bg.ztools.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.ui.commands.OpenAnalysisCommand;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.ResultsView;

public class AppController {

	private AppView gui;

	public AppController(AppView gui) {
		this.gui = gui;
		
		registerActions();
		
		int rows = 40;
		int cols = 12;
		DoubleMatrix3D data = DoubleFactory3D.dense.random(2, rows, cols);
		
		final String[] rowNames = new String[data.rows()];
		for (int i = 0; i < rowNames.length; i++)
			rowNames[i] = "row " + (i + 1);
		
		final String[] colNames = new String[data.columns()];
		for (int i = 0; i < colNames.length; i++)
			colNames[i] = "col " + (i + 1);
		
		Results results = new Results(
				colNames, 
				rowNames, 
				new String[] {"right-p-value", "param2"}, 
				data);
		
		Analysis analysis = new Analysis();
		analysis.setResults(results);
		
		ResultsView view = new ResultsView(results);
		view.setName("demo");
		
		gui.addWorkspaceView(view);
		
		gui.setStatusText("Ok");
	}

	private void registerActions() {
		registerMenuActions();
		
		gui.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Options.instance().save();
				System.exit(0);
			}
		});
	}

	private void registerMenuActions() {
		gui.menuiFileLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				fileLoadAction();
			}
		});
	}

	public void start() {
		gui.setLocationByPlatform(true);
		gui.setVisible(true);
	}
	
	private void fileLoadAction() {			
		OpenAnalysisCommand.create(gui).execute();
	}

}
