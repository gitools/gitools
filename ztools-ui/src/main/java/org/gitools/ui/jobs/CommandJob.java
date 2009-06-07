package org.gitools.ui.jobs;

import org.gitools.ui.AppFrame;
import org.gitools.ui.commands.Command;

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
