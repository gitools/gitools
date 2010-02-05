package edu.upf.bg.progressmonitor;

public class DefaultProgressMonitor implements IProgressMonitor {

	protected String title;
	protected int totalWork;
	protected int worked;
	protected int level;
	protected boolean cancelled;

	protected Throwable cause;
	
	protected IProgressMonitor parent;
	
	public DefaultProgressMonitor() {
		title = "";
		totalWork = worked = level = 0;
		cancelled = false;
	}
	
	public DefaultProgressMonitor(IProgressMonitor parent) {
		this.parent = parent;
	}

	@Override
	public void begin(String title, int totalWork) {
		this.title = title;
		this.totalWork = totalWork;
		this.worked = 0;
		this.cancelled = false;
	}

	@Override
	public void title(String title) {
		this.title = title;
	}

	@Override
	public void worked(int workInc) {
		worked += workInc;
		if (worked > totalWork)
			worked = totalWork;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void cancel() {
		this.cancelled = true;
	}
	
	@Override
	public void end() {
		worked = totalWork;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}

	//@Override
	public int getWorked() {
		return worked;
	}

	public int getTotalWork() {
		return totalWork;
	}
	
	@Override
	public IProgressMonitor subtask() {
		return new DefaultProgressMonitor(this);
	}

	@Override
	public void debug(String msg) {}

	@Override
	public void info(String msg) {}

	@Override
	public void exception(Throwable cause) {
		this.cause = cause;
		this.cancelled = true;
	}

	public Throwable getCause() {
		return cause;
	}
}
