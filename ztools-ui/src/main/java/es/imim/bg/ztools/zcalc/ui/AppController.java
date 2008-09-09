package es.imim.bg.ztools.zcalc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.swing.JFileChooser;

import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory2D;
import cern.colt.matrix.ObjectMatrix2D;

import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.ui.utils.Options;
import es.imim.bg.ztools.zcalc.ui.views.AnalysisView;
import es.imim.bg.ztools.zcalc.ui.views.matrix.DoubleMatrixTableModel;
import es.imim.bg.ztools.zcalc.ui.views.matrix.MatrixView;

public class AppController {

	private AppView gui;

	public AppController(AppView gui) {
		this.gui = gui;
		
		registerActions();
		
		//MatrixView view = new MatrixView();
		
		int rows = 40;
		int cols = 12;
		ObjectMatrix2D data = ObjectFactory2D.dense.make(rows, cols);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				data.setQuick(i, j, DoubleFactory1D.dense.random(2));
		
		final String[] rowNames = new String[data.rows()];
		for (int i = 0; i < rowNames.length; i++)
			rowNames[i] = "row " + (i + 1);
		
		final String[] colNames = new String[data.columns()];
		for (int i = 0; i < colNames.length; i++)
			colNames[i] = "col " + (i + 1);
		
		/*DoubleMatrixTableModel model = 
			new DoubleMatrixTableModel(rowNames, colNames, data);
		view.setModel(model);
		view.setName("unnamed");*/
		
		AnalysisView view = new AnalysisView(
				rowNames,
				colNames,
				new String[] {"param1", "param2", "param3"},
				data);
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
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		fileChooser.setDialogTitle("Select the folder that contains the analysis data");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));
		//fileChooser.addChoosableFileFilter(fileFilter);
		int retval = fileChooser.showOpenDialog(gui);
		if (retval == JFileChooser.APPROVE_OPTION) {
			File selectedPath = fileChooser.getSelectedFile();
			if (selectedPath != null) {
				Options.instance().setLastPath(selectedPath.getParent());
				Options.instance().save();
				
				//TODO: Load analysis data
				File resultsFile = new File(selectedPath, "results.csv"); //FIXME results.csv from a constant
				ResultsFile rf = new ResultsFile(resultsFile);
				try {
					gui.setStatusText("Reading " + rf.getResourcePath() + "...");
					
					rf.read();
					
					gui.setStatusText("Initializing view...");
					
					/*MatrixView view = new MatrixView();
					
					ResultsMatrixTableModel model = 
						new ResultsMatrixTableModel(
								rf.getGroupNames(), 
								rf.getCondNames(), 
								rf.getResults(),
								0);
					
					view.setModel(model);
					view.setName(rf.getResourcePath()); //FIXME: use analysis name
					*/
					
					AnalysisView view = new AnalysisView(
							rf.getGroupNames(),
							rf.getCondNames(),
							rf.getParamNames(),
							rf.getResults());
					
					view.setName(rf.getResourcePath());
					
					gui.addWorkspaceView(view); //FIXME: use analysis name
					
					gui.setStatusText("Ok");
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (DataFormatException e) {
					e.printStackTrace();
				}
				//refresh();
			}
		}
	}

}
