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
import org.gitools.model.Container;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.PersistenceUtils;
import org.gitools.persistence.xml.adapter.FileXmlAdapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class ContainerXmlPersistence extends AbstractXmlPersistence<Container> {

    private String basePath;

    public ContainerXmlPersistence() {
        super(Container.class);
    }

    @Override
    protected XmlAdapter<?, ?>[] createAdapters() {
        return new XmlAdapter[]{ new FileXmlAdapter(basePath)};
    }

    @Override
    protected void beforeRead(IResourceLocator resourceLocator, IProgressMonitor monitor) throws PersistenceException {
        basePath = PersistenceUtils.getBasePath(resourceLocator);
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, Container resource, IProgressMonitor progressMonitor) throws PersistenceException {
        basePath = PersistenceUtils.getBasePath(resourceLocator);
    }
}
