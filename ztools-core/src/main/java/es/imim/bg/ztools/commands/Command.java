package es.imim.bg.ztools.commands;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface Command {

	public void run(ProgressMonitor monitor) 
		throws Exception;
}
