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
package org.gitools.analysis._DEPRECATED.heatmap.format;

import org.gitools.analysis._DEPRECATED.heatmap.Heatmap;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.analysis._DEPRECATED.formats.FileFormat;
import org.gitools.analysis._DEPRECATED.formats.analysis.AbstractXmlFormat;

import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

@ApplicationScoped
public class HeatmapFormat extends AbstractXmlFormat<Heatmap> {

    public static final String EXTENSION = "heatmap";
    public static final FileFormat FILE_FORMAT = new FileFormat("Heatmap", EXTENSION, true, false, true);

    public HeatmapFormat() {
        super(EXTENSION, Heatmap.class);
    }

    @Override
    public void afterRead(InputStream inputStream, IResourceLocator resourceLocator, Heatmap resource, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        // Initialize the heatmap
        resource.init();

    }
}
