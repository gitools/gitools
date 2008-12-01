package es.imim.bg.ztools.ui.jobs;

import es.imim.bg.ztools.ui.AppFrame;
import es.imim.bg.ztools.ui.commands.Command;

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
