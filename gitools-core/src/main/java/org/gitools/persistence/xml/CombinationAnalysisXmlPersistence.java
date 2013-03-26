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
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.persistence.*;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;

public class CombinationAnalysisXmlPersistence extends AbstractXmlPersistence<CombinationAnalysis> {

    public CombinationAnalysisXmlPersistence() {
        super(CombinationAnalysis.class);

        setPersistenceTitle("combination analysis");
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

        String basePath = PersistenceUtils.getBasePath(resourceLocator);

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(basePath);
        context.setProgressMonitor(progressMonitor);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, CombinationAnalysis entity,  IProgressMonitor progressMonitor) throws PersistenceException {

        String url = resourceLocator.getURL().toString();

        int lastSlash = url.lastIndexOf('/');
        String basePath = url.substring(0, lastSlash);
        String baseName = PersistenceUtils.getFileName(url.substring(lastSlash+1));

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(basePath);
        context.setProgressMonitor(progressMonitor);

        PersistenceManager pm = getPersistenceManager();

        String dataExt = pm.getExtensionFromEntity(entity.getData());
        context.setEntityContext(entity.getData(), new PersistenceEntityContext(
                new File(basePath, baseName + "-data." + dataExt + ".gz").getAbsolutePath(), true));

        context.setEntityContext(entity.getGroupsMap(), new PersistenceEntityContext(
                new File(basePath, baseName + "-modules.ixm.gz").getAbsolutePath(), false));

        String resultsExt = pm.getExtensionFromEntity(entity.getResults());
        context.setEntityContext(entity.getResults(), new PersistenceEntityContext(
                new File(basePath, baseName + "-results." + resultsExt + ".gz").getAbsolutePath()));
    }
}
