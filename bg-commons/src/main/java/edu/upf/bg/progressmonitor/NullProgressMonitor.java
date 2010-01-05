package edu.upf.bg.progressmonitor;

public class NullProgressMonitor implements IProgressMonitor {

	@Override
	public void begin(String title, int totalWork) {}

	@Override
	public void end() {}

	@Override
	public int getLevel() {	return 0; }

	@Override
	public boolean isCancelled() { return false; }

	@Override
	public void setLevel(int level) {}

	@Override
	public IProgressMonitor subtask() {
		return new NullProgressMonitor();
	}

	@Override
	public void title(String title) {}

	@Override
	public void worked(int workInc) {}

	@Override
	public void debug(String msg) {}

	@Override
	public void info(String msg) {}

}
