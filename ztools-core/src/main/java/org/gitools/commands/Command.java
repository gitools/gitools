package org.gitools.commands;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface Command {

	public void run(IProgressMonitor monitor) 
		throws Exception;
}
