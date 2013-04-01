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

import org.gitools.analysis.groupcomparison.GroupComparisonAnalysis;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;
import org.gitools.utils.progressmonitor.IProgressMonitor;

import javax.xml.bind.Marshaller;

public class GroupComparisonAnalysisXmlFormat extends AbstractXmlFormat<GroupComparisonAnalysis>
{

    public GroupComparisonAnalysisXmlFormat()
    {
        super(FileSuffixes.GROUP_COMPARISON, MimeTypes.GROUPCOMPARISON_ANALYSIS, GroupComparisonAnalysis.class);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, GroupComparisonAnalysis resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException
    {

        String baseName = resourceLocator.getBaseName();
        PersistenceReferenceXmlAdapter adapter = new PersistenceReferenceXmlAdapter(resourceLocator, progressMonitor);

        addReference(adapter, resource.getData(), baseName + "-data");
        addReference(adapter, resource.getResults(), baseName + "-results");

        marshaller.setAdapter(adapter);

    }

}
