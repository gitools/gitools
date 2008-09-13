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

import cern.colt.matrix.DoubleFactory3D;
import cern.colt.matrix.DoubleMatrix3D;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.ui.utils.Options;
import es.imim.bg.ztools.zcalc.ui.views.AnalysisView;

public class AppController {

	private AppView gui;

	public AppController(AppView gui) {
		this.gui = gui;
		
		registerActions();
		
		int rows = 40;
		int cols = 12;
		DoubleMatrix3D data = DoubleFactory3D.dense.random(cols, rows, 2);
		
		final String[] rowNames = new String[data.rows()];
		for (int i = 0; i < rowNames.length; i++)
			rowNames[i] = "row " + (i + 1);
		
		final String[] colNames = new String[data.columns()];
		for (int i = 0; i < colNames.length; i++)
			colNames[i] = "col " + (i + 1);
		
		Results results = new Results(
				colNames, 
				rowNames, 
				new String[] {"param1", "param2"}, 
				data);
		Analysis analysis = new Analysis();
		analysis.setResults(results);
		
		AnalysisView view = new AnalysisView(analysis);
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
					
					Results results = rf.read();
					Analysis analysis = new Analysis();
					analysis.setResults(results); //TEMP
					
					gui.setStatusText("Initializing view...");
					
					AnalysisView view = new AnalysisView(analysis);
					
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
