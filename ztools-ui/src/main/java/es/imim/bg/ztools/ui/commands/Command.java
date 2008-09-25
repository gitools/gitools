package es.imim.bg.ztools.ui.commands;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface Command {

	void execute(ProgressMonitor monitor) throws Exception;
}
