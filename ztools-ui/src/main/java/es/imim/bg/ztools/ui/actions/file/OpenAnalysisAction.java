package es.imim.bg.ztools.ui.actions.file;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.CsvAnalysisResource;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.IconNames;
import es.imim.bg.ztools.ui.actions.BaseAction;
import es.imim.bg.ztools.ui.jobs.Job;
import es.imim.bg.ztools.ui.model.deprecated.AnalysisModel;
import es.imim.bg.ztools.ui.utils.Options;
import es.imim.bg.ztools.ui.views.analysis.AnalysisView;

public class OpenAnalysisAction extends BaseAction {

	private static final long serialVersionUID = -6528634034161710370L;

	public OpenAnalysisAction() {
		super("Open analysis...");
		setDesc("Open an analysis from the file system");
		setSmallIconFromResource(IconNames.open16);
		setLargeIconFromResource(IconNames.open24);
		setMnemonic(KeyEvent.VK_O);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			Options.instance().setLastPath(selectedPath.getParent());
			Options.instance().save();

			final ProgressMonitor monitor = createProgressMonitor();
			
			AppFrame.instance().getJobProcessor().addJob(new Job() {
					@Override
					public void run() {
						openAnalysisJob(selectedPath, monitor);
					}
				});
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
	
	private void openAnalysisJob(
			File selectedPath, ProgressMonitor monitor) {
		
		if (selectedPath == null)
			return;
		
		try {
			AnalysisResource analysisRes =
				new CsvAnalysisResource(selectedPath.getAbsolutePath());
			
			monitor.begin("Loading analysis from " + selectedPath.getAbsolutePath(), 1);
			Analysis analysis = analysisRes.load(monitor);
				
			final AnalysisView view = 
				new AnalysisView(
					new AnalysisModel(analysis));

			view.setName(analysis.getName());

			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getWorkspace().addView(view);
					AppFrame.instance().refresh();
				}
			});
			
			monitor.end();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance()
						.setStatusText("Error loading analysis.");
				}
			});
		}
	}
}
