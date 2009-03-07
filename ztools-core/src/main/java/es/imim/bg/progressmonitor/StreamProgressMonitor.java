package es.imim.bg.progressmonitor;

import java.io.PrintStream;

import cern.colt.Timer;

public class StreamProgressMonitor extends DefaultProgressMonitor {

	private static final String indentString = "  ";
	
	protected PrintStream out;
	
	protected Timer timer;
	protected int lastprogress;
	
	protected String tabs;
	protected boolean flag;
	
	protected boolean verbose;
	protected boolean debug;
	
	public StreamProgressMonitor(ProgressMonitor parent, PrintStream out, boolean verbose, boolean debug) {
		super(parent);
		this.out = out;
		this.timer = new Timer();
		this.lastprogress = 0;
		this.flag = false;
		this.tabs = "";
		this.verbose = verbose;
		this.debug = debug;
	}
	
	public StreamProgressMonitor(PrintStream out, boolean verbose, boolean debug) {
		this(null, out, verbose, debug);
	}
	
	@Override
	public void begin(String title, int totalWork) {
		super.begin(title, totalWork);
		flag = false;
		tabs = tabbulate(level);
		print("\n" + tabs + title);
		timer.start();
	}
	
	@Override
	public void title(String title) {
		super.title(title);
		print("\n" + tabs + title);
	}
	
	@Override
	public void worked(int workInc) {
		super.worked(workInc);
		/*int progress = (worked * 100 / totalWork);
		if (lastprogress != progress) {
			lastprogress = progress;
			out.println(tabbulate(title + " " + progress + "%", level));
		}*/
	}
	
	@Override
	public ProgressMonitor subtask() {
		ProgressMonitor subtask = 
			createSubtaskMonitor(this, out, verbose, debug);
		subtask.setLevel(level + 1);
		flag = true;
		return subtask;
	}
	
	protected ProgressMonitor createSubtaskMonitor(
			ProgressMonitor parentMonitor, 
			PrintStream out,
			boolean verbose, 
			boolean debug) {
		
		return new StreamProgressMonitor(
				parentMonitor, out, verbose, debug);
	}

	@Override
	public void end() {
		super.end();
		
		double millis = timer.millis();
		double secs = timer.seconds();
		double mins = timer.minutes();
		
		String time = "";
		if (millis < 1000)
			time = Double.toString(millis) + " millisecs";
		else if (secs < 60)
			time = Double.toString(secs) + " secs";
		else
			time = Double.toString(mins) + " mins";
		
		if (flag) {
			print("\n" + tabs + title + " " + time);
		}
		else
			print(" " + time);
	}
	
	public void info(String msg) {
		if (verbose)
			log(msg);
	}
	
	public void debug(String msg) {
		if (debug)
			log(msg);
	}
	
	private void log(String msg) {
		print("\n" + tabs + indentString + msg);
		flag = true;
	}
	
	protected void print(String text) {
		out.print(text);
	}
	
	private String tabbulate(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++)
			sb.append(indentString);
		return sb.toString();
	}
}
