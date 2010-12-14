/*
 *  Copyright 2010 Universitat Pompeu Fabra.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.gitools.analysis;

import edu.upf.bg.cutoffcmp.CutoffCmp;
import edu.upf.bg.progressmonitor.IProgressMonitor;
import java.io.File;
import java.util.Properties;
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.BinaryCutoffTranslator;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;

public abstract class AnalysisCommand {

	protected String workdir;

	protected String fileName;

	protected boolean storeAnalysis;
	
	public AnalysisCommand(String workdir, String fileName) {
		this.workdir = workdir;
		this.fileName = fileName;
		this.storeAnalysis = true;
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

	public boolean isStoreAnalysis() {
		return storeAnalysis;
	}

	public void setStoreAnalysis(boolean storeAnalysis) {
		this.storeAnalysis = storeAnalysis;
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

	protected ValueTranslator createValueTranslator(
			boolean binaryCutoffEnabled, CutoffCmp cutoffCmp, Double cutoffValue) {
		
		return binaryCutoffEnabled ?
			new BinaryCutoffTranslator(new BinaryCutoff(cutoffCmp, cutoffValue)) :
			new DoubleTranslator();
	}
}
