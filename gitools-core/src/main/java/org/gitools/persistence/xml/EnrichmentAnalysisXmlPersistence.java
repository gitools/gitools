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
import org.gitools.analysis.htest.enrichment.EnrichmentAnalysis;
import org.gitools.persistence.*;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;

public class EnrichmentAnalysisXmlPersistence
        extends AbstractXmlPersistence<EnrichmentAnalysis> {

    public EnrichmentAnalysisXmlPersistence() {
        super(EnrichmentAnalysis.class);

        setPersistenceTitle("enrichment analysis");
    }

    @Override
    protected XmlAdapter<?, ?>[] createAdapters() {
        PersistenceContext context = getPersistenceContext();
        return new XmlAdapter<?, ?>[]{
                new PersistenceReferenceXmlAdapter(context)
        };
    }

    @Override
    protected void beforeRead(IResourceLocator resourceLocator, IProgressMonitor progressMonitor) throws PersistenceException {
        String baseFile = PersistenceUtils.getBasePath(resourceLocator);

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(baseFile);
        context.setProgressMonitor(progressMonitor);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, EnrichmentAnalysis resource,        IProgressMonitor progressMonitor) throws PersistenceException {

        String baseFile = PersistenceUtils.getBasePath(resourceLocator);
        String baseName = PersistenceUtils.getFileName(resourceLocator.getName());

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(baseFile);
        context.setProgressMonitor(progressMonitor);

        PersistenceManager pm = getPersistenceManager();

        String dataExt = pm.getExtensionFromEntity(resource.getData());
        context.setEntityContext(resource.getData(), new PersistenceEntityContext(
                new File(baseFile, baseName + "-data." + dataExt + ".gz").getAbsolutePath(), false));

        context.setEntityContext(resource.getModuleMap(), new PersistenceEntityContext(
                new File(baseFile, baseName + "-modules.ixm.gz").getAbsolutePath(), false));

        String resultsExt = pm.getExtensionFromEntity(resource.getResults());
        context.setEntityContext(resource.getResults(), new PersistenceEntityContext(
                new File(baseFile, baseName + "-results." + resultsExt + ".gz").getAbsolutePath()));
    }
}
