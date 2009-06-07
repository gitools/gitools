package edu.upf.bg.progressmonitor;

import edu.upf.bg.progressmonitor.NullProgressMonitor;
import edu.upf.bg.progressmonitor.ProgressMonitor;

public class NullProgressMonitor implements ProgressMonitor {

	public void begin(String title, int totalWork) {}

	public void end() {}

	public int getLevel() {	return 0; }

	public boolean isCancelled() { return false; }

	public void setLevel(int level) {}

	public ProgressMonitor subtask() {
		return new NullProgressMonitor();
	}

	public void title(String title) {}

	public void worked(int workInc) {}

	public void debug(String msg) {}

	public void info(String msg) {}

}
