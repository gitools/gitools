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
package org.gitools.analysis.htest.enrichment.format;

import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.api.PersistenceException;
import org.gitools.api.analysis.IProgressMonitor;
import org.gitools.api.persistence.FileFormat;
import org.gitools.api.resource.IResourceLocator;
import org.gitools.resource.AbstractXmlFormat;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnrichmentAnalysisFormat extends AbstractXmlFormat<EnrichmentAnalysis> {

    public static final String EXTENSION = "enrichment";
    public static final FileFormat FILE_FORMAT = new FileFormat("Enrichment analysis", EXTENSION, true, false, true);

    public EnrichmentAnalysisFormat() {
        super(EXTENSION, EnrichmentAnalysis.class);
    }

    @Override
    protected void writeResource(IResourceLocator resourceLocator, EnrichmentAnalysis resource, IProgressMonitor monitor) throws PersistenceException {

        //TODO Find a better solution for this error
        resource.getData().get();
        resource.getResults().get();
        resource.getModuleMap().get();

        super.writeResource(resourceLocator, resource, monitor);
    }

}
