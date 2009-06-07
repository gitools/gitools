package org.gitools.commands;

import edu.upf.bg.progressmonitor.ProgressMonitor;

public interface Command {

	public void run(ProgressMonitor monitor) 
		throws Exception;
}
