package org.gitools.ui.jobs;

import java.io.File;

import org.gitools.ui.AppFrame;

import edu.upf.bg.progressmonitor.ProgressMonitor;
import org.gitools.commands.ZCalcCommand;

public class ZCalcCommandJob implements Job {
	
	ZCalcCommand command;
	ProgressMonitor monitor;
	File analysisPath;
	
	public ZCalcCommandJob(
			ZCalcCommand command, 
			ProgressMonitor monitor, 
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
