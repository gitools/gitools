package org.gitools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import org.gitools.ui.editor.analysis.AnalysisEditor;
import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.HtestAnalysis;

import org.gitools.model.Analysis;
import org.gitools.persistence.AnalysisPersistence;
import org.gitools.persistence.analysis.CsvAnalysisResource;

public class OpenAnalysisJob implements Job {

	private File selectedPath;
	private IProgressMonitor monitor;
	
	public OpenAnalysisJob(
			File selectedPath, IProgressMonitor monitor) {
		
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
			File selectedPath, IProgressMonitor monitor) {
		
		if (selectedPath == null)
			return;
		
		try {
			//ProjectResource projRes = new ProjectResource(selectedPath);
			//Project proj = projRes.load(monitor);
			
			AnalysisPersistence analysisPer =
				new CsvAnalysisResource(selectedPath.getAbsolutePath());
			
			monitor.begin("Loading analysis ...", 1);
			HtestAnalysis analysis = analysisPer.load(monitor);

			final AnalysisEditor editor = new AnalysisEditor(analysis);
			
			editor.setName(analysis.getTitle());
			
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					AppFrame.instance().getEditorsPanel().addEditor(editor);
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
