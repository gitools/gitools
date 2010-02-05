package edu.upf.bg.progressmonitor;

public interface IProgressMonitor {
	
	public void begin(String title, int totalWork);

	public void title(String title);
	
	public void worked(int workInc);

	public void cancel();
	
	public boolean isCancelled();
	
	public void end();
	
	public int getLevel();
	
	public void setLevel(int level);

	//public int getWorked();
	
	public IProgressMonitor subtask();

	public void info(String msg);
	public void debug(String msg);

	public void exception(Throwable cause);
}
