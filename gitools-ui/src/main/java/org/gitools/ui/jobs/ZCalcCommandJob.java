package org.gitools.ui.jobs;

import java.io.File;

import org.gitools.ui.platform.AppFrame;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.htest.enrichment.EnrichmentCommand;

public class ZCalcCommandJob implements Job {
	
	EnrichmentCommand command;
	IProgressMonitor monitor;
	File analysisPath;
	
	public ZCalcCommandJob(
			EnrichmentCommand command,
			IProgressMonitor monitor, 
			File analysisPath) {
		
		this.command = command;
		this.monitor = monitor;
		this.analysisPath = analysisPath;
	}

	@Override
	public void run() {
		try {
	        AppFrame.instance()
	        	.setStatusText("Executing analysis...");
	        
			command.run(monitor);

			new OpenAnalysisJob(analysisPath, monitor).run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
