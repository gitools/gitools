package es.imim.bg.ztools.ui.jobs;

import java.io.File;

import javax.swing.SwingUtilities;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.model.Analysis;
import es.imim.bg.ztools.resources.analysis.AnalysisResource;
import es.imim.bg.ztools.resources.analysis.CsvAnalysisResource;
import es.imim.bg.ztools.table.ITable;
import es.imim.bg.ztools.table.ResultsMatrixTableContentsAdapter;
import es.imim.bg.ztools.table.Table;
import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.views.table.TableView;

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
			
			ITable table = new Table(
					new ResultsMatrixTableContentsAdapter(analysis.getResults()));
			
			final TableView view = new TableView(table);
			
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
