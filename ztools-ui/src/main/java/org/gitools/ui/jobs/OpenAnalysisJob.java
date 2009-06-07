package org.gitools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.ui.AppFrame;
import org.gitools.ui.views.analysis.AnalysisView;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.CsvAnalysisResource;

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

			final AnalysisView view = new AnalysisView(analysis);
			
			view.setName(analysis.getName());
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getWorkspace().addView(view);
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
