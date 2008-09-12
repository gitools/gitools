package es.imim.bg.progressmonitor;

public interface ProgressMonitor {
	
	public void begin(String title, int totalWork);

	public void title(String title);
	
	public void worked(int workInc);
	
	public boolean isCancelled();
	
	public void end();
	
	public int getLevel();
	
	public void setLevel(int level);
	
	public ProgressMonitor subtask();

	public void info(String msg);
	public void debug(String msg);
}
