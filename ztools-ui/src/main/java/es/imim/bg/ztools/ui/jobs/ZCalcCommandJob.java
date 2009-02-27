package es.imim.bg.ztools.ui.jobs;

import java.io.File;
import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;
import es.imim.bg.ztools.commands.ZCalcCommand;
import es.imim.bg.ztools.ui.AppFrame;

public class ZCalcCommandJob implements Job {
	
	ZCalcCommand command;
	ProgressMonitor monitor;
	File newAnalysis;
	
	
	public ZCalcCommandJob(ZCalcCommand command, ProgressMonitor monitor, File newAnalysis) {
		this.command = command;
		this.monitor = monitor;
		this.newAnalysis = newAnalysis;
	}

	@Override
	public void run() {
		try {
	        AppFrame.instance()
	        	.setStatusText("Executing analysis");
			command.run(monitor);
			
			// open new analysis
			AppFrame.instance().getJobProcessor().addJob(
					new OpenAnalysisJob(newAnalysis, monitor) {
				});	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
