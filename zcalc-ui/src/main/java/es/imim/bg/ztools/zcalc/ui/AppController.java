package es.imim.bg.ztools.zcalc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFileChooser;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix2D;

import es.imim.bg.ztools.zcalc.ui.utils.Options;
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
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));			
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		//fileChooser.addChoosableFileFilter(fileFilter);
		int retval = fileChooser.showOpenDialog(gui);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				//umlText.setText(file.getAbsolutePath());
				Options.instance().setLastPath(file.getParent());
				//refresh();
			}
		}
	}

}
