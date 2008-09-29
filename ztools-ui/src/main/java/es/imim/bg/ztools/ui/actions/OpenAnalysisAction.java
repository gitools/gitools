package es.imim.bg.ztools.ui.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.commands.OpenAnalysisCommand;
import es.imim.bg.ztools.ui.model.AnalysisModel;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction() {
		super("Open analysis...");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			ProgressMonitor monitor = createProgressMonitor();
			
			try {
				OpenAnalysisCommand cmd = 
					OpenAnalysisCommand.create(selectedPath);
				
				cmd.execute(monitor);

				Analysis analysis = cmd.getAnalysis();
				
				AnalysisView view = 
					new AnalysisView(
						new AnalysisModel(analysis));

				view.setName(analysis.getName());

				AppFrame.instance().getWorkspace().addView(view);
				AppFrame.instance().refresh();

			} catch (Exception ex) {
				ex.printStackTrace();
				AppFrame.instance()
					.setStatusText("Error loading analysis.");
			}
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
		
		int retval = fileChooser.showOpenDialog(AppFrame.instance());
		if (retval == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		
		return null;
	}
}
