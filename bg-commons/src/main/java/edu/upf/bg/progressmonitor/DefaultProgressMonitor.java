package edu.upf.bg.progressmonitor;

import edu.upf.bg.progressmonitor.DefaultProgressMonitor;
import edu.upf.bg.progressmonitor.IProgressMonitor;

public class DefaultProgressMonitor implements IProgressMonitor {

	protected String title;
	protected int totalWork;
	protected int worked;
	protected int level;
	
	protected IProgressMonitor parent;
	
	public DefaultProgressMonitor() {
		title = "";
		totalWork = worked = level = 0;
	}
	
	public DefaultProgressMonitor(IProgressMonitor parent) {
		this.parent = parent;
	}

	public void begin(String title, int totalWork) {
		this.title = title;
		this.totalWork = totalWork;
		this.worked = 0;
	}

	public void title(String title) {
		this.title = title;
	}

	public void worked(int workInc) {
		worked += workInc;
		if (worked > totalWork)
			worked = totalWork;
	}
	
	public boolean isCancelled() {
		return false;
	}
	
	public void end() {
		worked = totalWork;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public IProgressMonitor subtask() {
		return new DefaultProgressMonitor(this);
	}

	@Override
	public void debug(String msg) {}

	@Override
	public void info(String msg) {}
}
