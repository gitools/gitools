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
import org.gitools.heatmap.Heatmap;
import org.gitools.persistence.*;
import org.gitools.persistence.xml.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.io.File;

public class HeatmapXmlPersistence extends AbstractXmlPersistence<Heatmap> {

    public HeatmapXmlPersistence() {
        super(Heatmap.class);
    }

    @Override
    protected XmlAdapter<?, ?>[] createAdapters() {
        PersistenceContext context = getPersistenceContext();
        return new XmlAdapter<?, ?>[]{
                new PersistenceReferenceXmlAdapter(context)
        };
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, Heatmap resource, IProgressMonitor progressMonitor) throws PersistenceException {

        String basePath = PersistenceUtils.getBasePath(resourceLocator);
        String baseName = PersistenceUtils.getFileName(resourceLocator.getName());

        PersistenceContext context = getPersistenceContext();
        context.setBasePath(basePath);
        context.setProgressMonitor(progressMonitor);

        context.setEntityContext(resource.getMatrixView().getContents(), new PersistenceEntityContext(
                new File(basePath, baseName + ".data.gz").getAbsolutePath()));

        context.setEntityContext(resource.getRowDim().getAnnotations(), new PersistenceEntityContext(
                new File(basePath, baseName + ".row.annotations.gz").getAbsolutePath()));

        context.setEntityContext(resource.getColumnDim().getAnnotations(), new PersistenceEntityContext(
                new File(basePath, baseName + ".column.annotations.gz").getAbsolutePath()));
    }


}
