package es.imim.bg.ztools.command;

import java.io.IOException;
import java.util.zip.DataFormatException;

import es.imim.bg.progressmonitor.ProgressMonitor;

public interface Command {

	public void run(ProgressMonitor monitor) 
		throws IOException, DataFormatException, InterruptedException;
}
