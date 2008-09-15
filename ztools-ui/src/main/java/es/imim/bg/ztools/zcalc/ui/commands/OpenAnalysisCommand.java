package es.imim.bg.ztools.zcalc.ui.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import javax.swing.JFileChooser;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.model.Results;
import es.imim.bg.ztools.resources.ResultsFile;
import es.imim.bg.ztools.zcalc.ui.AppView;
import es.imim.bg.ztools.zcalc.ui.utils.Options;
import es.imim.bg.ztools.zcalc.ui.views.ResultsView;

public class OpenAnalysisCommand implements Command {

	public static OpenAnalysisCommand create(AppView appView) {
		return new OpenAnalysisCommand(appView, null);
	}
	
	public static OpenAnalysisCommand create(AppView appView, File path) {
		return new OpenAnalysisCommand(appView, path);
	}
	
	private AppView appView;
	private File path;
	
	public OpenAnalysisCommand(AppView appView, File path) {
		this.appView = appView;
		this.path = path;
	}
	
	@Override
	public void execute() {
		
		File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			// TODO: Load analysis data
			File resultsFile = new File(selectedPath, "results.csv"); // FIXME
																		// results
																		// .csv
																		// from
																		// a
																		// constant
			ResultsFile rf = new ResultsFile(resultsFile);
			try {
				appView.setStatusText(
						"Reading " + rf.getResourcePath() + "...");

				Results results = rf.read();
				Analysis analysis = new Analysis();
				analysis.setResults(results); // TEMP

				appView.setStatusText("Initializing view...");

				ResultsView view = new ResultsView(results);

				view.setName(rf.getResourcePath()); // FIXME: use analysis name

				appView.addWorkspaceView(view);

				appView.setStatusText("Ok");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
			// refresh();
		}
	}
	
	private File getSelectedPath() {
		
		if (path != null)
			return path;
		
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the analysis folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));
		//fileChooser.addChoosableFileFilter(fileFilter);
		
		int retval = fileChooser.showOpenDialog(appView);
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}
