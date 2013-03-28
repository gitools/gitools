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

package org.gitools.persistence.formats.analysis;

import edu.upf.bg.progressmonitor.IProgressMonitor;
import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.*;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class OverlappingAnalysisXmlFormat extends AbstractXmlFormat<OverlappingAnalysis> {

    public OverlappingAnalysisXmlFormat() {
        super(FileSuffixes.OVERLAPPING, MimeTypes.OVERLAPPING_ANALYSIS, OverlappingAnalysis.class);
    }

    @Override
    protected void afterRead(IResourceLocator resourceLocator, OverlappingAnalysis resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        if (isLoadReferences()) {
            // Load filter data matrix
            IResourceLocator dataLocator = resourceLocator.getReferenceLocator(resource.getFilteredDataResource().getPath());
            resource.setData(
                    convertToMatrix(
                            PersistenceManager.get().load(dataLocator, IResource.class, progressMonitor)
                    ));

            // Load results matrix
            IResourceLocator resultsLocator = resourceLocator.getReferenceLocator(resource.getCellResultsResource().getPath());
            resource.setCellResults(
                    convertToMatrix(
                            PersistenceManager.get().load(resultsLocator, IResource.class, progressMonitor)
                    ));
        }

    }

    private BaseMatrix convertToMatrix(IResource resource) throws PersistenceException {

        BaseMatrix matrix = null;
        if (resource instanceof BaseMatrix)
            matrix = (BaseMatrix) resource;
        else if (resource instanceof ModuleMap)
            matrix = MatrixUtils.moduleMapToMatrix((ModuleMap) resource);
        else
            throw new PersistenceException("Invalid MIME type");

        return matrix;
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, OverlappingAnalysis resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        String baseName = resourceLocator.getBaseName();
        PersistenceManager pm = PersistenceManager.get();

        if (resource.isBinaryCutoffEnabled()) {

            IMatrix data = resource.getData();
            String dataExtension = pm.getDefaultExtension(resource.getData());
            String dataPath = baseName + "-data." + dataExtension + ".gz";
            IResourceLocator dataLocator = resourceLocator.getReferenceLocator(dataPath);

            pm.store(dataLocator, data, progressMonitor);

            if (resource.getFilteredDataResource() == null) {
                resource.setFilteredDataResource(new ResourceRef(null, dataPath));
            }

        } else {
            resource.setFilteredDataResource(resource.getSourceDataResource());
        }

        IMatrix results = resource.getResult();
        String resultsExtension = pm.getDefaultExtension(results);
        String resultsPath = baseName + "-results-cells." + resultsExtension + ".gz";
        IResourceLocator resultsLocator = resourceLocator.getReferenceLocator(resultsPath);

        if (resource.getCellResultsResource() == null) {
            resource.setCellResultsResource(new ResourceRef(null, resultsPath));
        }

        pm.store(resultsLocator, results, progressMonitor);
    }


}
