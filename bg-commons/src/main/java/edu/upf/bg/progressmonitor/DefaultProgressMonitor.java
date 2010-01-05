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

	@Override
	public void begin(String title, int totalWork) {
		this.title = title;
		this.totalWork = totalWork;
		this.worked = 0;
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
		return false;
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

	@Override
	public IProgressMonitor subtask() {
		return new DefaultProgressMonitor(this);
	}

	@Override
	public void debug(String msg) {}

	@Override
	public void info(String msg) {}
}
