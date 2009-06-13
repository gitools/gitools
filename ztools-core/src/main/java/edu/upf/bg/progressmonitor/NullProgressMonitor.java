package edu.upf.bg.progressmonitor;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class NullProgressMonitor implements IProgressMonitor {

	public void begin(String title, int totalWork) {}

	public void end() {}

	public int getLevel() {	return 0; }

	public boolean isCancelled() { return false; }

	public void setLevel(int level) {}

	public IProgressMonitor subtask() {
		return new NullProgressMonitor();
	}

	public void title(String title) {}

	public void worked(int workInc) {}

	public void debug(String msg) {}

	public void info(String msg) {}

}
