package org.gitools.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;

public interface AnalysisCommand {

	public void run(IProgressMonitor monitor) throws AnalysisException;
}
