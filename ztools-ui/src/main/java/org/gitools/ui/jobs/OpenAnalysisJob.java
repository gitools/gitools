package org.gitools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.ui.AppFrame;
import org.gitools.ui.editor.analysis.AnalysisEditor;

import edu.upf.bg.progressmonitor.ProgressMonitor;

import org.gitools.model.analysis.Analysis;
import org.gitools.resources.analysis.AnalysisResource;
import org.gitools.resources.analysis.CsvAnalysisResource;

public class OpenAnalysisJob implements Job {

	private File selectedPath;
	private ProgressMonitor monitor;
	
	public OpenAnalysisJob(
			File selectedPath, ProgressMonitor monitor) {
		
		this.selectedPath = selectedPath;
		this.monitor = monitor;
	}
	
	@Override
	public void run() {
        AppFrame.instance()
        	.setStatusText("Opening analysis...");
        
		openAnalysisJob(selectedPath, monitor);
	}
	
	private void openAnalysisJob(
			File selectedPath, ProgressMonitor monitor) {
		
		if (selectedPath == null)
			return;
		
		try {
			//ProjectResource projRes = new ProjectResource(selectedPath);
			//Project proj = projRes.load(monitor);
			
			AnalysisResource analysisRes =
				new CsvAnalysisResource(selectedPath.getAbsolutePath());
			
			monitor.begin("Loading analysis ...", 1);
			Analysis analysis = analysisRes.load(monitor);

			final AnalysisEditor view = new AnalysisEditor(analysis);
			
			view.setName(analysis.getName());
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getWorkspace().addEditor(view);
					AppFrame.instance().refresh();
				}
			});
			
			monitor.end();
			
			AppFrame.instance()
        		.setStatusText("Done.");
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
