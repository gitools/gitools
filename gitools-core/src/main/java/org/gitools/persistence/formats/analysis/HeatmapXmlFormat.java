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
import org.gitools.heatmap.Heatmap;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.analysis.adapter.PersistenceReferenceXmlAdapter;

import javax.xml.bind.Marshaller;

public class HeatmapXmlFormat extends AbstractXmlFormat<Heatmap> {

    public HeatmapXmlFormat() {
        super(FileSuffixes.HEATMAP, MimeTypes.HEATMAP, Heatmap.class);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, Heatmap resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {

        String baseName = resourceLocator.getBaseName();
        PersistenceReferenceXmlAdapter adapter = new PersistenceReferenceXmlAdapter(resourceLocator, progressMonitor);

        addReference(adapter, resource.getMatrixView().getContents(), baseName + "-data");
        addReference(adapter, resource.getRowDim().getAnnotations(), baseName + "-rows");
        addReference(adapter, resource.getColumnDim().getAnnotations(), baseName + "-columns");

        marshaller.setAdapter(adapter);

    }
}
