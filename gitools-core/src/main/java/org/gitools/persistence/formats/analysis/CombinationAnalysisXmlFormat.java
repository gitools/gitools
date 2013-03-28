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
import org.gitools.analysis.combination.CombinationAnalysis;
import org.gitools.persistence._DEPRECATED.FileSuffixes;
import org.gitools.persistence.IResourceLocator;
import org.gitools.persistence._DEPRECATED.MimeTypes;
import org.gitools.persistence.PersistenceException;
import org.gitools.persistence.formats.analysis.adapter.ResourceReferenceXmlAdapter;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class CombinationAnalysisXmlFormat extends AbstractXmlFormat<CombinationAnalysis> {

    public CombinationAnalysisXmlFormat() {
        super(FileSuffixes.COMBINATION, MimeTypes.COMBINATION_ANALYSIS, CombinationAnalysis.class);
    }

    protected void beforeRead(IResourceLocator resourceLocator, Unmarshaller unmarshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        unmarshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }

    @Override
    protected void beforeWrite(IResourceLocator resourceLocator, CombinationAnalysis resource, Marshaller marshaller, IProgressMonitor progressMonitor) throws PersistenceException {
        marshaller.setAdapter(new ResourceReferenceXmlAdapter(resourceLocator, progressMonitor));
    }
}
