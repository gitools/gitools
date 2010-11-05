package org.gitools.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.Properties;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;

public abstract class AnalysisCommand {

	protected String workdir;

	protected String fileName;

	public AnalysisCommand(String workdir, String fileName) {
		this.workdir = workdir;
		this.fileName = fileName;
	}

	public String getWorkdir() {
		return workdir;
	}

	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public abstract void run(IProgressMonitor monitor) throws AnalysisException;

	protected BaseMatrix loadDataMatrix(
			File file, String mime,
			Properties props, IProgressMonitor monitor) throws PersistenceException {

		Object obj = PersistenceManager.getDefault()
				.load(file, mime, props, monitor);

		BaseMatrix matrix = null;
		if (obj instanceof BaseMatrix)
			matrix = (BaseMatrix) obj;
		else if (obj instanceof ModuleMap)
			matrix = MatrixUtils.moduleMapToMatrix((ModuleMap) obj);
		else
			throw new PersistenceException("Invalid MIME type for data: " + mime);

		return matrix;
	}

	protected ModuleMap loadModuleMap(
			File file, String mime,
			Properties props, IProgressMonitor monitor) throws PersistenceException {

		Object obj = PersistenceManager.getDefault()
				.load(file, mime, props, monitor);

		ModuleMap moduleMap = null;
		if (obj instanceof BaseMatrix)
			moduleMap = MatrixUtils.matrixToModuleMap((BaseMatrix) obj);
		else if (obj instanceof ModuleMap)
			moduleMap = (ModuleMap) obj;
		else
			throw new PersistenceException("Invalid MIME type for modules: " + mime);

		return moduleMap;
	}
}
