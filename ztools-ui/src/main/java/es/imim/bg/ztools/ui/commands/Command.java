package es.imim.bg.ztools.ui.commands;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface Command {

	public class CommandException extends Exception {
		private static final long serialVersionUID = 2147640402258540409L;
		
		public CommandException(Exception e) {
			super(e);
		}
		public CommandException(String msg) {
			super(msg);
		}
		public CommandException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
	
	void execute(ProgressMonitor monitor) throws CommandException;
}
