package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.TabAnalysisResource;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.commands.OpenAnalysisCommand;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.AnalysisView;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction(AppFrame app) {
		super(app, "Open analysis...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			try {
				app.setStatusText(
						"Reading " + selectedPath.getAbsolutePath() + "...");
				
				OpenAnalysisCommand cmd = 
					OpenAnalysisCommand.create(selectedPath);
				
				cmd.execute(app.createMonitor());

				app.setStatusText("Initializing view...");

				Analysis analysis = cmd.getAnalysis();
				
				AnalysisView view = new AnalysisView(analysis);

				view.setName(analysis.getName());

				app.addWorkspaceView(view);

				app.setStatusText("Ok");

			} catch (Exception ex) {
				ex.printStackTrace();
				app.setStatusText("Error loading analysis.");
			}
			app.refresh();
		}
	}
	
	private File getSelectedPath() {
		JFileChooser fileChooser = new JFileChooser(
				Options.instance().getLastPath());
		
		fileChooser.setDialogTitle("Select the analysis folder");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//FileFilter fileFilter = new FileFilter(LangKey.XMI);
		//fileFilter.setDescription(LangManager.instance().getString(LangKey.XMI_FILES));
		//fileChooser.addChoosableFileFilter(fileFilter);
		
		int retval = fileChooser.showOpenDialog(app);
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}
