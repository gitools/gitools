package org.gitools.persistence;

import edu.upf.bg.progressmonitor.ProgressMonitor;

public interface IPersistenceManager {

	Object read(IResource resource, ProgressMonitor monitor);
}
