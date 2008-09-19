package es.imim.bg.ztools.ui.commands;

import java.io.File;

import javax.swing.JFileChooser;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.TabAnalysisResource;
import es.imim.bg.ztools.ui.AppView;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AnalysisView;

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

			try {
				AnalysisResource analysisRes =
					new TabAnalysisResource(selectedPath.getAbsolutePath());
				
				Analysis analysis = analysisRes.load();
				
				appView.setStatusText(
						"Reading " + selectedPath.getAbsolutePath() + "...");

				appView.setStatusText("Initializing view...");

				AnalysisView view = new AnalysisView(analysis);

				view.setName(analysis.getName());

				appView.addWorkspaceView(view);

				appView.setStatusText("Ok");

			} catch (Exception e) {
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
