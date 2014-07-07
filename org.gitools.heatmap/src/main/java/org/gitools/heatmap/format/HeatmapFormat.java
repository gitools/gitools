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
package org.gitools.heatmap.format;

import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.heatmap.Heatmap;
import org.gitools.matrix.model.hashmatrix.HashMatrix;
import org.gitools.matrix.model.hashmatrix.HashMatrixDimension;
import org.gitools.resource.AbstractXmlFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.OutputStream;

@ApplicationScoped
public class HeatmapFormat extends AbstractXmlFormat<Heatmap> {

    public static final String EXTENSION = "heatmap";
    public static final FileFormat FILE_FORMAT = new FileFormat("Heatmap", EXTENSION, true, false, true);

    public HeatmapFormat() {
        super(EXTENSION, Heatmap.class);
    }

    @Override
    public void beforeWrite(OutputStream out, IResourceLocator resourceLocator, Heatmap resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        if (resource.getContents() instanceof HashMatrix) {

            HashMatrixDimension rows = (HashMatrixDimension) resource.getContents().getRows();
            HashMatrixDimension columns = (HashMatrixDimension) resource.getContents().getColumns();

            rows.optimize(resource.getRows());
            columns.optimize(resource.getColumns());
        }

        super.beforeWrite(out, resourceLocator, resource, marshaller, progressMonitor);
    }

    @Override
    public void afterRead(InputStream inputStream, IResourceLocator resourceLocator, Heatmap resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        // Initialize the heatmap
        resource.init();

    }
}
