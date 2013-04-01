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

import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class CombinationAnalysisXmlFormat extends AbstractXmlFormat<CombinationAnalysis>
{

    public CombinationAnalysisXmlFormat()
    {
        super(FileSuffixes.COMBINATION, MimeTypes.COMBINATION_ANALYSIS, CombinationAnalysis.class);
    }

    protected void beforeRead(IResourceLocator resourceLocator, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        unmarshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, CombinationAnalysis resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {
        marshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }
}
