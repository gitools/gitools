/*
 * #%L
 * gitools-core
 * %%
 * Copyright (C) 2013 Universitat Pompeu Fabra - Biomedical Genomics group
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.gitools.persistence.formats.analysis;

import org.gitools.analysis.overlapping.OverlappingAnalysis;
import org.gitools.matrix.MatrixUtils;
import org.gitools.matrix.model.BaseMatrix;
import org.gitools.matrix.model.IMatrix;
import org.gitools.model.ModuleMap;
import org.gitools.model.ResourceRef;
import org.gitools.persistence.IResource;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceManager;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class OverlappingAnalysisXmlFormat extends AbstractXmlFormat<OverlappingAnalysis>
{

    public OverlappingAnalysisXmlFormat()
    {
        super(FileSuffixes.OVERLAPPING, MimeTypes.OVERLAPPING_ANALYSIS, OverlappingAnalysis.class);
    }

    @Override
    protected void afterRead(IResourceLocator resourceLocator, OverlappingAnalysis resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {

        if (isLoadReferences())
        {
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

    private BaseMatrix convertToMatrix(IResource resource) throws PersistenceException
    {

        BaseMatrix matrix = null;
        if (resource instanceof BaseMatrix)
        {
            matrix = (BaseMatrix) resource;
        }
        else if (resource instanceof ModuleMap)
        {
            matrix = MatrixUtils.moduleMapToMatrix((ModuleMap) resource);
        }
        else
        {
            throw new PersistenceException("Invalid MIME type");
        }

        return matrix;
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, OverlappingAnalysis resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {

        String baseName = resourceLocator.getBaseName();
        PersistenceManager pm = PersistenceManager.get();

        if (resource.isBinaryCutoffEnabled())
        {

            IMatrix data = resource.getData();
            String dataExtension = pm.getDefaultExtension(resource.getData());
            String dataPath = baseName + "-data." + dataExtension + ".gz";
            IResourceLocator dataLocator = resourceLocator.getReferenceLocator(dataPath);

            pm.store(dataLocator, data, progressMonitor);

            if (resource.getFilteredDataResource() == null)
            {
                resource.setFilteredDataResource(new ResourceRef(null, dataPath));
            }

        }
        else
        {
            resource.setFilteredDataResource(resource.getSourceDataResource());
        }

        IMatrix results = resource.getResult();
        String resultsExtension = pm.getDefaultExtension(results);
        String resultsPath = baseName + "-results-cells." + resultsExtension + ".gz";
        IResourceLocator resultsLocator = resourceLocator.getReferenceLocator(resultsPath);

        if (resource.getCellResultsResource() == null)
        {
            resource.setCellResultsResource(new ResourceRef(null, resultsPath));
        }

        pm.store(resultsLocator, results, progressMonitor);
    }


}
