package org.gitools.ui.commands;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;

import org.gitools.model.Analysis;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.analysis.CsvAnalysisResource;

@Deprecated
public class OpenAnalysisCommand implements Command {
	
	public static OpenAnalysisCommand create(File path) {
		return new OpenAnalysisCommand(path);
	}
	
	private File selectedPath;
	
	private Analysis analysis;
	
	public OpenAnalysisCommand(File path) {
		this.selectedPath = path;
	}
	
	@Override
	public void execute(IProgressMonitor monitor) throws CommandException {
		
		File selectedPath = getSelectedPath();
		
		if (selectedPath != null) {
			AnalysisPersistence analysisRes =
				new CsvAnalysisResource(selectedPath.getAbsolutePath());
			
			monitor.begin("Loading analysis from " + selectedPath.getAbsolutePath(), 1);
			try {
				analysis = analysisRes.load(monitor);
				
				/*final AnalysisView view = 
					new AnalysisView(
						new AnalysisModel(analysis));

				view.setName(analysis.getName());

				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						AppFrame.instance().getWorkspace().addView(view);
						AppFrame.instance().refresh();
					}
				});*/
				
			} catch (Exception e) {
				throw new CommandException(e);
			}
			monitor.end();
		}
	}
	
	private File getSelectedPath() {
		return selectedPath;
	}

	public void setSelectedPath(File selectedPath) {
		this.selectedPath = selectedPath;
	}
	
	public Analysis getAnalysis() {
		return analysis;
	}
}
