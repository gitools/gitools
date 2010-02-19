package org.gitools.ui._DEPRECATED.jobs;

import org.gitools.ui.commands.Command;
import org.gitools.ui.platform.AppFrame;

public class CommandJob implements Job {

	protected Command cmd;
	
	public CommandJob(Command cmd) {
		this.cmd = cmd;
	}
	
	@Override
	public void run() {
		try {
			cmd.execute(
					AppFrame.instance().createMonitor());
		} catch (Exception e) {
			e.printStackTrace(); //FIXME
		}
	}

}
