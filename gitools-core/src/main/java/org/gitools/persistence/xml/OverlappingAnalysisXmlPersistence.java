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

package org.gitools.persistence.xml;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.*;
import org.gitools.persistence.xml.adapter.ResourceRefXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;

public class OverlappingAnalysisXmlPersistence
        extends AbstractXmlPersistence<OverlappingAnalysis> {

    public OverlappingAnalysisXmlPersistence() {
        super(OverlappingAnalysis.class);

        setPersistenceTitle("overlapping analysis");
    }

    @Override
    protected XmlAdapter<?, ?>[] createAdapters() {
        PersistenceContext context = getPersistenceContext();
        return new XmlAdapter<?, ?>[]{
                new ResourceRefXmlAdapter(context)};
    }

    @Override
    protected void beforeRead(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        String basePath = PersistenceUtils.getBasePath(resourceLocator);

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(basePath);
        context.setProgressMonitor(progressMonitor);
    }

    @Override
    protected void afterRead(IResourceLocator resourceLocator, OverlappingAnalysis resource, IProgressMonitor progressMonitor) throws PersistenceException {
        String basePath = PersistenceUtils.getBasePath(resourceLocator);

        PersistenceContext context = getPersistenceContext();

        IMatrix data = null;
        if (context.isLoadReferences())
            data = loadData(basePath, resource.getFilteredDataResource(), progressMonitor);
        resource.setData(data);

        IMatrix cellResults = null;
        if (context.isLoadReferences())
            cellResults = (IMatrix) load(basePath, resource.getCellResultsResource(), progressMonitor);
        resource.setCellResults(cellResults);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, OverlappingAnalysis resource, IProgressMonitor progressMonitor) throws PersistenceException {

        String basePath = PersistenceUtils.getBasePath(resourceLocator);
        String baseName = PersistenceUtils.getFileName(resourceLocator.getName());

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(basePath);
        context.setProgressMonitor(progressMonitor);

        PersistenceManager pm = getPersistenceManager();

        if (resource.isBinaryCutoffEnabled()) {
            String dataExt = pm.getExtensionFromEntity(resource.getData());
            File dataFile = new File(basePath, baseName + "-data." + dataExt + ".gz");
            if (resource.getFilteredDataResource() == null)
                resource.setFilteredDataResource(new ResourceRef(
                        PersistenceManager.getDefault().getMimeFromEntity(resource.getData().getClass()),
                        dataFile.getAbsolutePath()));

            PersistenceManager.getDefault().store(dataFile, resource.getData(), progressMonitor);
        } else
            resource.setFilteredDataResource(resource.getSourceDataResource());

        String cellResultsExt = pm.getExtensionFromEntity(resource.getResult());
        File cellResultsFile = new File(basePath, baseName + "-results-cells." + cellResultsExt + ".gz");
        if (resource.getCellResultsResource() == null)
            resource.setCellResultsResource(new ResourceRef(
                    PersistenceManager.getDefault().getMimeFromEntity(resource.getCellResults().getClass()),
                    cellResultsFile.getAbsolutePath()));

        PersistenceManager.getDefault().store(cellResultsFile, resource.getCellResults(), progressMonitor);
    }

    private Object load(String basePath, ResourceRef resourceRef, IProgressMonitor monitor) throws PersistenceException {
        if (resourceRef == null)
            return null;

        String path = resourceRef.getPath();
        boolean absolute = PersistenceUtils.isAbsolute(path);

        File file = absolute ?
                new File(path) : new File(basePath, path);

        String mimeType = resourceRef.getMime();
        if (mimeType == null)
            mimeType = PersistenceManager.getDefault().getMimeFromFile(file.getName());

        Object entity = PersistenceManager.getDefault().load(file, mimeType, monitor);

        return entity;
    }

    private BaseMatrix loadData(String basePath, ResourceRef resourceRef, IProgressMonitor monitor) throws PersistenceException {
        Object obj = load(basePath, resourceRef, monitor);

        BaseMatrix matrix = null;
        if (obj instanceof BaseMatrix)
            matrix = (BaseMatrix) obj;
        else if (obj instanceof ModuleMap)
            matrix = MatrixUtils.moduleMapToMatrix((ModuleMap) obj);
        else
            throw new PersistenceException("Invalid MIME type for data: " + resourceRef.getMime());

        return matrix;
    }
}
