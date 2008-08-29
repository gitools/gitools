package es.imim.bg.ztools.analysis;

import java.util.Date;

public abstract class Analysis {

	protected String name;
	protected Date startTime;
	protected long elapsedTime;
	
	public String getName() {
		return name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}
	
}
