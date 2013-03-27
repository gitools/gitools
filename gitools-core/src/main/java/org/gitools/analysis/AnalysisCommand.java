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
import org.gitools.datafilters.BinaryCutoff;
import org.gitools.datafilters.BinaryCutoffTranslator;
import org.gitools.datafilters.DoubleTranslator;
import org.gitools.datafilters.ValueTranslator;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.persistence.*;

import java.util.Properties;

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

    protected BaseMatrix loadDataMatrix(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        return loadDataMatrix(resourceLocator, new Properties(), progressMonitor);
    }

    @Deprecated
	protected BaseMatrix loadDataMatrix(IResourceLocator resourceLocator, Properties props, IProgressMonitor progressMonitor) throws PersistenceException {

        IResource resource = PersistenceManager.get().load(resourceLocator, IResource.class, props, progressMonitor);

		BaseMatrix matrix = null;
		if (resource instanceof BaseMatrix)
			matrix = (BaseMatrix) resource;
		else if (resource instanceof ModuleMap)
			matrix = MatrixUtils.moduleMapToMatrix((ModuleMap) resource);
		else
			throw new PersistenceException("Invalid data format for '" + resourceLocator.getURL() + "'");

		return matrix;
	}

	protected ModuleMap loadModuleMap(IResourceLocator resourceLocator, Properties props, IProgressMonitor monitor) throws PersistenceException {

        IResource resource = PersistenceManager.get().load(resourceLocator, IResource.class, props, monitor);

		ModuleMap moduleMap = null;
		if (resource instanceof BaseMatrix)
			moduleMap = MatrixUtils.matrixToModuleMap((BaseMatrix) resource);
		else if (resource instanceof ModuleMap)
			moduleMap = (ModuleMap) resource;
		else
			throw new PersistenceException("Invalid data format for '" + resourceLocator.getURL() + "'");

		return moduleMap;
	}

	protected ValueTranslator createValueTranslator(
			boolean binaryCutoffEnabled, CutoffCmp cutoffCmp, Double cutoffValue) {
		
		return binaryCutoffEnabled ?
			new BinaryCutoffTranslator(new BinaryCutoff(cutoffCmp, cutoffValue)) :
			new DoubleTranslator();
	}
}
